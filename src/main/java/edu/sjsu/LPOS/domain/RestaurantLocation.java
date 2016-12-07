package edu.sjsu.LPOS.domain;

import javax.persistence.Id;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="restaurant")
public class RestaurantLocation {
	@Id
	private ObjectId id;
	
	private Integer rid;
	
	private String address;
//	private String city;
//	private String state;
	
	private Location location;
	
	public Integer getRid() {
		return rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

//	public String getState() {
//		return state;
//	}
//
//	public void setState(String state) {
//		this.state = state;
//	}
//
//	public String getCity() {
//		return city;
//	}
//
//	public void setCity(String city) {
//		this.city = city;
//	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "RestaurantLocation [id=" + id + ", rid=" + rid + ", address=" + address + ", location=" + location
				+ "]";
	}

//	@Override
//	public String toString() {
//		return "RestaurantLocation [id=" + id + ", rid=" + rid + ", address=" + address + ", city=" + city + ", state="
//				+ state + ", location=" + location + "]";
//	}

}
