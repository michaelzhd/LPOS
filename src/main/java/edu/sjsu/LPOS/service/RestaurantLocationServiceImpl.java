package edu.sjsu.LPOS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.LPOS.domain.RestaurantLocation;
import edu.sjsu.LPOS.repository.RestaurantLocationRepository;

@Service(value="RestaurantLocationService")
public class RestaurantLocationServiceImpl implements RestaurantLocationService {

	@Autowired
	private RestaurantLocationRepository restaurantLocationRepository;
	
	@Override
	public RestaurantLocation findRestaurantById(long id) {
		return restaurantLocationRepository.findByRid(id);
	}

	@Override
	public List<RestaurantLocation> findRestaurantsByCity(String city) {
		return restaurantLocationRepository.findByCity(city);
	}
	
	@Override
	public List<RestaurantLocation> findAllRestaurant() {
		return restaurantLocationRepository.findAll()
;	}

}
