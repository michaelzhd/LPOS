package edu.sjsu.LPOS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import edu.sjsu.LPOS.domain.Restaurant;
import edu.sjsu.LPOS.domain.TableReserve;



public interface TableReserveRepository extends CrudRepository<TableReserve, Integer>{
	List<TableReserve> findByUser_id(Integer userId);
	List<TableReserve> findByRestaurant_idAndDateAndTimeSlot(Integer restaurantId, String date, String timeSlot);
	@Query("SELECT t FROM TableReserve t where t.user.id=:userId and t.date>=:start and t.date<=:end")
    List<TableReserve> findByUserIdAndDate(@Param("userId") Integer userId, @Param("start") String start, @Param("end") String end);
	
	@Query("SELECT t FROM TableReserve t where t.user.id=:userId and t.date>=:start and t.date<=:end and t.takeOut=true")
    List<TableReserve> findByUserIdAndDateAndTakeOut(@Param("userId") Integer userId, @Param("start") String start, @Param("end") String end);	

	@Query("SELECT t FROM TableReserve t where t.user.id=:userId and t.date>=:start and t.date<=:end and t.takeOut=false")
    List<TableReserve> findByUserIdAndDateAndReservation(@Param("userId") Integer userId, @Param("start") String start, @Param("end") String end);	

	@Query("SELECT t FROM TableReserve t where t.restaurant.id=:restaurantId and t.date>=:start and t.date<=:end")
    List<TableReserve> findByRestaurantIdAndDateAndReservation(@Param("restaurantId") Integer restaurantId, @Param("start") String start, @Param("end") String end);
}