package edu.sjsu.LPOS.repository;


import org.springframework.data.repository.CrudRepository;

import edu.sjsu.LPOS.domain.Menu;


public interface MenuRepository extends CrudRepository<Menu, Integer>{
	Menu findByName(String name);
	
	Iterable<Menu> findByStatus(String status);
	
	Menu findByRestaurant_id(Integer id);
	
	Iterable<Menu> findByRestaurant_idAndStatus(String id, String status);
	//TODO
	Iterable<Menu> findByPrice(String price);
}