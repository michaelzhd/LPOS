package edu.sjsu.LPOS.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import edu.sjsu.LPOS.DTO.CreateMenuDTO;
import edu.sjsu.LPOS.DTO.MenuDTO;
import edu.sjsu.LPOS.DTO.ReservationResponseDTO;
import edu.sjsu.LPOS.DTO.ResponseDTO;
import edu.sjsu.LPOS.domain.Favorite;
import edu.sjsu.LPOS.domain.Location;
import edu.sjsu.LPOS.domain.Menu;
import edu.sjsu.LPOS.domain.Order;
import edu.sjsu.LPOS.domain.Restaurant;
import edu.sjsu.LPOS.domain.RestaurantLocation;
import edu.sjsu.LPOS.domain.TableReserve;
import edu.sjsu.LPOS.domain.TimeSlot;
import edu.sjsu.LPOS.domain.User;
import edu.sjsu.LPOS.service.FavoriteService;
import edu.sjsu.LPOS.service.MenuService;
import edu.sjsu.LPOS.service.OrderService;
import edu.sjsu.LPOS.service.RestaurantLocationServiceImpl;
import edu.sjsu.LPOS.service.RestaurantService;
import edu.sjsu.LPOS.service.TableReserveService;
import edu.sjsu.LPOS.service.TimeSlotService;

@CrossOrigin(origins="*")
@RequestMapping(value="/management")
@RestController
public class ManagementRestController {
	
	@Autowired private RestaurantService restaurantService;
	@Autowired private TimeSlotService timeSlotService;
	@Autowired private RestaurantLocationServiceImpl restaurantLocationService;
	@Autowired private MenuService menuService;
	@Autowired private TableReserveService tableReserveService;
	@Autowired private OrderService orderService;
	@Autowired private FavoriteService favoriteService;
	
	@RequestMapping(value = "/restaurant", method = RequestMethod.POST) 
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
		

		RestTemplate restTemplate = new RestTemplate();
		final String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+restaurant.getAddress();
		String result = restTemplate.getForObject(url, String.class);
		JSONObject obj = new JSONObject(result);
		JSONObject jsonobject = (JSONObject) obj.getJSONArray("results").get(0);

	    JSONObject geometry = jsonobject.getJSONObject("geometry");
	    JSONObject googleloaction = geometry.getJSONObject("location");
	    
	    restaurant.setLatitude((double)googleloaction.get("lat"));
	    restaurant.setLongtitude((double)googleloaction.get("lng"));
	    
	    Restaurant r = restaurantService.saveRestaurant(restaurant);    
	    
