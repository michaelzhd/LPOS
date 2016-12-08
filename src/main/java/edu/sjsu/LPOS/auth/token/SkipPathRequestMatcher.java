package edu.sjsu.LPOS.auth.token;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class SkipPathRequestMatcher implements RequestMatcher {
	
	private OrRequestMatcher matchers;
//	private RequestMatcher matcher;
	private RequestMatcher processingMatcher;
	
	public SkipPathRequestMatcher(List<String> pathToSkip) {
		if (pathToSkip == null || pathToSkip.size() == 0) {
			throw new IllegalArgumentException("path to skip list is empty");
		}
		List<RequestMatcher> requestMatchers = new ArrayList<>();
		for (String path: pathToSkip) {
			requestMatchers.add(new AntPathRequestMatcher(path));
			requestMatchers.add(new RegexRequestMatcher(path + ".*", "GET"));
		}
		matchers = new OrRequestMatcher(requestMatchers);
		
	}
	public SkipPathRequestMatcher(List<String> pathsToSkip, String processingPath) {
        Assert.notNull(pathsToSkip);
        List<RequestMatcher> m = pathsToSkip.stream().map(path -> new AntPathRequestMatcher(path)).collect(Collectors.toList());
        matchers = new OrRequestMatcher(m);
        processingMatcher = new AntPathRequestMatcher(processingPath);
    }	

	@Override
	public boolean matches(HttpServletRequest request) {
		if (matchers.matches(request)) {
			return false;
		}
		return true;
	}





}
