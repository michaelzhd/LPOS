package edu.sjsu.LPOS.controller;

import java.sql.SQLException;
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
import org.springframework.web.bind.annotation.RestController;

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
	
	@RequestMapping(value = "/info/{restaurantId}", method = RequestMethod.POST) 
	public ResponseEntity<ResponseDTO> createTableInfo (@PathVariable("restaurantId") Integer restaurantId, @RequestBody TableInfo tableinfo) {
		ResponseDTO response = new ResponseDTO();
		Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
		List<TimeSlot> listSlot = new ArrayList<TimeSlot>();
		for(String time : tableinfo.getSlots()) {
			TimeSlot s = timeSlotService.getTimeSlotByTime(time);
			listSlot.add(s);
		}
		tableinfo.setTimeslot(listSlot);
		tableinfo.setRestaurant(restaurant);
		TableInfo t = tableInfoService.saveTableInfo(tableinfo);
		response.setData(t);
		response.setStatus(HttpStatus.OK.name());
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/info/{restaurantId}", method = RequestMethod.GET) 
	public ResponseEntity<ResponseDTO> getTableInfo (@PathVariable("restaurantId") Integer restaurantId) {
		ResponseDTO response = new ResponseDTO();
		List<TableInfo> tableInfos = tableInfoService.getTableInfoByRestaurant_id(restaurantId);

		for(TableInfo t : tableInfos) {
			String[] str = new String[t.getTimeslot().size()];
			int i = 0;
			for(TimeSlot s : t.getTimeslot()) {
				str[i] = s.getTimeSlot();
				i++;
			}
			t.setSlots(str);
		}
		response.setData(tableInfos);
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
	
	@RequestMapping(value = "/reserve/{tableId}", method = RequestMethod.POST) 
	public ResponseEntity<ResponseDTO> createTableReserveInfo (HttpServletRequest request, @PathVariable("tableId") Integer tableId, @RequestBody TableReserve tableReserve) {
		ResponseDTO response = new ResponseDTO();
		TableInfo tableInfo = tableInfoService.getTableInfoByTableId(tableId);
		
		tableReserve.setTableInfo(tableInfo);
		
		Integer userId = (Integer) request.getAttribute("userId");
		System.out.println(userId);
		userId = 1;
		User user = userService.getUserById(userId);
		tableReserve.setUser(user);
		
		tableReserveService.createReserve(tableReserve);
		
		response.setData(tableReserve);
		response.setStatus(HttpStatus.OK.name());
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
	
}