		RestaurantLocation location = new RestaurantLocation();
		location.setAddress(restaurant.getAddress());
		location.setRid(r.getId());
		location.setLocation(new Location((double)googleloaction.get("lng"),(double)googleloaction.get("lat")));
		restaurantLocationService.createRestaurantLocation(location);
		System.out.println(location);
		response.setStatus(HttpStatus.OK.name());
		response.setData(r);
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "restaurant/id/{id}", method=RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> updateRestaurant(@RequestBody Restaurant restaurant) {
		ResponseDTO response = new ResponseDTO();
		if (restaurant == null) {
			response.setStatus("FAILED");
			response.setMessage("BAD INPUT, cannot map int A Restaurant object.");
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.BAD_REQUEST);
		}
		Restaurant original = restaurantService.getRestaurantById(restaurant.getId());
		if (restaurant.getCapacity() != 0) {
			original.setCapacity(restaurant.getCapacity());
		}
		if (restaurant.getDescription() != null) {
			original.setDescription(restaurant.getDescription());
		}
		if (restaurant.getName() != null) {
			original.setName(restaurant.getName());
		}
		if (restaurant.getOpentime() != null) {
			original.setOpentime(restaurant.getOpentime());
		}
		if (restaurant.getMenu() != null) {
			original.setMenu(restaurant.getMenu());
		}
		if (restaurant.getTimeslot() != null) {
			original.setTimeslot(restaurant.getTimeslot());
		}
		try {
			restaurantService.saveRestaurant(original);
		} catch(Exception ex) {
			response.setStatus("FAILED");
			response.setMessage("An error happend while updating restaurant info, please try again.");
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.CONFLICT);
		}
		response.setStatus("OK");
		response.setData(original);
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}

	
	@RequestMapping(value = "/menu/{restaurantId}", method = RequestMethod.POST) 
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

	@RequestMapping(value = "/menu", method = RequestMethod.PUT) 
	public ResponseEntity<ResponseDTO> updateMenu (@RequestBody CreateMenuDTO createMenuDTO) {
		ResponseDTO response = new ResponseDTO();
		Restaurant r = restaurantService.getRestaurantById(createMenuDTO.getRestaurantId());
		if( r == null) {
			response.setStatus(HttpStatus.NOT_FOUND.name());
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
		}
		Menu menu = createMenuDTO.getMenu();
		menu.setRestaurant(r);
		Menu m = menuService.saveMenu(menu);
		response.setStatus(HttpStatus.OK.name());
		response.setData(m);
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/menu/{id}", method = RequestMethod.GET) 
	public ResponseEntity<ResponseDTO> getMenuById (@PathVariable("id") int id) {
		ResponseDTO response = new ResponseDTO();
		Menu m = menuService.getMenuById(id);
		if (m == null) {
			response.setStatus("NOT_FOUND");
			response.setData(m);
			response.setMessage("The menu with id: " + id + " can not be found.");
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
		}
		response.setStatus(HttpStatus.OK.name());
		response.setData(m);
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/restaurant/id/{id}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getRestaurantById(HttpServletRequest request, @PathVariable("id") Integer id) {
		Restaurant r = restaurantService.getRestaurantById(id);
		User user = (User) request.getAttribute("user");
		if (user != null) {
			Favorite favorite = favoriteService.getFavoriteByUserIdAndRestaurantId(user.getId(), id);
			if (favorite != null) {
				r.setIsfavorite(true);
			}
		}
		ResponseDTO response = new ResponseDTO();
		if (r == null) {
			response.setStatus(HttpStatus.NOT_FOUND.name());
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
		}
		r.setTimeSlotsForAPI();
		response.setData(r);
		response.setStatus(HttpStatus.OK.name());
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/restaurant/name/{likename}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getRestaurantByName(HttpServletRequest request, @PathVariable("likename") String name) {
		List<Restaurant> rs = restaurantService.getRestaurantsContainsName(name);

		ResponseDTO response = new ResponseDTO();
		if (rs == null) {
			response.setStatus(HttpStatus.NOT_FOUND.name());
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.NOT_FOUND);
		}
		response.setData(rs);
		response.setStatus(HttpStatus.OK.name());
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/menu/restaurant/{id}", method = RequestMethod.GET) 
	public ResponseEntity<ResponseDTO> getMenuByRestaurantIdWithPage (@PathVariable("id") int id,
			@RequestParam("pageNumber") int pageNumber, @RequestParam("menusPerPage") int menusPerPage) {
		ResponseDTO response = new ResponseDTO();
		List<Menu> menus = menuService.getMenuByRestaurantId(id);
		if (menusPerPage == 0) {
			menusPerPage = 4;
		}
		List<Menu> result = new ArrayList<>();
		if (menus != null && menus.size() != 0) {
			for (int i = menusPerPage * (pageNumber - 1); i < menusPerPage * pageNumber && i < menus.size(); i++) {
				result.add(menus.get(i));
			}
		}
		
		response.setStatus(HttpStatus.OK.name());
		response.setData(result);
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
	
	
	
	@RequestMapping(value = "/order/{restaurantId}", method = RequestMethod.GET) 
	public ResponseEntity<ResponseDTO> getTableReserveInfo ( 
			@PathVariable("restaurantId") Integer restaurantId,
			@RequestParam(value="start", required=false) String start, 
			@RequestParam(value="end", required=false) String end) {
		ResponseDTO response = new ResponseDTO();
		if(start == null) {
			start = "0";
		}
		if(end == null) {
			end = "9999-99-99";
		}
		List<ReservationResponseDTO> reservationResponseDTO = new ArrayList<>();
		
		List<TableReserve> tableReserve = tableReserveService.findByRestaurantIdAndDateAndReservation(restaurantId, start, end);
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
	
}
