package com.hc.comms.Model;

public class GenerateTokenResponse {

    private String clientId;

    private String authToken;

    public GenerateTokenResponse(String clientId , String authToken){
        this.clientId = clientId;
        this.authToken = authToken;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
