package edu.sjsu.LPOS.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

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
public class UserRestController {
	
	@Autowired
	private UserService userService;
		
//	//username check 
//	@RequestMapping({ "/user/register" })
//	public ResponseEntity<String> register (@RequestBody User user){
//		User u = userService.getUserByPhoneNumber(user.getPhonenumber());
//		if(u != null) {
//			return new ResponseEntity<String>("User already registered", HttpStatus.CONFLICT);
//		}
//		String uuid = UUID.randomUUID().toString();
//		System.out.println("uuid = " + uuid);
//		user.setToken(uuid);
//		userService.saveUser(user);
//		return new ResponseEntity<String>(uuid, HttpStatus.OK);
//
//	}


	@RequestMapping(value = "/api/user/update", method = RequestMethod.GET)
	public ResponseEntity<String> update (HttpServletRequest req){
    	return new ResponseEntity<String>(String.valueOf(req.getAttribute("user")), HttpStatus.OK);
	}
}
