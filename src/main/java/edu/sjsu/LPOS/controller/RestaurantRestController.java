package edu.sjsu.LPOS.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import edu.sjsu.LPOS.domain.Favorite;
import edu.sjsu.LPOS.domain.Restaurant;
import edu.sjsu.LPOS.domain.User;
import edu.sjsu.LPOS.service.FavoriteService;
import edu.sjsu.LPOS.service.RestaurantService;



@RequestMapping(value="/restaurant")
@RestController
public class RestaurantRestController {
	
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private FavoriteService favoriteService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getRestaurant (
			HttpServletRequest request,
			@RequestParam(value="name", required=false) String name, 
			@RequestParam(value="type", required=false) String type){
		//restaurantService.getRestaurantByName(name);
		User user = (User) request.getAttribute("user");
		List<Favorite> favorites = new ArrayList<Favorite>();
		if(user != null) {
			System.out.println(user.toString());
			favorites = favoriteService.getFavoriteByUserId(user.getId());
		}
		ResponseDTO response = new ResponseDTO();
		List<Restaurant> restaurant;
		if(name != null && name.length() != 0) {
			restaurant = restaurantService.getRestaurantsContainsName(name);
			if(restaurant == null || restaurant.size() == 0) {
				response.setStatus(HttpStatus.NOT_FOUND.name());
				return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
			}
			for(Restaurant r : restaurant) {
				r.setFavoriteFlag(favorites);
				r.changeTimeSlotFormat();
			}
			
		} else if(type != null && type.length() != 0) {
			restaurant = restaurantService.getRestaurantsContainsType(type);
			if(restaurant == null || restaurant.size() == 0) {
				response.setStatus(HttpStatus.NOT_FOUND.name());
				return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
			}
			for(Restaurant r : restaurant) {
				r.setFavoriteFlag(favorites);
				r.changeTimeSlotFormat();
			}
		} else {
			restaurant = (List<Restaurant>) restaurantService.getAllRestaurant();
			for(Restaurant r : restaurant) {
				r.setFavoriteFlag(favorites);
				r.changeTimeSlotFormat();
			}
		}
		response.setData(restaurant);
		response.setStatus(HttpStatus.OK.name());

		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getSpecifiedRestaurant (
												HttpServletRequest request,
												@PathVariable("id") Integer id) {
		Restaurant r = restaurantService.getRestaurantById(id);
		User user = (User) request.getAttribute("user");
		if(user != null) {
			Favorite favorite = favoriteService.getFavoriteByUserIdAndRestaurantId(user.getId(), id);
			if(favorite != null) {
				r.setIsfavorite(true);
			}
		}
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
