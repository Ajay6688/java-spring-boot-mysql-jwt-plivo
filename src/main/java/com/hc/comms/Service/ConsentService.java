package com.hc.comms.Service;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hc.comms.Entity.Consent;
import com.hc.comms.Repository.ConsentRepository;
import com.plivo.api.Plivo;
import com.plivo.api.models.message.Message;
import com.plivo.api.models.message.MessageCreateResponse;

import java.net.URL;
import java.util.Date;


@Service
public class ConsentService {
	private static final Logger logger = LoggerFactory.getLogger(ConsentService.class);
	
	
	@Autowired
	private  ConsentRepository consentRepository;

	@Value("${plivo.source_number}")
	private String sourceNumber;

	@Value("${plivo.auth_id}")
	private String authId;

	@Value("${plivo.auth_token}")
	private String authToken;

    @Value("${plivo.status_webhook_url}")
	private String statusWebhookURL;

	public void sendConsentRequest(String phoneNumber , String clientPhoneNumber) {

		Plivo.init(authId, authToken);

		String text = "Please reply with 'YES' to give consent or 'NO' to deny.";

		try {
			 // Send the message
            MessageCreateResponse response = Message.creator(sourceNumber, phoneNumber, text)
					.url(new URL(statusWebhookURL))
					.create();

            String messageUuid = response.getMessageUuid().get(0);

			Consent consentFromDB = getConsentByFromPhoneNumberAndClientPhoneNumber(phoneNumber , clientPhoneNumber);

			if(consentFromDB != null){
				consentFromDB.setConsentRequestUpdatedDate(new Date().toString());
				consentRepository.save(consentFromDB);
				return;
			}

            Consent consent = new Consent();
            consent.setConsentRequestDate(java.time.LocalDateTime.now().toString());
            consent.setRequestedConsentMessageUuid(messageUuid);
            consent.setFromPhoneNumber(phoneNumber.trim());
			consent.setClientPhoneNumber(clientPhoneNumber);
			consent.setConsentRequestUpdatedDate(java.time.LocalDateTime.now().toString());
            consentRepository.save(consent);

//            // Optionally, you can fetch the consent record and update it with the message UUID
////          Consent consent = consentRepository.findByPhoneNumber(phoneNumber);
//            if (consent != null) {
//                consent.setMessageUuid(messageUuid); // Assuming Consent has a field for storing message UUID
//                consentRepository.save(consent);
//            } else {
//                logger.warn("No consent record found for phone number: " + phoneNumber);
//            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public Consent saveConsent(String phoneNumber , String clientPhoneNumber, boolean isConsentGiven , String messageUUID) {

		Consent consent = new Consent();
		consent.setConsentGiven(isConsentGiven);
		consent.setFromPhoneNumber(phoneNumber);
		consent.setConsentReceivedDate(java.time.LocalDateTime.now().toString());
		consent.setClientPhoneNumber(clientPhoneNumber);
		consent.setReceivedConsentMessageUuid(messageUUID);
		consent.setConsentRequestUpdatedDate(java.time.LocalDateTime.now().toString());
		return consentRepository.save(consent);
	}

	public Consent updateConsent(String phoneNumber , String clientPhoneNumber , boolean isConsentGiven , String messageUUID , Consent consentFromDB){

		consentFromDB.setFromPhoneNumber(phoneNumber);
		consentFromDB.setClientPhoneNumber(clientPhoneNumber);
		consentFromDB.setConsentGiven(isConsentGiven);
		consentFromDB.setConsentReceivedDate(java.time.LocalDateTime.now().toString()); // Optionally, update the consent date
		consentFromDB.setReceivedConsentMessageUuid(messageUUID);
		consentFromDB.setConsentRequestUpdatedDate(java.time.LocalDateTime.now().toString());
		return consentRepository.save(consentFromDB);
	}

	public Consent getConsentByFromPhoneNumberAndClientPhoneNumber(String fromPhoneNumber , String clientPhoneNumber) {

		return consentRepository.findByFromPhoneNumberAndClientPhoneNumber(fromPhoneNumber , clientPhoneNumber);
	}
}
