package edu.sjsu.LPOS.service;



import edu.sjsu.LPOS.domain.User;

public interface UserService {
	Iterable<User> listAllUsers();
	 
	User getUserById(int userId);
	
	User findUserByUsername(String username);
 
	User saveUser(User user);
 
    void deleteUser(int id);
    
    User findUserById(int id);
}
