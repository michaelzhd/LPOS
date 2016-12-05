package edu.sjsu.LPOS.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import edu.sjsu.LPOS.domain.Order;

public interface OrderRepository extends CrudRepository<Order, Integer>{
	List<Order> findByTableReserveId(Integer id);
	
	Order findByTableReserveIdAndMenuId(Integer reservationId, Integer menuId);
}
