package edu.sjsu.LPOS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.LPOS.DTO.ResponseDTO;
import edu.sjsu.LPOS.domain.Restaurant;
import edu.sjsu.LPOS.service.RestaurantService;



@RequestMapping(value="/restaurant")
@RestController
public class RestaurantRestController {
	
	@Autowired
	private RestaurantService restaurantService;
		
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getRestaurant (
			@RequestParam(value="name", required=false) String name, 
			@RequestParam(value="type", required=false) String type){
		//restaurantService.getRestaurantByName(name);
		ResponseDTO response = new ResponseDTO();
		if(name != null && name.length() != 0) {
			List<Restaurant> restaurant = restaurantService.getRestaurantsContainsName(name);
			if(restaurant == null || restaurant.size() == 0) {
				response.setStatus(HttpStatus.NOT_FOUND.name());
				return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
			}
			for(Restaurant r : restaurant) {
				r.changeTimeSlotFormat();
			}
			response.setData(restaurant);
		} else if(type != null && type.length() != 0) {
			List<Restaurant> restaurant = restaurantService.getRestaurantsContainsType(type);
			if(restaurant == null || restaurant.size() == 0) {
				response.setStatus(HttpStatus.NOT_FOUND.name());
				return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
			}
			for(Restaurant r : restaurant) {
				r.changeTimeSlotFormat();
			}
			response.setData(restaurant);
		} else {
			Iterable<Restaurant> restaurant = restaurantService.getAllRestaurant();
			for(Restaurant r : restaurant) {
				r.changeTimeSlotFormat();
			}
			response.setData(restaurant);
		}
		
		response.setStatus(HttpStatus.OK.name());

		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getSpecifiedRestaurant (@PathVariable("id") Integer id) {
		Restaurant r = restaurantService.getRestaurantById(id);
		ResponseDTO response = new ResponseDTO();
		if(r ==  null) {
			response.setStatus(HttpStatus.NOT_FOUND.name());
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
		}
		r.changeTimeSlotFormat();
		response.setData(r);
		response.setStatus(HttpStatus.OK.name());
	    return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST) 
	public ResponseEntity<ResponseDTO> createRestaurant (@RequestBody Restaurant restaurant) {
		ResponseDTO response = new ResponseDTO();

		if(restaurantService.getRestaurantByName(restaurant.getName()) != null) {
			response.setStatus(HttpStatus.CONFLICT.name());
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.CONFLICT);
		}
		Restaurant r = restaurantService.saveRestaurant(restaurant);
		response.setStatus(HttpStatus.OK.name());
		response.setData(r);
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
		
	}
}