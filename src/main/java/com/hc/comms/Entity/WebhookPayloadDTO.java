package com.hc.comms.Entity;

import java.util.Date;

import jakarta.persistence.*;


@Entity
@Table(name = "web_hook_payload_t")
public class WebhookPayloadDTO {

	@Id
	@Column(name = "webhook_id")
	private String webhookId;
	
	@Column(name = "message_time")
	private Date messageTime ;
	
	@Column(name = "message_uuid")
	private String messageUUID ;
	
	@Column(name = "to_number")
	private String toNumber ;
	
	@Column(name = "status")
	private String status ;
	
	@Column(name = "delivery_report_time")
	private Date deliveryReportTime;
	
	@Column(name = "sent_time")
	private Date sentTime ;

	@Column(name = "client_phone_number")
	private String clientPhoneNumber;

	public String getWebhookId() {
		return webhookId;
	}

	public void setWebhookId(String webhookId) {
		this.webhookId = webhookId;
	}

	public Date getMessageTime() {
		return messageTime;
	}

	public void setMessageTime(Date messageTime) {
		this.messageTime = messageTime;
	}

	public String getMessageUUID() {
		return messageUUID;
	}

	public void setMessageUUID(String messageUUID) {
		this.messageUUID = messageUUID;
	}

	public String getToNumber() {
		return toNumber;
	}

	public void setToNumber(String toNumber) {
		this.toNumber = toNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getSentTime() {
		return sentTime;
	}

	public void setSentTime(Date sentTime) {
		this.sentTime = sentTime;
	}

	public Date getDeliveryReportTime() {
		return deliveryReportTime;
	}

	public void setDeliveryReportTime(Date deliveryReportTime) {
		this.deliveryReportTime = deliveryReportTime;
	}

	public String getClientPhoneNumber() {
		return clientPhoneNumber;
	}

	public void setClientPhoneNumber(String clientPhoneNumber) {
		this.clientPhoneNumber = clientPhoneNumber;
	}
}
