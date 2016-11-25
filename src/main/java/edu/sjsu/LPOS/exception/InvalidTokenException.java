package edu.sjsu.LPOS.exception;

import org.springframework.security.authentication.AuthenticationServiceException;

public class InvalidTokenException extends AuthenticationServiceException {

	public InvalidTokenException(String msg) {
		super(msg);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6469337098169513605L;

}
