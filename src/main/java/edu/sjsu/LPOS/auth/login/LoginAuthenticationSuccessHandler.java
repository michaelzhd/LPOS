package edu.sjsu.LPOS.auth.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.sjsu.LPOS.auth.JwtTokenUtil;
import edu.sjsu.LPOS.auth.LocalAuthServerSetting;
import edu.sjsu.LPOS.auth.UserAuthorities;
import edu.sjsu.LPOS.service.RedisTokenStoreService;



@Component("successHandler")
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private RedisTokenStoreService redisTokenStoreService;

	@Autowired
	private JwtTokenUtil tokenUtil;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private LocalAuthServerSetting setting;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication auth) throws IOException, ServletException {
			
			UserAuthorities userAuthorities = new UserAuthorities(auth.getPrincipal(), auth.getAuthorities());
			String accessToken = tokenUtil.createJwt(userAuthorities, setting.getAccessExpiresInSeconds() * 1000);
			String refreshToken = tokenUtil.createJwt(userAuthorities, setting.getRefreshExpiresInSeconds() * 1000);
			Map<String, String> map = new HashMap<>();
			map.put("accessToken", accessToken);
			map.put("refreshToken", refreshToken);
			objectMapper.writeValue(response.getWriter(), map);
			
			redisTokenStoreService.set(userAuthorities.getUsername() + "_accessToken", accessToken);
			redisTokenStoreService.set(userAuthorities.getUsername() + "_refreshToken", refreshToken);
			
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
			}
	}

}
