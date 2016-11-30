package edu.sjsu.LPOS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.LPOS.domain.TimeSlot;
import edu.sjsu.LPOS.repository.TimeSlotRepository;

@Service
public class TimeSlotService {
	@Autowired
	TimeSlotRepository timeSlotRepository;
	
	public TimeSlot saveTimeSlot(TimeSlot timeslot) {
		return timeSlotRepository.save(timeslot);
	}
	
	public void deleteTimeSlot(TimeSlot timeslot) {
		timeSlotRepository.delete(timeslot);
		return;
	}
	
	public  Iterable<TimeSlot> getAllTimeSlot() {
		return timeSlotRepository.findAll();
	}
	
	public TimeSlot getTimeSlotByTime(String time) {
		return timeSlotRepository.findByTimeSlot(time);
	}
}
