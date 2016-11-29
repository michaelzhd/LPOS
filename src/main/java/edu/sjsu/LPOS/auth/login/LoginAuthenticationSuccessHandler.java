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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.sjsu.LPOS.auth.JwtTokenUtil;
import edu.sjsu.LPOS.auth.LocalAuthServerSetting;
import edu.sjsu.LPOS.auth.UserAuthorities;
import edu.sjsu.LPOS.beans.Message;
import edu.sjsu.LPOS.beans.ResponseBean;
import edu.sjsu.LPOS.beans.UserBean;
import edu.sjsu.LPOS.domain.User;
import edu.sjsu.LPOS.service.RedisStoreService;
import edu.sjsu.LPOS.service.UserService;
import edu.sjsu.LPOS.util.UserBeanUtil;



@Component("successHandler")
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired private RedisStoreService redisTokenStoreService;
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
			String username = auth.getPrincipal().toString();
			User user = userService.findUserByUsername(username);
			UserBean userBean = UserBeanUtil.getUserBeanFromUser(user);
			Map<String, Object> map = new HashMap<>();
			map.put("user", userBean);
			map.put("accessToken", accessToken);
			map.put("refreshToken", refreshToken);
			
			redisTokenStoreService.setToken(userAuthorities.getUsername() + "_accessToken", 
									   accessToken, 
									   setting.getAccessExpiresInSeconds(),
									   TimeUnit.SECONDS);
			
			redisTokenStoreService.setToken(userAuthorities.getUsername() + "_refreshToken", 
									   refreshToken,
									   setting.getRefreshExpiresInSeconds(),
									   TimeUnit.SECONDS);
			
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
			}
			response.setStatus(HttpStatus.OK.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			ResponseBean respBean = new ResponseBean();
			respBean.setMessage(Message.OK.getMessageText());
			respBean.setStatus(Message.OK.name());
			respBean.setData(map);
			objectMapper.writeValue(response.getWriter(), respBean);
	}

}
