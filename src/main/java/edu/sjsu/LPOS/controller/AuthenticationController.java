package edu.sjsu.LPOS.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.LPOS.auth.TokenAuthenticationService;
import edu.sjsu.LPOS.beans.Message;
import edu.sjsu.LPOS.beans.ResponseBean;
import edu.sjsu.LPOS.configuration.WebSecurityConfig;

@RestController
public class AuthenticationController {

	@Autowired private TokenAuthenticationService tokenAuthenticationService;
	
	@RequestMapping(value = WebSecurityConfig.REFRESH_ENTRY_POINT, method = RequestMethod.GET)
	public ResponseEntity<ResponseBean> refresh(HttpServletRequest request, HttpServletResponse response) {
		String tokenHeader = request.getHeader("Authorization");
		Map<String, Object> tokenMap = new HashMap<>();
		ResponseBean respBean = new ResponseBean();
		if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
			respBean.setStatus(HttpStatus.UNAUTHORIZED.name());
			respBean.setMessage(Message.NO_TOKEN.getMessageText());
			return new ResponseEntity<ResponseBean>(respBean, HttpStatus.UNAUTHORIZED);
		}
		String refreshToken = tokenHeader.substring(7);
		String accessToken = tokenAuthenticationService.getAccessTokenByRefreshToken(refreshToken);
		if (accessToken == null) {
			respBean.setStatus("Failure");
			respBean.setMessage("Failed to generate a new access token.");
			
			return new ResponseEntity<ResponseBean>(respBean, HttpStatus.EXPECTATION_FAILED);
		}
		tokenMap.put("accessToken", accessToken);
		respBean.setData(tokenMap);
		return new ResponseEntity<ResponseBean>(respBean, HttpStatus.OK);
		
	}
}
