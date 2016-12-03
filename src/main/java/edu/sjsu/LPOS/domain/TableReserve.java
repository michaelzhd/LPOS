package edu.sjsu.LPOS.domain;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.sjsu.LPOS.DTO.ReservationDTO;

@Entity
@Table(name = "tablereserve")
public class TableReserve {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String timeSlot;
	
	private Integer people;	

	private String date;
	
	private boolean isPrivate;

	@OneToOne
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USERID"))
	@JsonIgnore
	private User user;
	
	@ManyToOne
	@JoinColumn(name="restaurant_id", referencedColumnName="id", nullable = false, updatable = true, insertable = true)
	@JsonIgnore
	private Restaurant restaurant;
	
	public TableReserve(User user, Restaurant restaurant, ReservationDTO reservation) {
		this.setUser(user);
		this.setDate(reservation.getDate());
		this.setPeople(reservation.getPeople());
		this.setPrivate(reservation.getIsPrivate());
		this.setRestaurant(restaurant);
		this.setTimeSlot(reservation.getTimeSlot());
	}
	public TableReserve() {
		
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean getIsPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
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

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	@Override
	public String toString() {
		return "TableReserve [id=" + id + ", timeSlot=" + timeSlot + ", people=" + people + ", date=" + date
				+ ", isPrivate=" + isPrivate + ", user=" + user + ", restaurant=" + restaurant + "]";
	}



}
