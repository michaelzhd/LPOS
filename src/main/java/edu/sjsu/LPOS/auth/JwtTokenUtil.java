package edu.sjsu.LPOS.auth;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.sjsu.LPOS.beans.UserBean;
import edu.sjsu.LPOS.domain.User;
import edu.sjsu.LPOS.service.RedisStoreService;
import edu.sjsu.LPOS.service.UserService;
import edu.sjsu.LPOS.util.UserBeanUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {
	
	@Autowired private LocalAuthServerSetting setting;
	@Autowired private RedisStoreService redisTokenStoreService;


	@Autowired private UserService userService;
	
	public  Claims parseJwt(String jwt) {
		try {
			Claims claims = Jwts.parser()
					.setSigningKey(setting.getClientSecret().getBytes())
					.parseClaimsJws(jwt)
					.getBody();
			return claims;
		} catch (Exception e) {
			return null;
		}
	}
	
	public String createJwt(UserAuthorities userAuthorities, long TTLMilliSeconds) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		
		long currentTimeMillis = System.currentTimeMillis();
		Date now = new Date(currentTimeMillis);
		byte[] keySecretBytes = setting.getClientSecret().getBytes();
		Key keySecret = new SecretKeySpec(keySecretBytes, signatureAlgorithm.getJcaName());
		List<String> authorities = new ArrayList<>();
		for (GrantedAuthority authority: userAuthorities.getAuthorities()) {
			authorities.add(authority.toString());
		}
		
		JwtBuilder jwtBuilder = Jwts.builder()
								.setHeaderParam("typ", "JWT")
								.claim("username", userAuthorities.getUsername())
								.claim("authorities", authorities)
								.setIssuer(setting.getClientName())
								.signWith(signatureAlgorithm, keySecret);
		if (TTLMilliSeconds >= 0) {
			long expireTimeMillis = currentTimeMillis + TTLMilliSeconds;
			Date expireDate = new Date(expireTimeMillis);
			jwtBuilder.setExpiration(expireDate).setNotBefore(now);
		}
		return jwtBuilder.compact();
	}
	
	public String createAccessToken(UserAuthorities userAuthorities) {
		String accessToken = createJwt(userAuthorities, setting.getAccessExpiresInSeconds() * 1000);
		
		redisTokenStoreService.setToken(userAuthorities.getUsername() + "_accessToken", 
								   accessToken, 
								   setting.getAccessExpiresInSeconds(),
								   TimeUnit.SECONDS);
		
		return accessToken;
	}
	
	public String createRefreshToken(UserAuthorities userAuthorities) {
		String refreshToken = createJwt(userAuthorities, setting.getRefreshExpiresInSeconds() * 1000);
		redisTokenStoreService.setToken(userAuthorities.getUsername() + "_refreshToken", 
								   refreshToken,
								   setting.getRefreshExpiresInSeconds(),
								   TimeUnit.SECONDS);
		
		return refreshToken;
	}
	
}
