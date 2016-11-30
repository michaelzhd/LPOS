package edu.sjsu.LPOS.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import edu.sjsu.LPOS.domain.TableInfo;

public interface TableInfoRepository extends CrudRepository<TableInfo, Integer>{
	List<TableInfo> findTableInfoByRestaurant_id(Integer id);
}
