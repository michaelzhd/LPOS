package edu.sjsu.LPOS.service;

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
}
