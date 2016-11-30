package edu.sjsu.LPOS.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "appuser")
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer userId;

    private String username;
    
//    @JsonIgnore
    private String password;
    
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "USER_AUTHORITY",
//            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "USERID")},
//            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "AUTHORITYID")})
//    @JsonManagedReference
 
    private String authorities;
    
    private String address;
    
    public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getAuthorities() {
		return authorities;
	}
	public void setAuthorities(String authorities) {
		this.authorities = authorities;
	}
	@Override
	public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("userId: ").append(userId)
    	  .append("username: ").append(username)
    	  .append("email: ").append(email)
    	  .append("phonenumber: ").append(phonenumber)
    	  .append("authorities: ").append(authorities.toString());
		return sb.toString();
	}
	private String email;
    private String phonenumber;

    public User() {
    	
    }

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


//	public List<Authority> getAuthorities() {
//		return authorities;
//	}
//	public void setAuthorities(List<Authority> authorities) {
//		this.authorities = authorities;
//	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	
    
}
