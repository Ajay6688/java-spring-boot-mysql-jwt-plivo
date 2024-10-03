package com.hc.comms.Model;

public class SmsServiceResponse {
	
	    private boolean success;
	    private String message;
	    private Object responseData;

	    // Getters and Setters
	    public boolean isSuccess() {
	        return success;
	    }

	    public void setSuccess(boolean success) {
	        this.success = success;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	    public Object getResponseData() {
	        return responseData;
	    }

	    public void setResponseData(Object responseData) {
	        this.responseData = responseData;
	    }
	}


//	private int digitalRxStatusCode;
//	private String digitalRxStatusCodeDescription;
//	private String digitalRxStatusMessage;
//	private List<?> response;
//	
//	public SmsServiceResponse() {
//		super();
//	}
//	
//	public SmsServiceResponse(HttpStatus status) {
//		super();
//		if(status.equals(HttpStatus.BAD_REQUEST)) {
//			setDigitalRxStatusCode(400);
//			setDigitalRxStatusCodeDescription("400 - BAD REQUEST");
//		} else if (status.equals(HttpStatus.OK)) {
//			setDigitalRxStatusCode(200);
//			setDigitalRxStatusCodeDescription("200 - OK");
//		} else if (status.equals(HttpStatus.NOT_FOUND)) {
//			setDigitalRxStatusCode(404);
//			setDigitalRxStatusCodeDescription("404 - NOT FOUND");
//		} else if (status.equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
//			setDigitalRxStatusCode(500);
//			setDigitalRxStatusCodeDescription("500 - INTERNAL SERVER ERROR");
//		} else {
//			setDigitalRxStatusCode(404);
//			setDigitalRxStatusCodeDescription("404 - NOT FOUND");
//		}
//	}
//	
//	public SmsServiceResponse(HttpStatus status, String digitalRxStatusMessage) {
//		super();
//		setDigitalRxStatusMessage(digitalRxStatusMessage);
//		if(status.equals(HttpStatus.BAD_REQUEST)) {
//			setDigitalRxStatusCode(400);
//			setDigitalRxStatusCodeDescription("400 - BAD REQUEST");
//		} else if (status.equals(HttpStatus.OK)) {
//			setDigitalRxStatusCode(200);
//			setDigitalRxStatusCodeDescription("200 - OK");
//		} else if (status.equals(HttpStatus.NOT_FOUND)) {
//			setDigitalRxStatusCode(404);
//			setDigitalRxStatusCodeDescription("404 - NOT FOUND");
//		} else if (status.equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
//			setDigitalRxStatusCode(500);
//			setDigitalRxStatusCodeDescription("500 - INTERNAL SERVER ERROR");
//		} else {
//			setDigitalRxStatusCode(404);
//			setDigitalRxStatusCodeDescription("404 - NOT FOUND");
//		}
//	}
//	
//	public SmsServiceResponse(HttpStatus status, String digitalRxStatusMessage, List<?> response) {
//		super();
//		setDigitalRxStatusMessage(digitalRxStatusMessage);
//		setResponse(response);
//		if(status.equals(HttpStatus.BAD_REQUEST)) {
//			setDigitalRxStatusCode(400);
//			setDigitalRxStatusCodeDescription("400 - BAD REQUEST");
//		} else if (status.equals(HttpStatus.OK)) {
//			setDigitalRxStatusCode(200);
//			setDigitalRxStatusCodeDescription("200 - OK");
//		} else if (status.equals(HttpStatus.NOT_FOUND)) {
//			setDigitalRxStatusCode(404);
//			setDigitalRxStatusCodeDescription("404 - NOT FOUND");
//		} else if (status.equals(HttpStatus.FORBIDDEN)) {
//			setDigitalRxStatusCode(403);
//			setDigitalRxStatusCodeDescription("403 - FORBIDDEN");
//		} else if (status.equals(HttpStatus.INTERNAL_SERVER_ERROR)) {
//			setDigitalRxStatusCode(500);
//			setDigitalRxStatusCodeDescription("500 - INTERNAL SERVER ERROR");
//		} else {
//			setDigitalRxStatusCode(404);
//			setDigitalRxStatusCodeDescription("404 - NOT FOUND");
//		}
//	}
//
//	
//	public int getDigitalRxStatusCode() {
//		return digitalRxStatusCode;
//	}
//
//	public void setDigitalRxStatusCode(int digitalRxStatusCode) {
//		this.digitalRxStatusCode = digitalRxStatusCode;
//	}
//
//	public String getDigitalRxStatusCodeDescription() {
//		return digitalRxStatusCodeDescription;
//	}
//	public void setDigitalRxStatusCodeDescription(String digitalRxStatusCodeDescription) {
//		this.digitalRxStatusCodeDescription = digitalRxStatusCodeDescription;
//	}
//	public String getDigitalRxStatusMessage() {
//		return digitalRxStatusMessage;
//	}
//	public void setDigitalRxStatusMessage(String digitalRxStatusMessage) {
//		this.digitalRxStatusMessage = digitalRxStatusMessage;
//	}
//	public List<?> getResponse() {
//		return response;
//	}
//	public void setResponse(List<?> respose) {
//		this.response = respose;
//	}
	
	

