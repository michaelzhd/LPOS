package edu.sjsu.LPOS.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.LPOS.DTO.ResponseDTO;
import edu.sjsu.LPOS.domain.Favorite;
import edu.sjsu.LPOS.domain.Restaurant;
import edu.sjsu.LPOS.domain.User;
import edu.sjsu.LPOS.service.FavoriteService;
import edu.sjsu.LPOS.service.RestaurantService;
import edu.sjsu.LPOS.service.UserService;

@RequestMapping(value="/favorite")
@RestController
public class FavoriteRestController {
	@Autowired
	private FavoriteService favoriteService;
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private UserService userService;
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/{restaurantId}", method = RequestMethod.POST)
	public ResponseEntity<ResponseDTO> createFavorite (HttpServletRequest request,@PathVariable("restaurantId") Integer restaurantId) {
		User user = (User) request.getAttribute("user");
		ResponseDTO response = new ResponseDTO();
		if(user == null) {
			response.setStatus(HttpStatus.NOT_FOUND.name());
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
		}
		System.out.println(user);

		
		Favorite favorite = favoriteService.getFavoriteByUserIdAndRestaurantId(user.getId(), restaurantId);
		if(favorite != null) {
			response.setStatus(HttpStatus.OK.name());
			response.setData(favorite);
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
		}

		Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
		if(restaurant == null) {
			response.setStatus(HttpStatus.NOT_FOUND.name());
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
		}
		
		Favorite f = favoriteService.saveFavorite(new Favorite(user, restaurant));
		f.getRestaurant().setIsfavorite(true);
		response.setStatus(HttpStatus.OK.name());
		response.setData(f);
		
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getFavoriteRestaurantList(HttpServletRequest request) {
		ResponseDTO response = new ResponseDTO();
		User user = (User) request.getAttribute("user");
		if(user == null) {
			response.setStatus(HttpStatus.NOT_FOUND.name());
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
		}
		List<Favorite> list = favoriteService.getFavoriteByUserId(user.getId());
		if(list == null || list.size() == 0) {
			response.setStatus(HttpStatus.NOT_FOUND.name());
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
		}
		List<Restaurant> restaurants = new ArrayList<Restaurant>();
		for(Favorite f : list) {
			restaurants.add(f.getRestaurant());
		}
		response.setData(restaurants);
		response.setStatus(HttpStatus.OK.name());
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value = "/{restaurantId}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseDTO> deleteFavorite(HttpServletRequest request, @PathVariable("restaurantId") Integer restaurantId) {
		ResponseDTO response = new ResponseDTO();
		User user = (User) request.getAttribute("user");
		if(user == null) {
			response.setStatus(HttpStatus.NOT_FOUND.name());
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
		}
		Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
		if(restaurant == null) {
			response.setStatus(HttpStatus.NOT_FOUND.name());
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
		}
		Favorite f = favoriteService.getFavoriteByUserIdAndRestaurantId(user.getId(), restaurantId);
		if(f != null) {
			favoriteService.deleteFavorite(f);
		}
		response.setStatus(HttpStatus.OK.name());
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
	
}
