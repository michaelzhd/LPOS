package edu.sjsu.LPOS.service;



import edu.sjsu.LPOS.domain.User;

public interface UserService {
	Iterable<User> listAllUsers();
	 
	User getUserById(Integer userId);
	
	User findUserByUsername(String username);
 
	User saveUser(User user);
 
    void deleteUser(Integer id);
    
    User findUserById(Integer id);
}
