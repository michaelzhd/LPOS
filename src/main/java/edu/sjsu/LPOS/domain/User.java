package edu.sjsu.LPOS.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "appuser")
public class User {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
	
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

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
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	private List<PaymentInfo> paymentInfo = new ArrayList<>();
    
    public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String getAuthorities() {
		return authorities;
	}
	public void setAuthorities(String authorities) {
		this.authorities = authorities;
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
	
	public List<PaymentInfo> getPaymentInfo() {
		return paymentInfo;
	}
	public void setPaymentInfo(List<PaymentInfo> paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", authorities="
				+ authorities + ", address=" + address + ", email=" + email + ", phonenumber=" + phonenumber + "]";
	}

	
    
}
