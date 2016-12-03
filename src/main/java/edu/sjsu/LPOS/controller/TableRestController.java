package edu.sjsu.LPOS.controller;

import java.sql.SQLException;
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

import com.google.gson.Gson;

import edu.sjsu.LPOS.DTO.ReservationDTO;
import edu.sjsu.LPOS.DTO.ResponseDTO;
import edu.sjsu.LPOS.domain.Restaurant;
import edu.sjsu.LPOS.domain.TableInfo;
import edu.sjsu.LPOS.domain.TableReserve;
import edu.sjsu.LPOS.domain.TimeSlot;
import edu.sjsu.LPOS.domain.User;
import edu.sjsu.LPOS.service.RestaurantService;
import edu.sjsu.LPOS.service.TableInfoService;
import edu.sjsu.LPOS.service.TableReserveService;
import edu.sjsu.LPOS.service.TimeSlotService;
import edu.sjsu.LPOS.service.UserService;


@RequestMapping(value="/table")
@RestController
public class TableRestController {
	
	@Autowired
	private TimeSlotService timeSlotService;
	@Autowired
	private TableInfoService tableInfoService;
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private UserService userService;
	@Autowired
	private TableReserveService tableReserveService;
	
//	@RequestMapping(value = "/info/{restaurantId}", method = RequestMethod.POST) 
//	public ResponseEntity<ResponseDTO> createTableInfo (@PathVariable("restaurantId") Integer restaurantId, @RequestBody TableInfo tableinfo) {
//		ResponseDTO response = new ResponseDTO();
//		Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
//		List<TimeSlot> listSlot = new ArrayList<TimeSlot>();
//		for(String time : tableinfo.getSlots()) {
//			TimeSlot s = timeSlotService.getTimeSlotByTime(time);
//			listSlot.add(s);
//		}
//		tableinfo.setTimeslot(listSlot);
//		tableinfo.setRestaurant(restaurant);
//		TableInfo t = tableInfoService.saveTableInfo(tableinfo);
//		response.setData(t);
//		response.setStatus(HttpStatus.OK.name());
//		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
//	}
//	
//	@RequestMapping(value = "/info/{restaurantId}", method = RequestMethod.GET) 
//	public ResponseEntity<ResponseDTO> getTableInfo (@PathVariable("restaurantId") Integer restaurantId) {
//		ResponseDTO response = new ResponseDTO();
//		List<TableInfo> tableInfos = tableInfoService.getTableInfoByRestaurant_id(restaurantId);
//
//		for(TableInfo t : tableInfos) {
//			String[] str = new String[t.getTimeslot().size()];
//			int i = 0;
//			for(TimeSlot s : t.getTimeslot()) {
//				str[i] = s.getTimeSlot();
//				i++;
//			}
//			t.setSlots(str);
//		}
//		response.setData(tableInfos);
//		response.setStatus(HttpStatus.OK.name());
//		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
//	}
//	 bgtry55666y7uh7jnmk bvggby
	
	@RequestMapping(value = "/reserve/{restaurantId}", method = RequestMethod.POST) 
	public ResponseEntity<ResponseDTO> createTableReserveInfo (
								HttpServletRequest request, 
								@PathVariable("restaurantId") Integer restaurantId, 
								@RequestBody ReservationDTO reservationDTO) {
		ResponseDTO response = new ResponseDTO();
		Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
		if(restaurant == null) {
			response.setMessage("Not find restaurant by id");
			response.setStatus(HttpStatus.BAD_REQUEST.name());
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.BAD_REQUEST);
		}
		
		if(reservationSize(restaurant, reservationDTO.getDate(), reservationDTO.getTimeSlot()) < reservationDTO.getPeople()) {
			response.setMessage("Not enough space to reservation");
			response.setStatus(HttpStatus.CONFLICT.name());
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.CONFLICT);
		}
		
		User user = (User) request.getAttribute("user");
		
		TableReserve tableReserve = new TableReserve(user, restaurant, reservationDTO);

		tableReserveService.createReserve(tableReserve);
		
		response.setData(tableReserve);
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
		List<TableReserve> tableReserve = tableReserveService.findByUserIdAndDate(user.getId(), start, end);
		System.out.println(tableReserve);
		if(tableReserve == null || tableReserve.size() == 0) {
			response.setMessage("Not find reservation information");
			response.setStatus(HttpStatus.NOT_FOUND.name());
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
		}
		
		response.setData(tableReserve);
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

