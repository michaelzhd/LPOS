package edu.sjsu.LPOS.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocalAuthServerSetting {
	
	@Value("${localauthserver.clientId}")
	private String clientId;
	
	@Value("${localauthserver.clientSecret}")
	private String clientSecret;
	
	@Value("${localauthserver.clientName}")
	private String clientName;
	
	@Value("${localauthserver.accessExpiresInSeconds}")
	private int accessExpiresInSeconds;
	
	@Value("${localauthserver.refreshExpiresInSeconds}")
	private int refreshExpiresInSeconds;
	
	
	public LocalAuthServerSetting() {
		super();
	}
	
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

	public int getAccessExpiresInSeconds() {
		return accessExpiresInSeconds;
	}

	public void setAccessExpiresInSeconds(int accessExpiresInSeconds) {
		this.accessExpiresInSeconds = accessExpiresInSeconds;
	}

	public int getRefreshExpiresInSeconds() {
		return refreshExpiresInSeconds;
	}

	public void setRefreshExpiresInSeconds(int refreshExpiresInSeconds) {
		this.refreshExpiresInSeconds = refreshExpiresInSeconds;
	}

}
