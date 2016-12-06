package edu.sjsu.LPOS.DTO;

import java.util.List;

import edu.sjsu.LPOS.domain.Restaurant;

public class ReservationResponseDTO {
	private Restaurant restaurant;
	private Object reservation;
	private List<MenuDTO> menus;
	
	public ReservationResponseDTO() {
		
	}
	public ReservationResponseDTO(Restaurant restaurant, Object reservation) {
		this.restaurant = restaurant;
		this.reservation = reservation;
	}
	public Restaurant getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
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
	@Override
	public String toString() {
		return "ReservationResponseDTO [restaurant=" + restaurant + ", reservation=" + reservation + ", menus=" + menus
				+ "]";
	}
	
}
