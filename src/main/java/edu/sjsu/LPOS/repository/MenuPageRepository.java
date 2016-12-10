package edu.sjsu.LPOS.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import edu.sjsu.LPOS.domain.Menu;

public interface MenuPageRepository extends PagingAndSortingRepository<Menu,Integer> {
	Page<Menu> findByRestaurant_id(Integer id, Pageable pageable);

}
