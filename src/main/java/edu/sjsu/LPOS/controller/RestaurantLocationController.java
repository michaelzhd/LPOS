package edu.sjsu.LPOS.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.LPOS.beans.ResponseBean;
import edu.sjsu.LPOS.domain.RestaurantLocation;
import edu.sjsu.LPOS.service.RestaurantLocationService;

@RestController
@RequestMapping("restaurant")
public class RestaurantLocationController {

	@Autowired
	private RestaurantLocationService restaurantLocationService;
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<ResponseBean> getRestaurantLocationById(@PathVariable("id") long id) {
		RestaurantLocation restaurantLocation = restaurantLocationService.findRestaurantById(id);
		Map<String, Object> map = new HashMap<>();
		map.put("restaurantlocation", restaurantLocation);
		ResponseBean respBean = new ResponseBean();
		respBean.setStatus("OK");
		respBean.setData(map);
		return new ResponseEntity<ResponseBean>(respBean, HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
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
}
