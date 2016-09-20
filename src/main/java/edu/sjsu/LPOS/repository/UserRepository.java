package edu.sjsu.LPOS.repository;

import org.springframework.data.repository.CrudRepository;

import edu.sjsu.LPOS.domain.User;

public interface UserRepository extends CrudRepository<User, Integer>{
	User findByUsername(String username);
	User findById(long id);
}
