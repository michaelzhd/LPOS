package edu.sjsu.LPOS.auth;

public class AuthResponse {
	private String username;
	private String roles;
	private AccessToken accessToken;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	public AccessToken getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}
}
