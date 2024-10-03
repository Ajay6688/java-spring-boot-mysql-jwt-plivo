package com.hc.comms.Controller;

import com.hc.comms.Entity.Client;
import com.hc.comms.Entity.WebhookPayloadDTO;
import com.hc.comms.Model.*;
import com.hc.comms.Model.ResponseBody;
import com.hc.comms.Service.ClientDetailsService;
import com.hc.comms.Utils.JwtUtils;
import com.hc.comms.Utils.SmsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.hc.comms.Entity.Consent;
import com.hc.comms.Repository.ConsentRepository;
import com.hc.comms.Service.ConsentService;
import com.hc.comms.Service.SmsWebhookService;
import com.hc.comms.config.SmsProperties;
import com.plivo.api.models.phlo.PhloUpdateResponse;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/consent")
public class ConsentController {
	private static final Logger logger = LoggerFactory.getLogger(ConsentController.class);

	@Autowired
	private ConsentService consentService;

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Autowired
	private  SmsProperties smsProperties;

	@Autowired
	private SmsWebhookService smsWebhookService;

	@Autowired
	private  ConsentRepository consentRepository;

	@Autowired
	private SmsUtils smsUtils;

	@Autowired
	private JwtUtils jwtUtils;

	@Lazy
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

    @GetMapping("")
	public String test(HttpServletRequest request){
		return "Hello, Server is running on port 8080.";
	}

    //generate token
	@PostMapping("/generate-token")
	public ResponseEntity<?> authenticateUser(@RequestBody GenerateTokenRequest generateTokenRequest) {
		Authentication authentication;

		System.out.println("Hashed Client Secret : "+passwordEncoder.encode(generateTokenRequest.getClientSecret()));

		CommonResponse commonResponse = new CommonResponse();
		try {
			authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(generateTokenRequest.getClientId(), generateTokenRequest.getClientSecret()));

		    SecurityContextHolder.getContext().setAuthentication(authentication);

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();

			// passing clientId to generate the token
		    String jwtToken = jwtUtils.generateTokenFromClientId(userDetails.getUsername());

		    GenerateTokenResponse generateTokenResponse = new GenerateTokenResponse(userDetails.getUsername(), jwtToken);

		    return ResponseEntity.ok(generateTokenResponse);

		} catch (AuthenticationException exception) {
			commonResponse.setMessage("Bad credentials");
			commonResponse.setStatus(false);
			return new ResponseEntity<Object>(commonResponse, HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			logger.error("[authTokenApiController][generateToken] " + e.getMessage());
			commonResponse.setStatus(false);
			commonResponse.setMessage("Failed to generate auth token due to an internal error.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(commonResponse);
		}
	}

	/*
	 * sends message to the end user
	 */
	@PostMapping("/sendMessage")
	public ResponseEntity<SmsServiceResponse> sendMessages(HttpServletRequest request,
			@RequestBody SmsBody smsBody)throws Exception {

		PhloUpdateResponse response = null;
		SmsServiceResponse smsServiceResponse = new SmsServiceResponse();
		Consent consent = null ;
		Client client = null ;
		String clientPhoneNumber = null;
		String toPhoneNumber;
		int mode = 1;
		try {
			if(smsBody!=null) {
				if (StringUtils.isBlank(smsBody.getTo())) {
					logger.error("[ConsentController][sendMessages] To is Blank: ");
					throw new IllegalArgumentException("Please enter To phone Number");
				}
				if(StringUtils.isBlank(smsBody.getMessage())) {
					logger.error("[ConsentController][sendMessages] Text message is Blank: ");
					throw new IllegalArgumentException("Please enter Text message.");
				}
				String clientId = jwtUtils.getClientIdFromHttpServletRequest(request);

				if(clientId != null){
					client = clientDetailsService.getClientById(clientId);
				}

				if(smsBody.getTo().trim().startsWith("+")){
					toPhoneNumber = smsBody.getTo().trim().replace("+","");
				}
				else{
					toPhoneNumber = smsBody.getTo().trim();
				}

				if(client != null && client.getPhoneNumber() != null){
					if(client.getPhoneNumber().startsWith("+")){
						clientPhoneNumber = client.getPhoneNumber().trim().replace("+","");
					}
					else{
						clientPhoneNumber = client.getPhoneNumber().trim();
					}
				}

				if(smsBody != null){

					if(client != null && client.getMode() != null) {
						mode = client.getMode();
					}
					else{
						smsServiceResponse.setSuccess(false);
						smsServiceResponse.setMessage("Mode is invalid!");
						return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).body(smsServiceResponse);
					}
				}

				// to check the consent
				consent = consentService.getConsentByFromPhoneNumberAndClientPhoneNumber(toPhoneNumber , clientPhoneNumber);

				// for mode 1
				// if consent is available and its false then don't send message otherwise continue sending message
				if(mode == 1){
					if(consent != null && consent.getConsentGiven() != null && !consent.getConsentGiven() ){
						smsServiceResponse.setSuccess(false);
						smsServiceResponse.setMessage("Can't send SMS, Consent is given false.");
						return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).body(smsServiceResponse);
					}
					else{
						response = smsUtils.sendPlivoMessage(toPhoneNumber , smsBody.getMessage() , clientPhoneNumber);
						logger.info("[ConsentController][sendMessage] Message sent successfully via mode 1");
					}
				}
				// for mode 0
				else if( mode == 0 ){
					List<WebhookPayloadDTO> messageList = smsWebhookService.findSentMessagesByPhoneNumberAndSentAndClientPhoneNumber(toPhoneNumber , clientPhoneNumber);
					System.out.println("message list size--------> "+messageList.size());

					// if the consent is present and its false then we can't send the message
					if(consent != null && consent.getConsentGiven() != null && !consent.getConsentGiven()){
						smsServiceResponse.setSuccess(false);
						smsServiceResponse.setMessage("Can't send SMS, Consent is given false.");
						return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).body(smsServiceResponse);
					}
					// if at least one message is sent then send consent request only
					else if(messageList.size() > 0){
						consentService.sendConsentRequest(toPhoneNumber , clientPhoneNumber);
						smsServiceResponse.setSuccess(false);
						smsServiceResponse.setMessage("Consent request sent. Please provide consent to receive SMS.");
						return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).body(smsServiceResponse);
					}
					else{
						response = smsUtils.sendPlivoMessage(toPhoneNumber , smsBody.getMessage() , clientPhoneNumber);
						logger.info("[ConsentController][sendMessage] Message sent successfully via mode 0");
					}
				}
				else{
					throw new IllegalArgumentException("Mode is invalid.");
				}

