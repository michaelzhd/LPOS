package edu.sjsu.LPOS.DTO;

public class ReservationDTO {
	
	private String date;
	private String timeSlot;
	private Integer people;
	private boolean isPrivate;
	
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
				+ isPrivate + "]";
	}

}
