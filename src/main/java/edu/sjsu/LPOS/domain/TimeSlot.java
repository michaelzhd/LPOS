package edu.sjsu.LPOS.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "timeslot")
public class TimeSlot {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(unique = true)
	String timeSlot;
	
//	@ManyToMany
//	@JoinTable(name="table_timeslot", 
//				joinColumns={@JoinColumn(name="timeslot_id", referencedColumnName="id")},
//				inverseJoinColumns={@JoinColumn(name="tableinfo_id", referencedColumnName="id")})
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(name="restaurant_timeslot", 
				joinColumns={@JoinColumn(name="timeslot_id", referencedColumnName="id", updatable = true, insertable = true)}, 
				inverseJoinColumns={@JoinColumn(name="restaurant_id", referencedColumnName="id", updatable = true, insertable = true)})
	@JsonIgnore
	private Set<Restaurant> restaurant = new HashSet<Restaurant>();

//	@ManyToOne
//	@JoinColumn(name="restaurant_id", referencedColumnName="id", nullable = false, updatable = true, insertable = true)
//	@JsonIgnore
//	private Restaurant restaurant;
	
//	@ManyToMany(cascade = {CascadeType.ALL})
//	@JoinTable(name="restaurant_timeslot", 
//				joinColumns={@JoinColumn(name="timeslot_id", referencedColumnName="id")}, 
//				inverseJoinColumns={@JoinColumn(name="tableinfo_id", referencedColumnName="id")})
//	private Set<Restaurant> restaurant = new HashSet<Restaurant>();
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}

	public Set<Restaurant> getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Set<Restaurant> restaurant) {
		this.restaurant = restaurant;
	}

//	public Restaurant getRestaurant() {
//		return restaurant;
//	}
//
//	public void setRestaurant(Restaurant restaurant) {
//		this.restaurant = restaurant;
//	}

	@Override
	public String toString() {
		return "ReserveTimeSlot [id=" + id + ", timeSlot=" + timeSlot + "]";
	}

	
}
