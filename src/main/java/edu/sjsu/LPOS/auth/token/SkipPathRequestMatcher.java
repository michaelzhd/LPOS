package edu.sjsu.LPOS.auth.token;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class SkipPathRequestMatcher implements RequestMatcher {
	
	private OrRequestMatcher matchers;
//	private RequestMatcher matcher;

	public SkipPathRequestMatcher(List<String> pathToSkip) {
		if (pathToSkip == null || pathToSkip.size() == 0) {
			throw new IllegalArgumentException("path to skip list is empty");
		}
		List<RequestMatcher> requestMatchers = new ArrayList<>();
		for (String path: pathToSkip) {
			requestMatchers.add(new AntPathRequestMatcher(path));
		}
		matchers = new OrRequestMatcher(requestMatchers);
		
	}
	

	@Override
	public boolean matches(HttpServletRequest request) {
		if (matchers.matches(request)) {
			return false;
		}
		return true;
	}





}
