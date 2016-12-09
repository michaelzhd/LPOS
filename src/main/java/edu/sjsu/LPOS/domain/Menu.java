package edu.sjsu.LPOS.domain;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "menu")
public class Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String name;
	private String description;
	private String image;
	private double price;
	private String status;
	
	@Transient
	@JsonIgnore
	private Integer restaurantId;
	
	@Transient
	public Integer getRestaurantId() {
		return restaurantId;
	}
	
	@Transient
	public void setRestaurantId(Integer restaurantId) {
		this.restaurantId = restaurantId;
	}
	
	@ManyToOne
	@JoinColumn(name="restaurant_id", referencedColumnName="id", nullable = false, updatable = true, insertable = true)
	@JsonIgnore
	private Restaurant restaurant;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Restaurant getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
	@Override
	public String toString() {
		return "Menu [id=" + id + ", name=" + name + ", description=" + description + ", image=" + image + ", price="
				+ price + ", status=" + status + ", restaurant=" + restaurant + "]";
	}
	
}
