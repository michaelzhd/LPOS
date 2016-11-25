package edu.sjsu.LPOS.auth;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import edu.sjsu.LPOS.service.RedisTokenStoreService;
import io.jsonwebtoken.Claims;

@Service
public class TokenAuthenticationService {

	@Autowired private LocalAuthServerSetting setting;
	@Autowired private JwtTokenUtil tokenUtil;
	@Autowired private RedisTokenStoreService redisTokenStoreService;
	@Autowired private UserDetailsService userDetailsService;
	
	public String getAccessTokenByRefreshToken(String refreshToken) {
		if (refreshToken == null) {
			return null;
		}
		Claims claims = tokenUtil.parseJwt(refreshToken);
		String username = (String) claims.get("username");
		String refreshTokenInRedis = redisTokenStoreService.get(username + "_refreshToken");
		if (refreshTokenInRedis == null || !refreshTokenInRedis.equals(refreshToken)) {
			return null;
		}
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		UserAuthorities userAuthorities = new UserAuthorities(username, userDetails.getAuthorities());
						
		String accessToken = tokenUtil.createJwt(userAuthorities, setting.getAccessExpiresInSeconds() * 1000);
		redisTokenStoreService.set(username + "_accessToken", 
								   accessToken, 
								   setting.getAccessExpiresInSeconds(),
								   TimeUnit.SECONDS);
		return accessToken;
	}
}
