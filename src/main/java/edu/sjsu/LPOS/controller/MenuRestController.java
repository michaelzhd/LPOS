package edu.sjsu.LPOS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.LPOS.DTO.ResponseDTO;
import edu.sjsu.LPOS.domain.Menu;
import edu.sjsu.LPOS.domain.Restaurant;
import edu.sjsu.LPOS.service.MenuService;
import edu.sjsu.LPOS.service.RestaurantService;


@RequestMapping(value="/menu")
@RestController
public class MenuRestController {
	@Autowired
	private MenuService menuService;
	@Autowired
	private RestaurantService restaurantService;
	
	@RequestMapping(value = "/{restaurantId}", method = RequestMethod.POST) 
	public ResponseEntity<ResponseDTO> createRestaurant (@PathVariable("restaurantId") Integer restaurantId, @RequestBody Menu menu) {
		ResponseDTO response = new ResponseDTO();
		Restaurant r = restaurantService.getRestaurantById(restaurantId);
		if( r == null) {
			response.setStatus(HttpStatus.NOT_FOUND.name());
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
		}
		//Restaurant customer = entityManager.getReference(Restaurant.class, menu.getRestaurantId());
		menu.setRestaurant(r);
		Menu m = menuService.saveMenu(menu);
		response.setStatus(HttpStatus.OK.name());
		response.setData(m);
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
}
