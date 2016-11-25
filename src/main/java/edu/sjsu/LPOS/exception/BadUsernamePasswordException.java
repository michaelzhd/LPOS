package edu.sjsu.LPOS.exception;

import org.springframework.security.authentication.AuthenticationServiceException;

public class BadUsernamePasswordException extends AuthenticationServiceException {

	public BadUsernamePasswordException(String msg) {
		super(msg);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6469337098169513605L;

}
