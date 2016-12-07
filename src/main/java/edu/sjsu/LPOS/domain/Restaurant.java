package edu.sjsu.LPOS.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "restaurant")
public class Restaurant {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String name;
	private String address;
	private String opentime;
	private String description;
	private String phonenumber;
	private String type;
	private int capacity;
	private String url;
	@Transient
	private boolean isfavorite;
	@Transient
	private double distance;
	private double latitude;
	private double longtitude;
//	@OneToMany(orphanRemoval = true, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
//	@JoinColumn(name = "menu_id", nullable = true)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
	private List<Menu> menu = new ArrayList<>();

//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
//	private List<ReserveTimeSlot> reserveTimeSlot = new ArrayList<>();
	
//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
//	private List<TableInfo> tableinfo = new ArrayList<>();
	
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(name="restaurant_timeslot", 
				joinColumns={@JoinColumn(name="restaurant_id", referencedColumnName="id", updatable = true, insertable = true )}, 
				inverseJoinColumns={@JoinColumn(name="timeslot_id", referencedColumnName="id", updatable = true, insertable = true)})
	@JsonIgnore
	private List<TimeSlot> timeslot;
	
	@Transient
	private String[] slots;
	
	
	@Transient
	public boolean isIsfavorite() {
		return isfavorite;
	}
	@Transient
	public void setIsfavorite(boolean isfavorite) {
		this.isfavorite = isfavorite;
	}


//	public List<TableInfo> getTableinfo() {
//		return tableinfo;
//	}
//
//	public void setTableinfo(List<TableInfo> tableinfo) {
//		this.tableinfo = tableinfo;
//	}

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

//	public String getAddress() {
//		return address;
//	}
//
//	public void setAddress(String address) {
//		this.address = address;
//	}

	public String getOpentime() {
		return opentime;
	}

	public void setOpentime(String opentime) {
		this.opentime = opentime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public List<Menu> getMenu() {
		return menu;
	}

	public void setMenu(List<Menu> menu) {
		this.menu = menu;
	}

//	public List<ReserveTimeSlot> getReserveTimeSlot() {
//		return reserveTimeSlot;
//	}
//
//	public void setReserveTimeSlot(List<ReserveTimeSlot> reserveTimeSlot) {
//		this.reserveTimeSlot = reserveTimeSlot;
//	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public List<TimeSlot> getTimeslot() {
		return timeslot;
	}
	public void setTimeslot(List<TimeSlot> timeslot) {
		this.timeslot = timeslot;
	}
	public String[] getSlots() {
		return slots;
	}
	public void setSlots(String[] slots) {
		this.slots = slots;
	}	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "Restaurant [id=" + id + ", name=" + name + ", opentime=" + opentime
				+ ", description=" + description + ", phonenumber=" + phonenumber + ", capacity=" + capacity + "]";
	}
	
	public void setTimeSlotsForAPI() {
		String[] s = new String[this.timeslot.size()];
		for(int i = 0; i < this.timeslot.size(); i++) {
			s[i] = this.timeslot.get(i).getTimeSlot();
		}
		setSlots(s);
		
	}
	
	public void setFavoriteFlag(List<Favorite> favorites) {
		for(Favorite f : favorites) {
			if(f.getRestaurant().getId() == this.id) {
				this.isfavorite = true;
				break;
			}
		}
	}
	
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	
	
	
}
