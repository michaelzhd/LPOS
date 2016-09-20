package edu.sjsu.LPOS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.LPOS.domain.User;
import edu.sjsu.LPOS.service.UserService;

@RestController
@RequestMapping("user")
public class UserController {
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<User> createUser(@RequestBody User user) {
		userService.saveUser(user);
		return new ResponseEntity<User>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "users", method = RequestMethod.GET)
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> userlist = (List<User>) userService.listAllUsers();
		return new ResponseEntity<List<User>>(userlist, HttpStatus.OK);
	}
	
}
