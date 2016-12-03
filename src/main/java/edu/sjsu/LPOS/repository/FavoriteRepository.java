package edu.sjsu.LPOS.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


import edu.sjsu.LPOS.domain.Favorite;

public interface FavoriteRepository extends CrudRepository<Favorite, Integer>{
	
	List<Favorite> findByUser_id(Integer id);
	
	Favorite findByUser_idAndRestaurant_id(Integer userId, Integer restaurantid);
}
