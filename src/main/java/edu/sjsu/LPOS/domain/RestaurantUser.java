package edu.sjsu.LPOS.domain;

import javax.persistence.ManyToOne;

public class RestaurantUser {
	
	private int id;
	private String username;
	private String password;
	
	@ManyToOne
	private int restaurantid;
	private String role;
	
	
	
}
