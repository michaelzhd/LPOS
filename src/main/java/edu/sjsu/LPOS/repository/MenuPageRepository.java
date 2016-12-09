package edu.sjsu.LPOS.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import edu.sjsu.LPOS.domain.Menu;

public interface MenuPageRepository extends PagingAndSortingRepository<Menu,Integer> {
	List<Menu> findByRestaurant_id(Integer id);

}
