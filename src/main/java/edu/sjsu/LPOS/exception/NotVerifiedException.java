package edu.sjsu.LPOS.exception;

import org.springframework.security.authentication.AuthenticationServiceException;

public class NotVerifiedException extends AuthenticationServiceException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -727763462750972594L;
	
	public NotVerifiedException(String msg) {
		super(msg);
	}


}
