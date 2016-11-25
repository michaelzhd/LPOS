package edu.sjsu.LPOS.auth.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.sjsu.LPOS.auth.JwtTokenUtil;
import edu.sjsu.LPOS.auth.LocalAuthServerSetting;
import edu.sjsu.LPOS.auth.UserAuthorities;
import edu.sjsu.LPOS.domain.User;
import edu.sjsu.LPOS.repository.UserRepository;
import edu.sjsu.LPOS.service.RedisTokenStoreService;
import edu.sjsu.LPOS.service.UserService;



@Component("successHandler")
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired private RedisTokenStoreService redisTokenStoreService;
	@Autowired private JwtTokenUtil tokenUtil;
	@Autowired private ObjectMapper objectMapper;
	@Autowired private LocalAuthServerSetting setting;
	@Autowired private UserService userService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication auth) throws IOException, ServletException {
			
			UserAuthorities userAuthorities = new UserAuthorities(auth.getPrincipal(), auth.getAuthorities());
			String accessToken = tokenUtil.createJwt(userAuthorities, setting.getAccessExpiresInSeconds() * 1000);
			String refreshToken = tokenUtil.createJwt(userAuthorities, setting.getRefreshExpiresInSeconds() * 1000);
//			System.out.println("~~~~~~~~~~~~~~~~~in successhandler~~~~~~~~~~~~~~~~~~~");
			String username = auth.getPrincipal().toString();
//			User user = userRepository.findByUsername(username);
//			UserDetails user = userDetailsService.loadUserByUsername(username);
			User user = userService.findUserByUsername(username);
//			System.out.println("~~~~~~~~~~~~~~~~~user details ~~~~~~~~~~~~~~~~~~~");
			Map<String, Object> map = new HashMap<>();
			user.setPassword("Confidential");
			map.put("user", user);
			map.put("accessToken", accessToken);
			map.put("refreshToken", refreshToken);
			
//			System.out.println("~~~~~~~~~~~~~~~~~accessToken generated~~~~~~~~~~~~~~~~~~~");
			
			objectMapper.writeValue(response.getWriter(), map);
			
			redisTokenStoreService.set(userAuthorities.getUsername() + "_accessToken", 
									   accessToken, 
									   setting.getAccessExpiresInSeconds(),
									   TimeUnit.SECONDS);
			
			redisTokenStoreService.set(userAuthorities.getUsername() + "_refreshToken", 
									   refreshToken,
									   setting.getRefreshExpiresInSeconds(),
									   TimeUnit.SECONDS);
			
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
			}
	}

}
