package edu.sjsu.LPOS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import edu.sjsu.LPOS.domain.Restaurant;


public interface RestaurantRepository extends CrudRepository<Restaurant, Integer>{
	
	List<Restaurant> findByName(String name);
	Restaurant findById(Integer id);
	List<Restaurant> findByType(String type);
	Restaurant findByAddress(String address);
	@Query("SELECT r FROM Restaurant r where r.name LIKE %:key%")
    List<Restaurant> findByRestaurantContainsName(@Param("key") String name);
	
	@Query("SELECT r FROM Restaurant r where r.type LIKE %:key%")
    List<Restaurant> findByRestaurantContainsType(@Param("key") String type);
}
