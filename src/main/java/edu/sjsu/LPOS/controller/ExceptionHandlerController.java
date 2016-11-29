package edu.sjsu.LPOS.controller;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.InternalServerErrorException;

import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import edu.sjsu.LPOS.beans.ResponseBean;

@RestControllerAdvice
public class ExceptionHandlerController {
	
	/**
	 * Handles 404, 500 not found exception. Return a custom response.
	 * @return
	 */
//    @ExceptionHandler(EntityNotFoundException.class)
//    @ResponseStatus(value= HttpStatus.NOT_FOUND)
//    public ResponseEntity<ResponseBean> requestHandling404NoHandlerFound(EntityNotFoundException exception) {
//		ResponseBean respBean = new ResponseBean();
//		respBean.setMessage(Message.NOT_FOUND.getMessageText());
//		respBean.setStatus(Message.NOT_FOUND.name());
//        return new ResponseEntity<ResponseBean>(respBean, HttpStatus.NOT_FOUND);
//    }
    
//    @ExceptionHandler(NoHandlerFoundException.class)
//    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
//    @ResponseBody
//    public ResponseEntity<ResponseBean> requestHandling500NoHandlerFound() {
//		ResponseBean respBean = new ResponseBean();
//		respBean.setMessage(Message.INTERNAL_SERVER_ERROR.getMessageText());
//		respBean.setStatus(Message.INTERNAL_SERVER_ERROR.name());
//        return new ResponseEntity<ResponseBean>(respBean, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
    
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ResponseBean> requestHandlingAllNoHandlerFound(Exception exception) {
		ResponseBean respBean = new ResponseBean();
		respBean.setMessage(exception.getMessage());
		respBean.setStatus("Failed");
		if (exception instanceof EntityNotFoundException) {
			return new ResponseEntity<ResponseBean>(respBean, HttpStatus.NOT_FOUND);
		} else if (exception instanceof InternalServerErrorException)  {
			return new ResponseEntity<ResponseBean>(respBean, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<ResponseBean>(respBean, HttpStatus.SERVICE_UNAVAILABLE);
		}

    }
}