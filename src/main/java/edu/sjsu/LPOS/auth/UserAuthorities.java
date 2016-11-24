package edu.sjsu.LPOS.auth;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public class UserAuthorities {
	public UserAuthorities(String username, List<GrantedAuthority> authorities) {
		super();
		this.username = username;
		this.authorities = authorities;
	}
	public UserAuthorities(Object principal, Collection<? extends GrantedAuthority> authorities2) {
		this.username = (String) principal;
		this.authorities = (List<GrantedAuthority>) authorities2;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<GrantedAuthority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(List<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}
	private String username;
	private List<GrantedAuthority> authorities;	
}
