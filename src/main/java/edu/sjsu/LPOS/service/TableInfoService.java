package edu.sjsu.LPOS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.LPOS.domain.TableInfo;
import edu.sjsu.LPOS.repository.TableInfoRepository;

@Service
public class TableInfoService {
	@Autowired
	TableInfoRepository tableInforepository;
	
	public List<TableInfo> getTableInfoByRestaurant_id(Integer restaurantId) {
		return tableInforepository.findTableInfoByRestaurant_id(restaurantId);
	}
	
	public TableInfo getTableInfoByTableId(Integer tableId) {
		return tableInforepository.findOne(tableId);
	}
	
	public TableInfo saveTableInfo(TableInfo tableinfo) {
		return tableInforepository.save(tableinfo);
	}
	
	public void deleteTableInfo(TableInfo tableinfo) {
		tableInforepository.delete(tableinfo);
	}
}
