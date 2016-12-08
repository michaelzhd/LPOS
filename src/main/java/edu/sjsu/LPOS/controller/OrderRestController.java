package edu.sjsu.LPOS.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.LPOS.DTO.MenuDTO;
import edu.sjsu.LPOS.DTO.OrderUpdateDTO;
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

@RequestMapping(value="/order")
@RestController
public class OrderRestController {
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private TableReserveService tableReserveService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private MenuService menuService;
	
	@RequestMapping(value = "/takeout", method = RequestMethod.GET) 
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
		
		List<TableReserve> tableReserve = tableReserveService.findByUserIdAndDateAndTakeOut(user.getId(), start, end);
		System.out.println(tableReserve);
		if(tableReserve == null || tableReserve.size() == 0) {
			response.setMessage("Not find any takeout order");
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
	
	@RequestMapping(value = "/checkout", method = RequestMethod.PUT) 
	public ResponseEntity<ResponseDTO> checkout (@RequestBody OrderUpdateDTO orderUpdateDTO) {
		ResponseDTO response = new ResponseDTO();
		TableReserve tableReserve = tableReserveService.getReserveById(orderUpdateDTO.getOrderId());
		if(tableReserve == null) {
			response.setMessage("Not find order");
			response.setStatus(HttpStatus.NOT_FOUND.name());
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
		}
		tableReserve.setStatus("completed");
		tableReserveService.createReserve(tableReserve);
		response.setData(tableReserve);
		response.setStatus(HttpStatus.OK.name());
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}

}
