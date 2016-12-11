package edu.sjsu.LPOS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.LPOS.domain.Order;
import edu.sjsu.LPOS.repository.OrderRepository;

@Service
public class OrderService {
	@Autowired
	OrderRepository orderRepository;
	
	public List<Order> getMenuListByReservationId(Integer id) {
		return orderRepository.findByTableReserveId(id);
	}
	
	public Order getOrderById(Integer id) {
		return orderRepository.findById(id);
	}
	
	public Order getOrderByReservationIdAndMenuId(Integer reservationId, Integer menuId) {
		return orderRepository.findByTableReserveIdAndMenuId(reservationId, menuId);
	}
	
	public Order saveOrder(Order order) {
		return orderRepository.save(order);
	}
	
	public void deleteOrder(Order order) {
		orderRepository.delete(order);
	}
	
	public void saveOrderList(Iterable<Order> order) {
		orderRepository.save(order);
		return;
	}
	
}
