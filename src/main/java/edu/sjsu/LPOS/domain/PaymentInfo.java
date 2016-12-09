package edu.sjsu.LPOS.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "paymentinfo")
public class PaymentInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String cardNumber;
	private String expiredMonth;
	private String expiredYear;
	private String cardHolder;
	private String address;
	private String zipcode;
	private String cvv;
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id", nullable = false, updatable = true, insertable = true)
	@JsonIgnore
	private User user;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getExpiredMonth() {
		return expiredMonth;
	}

	public void setExpiredMonth(String expiredMonth) {
		this.expiredMonth = expiredMonth;
	}

	public String getExpiredYear() {
		return expiredYear;
	}

	public void setExpiredYear(String expiredYear) {
		this.expiredYear = expiredYear;
	}

	public String getCardHolder() {
		return cardHolder;
	}

	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
