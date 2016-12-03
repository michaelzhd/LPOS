package edu.sjsu.LPOS.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.LPOS.domain.User;

public interface UserRepository extends CrudRepository<User, Integer>{
	
	User findByUsername(String username);
	User findById(int userid);
}
