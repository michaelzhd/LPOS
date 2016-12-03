package edu.sjsu.LPOS.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class TableInfo {

	private Integer id;
	private Integer capacity;
	
//	private String reserveTime;
//	private String orderEndTime;
//	private String startTime;
	
//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tableinfo")
	//private List<TableReserve> tableReserve = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name="restaurant_id", referencedColumnName="id")
	@JsonIgnore
	private Restaurant restaurant;
	
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(name="table_timeslot", 
				joinColumns={@JoinColumn(name="tableinfo_id", referencedColumnName="id")}, 
				inverseJoinColumns={@JoinColumn(name="timeslot_id", referencedColumnName="id")})
	@JsonIgnore
	private List<TimeSlot> timeslot;
	
	@Transient
	private String[] slots;

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public String[] getSlots() {
		return this.slots;
	}

	public void setSlots(String[] slots) {

		this.slots = slots;
	}

//	public List<TableReserve> getTableReserve() {
//		return tableReserve;
//	}
//
//	public void setTableReserve(List<TableReserve> tableReserve) {
//		this.tableReserve = tableReserve;
//	}	
//	@ManyToOne
//	@JoinColumn(name="owner_id", referencedColumnName="id")
//	private User user;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

//	public String getReserveTime() {
//		return reserveTime;
//	}
//
//	public void setReserveTime(String reserveTime) {
//		this.reserveTime = reserveTime;
//	}
//
//	public String getOrderEndTime() {
//		return orderEndTime;
//	}
//
//	public void setOrderEndTime(String orderEndTime) {
//		this.orderEndTime = orderEndTime;
//	}
//
//	public String getStartTime() {
//		return startTime;
//	}
//
//	public void setStartTime(String startTime) {
//		this.startTime = startTime;
//	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public List<TimeSlot> getTimeslot() {
		return timeslot;
	}

	public void setTimeslot(List<TimeSlot> timeslot) {
		this.timeslot = timeslot;
	}

	public void setTimeSlotsForAPI() {
		String[] s = new String[this.timeslot.size()];
		for(int i = 0; i < this.timeslot.size(); i++) {
			s[i] = this.timeslot.get(i).getTimeSlot();
		}
		setSlots(s);
		
	}
//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}

//	@Override
//	public String toString() {
//		return "TableInfo [id=" + id + ", capacity=" + capacity + ", reserveTime=" + reserveTime + ", orderEndTime="
//				+ orderEndTime + ", startTime=" + startTime + ", restaurant=" + restaurant + ", user=" + user + "]";
//	}
	

}
