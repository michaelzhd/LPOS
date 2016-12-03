package edu.sjsu.LPOS.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import edu.sjsu.LPOS.domain.TableReserve;



public interface TableReserveRepository extends CrudRepository<TableReserve, Integer>{
	List<TableReserve> findByUser_id(Integer userId);
	List<TableReserve> findByRestaurant_idAndDateAndTimeSlot(Integer restaurantId, String date, String timeSlot);
//	@Query("SELECT SUM(t.people) FROM TableReserve t where t.timeSlot=:timeSlot and t.date=:date and t.restaurant_id=:restaurantId")
//    Integer sumPeopleByRestaurantIdAndDateAndTimeSlot(@Param("timeSlot") String timeSlot, @Param("date") Date date, @Param("restaurantId") Integer restaurantId);
}