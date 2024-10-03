package com.hc.comms.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hc.comms.Entity.WebhookPayloadDTO;
import com.hc.comms.Repository.SmsWebhookRepository;
import com.hc.comms.Utils.SmsUtils;

import io.micrometer.common.util.StringUtils;

@Service
public class SmsWebhookService {
	private static final Logger logger = LogManager.getLogger(SmsWebhookService.class);
	
	@Autowired	
	private SmsUtils smsUtils;
		
	@Autowired
	private SmsWebhookRepository repository;

	public boolean saveWebhookPayloadDTO(WebhookPayloadDTO webhookPayloadDTO) throws Exception {
		boolean isSaved = false;

		try {
			WebhookPayloadDTO savedPayload = repository.save(webhookPayloadDTO);
			if (savedPayload.getWebhookId() != null) {
				return true;
			} else {
				return false;	               
			}
		} catch (Exception e) {
			logger.error("[SmsWebhookService][saveWebhookPayloadDTO]",e);
		}		
		return isSaved;
	}

	public void processPayload(String payload) {
		boolean isSaved = false;
		try {
			Map<String, String> params = smsUtils.parseUrlEncodedString(payload);

			WebhookPayloadDTO webhookPayloadDTO = new WebhookPayloadDTO();

			UUID generatedId = UUID.randomUUID();
			webhookPayloadDTO.setWebhookId(generatedId.toString());
			if(StringUtils.isNotBlank(params.get("DeliveryReportTime"))) {
				Date deliveryReportTime = new SimpleDateFormat("yyyy-MM-dd").parse(params.get("DeliveryReportTime"));
				webhookPayloadDTO.setDeliveryReportTime(deliveryReportTime);
			}
			webhookPayloadDTO.setToNumber(params.get("To")); 
			webhookPayloadDTO.setMessageUUID(params.get("MessageUUID"));
			if(StringUtils.isNotBlank(params.get("MessageTime"))) {
				Date messageTime = new SimpleDateFormat("yyyy-MM-dd").parse(params.get("MessageTime"));
				webhookPayloadDTO.setMessageTime(messageTime);
			}
			if(StringUtils.isNotBlank(params.get("SentTime"))) {
				Date sentTime = new SimpleDateFormat("yyyy-MM-dd").parse(params.get("SentTime"));
				webhookPayloadDTO.setSentTime(sentTime);
			}
			if(StringUtils.isNotBlank(params.get("From"))){
				webhookPayloadDTO.setClientPhoneNumber(params.get("From"));
			}

			webhookPayloadDTO.setStatus(params.get("Status"));

			if(webhookPayloadDTO!=null) {
				isSaved = saveWebhookPayloadDTO(webhookPayloadDTO); // have boolean value
				if(isSaved) {
					logger.info("[SmsWebhookService][processPayload] WebhookPayloadDTO Saved Successfully :"+ webhookPayloadDTO );
				} else {
					logger.info("[SmsWebhookService][processPayload] WebhookPayloadDTO Failed to Save :"+ webhookPayloadDTO );									
				}
			}

		}catch (Exception e) {
			logger.error("[SmsWebhookService][processPayload]" + e.getMessage());
		}	
	}

	public List<WebhookPayloadDTO> findSentMessagesByPhoneNumberAndSentAndClientPhoneNumber(String phoneNumber , String clientPhoneNumber){
		return repository.findMessagesByPhoneNumberAndSentAndClientPhoneNumber(phoneNumber , clientPhoneNumber);
	}

}
