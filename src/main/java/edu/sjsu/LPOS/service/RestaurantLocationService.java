package edu.sjsu.LPOS.service;

import java.util.List;

import edu.sjsu.LPOS.domain.RestaurantLocation;

public interface RestaurantLocationService {
	public RestaurantLocation findRestaurantById(long id);
	public List<RestaurantLocation> findRestaurantsByCity(String city);
	public List<RestaurantLocation> findAllRestaurant();
}
