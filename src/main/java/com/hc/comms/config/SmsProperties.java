package com.hc.comms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "plivo")
public class SmsProperties {

	private String authId;

	private String authToken;

	private String sourceNumber;

	private String phloId;

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getSourceNumber() {
		return sourceNumber;
	}

	public void setSourceNumber(String sourceNumber) {
		this.sourceNumber = sourceNumber;
	}

	public String getPhloId() {
		return phloId;
	}

	public void setPhloId(String phloId) {
		this.phloId = phloId;
	}
}
