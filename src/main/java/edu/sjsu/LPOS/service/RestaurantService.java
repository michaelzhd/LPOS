package edu.sjsu.LPOS.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.LPOS.domain.Restaurant;

import edu.sjsu.LPOS.repository.RestaurantRepository;


@Service
public class RestaurantService {
	@Autowired private RestaurantRepository restaurantRepository;
	
	public List<Restaurant> getRestaurantByName(String name) {
		return restaurantRepository.findByName(name);
	}

	public List<Restaurant> getRestaurantByType(String type) {
		return restaurantRepository.findByType(type);
	}
	
	public Restaurant getRestaurantByAddress(String address) {
		return restaurantRepository.findByAddress(address);
	}

	public Restaurant getRestaurantById(int id) {
		return restaurantRepository.findOne(id);
	}
	
	public Restaurant saveRestaurant(Restaurant restaurant) {
		return restaurantRepository.save(restaurant);
	}
	
	public void deleteRestaurantById(int id) {
		restaurantRepository.delete(restaurantRepository.findById(id));
	}
	
	public Iterable<Restaurant> getAllRestaurant() {
		
		return restaurantRepository.findAll();
	}
	
	public List<Restaurant> getRestaurantsContainsName(String name) {
		return restaurantRepository.findByRestaurantContainsName(name);
	}
	
	public List<Restaurant> getRestaurantsContainsType(String type) {
		return restaurantRepository.findByRestaurantContainsType(type);
	}
}
