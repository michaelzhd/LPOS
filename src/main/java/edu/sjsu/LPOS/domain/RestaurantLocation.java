package edu.sjsu.LPOS.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Document(collection="restaurant")
public class RestaurantLocation {
	@Id
	private long rid;
	private String name;
	private String address;
	private String city;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public RestaurantLocation(long id, String name, String address, String city) {
		super();
		this.rid = id;
		this.name = name;
		this.address = address;
		this.city = city;
	}
	public long getRid() {
		return rid;
	}
	public void setRid(long rid) {
		this.rid = rid;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public RestaurantLocation() {
		super();
	}
	

}
