package edu.sjsu.LPOS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import edu.sjsu.LPOS.domain.RestaurantLocation;
import edu.sjsu.LPOS.repository.RestaurantLocationRepository;

@Service(value="RestaurantLocationService")
public class RestaurantLocationServiceImpl implements RestaurantLocationService {

	@Autowired private RestaurantLocationRepository restaurantLocationRepository;
	@Autowired private MongoOperations mongoOperations;
	
	@Override
	public RestaurantLocation findRestaurantByRid(long rid) {
		return restaurantLocationRepository.findByRid(rid);
	}

//	@Override
//	public List<RestaurantLocation> findRestaurantsByCity(String city) {
//		return restaurantLocationRepository.findByCity(city);
//	}
	
	@Override
	public List<RestaurantLocation> findAllRestaurant() {
		return restaurantLocationRepository.findAll()
;	}

	@Override
	public void createRestaurantLocation(RestaurantLocation restaurantLocation) {
		
		mongoOperations.save(restaurantLocation);		
	}

	@Override
	public void updateRestaurantLocation(long rid, RestaurantLocation restaurantLocation) {
//		Query query = new Query();
//		query.addCriteria(Criteria.where("rid").is(rid));
		
//		Update update = new Update();
//		update.
//		mongoOperations.updateFirst(query, restaurantLocation, RestaurantLocation.class);
	}

	@Override
	public List<RestaurantLocation> findRestaurantsByMileDistance(double longitude, double latitude, int distance) {
		Point from = new Point(longitude, latitude);
		Distance radius = new Distance(distance, Metrics.MILES);
		Circle circle = new Circle(from, radius);
		Query query = new Query();
		query.addCriteria(Criteria.where("location").withinSphere(circle));
		return mongoOperations.find(query, RestaurantLocation.class);
	}

	@Override
	public void deleteRestaurantLocation(long rid) {
		// TODO Auto-generated method stub
		
	}

}
