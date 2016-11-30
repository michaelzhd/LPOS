package edu.sjsu.LPOS.repository;

import org.springframework.data.repository.CrudRepository;


import edu.sjsu.LPOS.domain.TimeSlot;

public interface TimeSlotRepository extends CrudRepository<TimeSlot, Integer>{
	TimeSlot findByTimeSlot(String time);
}
