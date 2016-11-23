package edu.sjsu.LPOS.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import edu.sjsu.LPOS.util.EncryptionUtil;

public class HttpBasicFilter extends UsernamePasswordAuthenticationFilter {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest)req;
		String authString = httpReq.getHeader("Authorization");
		if (authString != null && authString.substring(0, 5).equalsIgnoreCase("basic")) {
			String b64String = authString.substring(6);
			System.out.println(b64String);
			String decodedUserPasswordPair = EncryptionUtil.decodeBase64(b64String);
			System.out.println(decodedUserPasswordPair);
			String[] splited = decodedUserPasswordPair.split(":");
			String username = splited[0];
			String password = splited[1];
			String md5password = EncryptionUtil.digestWithMD5(password);
			System.out.println(username);
			System.out.println(md5password);
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			if (userDetails != null && userDetails.getPassword().equals(md5password) && userDetails.isEnabled()) {
				
				if (SecurityContextHolder.getContext().getAuthentication() == null) {
					UsernamePasswordAuthenticationToken authentication =
							new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					 authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpReq));
		             SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		} 
		chain.doFilter(req, resp);
		return;
		
//		HttpServletResponse httpResp = (HttpServletResponse)resp;
//		httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//		httpResp.getWriter().write("BAD USERNAME or PASSWORD");
//		return;
		
	}

//	@Override
//	public void init(FilterConfig filterConfig) throws ServletException {
//		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
//	}

}
