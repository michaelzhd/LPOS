package edu.sjsu.LPOS.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;


import edu.sjsu.LPOS.beans.Message;
import edu.sjsu.LPOS.beans.ResponseBean;

@ControllerAdvice
public class ExceptionHandlerController {
	
	/**
	 * Handles 404 not found exception. Return a custom response.
	 * @return
	 */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<ResponseBean> requestHandlingNoHandlerFound() {
		ResponseBean respBean = new ResponseBean();
		respBean.setMessage(Message.NOT_FOUND.getMessageText());
		respBean.setStatus(Message.NOT_FOUND.name());
        return new ResponseEntity<ResponseBean>(respBean, HttpStatus.NOT_FOUND);
    }
}