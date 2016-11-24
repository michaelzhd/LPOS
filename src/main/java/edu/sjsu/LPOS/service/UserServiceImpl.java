package edu.sjsu.LPOS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.LPOS.domain.User;
import edu.sjsu.LPOS.repository.UserRepository;
import edu.sjsu.LPOS.service.UserService;
import edu.sjsu.LPOS.util.EncryptionUtil;

@Service(value="userService")
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	 
	@Autowired
	public void UserRepository(UserRepository userRepository) {
	    this.userRepository = userRepository;
	}
	
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
		String digestedPassword = EncryptionUtil.digestWithMD5(user.getPassword());
		user.setPassword(digestedPassword);

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
		return userRepository.findById(id);
	}
}
