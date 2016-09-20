package edu.sjsu.LPOS.auth;

public class Audience {
	private String clientId;
	private String clientSecret;
	private String clientName;
	public Audience() {
		super();
	}
	private int expiresInSeconds;
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public int getExpiresInSeconds() {
		return expiresInSeconds;
	}
	public void setExpiresInSeconds(int expiresInSeconds) {
		this.expiresInSeconds = expiresInSeconds;
	}
}
