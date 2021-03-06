package edu.sjsu.LPOS.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import edu.sjsu.LPOS.DTO.ResponseDTO;
import edu.sjsu.LPOS.domain.Favorite;
import edu.sjsu.LPOS.domain.Location;
import edu.sjsu.LPOS.domain.Restaurant;
import edu.sjsu.LPOS.domain.RestaurantLocation;
import edu.sjsu.LPOS.domain.TimeSlot;
import edu.sjsu.LPOS.domain.User;
import edu.sjsu.LPOS.service.FavoriteService;
import edu.sjsu.LPOS.service.RestaurantLocationServiceImpl;
import edu.sjsu.LPOS.service.RestaurantService;
import edu.sjsu.LPOS.service.TimeSlotService;



@RequestMapping(value="/restaurant")
@RestController
public class RestaurantRestController {
	
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private FavoriteService favoriteService;
	@Autowired
	private TimeSlotService timeSlotService;

	
	@RequestMapping(value = "/anonymous", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getRestaurantWithoutLogin (
			HttpServletRequest request,
			@RequestParam(value="name", required=false) String name, 
			@RequestParam(value="type", required=false) String type,
			@RequestParam(value="latitude", required=false) Double latitude,
			@RequestParam(value="longtitude", required=false) Double longtitude){
		//restaurantService.getRestaurantByName(name);
		
		ResponseDTO response = new ResponseDTO();
		List<Restaurant> restaurant = new ArrayList<Restaurant>();
		if(name != null && name.length() != 0) {
			restaurant = restaurantService.getRestaurantsContainsName(name);
			if(restaurant == null || restaurant.size() == 0) {
				response.setStatus(HttpStatus.NOT_FOUND.name());
				return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
			}
			for(Restaurant r : restaurant) {
				r.setTimeSlotsForAPI();
				if(latitude != null && longtitude != null) {
					r.setDistance(calculateDistance(r.getLatitude(), r.getLongtitude(), longtitude, longtitude));
				}
			}
			
		} else if(type != null && type.length() != 0) {
			restaurant = restaurantService.getRestaurantsContainsType(type);
			if(restaurant == null || restaurant.size() == 0) {
				response.setStatus(HttpStatus.NOT_FOUND.name());
				return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
			}
			for(Restaurant r : restaurant) {
				r.setTimeSlotsForAPI();
				if(latitude != null && longtitude != null) {
					r.setDistance(calculateDistance(r.getLatitude(), r.getLongtitude(), longtitude, longtitude));
				}
			}
		} else {
			restaurant = (List<Restaurant>) restaurantService.getAllRestaurant();
			for(Restaurant r : restaurant) {
				r.setTimeSlotsForAPI();
				if(latitude != null && longtitude != null) {
					r.setDistance(calculateDistance(r.getLatitude(), r.getLongtitude(), longtitude, longtitude));
				}
			}
		}
		Collections.sort(restaurant);
		response.setData(restaurant);
		response.setStatus(HttpStatus.OK.name());

		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getRestaurant (
			HttpServletRequest request,
			@RequestParam(value="name", required=false) String name, 
			@RequestParam(value="type", required=false) String type,
			@RequestParam(value="latitude", required=false) Double latitude,
			@RequestParam(value="longtitude", required=false) Double longtitude){
		//restaurantService.getRestaurantByName(name);
		User user = (User) request.getAttribute("user");
		List<Favorite> favorites = new ArrayList<Favorite>();
		if(user != null) {
			System.out.println(user.toString());
			favorites = favoriteService.getFavoriteByUserId(user.getId());
		}
		ResponseDTO response = new ResponseDTO();
		List<Restaurant> restaurant = new ArrayList<Restaurant>();
		if(name != null && name.length() != 0) {
			restaurant = restaurantService.getRestaurantsContainsName(name);
			if(restaurant == null || restaurant.size() == 0) {
				response.setStatus(HttpStatus.NOT_FOUND.name());
				return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
			}
			for(Restaurant r : restaurant) {
				r.setFavoriteFlag(favorites);
				r.setTimeSlotsForAPI();
				if(latitude != null && longtitude != null) {
					r.setDistance(calculateDistance(r.getLatitude(), r.getLongtitude(), longtitude, longtitude));
				}
			}
			
		} else if(type != null && type.length() != 0) {
			restaurant = restaurantService.getRestaurantsContainsType(type);
			if(restaurant == null || restaurant.size() == 0) {
				response.setStatus(HttpStatus.NOT_FOUND.name());
				return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
			}
			for(Restaurant r : restaurant) {
				r.setFavoriteFlag(favorites);
				r.setTimeSlotsForAPI();
				if(latitude != null && longtitude != null) {
					r.setDistance(calculateDistance(r.getLatitude(), r.getLongtitude(), longtitude, longtitude));
				}
			}
		} else {
			restaurant = (List<Restaurant>) restaurantService.getAllRestaurant();
			for(Restaurant r : restaurant) {
				r.setFavoriteFlag(favorites);
				r.setTimeSlotsForAPI();
				if(latitude != null && longtitude != null) {
					r.setDistance(calculateDistance(r.getLatitude(), r.getLongtitude(), longtitude, longtitude));
				}
			}
		}
		Collections.sort(restaurant);
		response.setData(restaurant);
		response.setStatus(HttpStatus.OK.name());

		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
	private double EARTH_RADIUS = 6378.137;   
	private static double rad(double d)   
	{   
	     return d * Math.PI / 180.0;   
	}
	public double calculateDistance(double lat1, double lng1, double lat2, double lng2 ) {
		double radLat1 = rad(lat1);   
	    double radLat2 = rad(lat2);   
	    double a = radLat1 - radLat2;   
	    double b = rad(lng1) - rad(lng2);   
	    double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +   
	    Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));   
	    s = s * EARTH_RADIUS;   
	    s = Math.round(s * 10000) / 10000;   
	    return s;   

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
		r.setTimeSlotsForAPI();
		response.setData(r);
		response.setStatus(HttpStatus.OK.name());
	    return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/timeslot", method = RequestMethod.POST) 
	public ResponseEntity<ResponseDTO> createTimeSlot (@RequestBody TimeSlot timeslot) throws SQLException {
		ResponseDTO response = new ResponseDTO();
		HttpStatus status = HttpStatus.OK;
		try {
			TimeSlot slot = timeSlotService.saveTimeSlot(timeslot);
			response.setData(slot);
		} catch(Exception e) {
			response.setMessage(e.getCause().getMessage());
			status = HttpStatus.EXPECTATION_FAILED;
			response.setStatus(HttpStatus.EXPECTATION_FAILED.name());
		}
		return new ResponseEntity<ResponseDTO>(response, status);
	}
	
	@RequestMapping(value = "/timeslot", method = RequestMethod.GET) 
	public ResponseEntity<ResponseDTO> getAllAvailableTimeSlot () {
		ResponseDTO response = new ResponseDTO();
		Iterable<TimeSlot> timeslots= timeSlotService.getAllTimeSlot();
		response.setData(timeslots);
		response.setStatus(HttpStatus.OK.name());
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
}
