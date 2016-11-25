package edu.sjsu.LPOS.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.LPOS.domain.AuthorityName;
import edu.sjsu.LPOS.domain.User;
import edu.sjsu.LPOS.service.UserService;

@RestController
@RequestMapping("user")
public class UserController {
	
	@Autowired private UserService userService;
	
//	@PreAuthorize("hasRole('')")
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public ResponseEntity<User> createUser(@RequestBody User user) {
		System.out.println("register-----" + user.toString());
		if (user == null || user.getUserId() != null || user.getUsername() == null
			|| user.getPassword() == null || user.getEmail() == null) {
			return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		}
		String authorityString = user.getAuthorities();
		List<GrantedAuthority> authlist = AuthorityUtils.commaSeparatedStringToAuthorityList(authorityString);
//		if (authlist == null) {
//			return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
//		}
		User existedUser = userService.findUserByUsername(user.getUsername());
		if (existedUser != null) {
			return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		}
		System.out.println(authlist);
		userService.saveUser(user);
		User savedUser = userService.findUserByUsername(user.getUsername());
		return new ResponseEntity<User>(savedUser, HttpStatus.OK);
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> userlist = (List<User>) userService.listAllUsers();
		return new ResponseEntity<List<User>>(userlist, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{username}", method = RequestMethod.GET)
	public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username) {
		User user = userService.findUserByUsername(username);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
}
