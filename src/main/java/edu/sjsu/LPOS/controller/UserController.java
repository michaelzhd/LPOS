package edu.sjsu.LPOS.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import edu.sjsu.LPOS.beans.ResponseBean;
import edu.sjsu.LPOS.domain.AuthorityName;
import edu.sjsu.LPOS.domain.User;
import edu.sjsu.LPOS.service.UserService;

@RestController
@RequestMapping("user")
public class UserController {
	
	@Autowired private UserService userService;
	
//	@PreAuthorize("hasRole('')")
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public ResponseEntity<ResponseBean> createUser(@RequestBody User user) {

		ResponseBean respBean = new ResponseBean();
		if (user == null || user.getUserId() != null || user.getUsername() == null
			|| user.getPassword() == null || user.getEmail() == null) {
			respBean.setStatus("Failed");
			respBean.setMessage("Incomplete or bad input.");
			return new ResponseEntity<ResponseBean>(respBean, HttpStatus.BAD_REQUEST);
		}
		String authorityString = user.getAuthorities();
		List<GrantedAuthority> authlist = AuthorityUtils.commaSeparatedStringToAuthorityList(authorityString);
//		if (authlist == null) {
//			return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
//		}
		User existedUser = userService.findUserByUsername(user.getUsername());
		if (existedUser != null) {
			respBean.setStatus("Failed");
			respBean.setMessage("User already existed.");
			return new ResponseEntity<ResponseBean>(respBean, HttpStatus.BAD_REQUEST);
		}
		System.out.println(authlist);
		userService.saveUser(user);
		User savedUser = userService.findUserByUsername(user.getUsername());
		Map<String, Object> map = new HashMap<>();
		map.put("user", savedUser);
		respBean.setStatus("OK");
		respBean.setData(map);
		return new ResponseEntity<ResponseBean>(respBean, HttpStatus.OK);
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<ResponseBean> getAllUsers() {
		List<User> userlist = (List<User>) userService.listAllUsers();
		ResponseBean respBean = new ResponseBean();
		Map<String, Object> map = new HashMap<>();
		map.put("users", userlist);
		respBean.setData(map);
		respBean.setStatus("OK");
		return new ResponseEntity<ResponseBean>(respBean, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{username}", method = RequestMethod.GET)
	public ResponseEntity<ResponseBean> getUserByUsername(@PathVariable("username") String username) {
		ResponseBean respBean = new ResponseBean();
		User user = userService.findUserByUsername(username);
		if (user == null) {
			respBean.setStatus("Failed");
			respBean.setMessage("User not found.");
			return new ResponseEntity<ResponseBean>(respBean, HttpStatus.NOT_FOUND);
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("user", user);
		respBean.setData(map);
		respBean.setStatus("OK");
		return new ResponseEntity<ResponseBean>(respBean, HttpStatus.OK);
	}
	
}
