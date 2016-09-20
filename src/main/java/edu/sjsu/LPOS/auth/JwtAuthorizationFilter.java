package edu.sjsu.LPOS.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import edu.sjsu.LPOS.configuration.WebSecurityConfig;
import edu.sjsu.LPOS.service.RedisTokenStoreService;
import io.jsonwebtoken.Claims;

public class JwtAuthorizationFilter extends UsernamePasswordAuthenticationFilter{
	
//	@Autowired
	private Audience audience;
	
	@Autowired
	private RedisTokenStoreService redisTokenStoreService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	public JwtAuthorizationFilter() throws Exception {
		audience = WebSecurityConfig.getAudience();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest)req;
		String authString = httpReq.getHeader("Authorization");
		System.out.println("authstring: " + authString);
		if (authString != null && authString.length() > 7 && authString.substring(0, 6).equalsIgnoreCase("bearer")) {
			
			String jwtString = authString.substring(7);
			System.out.println("jwtstring: " + jwtString);
			Claims claims = JwtUtil.parseJwt(jwtString, audience.getClientSecret());
			if (claims != null) {
				String username = (String) claims.get("username");
				System.out.println("is it a username? " + username);
				
				String token = redisTokenStoreService.get(username);
				System.out.println("Token retrieved from redis: " + token);
				if (token != null && token.equals(jwtString)) {
					System.out.println("token is ok");
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					if (userDetails != null) {//&& userDetails.isEnabled()) {
						
						if (SecurityContextHolder.getContext().getAuthentication() == null) {
							UsernamePasswordAuthenticationToken authentication =
									new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
							 authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpReq));
				             SecurityContextHolder.getContext().setAuthentication(authentication);
						}
					}
				}	
				
			} 
			
		} 
		chain.doFilter(req, resp);
		return;
//		HttpServletResponse httpResp = (HttpServletResponse)resp;
//		httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//		httpResp.getWriter().write("BAD Token or REQUEST INFO");
//		return;
	}

//	@Override
//	public void init(FilterConfig filterConfig) throws ServletException {
//			SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
//	}

}
