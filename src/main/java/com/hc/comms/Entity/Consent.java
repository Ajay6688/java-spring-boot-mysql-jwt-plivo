package com.hc.comms.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "consent")
public class Consent {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)	 
	    private Long id;
	 
	 	@Column(name = "from_phone_number")
	    private String fromPhoneNumber;

	 	@Column(name = "consent_given")
	    private Boolean consentGiven;

	 	@Column(name = "consent_request_date")
	    private String consentRequestDate;

		@Column(name = "consent_request_updated_date")
		private String consentRequestUpdatedDate;
	 	
	 	@Column(name = "requested_consent_message_uuid")
	    private String requestedConsentMessageUuid;

		@Column(name = "received_consent_message_uuid")
		private String receivedConsentMessageUuid;
	 	 
	 	@Column(name = "consent_received_date")
	    private String consentReceivedDate;

		@Column(name = "client_phone_number")
		private String clientPhoneNumber;
	    
	    // Getters and Setters

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getFromPhoneNumber() {
			return fromPhoneNumber;
		}

		public void setFromPhoneNumber(String phoneNumber) {
			this.fromPhoneNumber = phoneNumber;
		}

		public Boolean getConsentGiven() {
			return consentGiven;
		}

		public void setConsentGiven(Boolean consentGiven) {
			this.consentGiven = consentGiven;
		}

	    public String getRequestedConsentMessageUuid() {
			return requestedConsentMessageUuid;
	    }

	    public void setRequestedConsentMessageUuid(String requestedConsentMessageUuid) {
		     this.requestedConsentMessageUuid = requestedConsentMessageUuid;
		}

	    public String getReceivedConsentMessageUuid() {
		    return receivedConsentMessageUuid;
	    }

	    public void setReceivedConsentMessageUuid(String receivedConsentMessageUuid) {
			this.receivedConsentMessageUuid = receivedConsentMessageUuid;
	    }

	    public String getConsentRequestDate() {
			return consentRequestDate;
		}

		public void setConsentRequestDate(String consentRequestDate) {
			this.consentRequestDate = consentRequestDate;
		}

	    public String getConsentRequestUpdatedDate() {
			return consentRequestUpdatedDate;
	    }

	    public void setConsentRequestUpdatedDate(String consentRequestUpdatedDate) {
		    this.consentRequestUpdatedDate = consentRequestUpdatedDate;
	    }

	    public String getConsentReceivedDate() {
			return consentReceivedDate;
		}

		public void setConsentReceivedDate(String consentReceivedDate) {
			this.consentReceivedDate = consentReceivedDate;
		}

		public String getClientPhoneNumber(){
			return  clientPhoneNumber;
		}

		public void setClientPhoneNumber(String clientPhoneNumber){
			this.clientPhoneNumber = clientPhoneNumber;
		}
}
