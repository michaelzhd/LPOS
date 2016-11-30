package edu.sjsu.LPOS.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.LPOS.domain.User;
import edu.sjsu.LPOS.repository.UserRepository;
import edu.sjsu.LPOS.service.UserService;

@Service(value="userService")
public class UserServiceImpl implements UserService {

	@Autowired private UserRepository userRepository;
	
	@Override
	public Iterable<User> listAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public User getUserById(Integer id) {
		return userRepository.findOne(id);
	}

	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public void deleteUser(Integer id) {
		userRepository.delete(id);
	}

	@Override
	public User findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	@Override
	public User findUserById(Integer id) {
		return userRepository.findByUserId(id);
	}
}
