package edu.sjsu.LPOS.service;

import java.util.List;

import edu.sjsu.LPOS.domain.RestaurantLocation;

public interface RestaurantLocationService {
	public void createRestaurantLocation(RestaurantLocation restaurantLocation);
	public void updateRestaurantLocation(long rid, RestaurantLocation restuarantLocation);
	public RestaurantLocation findRestaurantByRid(long rid);
//	public List<RestaurantLocation> findRestaurantsByCity(String city);
	public List<RestaurantLocation> findAllRestaurant();
	public List<RestaurantLocation> findRestaurantsByMileDistance(double longitude, double latitude, int distance);
	public void deleteRestaurantLocation(long rid);
}
