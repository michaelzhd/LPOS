package edu.sjsu.LPOS.auth.token;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import edu.sjsu.LPOS.auth.JwtTokenUtil;
import edu.sjsu.LPOS.exception.InvalidTokenException;
import edu.sjsu.LPOS.service.RedisStoreService;
import io.jsonwebtoken.Claims;

@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired private RedisStoreService redisTokenStoreService;
	@Autowired private UserDetailsService userDetailsService;
	@Autowired private JwtTokenUtil tokenUtil;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String token = (String) authentication.getCredentials();
		Claims claims = tokenUtil.parseJwt(token);
		if (claims == null) {
			return null;
		}
		String username = claims.get("username", String.class);
		String tokenInRedis = redisTokenStoreService.get(username + "_accessToken");
		if (!token.equals(tokenInRedis)){
			throw new InvalidTokenException("Unknown or expired token");
		}
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		List<GrantedAuthority> authorities = (List<GrantedAuthority>) userDetails.getAuthorities();
		return new UsernamePasswordAuthenticationToken(username, null, authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
