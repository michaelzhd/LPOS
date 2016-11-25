package edu.sjsu.LPOS.auth.login;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class InterceptPathRequestMatcher implements RequestMatcher {

	private OrRequestMatcher matchers;
//	private RequestMatcher matcher;

	public InterceptPathRequestMatcher(List<String> pathToListen) {
		if (pathToListen == null || pathToListen.size() == 0) {
			throw new IllegalArgumentException("path to listen list is empty");
		}
		List<RequestMatcher> requestMatchers = new ArrayList<>();
		for (String path: pathToListen) {
			requestMatchers.add(new AntPathRequestMatcher(path));
		}
		matchers = new OrRequestMatcher(requestMatchers);
	}
	
	@Override
	public boolean matches(HttpServletRequest request) {
		if (matchers.matches(request)) {
			return true;
		}
		return false;
	}

}
