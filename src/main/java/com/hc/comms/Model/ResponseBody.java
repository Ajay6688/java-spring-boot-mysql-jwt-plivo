package com.hc.comms.Model;

public class ResponseBody {
	
	private String fromPhoneNumber;

	private String response;

	private String clientPhoneNumber;

	private String messageUUID;

	public String getFromPhoneNumber() {
		return fromPhoneNumber;
	}

	public void setFromPhoneNumber(String phoneNumber) {
		this.fromPhoneNumber = phoneNumber;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getClientPhoneNumber() {
		return clientPhoneNumber;
	}

	public void setClientPhoneNumber(String clientPhoneNumber) {
		this.clientPhoneNumber = clientPhoneNumber;
	}

	public String getMessageUUID() {
		return messageUUID;
	}

	public void setMessageUUID(String messageUUID) {
		this.messageUUID = messageUUID;
	}
}
