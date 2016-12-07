package edu.sjsu.LPOS.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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


import edu.sjsu.LPOS.DTO.MenuDTO;
import edu.sjsu.LPOS.DTO.OrderMenuDTO;
import edu.sjsu.LPOS.DTO.ReservationDTO;
import edu.sjsu.LPOS.DTO.ReservationResponseDTO;
import edu.sjsu.LPOS.DTO.ResponseDTO;
import edu.sjsu.LPOS.domain.Menu;
import edu.sjsu.LPOS.domain.Order;
import edu.sjsu.LPOS.domain.Restaurant;
import edu.sjsu.LPOS.domain.TableReserve;
import edu.sjsu.LPOS.domain.User;
import edu.sjsu.LPOS.service.MenuService;
import edu.sjsu.LPOS.service.OrderService;
import edu.sjsu.LPOS.service.RestaurantService;
import edu.sjsu.LPOS.service.TableReserveService;


@RequestMapping(value="/table")
@RestController
public class TableRestController {

	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private TableReserveService tableReserveService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private MenuService menuService;

	@RequestMapping(value = "/reserve/{restaurantId}", method = RequestMethod.POST) 
	public ResponseEntity<ResponseDTO> createTableReserveInfo (
								HttpServletRequest request, 
								@PathVariable("restaurantId") Integer restaurantId, 
								@RequestBody ReservationDTO reservationDTO) {
		ResponseDTO response = new ResponseDTO();
		ReservationResponseDTO data = new ReservationResponseDTO();
		Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
		System.out.println(reservationDTO);
		if(restaurant == null) {
			response.setMessage("Not find restaurant by id");
			response.setStatus(HttpStatus.BAD_REQUEST.name());
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.BAD_REQUEST);
		}
		data.setRestaurant(restaurant);
		if(!reservationDTO.getTakeOut()) {
			if(reservationSize(restaurant, reservationDTO.getDate(), reservationDTO.getTimeSlot()) < reservationDTO.getPeople()) {
				response.setMessage("Not enough space to reservation");
				response.setStatus(HttpStatus.CONFLICT.name());
				return new ResponseEntity<ResponseDTO>(response, HttpStatus.CONFLICT);
			}
		}
		User user = (User) request.getAttribute("user");
		TableReserve tableReserve = new TableReserve(user, restaurant, reservationDTO);
		tableReserve = tableReserveService.createReserve(tableReserve);
		List<MenuDTO> menu_order = new ArrayList<MenuDTO>();
		if(reservationDTO.getTakeOut()) {
			double sum = 0;
			tableReserve.setTakeOut(true);
			for(OrderMenuDTO menu :  reservationDTO.getMenus()) {
				Order order = new Order(tableReserve.getId(), menu.getMenuId(), menu.getQuatity());
				Order o = orderService.saveOrder(order);
				System.out.println(o);
				Menu m = menuService.getMenuById(menu.getMenuId());
				sum += m.getPrice() * menu.getQuatity();
				MenuDTO menuDTO = new MenuDTO(m, menu.getQuatity());
				menu_order.add(menuDTO);
			}
			tableReserve.setPrice(sum);
			
		}
		data.setReservation(tableReserve);
		if(reservationDTO.getTakeOut()) {
			data.setMenus(menu_order);
		}
		System.out.println(data);
		tableReserveService.createReserve(tableReserve);
		//orderService.saveOrderList(menu_order);
		response.setData(data);
		response.setStatus(HttpStatus.OK.name());
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/reserve", method = RequestMethod.GET) 
	public ResponseEntity<ResponseDTO> getTableReserveInfo (
			HttpServletRequest request, 
			@RequestParam(value="start", required=false) String start, 
			@RequestParam(value="end", required=false) String end) {
		ResponseDTO response = new ResponseDTO();
		User user = (User) request.getAttribute("user");
		if(start == null) {
			start = "0";
		}
		if(end == null) {
			end = "9999-99-99";
		}
		List<ReservationResponseDTO> reservationResponseDTO = new ArrayList<>();
		
		List<TableReserve> tableReserve = tableReserveService.findByUserIdAndDateAndReservation(user.getId(), start, end);
		System.out.println(tableReserve);
		if(tableReserve == null || tableReserve.size() == 0) {
			response.setMessage("Not find reservation information");
			response.setStatus(HttpStatus.NOT_FOUND.name());
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
		}
		
		for(TableReserve t: tableReserve) {
			ReservationResponseDTO r = new ReservationResponseDTO(t.getRestaurant(), t);
			List<Order> orders = orderService.getMenuListByReservationId(t.getId());
			List<MenuDTO> menus = new ArrayList<>();
			for(Order o : orders) {
				Menu m = menuService.getMenuById(o.getMenuId());
				MenuDTO menuDTO = new MenuDTO(m, o.getQuatity());
				menus.add(menuDTO);
			}
			r.setMenus(menus);
			reservationResponseDTO.add(r);
		}
			
		response.setData(reservationResponseDTO);
		response.setStatus(HttpStatus.OK.name());
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}

	
	
	
	private int reservationSize(Restaurant restaurant, String date, String timeSlot) {
		List<TableReserve> list = tableReserveService.findTableReservationByRestaurantIdAndDate(restaurant.getId(), date, timeSlot);
		int sum = 0;
		for(TableReserve t: list ) {
			sum += t.getPeople();
		}
		System.out.println("Remain capacity["+sum+"] in restaurant: " + restaurant.toString());
		return restaurant.getCapacity() - sum;
	}
	
	@RequestMapping(value = "/available/{restaurantId}", method = RequestMethod.GET) 
	public ResponseEntity<ResponseDTO> getAvailableTableReserveInfo (
			HttpServletRequest request, 
			@PathVariable("restaurantId") Integer restaurantId,
			@RequestParam(value="date", required=true) String date,
			@RequestParam(value="timeslot", required=false) String timeslot) {
		ResponseDTO response = new ResponseDTO();

		Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
		if(restaurant == null) {
			response.setMessage("Not find restaurant by id");
			response.setStatus(HttpStatus.BAD_REQUEST.name());
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.BAD_REQUEST);
		}
		int capacity = reservationSize(restaurant, date, timeslot);
		Map<String, Integer> map = new LinkedHashMap<>();
		map.put("available", capacity);
		response.setData(map);
		response.setStatus(HttpStatus.OK.name());
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
	
	
}

