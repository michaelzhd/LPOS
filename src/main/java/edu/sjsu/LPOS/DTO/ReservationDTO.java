package edu.sjsu.LPOS.DTO;

import java.util.List;

public class ReservationDTO {
	
	private String date;
	private String timeSlot;
	private Integer people;
	private boolean isPrivate;
	private boolean takeOut;
	private double price;
	private List<OrderMenuDTO> menus;

	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	public boolean getTakeOut() {
		return takeOut;
	}
	public void setTakeOut(boolean takeOut) {
		this.takeOut = takeOut;
	}
	public List<OrderMenuDTO> getMenus() {
		return menus;
	}
	public void setMenus(List<OrderMenuDTO> menus) {
		this.menus = menus;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTimeSlot() {
		return timeSlot;
	}
	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}
	public Integer getPeople() {
		return people;
	}
	public void setPeople(Integer people) {
		this.people = people;
	}

	public boolean getIsPrivate() {
		return isPrivate;
	}
	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	@Override
	public String toString() {
		return "ReservationDTO [date=" + date + ", timeSlot=" + timeSlot + ", people=" + people + ", isPrivate="
				+ isPrivate + ", menus=" + menus + "]";
	}
	

}
