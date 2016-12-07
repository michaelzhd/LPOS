package edu.sjsu.LPOS.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.sjsu.LPOS.domain.RestaurantLocation;

public interface RestaurantLocationRepository extends MongoRepository<RestaurantLocation, String>{
	RestaurantLocation findByRid(long rid);
	
	
}
