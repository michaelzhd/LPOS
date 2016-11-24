package edu.sjsu.LPOS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.LPOS.domain.RestaurantLocation;
import edu.sjsu.LPOS.service.RestaurantLocationService;

@RestController
@RequestMapping("restaurant")
public class RestaurantLocationController {

	@Autowired
	private RestaurantLocationService restaurantLocationService;
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<RestaurantLocation> getRestaurantLocationById(@PathVariable("id") long id) {
		RestaurantLocation restaurantLocation = restaurantLocationService.findRestaurantById(id);
		System.out.println("-~~~" +restaurantLocation);
		return new ResponseEntity<RestaurantLocation>(restaurantLocation, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="", method=RequestMethod.GET)
	public ResponseEntity<List<RestaurantLocation>> getAllRestaurantLocations() {
		List<RestaurantLocation> restaurantLocations = restaurantLocationService.findAllRestaurant();
		return new ResponseEntity<List<RestaurantLocation>>(restaurantLocations, HttpStatus.OK);
	}
}
