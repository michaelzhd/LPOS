package edu.sjsu.LPOS.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.LPOS.beans.ResponseBean;
import edu.sjsu.LPOS.domain.RestaurantLocation;
import edu.sjsu.LPOS.service.RestaurantLocationService;

@RestController
@RequestMapping("restaurantLocation")
public class RestaurantLocationController {

	@Autowired private RestaurantLocationService restaurantLocationService;
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<ResponseBean> getRestaurantLocationById(@PathVariable("id") long id) {
		RestaurantLocation restaurantLocation = restaurantLocationService.findRestaurantByRid(id);
		Map<String, Object> map = new HashMap<>();
		map.put("restaurantlocation", restaurantLocation);
		ResponseBean respBean = new ResponseBean();
		respBean.setStatus("OK");
		respBean.setData(map);
		return new ResponseEntity<ResponseBean>(respBean, HttpStatus.OK);
	}
	
//	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="", method=RequestMethod.GET)
	public ResponseEntity<ResponseBean> getAllRestaurantLocations() {
		List<RestaurantLocation> restaurantLocations = restaurantLocationService.findAllRestaurant();
		Map<String, Object> map = new HashMap<>();
		map.put("restaurantlocations", restaurantLocations);
		ResponseBean respBean = new ResponseBean();
		respBean.setStatus("OK");
		respBean.setData(map);
		return new ResponseEntity<ResponseBean>(respBean, HttpStatus.OK);
	}
	
//	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value="findWithinMiles", method=RequestMethod.GET)
	public ResponseEntity<ResponseBean> getRestaurantLocationsByLocation(@RequestParam(value="longitude") float longitude,
			@RequestParam(value="latitude") float latitude, @RequestParam(value="distance") int distance) {
		List<RestaurantLocation> restaurantLocations = 
				restaurantLocationService.findRestaurantsByMileDistance(longitude, latitude, distance);
		Map<String, Object> map = new HashMap<>();
		map.put("restaurantlocations", restaurantLocations);
		ResponseBean respBean = new ResponseBean();
		respBean.setStatus("OK");
		respBean.setData(map);
		return new ResponseEntity<ResponseBean>(respBean, HttpStatus.OK);
	}
	
//	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value="findIdWithinMiles", method=RequestMethod.GET)
	public ResponseEntity<ResponseBean> getRestaurantIdsLocationsByLocation(@RequestParam(value="longitude") float longitude,
			@RequestParam(value="latitude") float latitude, @RequestParam(value="distance") int distance) {
		List<RestaurantLocation> restaurantLocations = 
				restaurantLocationService.findRestaurantsByMileDistance(longitude, latitude, distance);
		List<Long> restaurantIds = new ArrayList<>();
		for (RestaurantLocation restLocation: restaurantLocations) {
			restaurantIds.add(restLocation.getRid());
		}
		Map<String, Object> map = new HashMap<>();
		map.put("restaurantIds", restaurantIds);
		ResponseBean respBean = new ResponseBean();
		respBean.setStatus("OK");
		respBean.setData(map);
		return new ResponseEntity<ResponseBean>(respBean, HttpStatus.OK);
	}
	
//	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="", method=RequestMethod.POST)
	public ResponseEntity<ResponseBean> createRestaurantLocations(@RequestBody RestaurantLocation restaurantLocation) {
		ResponseBean respBean = new ResponseBean();
		if (restaurantLocation == null) {
			respBean.setStatus("Failed");
			respBean.setMessage("Failed to create the restaurant location. Invalid input.");
			return new ResponseEntity<ResponseBean>(respBean, HttpStatus.BAD_REQUEST);
		}
		restaurantLocationService.createRestaurantLocation(restaurantLocation);
		RestaurantLocation savedRestaurantLocation = restaurantLocationService.findRestaurantByRid(restaurantLocation.getRid());
		
		
		if (savedRestaurantLocation == null) {
			respBean.setStatus("Failed");
			respBean.setMessage("Failed to create the restaurant location.");
			return new ResponseEntity<ResponseBean>(respBean, HttpStatus.CONFLICT);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("restaurantlocation", savedRestaurantLocation);
		
		respBean.setStatus("OK");
//		respBean.setData(map);
		return new ResponseEntity<ResponseBean>(respBean, HttpStatus.CREATED);
	}
}
