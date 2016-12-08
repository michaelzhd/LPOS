package edu.sjsu.LPOS.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.LPOS.domain.TableReserve;
import edu.sjsu.LPOS.repository.TableReserveRepository;

@Service
public class TableReserveService {
	@Autowired
	TableReserveRepository tableReserverepository;
	
	public TableReserve createReserve(TableReserve tableReserve) {
		return tableReserverepository.save(tableReserve);
	}
	
	public TableReserve getReserveById(Integer id) {
		return tableReserverepository.findOne(id);
	}
	
	public List<TableReserve> findTableReservationByUserId(Integer userId) {
		return tableReserverepository.findByUser_id(userId);
	}
	
	public List<TableReserve> findTableReservationByRestaurantIdAndDate(Integer restaurantId, String date, String timeSlot) {
		return tableReserverepository.findByRestaurant_idAndDateAndTimeSlot(restaurantId, date, timeSlot);
	}
	
	public List<TableReserve> findByUserIdAndDate(Integer userId, String start, String end) {
		return tableReserverepository.findByUserIdAndDate(userId, start, end);
	}
	
	public List<TableReserve> findByUserIdAndDateAndTakeOut(Integer userId, String start, String end) {
		return tableReserverepository.findByUserIdAndDateAndTakeOut(userId, start, end);
	}
	
	public List<TableReserve> findByUserIdAndDateAndReservation(Integer userId, String start, String end) {
		return tableReserverepository.findByUserIdAndDateAndReservation(userId, start, end);
	}
	
	public List<TableReserve> findByRestaurantIdAndDateAndReservation(Integer restaurantId, String start, String end) {
		return tableReserverepository.findByRestaurantIdAndDateAndReservation(restaurantId, start, end);
	}
//	public Integer reservationNumber(String timeSlot,Date date, Integer restaurantId ) {
//		return tableReserverepository.sumPeopleByRestaurantIdAndDateAndTimeSlot(timeSlot, date, restaurantId);
//	}
}
