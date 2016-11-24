package edu.sjsu.LPOS.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.LPOS.auth.LocalAuthServerSetting;
import edu.sjsu.LPOS.auth.TokenAuthenticationService;
import edu.sjsu.LPOS.configuration.WebSecurityConfig;
import edu.sjsu.LPOS.service.RedisTokenStoreService;

@RestController
public class AuthenticationController {

	@Autowired private TokenAuthenticationService tokenAuthenticationService;
	
	@RequestMapping(value = WebSecurityConfig.REFRESH_ENTRY_POINT, method = RequestMethod.GET)
	public ResponseEntity<Map> refresh(HttpServletRequest request, HttpServletResponse response) {
		String tokenHeader = request.getHeader("Authorization");
		Map<String, String> tokenMap = new HashMap<>();
		if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
			return new ResponseEntity<Map>(HttpStatus.BAD_REQUEST);
		}
		String refreshToken = tokenHeader.substring(7);
		String accessToken = tokenAuthenticationService.getAccessTokenByRefreshToken(refreshToken);
		if (accessToken == null) {
			return new ResponseEntity<Map>(HttpStatus.BAD_REQUEST);
		}
		tokenMap.put("accessToken", accessToken);
		
		return new ResponseEntity<Map>(tokenMap, HttpStatus.OK);
		
	}
}
