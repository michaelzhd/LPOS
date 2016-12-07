package edu.sjsu.LPOS.domain;


public class Location {
	
	private double longitude;
	private double latitude;
	
	public Location() {
		
	}
	
	public Location(double lon, double lat) {
		this.longitude = lon;
		this.latitude = lat;
	}
	
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return "Location [longitude=" + longitude + ", latitude=" + latitude + "]";
	}	
	
}