				if(response != null){

					// Log and return success response
					System.out.println(response);
					smsServiceResponse.setSuccess(true);
					smsServiceResponse.setMessage("SMS sent successfully");
					smsServiceResponse.setResponseData(response);
					logger.info("[SmsApiController][sendMessages] SMS sent successfully: " + response);
					return ResponseEntity.ok(smsServiceResponse);
				}
				else{
					throw new NullPointerException("Failed to send SMS due to unexpected error.");
				}
			}
			else{
				throw new IllegalArgumentException("Request body cannot be null.");
			}
		} catch (IllegalArgumentException e) {
			logger.error("[SmsApiController][sendMessages] " + e.getMessage());
			smsServiceResponse.setSuccess(false);
			smsServiceResponse.setMessage(e.getMessage());
			return ResponseEntity.badRequest().body(smsServiceResponse);
		} catch (Exception e) {
			logger.error("[SmsApiController][sendMessages] " + e.getMessage());
			smsServiceResponse.setSuccess(false);
			smsServiceResponse.setMessage("Failed to send SMS due to an internal error.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(smsServiceResponse);
		}
	}

	/*
	 * Webhook Call
	 * Will get the status of the message sent to the end user from the plivo webhook call
	 */
	@PostMapping("/webCallback")
	public ResponseEntity<Void> smsCallbackWebhook(@RequestBody(required = false) String payload) {
		logger.info("------------------------------------Webhook Call --------------------------------------");
		logger.info(payload);
		try {
			// Process webhook payload
			if (payload != null && !payload.isEmpty()) {
				try {
					smsWebhookService.processPayload(payload);
					return new ResponseEntity<>(HttpStatus.OK);
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Error processing webhook payload", e);
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Return bad request if payload is null or empty
			}
		} catch (Exception e) {
			logger.error("Error processing webhook payload", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/response")
	public ResponseEntity<String> respondConsent(HttpServletRequest request,
			@RequestBody ResponseBody responseBody)throws Exception {

		String fromPhoneNumber = null;
		String clientPhoneNumber = null;
		String requestMessage = responseBody.getResponse().trim();
		String messageUUID = responseBody.getMessageUUID().trim();
		boolean consentGiven;
		try {

			if(responseBody != null && responseBody.getClientPhoneNumber() != null && responseBody.getFromPhoneNumber() != null){

				// for client phone number
				if(responseBody.getClientPhoneNumber().trim().startsWith("+")){
					clientPhoneNumber = responseBody.getClientPhoneNumber().trim().replace("+","");
				}
				else{
					clientPhoneNumber = responseBody.getClientPhoneNumber().trim();
				}

				// for patient phone number
				if(responseBody.getFromPhoneNumber().trim().startsWith("+")){
					fromPhoneNumber = responseBody.getFromPhoneNumber().trim().replace("+","");
				}
				else{
					fromPhoneNumber = responseBody.getFromPhoneNumber().trim();
				}
			}

			if(requestMessage.equalsIgnoreCase("YES")){
				System.out.println("inside yes and consent given true");
				consentGiven = true;
			}
			else if(requestMessage.equalsIgnoreCase("NO")){
				System.out.println("inside no and consentGiven false");
				consentGiven = false;
			}
			else{
				logger.info("Reply received but it was invalid.");
				return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).body("It was not a valid consent reply.");
			}

			Consent consentFromDB = consentService.getConsentByFromPhoneNumberAndClientPhoneNumber(fromPhoneNumber , clientPhoneNumber);

			if (consentFromDB != null) {
				// Update the consent status if already present in the database
				consentService.updateConsent(fromPhoneNumber , clientPhoneNumber , consentGiven , messageUUID , consentFromDB);
				logger.info("[SmsApiController][repliedConsent] Consent updated successfully.");

			} else {
                // if not present in the database create a new record
				consentService.saveConsent(fromPhoneNumber , clientPhoneNumber , consentGiven , messageUUID);
				logger.info("[SmsApiController][repliedConsent] Consent saved successfully.");
			}

			return ResponseEntity.ok("Consent recorded for " + responseBody.getFromPhoneNumber());

		} catch (Exception e) {
			return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
		}
	}
}