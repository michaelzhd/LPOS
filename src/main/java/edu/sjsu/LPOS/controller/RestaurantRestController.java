package edu.sjsu.LPOS.controller;

import java.sql.SQLException;
import java.util.ArrayList;
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
	@Autowired
	private RestaurantLocationServiceImpl restaurantLocationService;
	
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
				r.setTimeSlotsForAPI();
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
			}
		} else {
			restaurant = (List<Restaurant>) restaurantService.getAllRestaurant();
			for(Restaurant r : restaurant) {
				r.setFavoriteFlag(favorites);
				r.setTimeSlotsForAPI();
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
		r.setTimeSlotsForAPI();
		response.setData(r);
		response.setStatus(HttpStatus.OK.name());
	    return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST) 
	public ResponseEntity<ResponseDTO> createRestaurant (@RequestBody Restaurant restaurant) {
		ResponseDTO response = new ResponseDTO();

		if(restaurantService.getRestaurantByAddress(restaurant.getAddress()) != null) {
			response.setStatus(HttpStatus.CONFLICT.name());
			response.setMessage(restaurant.getName() +"["+restaurant.getAddress()+"]"+ "already exist.");
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.CONFLICT);
		}
		List<TimeSlot> listSlot = new ArrayList<TimeSlot>();
		for(String time : restaurant.getSlots()) {
			TimeSlot s = timeSlotService.getTimeSlotByTime(time);
			if(s == null) {
				s = new TimeSlot();
				s.setTimeSlot(time);
				s = timeSlotService.saveTimeSlot(s);
			}
			listSlot.add(s);
		}
		restaurant.setTimeslot(listSlot);
		Restaurant r = restaurantService.saveRestaurant(restaurant);
		RestaurantLocation location = new RestaurantLocation();
		location.setAddress(restaurant.getAddress());
		location.setRid(r.getId());
		RestTemplate restTemplate = new RestTemplate();
		final String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+restaurant.getAddress();
		String result = restTemplate.getForObject(url, String.class);
		JSONObject obj = new JSONObject(result);
		JSONObject jsonobject = (JSONObject) obj.getJSONArray("results").get(0);

	    JSONObject geometry = jsonobject.getJSONObject("geometry");
	    JSONObject googleloaction = geometry.getJSONObject("location");
		    

		location.setLocation(new Location((double)googleloaction.get("lng"),(double)googleloaction.get("lat")));
		restaurantLocationService.createRestaurantLocation(location);
		System.out.println(location);
		response.setStatus(HttpStatus.OK.name());
		response.setData(r);
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
