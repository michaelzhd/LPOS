package edu.sjsu.LPOS.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.sjsu.LPOS.beans.Message;
import edu.sjsu.LPOS.beans.ResponseBean;

@Component("accessDeniedHanlder")
public class RestAccessDeniedHandler implements AccessDeniedHandler {

	@Autowired private ObjectMapper objectMapper;
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ResponseBean respBean = new ResponseBean();
		respBean.setMessage(Message.UNAUTHORIZED.getMessageText());
		respBean.setStatus(Message.UNAUTHORIZED.name());
		objectMapper.writeValue(response.getWriter(), respBean);
		
	}

}
