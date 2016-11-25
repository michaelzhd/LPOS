package edu.sjsu.LPOS.auth.login;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.sjsu.LPOS.auth.AuthRequest;
import edu.sjsu.LPOS.exception.BadUsernamePasswordException;

public class LoginBasicFilter extends AbstractAuthenticationProcessingFilter {
		
	@Autowired private ObjectMapper objectMapper;
	@Autowired private AuthenticationSuccessHandler successHandler;
	@Autowired private AuthenticationFailureHandler failureHandler;
	
	public LoginBasicFilter(RequestMatcher matcher) {
		super(matcher);
	}
	
//	public LoginBasicFilter(String defaulturl) {
//		super(defaulturl);
//	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		AuthRequest authRequest = objectMapper.readValue(request.getReader(), AuthRequest.class);
		if (authRequest == null || authRequest.getUsername() == "" || authRequest.getPassword() == "") {
			throw new BadUsernamePasswordException("No username or password provided.");
		}
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
				authRequest.getPassword());
		
		return this.getAuthenticationManager().authenticate(authentication);
	}

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }


}
