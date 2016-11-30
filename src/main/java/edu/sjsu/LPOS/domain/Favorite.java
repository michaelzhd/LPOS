package edu.sjsu.LPOS.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "favorite")
public class Favorite {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@OneToOne
	@JoinColumn(name="restaurant_id", referencedColumnName="id")
	//@JsonIgnore
	private Restaurant restaurant;
	
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName="userId")
	private User user;
	
	public Favorite() {
		
	}
	
	public Favorite(User user, Restaurant restaurant) {
		this.user = user;
		this.restaurant = restaurant;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Favorite [id=" + id + ", restaurant=" + restaurant + ", user=" + user + "]";
	}
	
	
}
