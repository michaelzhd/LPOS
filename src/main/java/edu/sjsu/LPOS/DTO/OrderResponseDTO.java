package edu.sjsu.LPOS.DTO;

import java.util.List;

import edu.sjsu.LPOS.domain.Restaurant;
import edu.sjsu.LPOS.domain.User;

public class OrderResponseDTO {
	//private Restaurant restaurant;
	private Object reservation;
	private List<MenuDTO> menus;
	private User user;
	public OrderResponseDTO() {
		
	}
	public OrderResponseDTO(Object reservation, User user) {
		//this.restaurant = restaurant;
		this.reservation = reservation;
		this.user = user;
	}
//	public Restaurant getRestaurant() {
//		return restaurant;
//	}
//	public void setRestaurant(Restaurant restaurant) {
//		this.restaurant = restaurant;
//	}
	public Object getReservation() {
		return reservation;
	}
	public void setReservation(Object reservation) {
		this.reservation = reservation;
	}
	public List<MenuDTO> getMenus() {
		return menus;
	}
	public void setMenus(List<MenuDTO> menus) {
		this.menus = menus;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
