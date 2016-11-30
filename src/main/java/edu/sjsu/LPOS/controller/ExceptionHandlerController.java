package edu.sjsu.LPOS.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import edu.sjsu.LPOS.beans.ResponseBean;

@EnableWebMvc
@RestControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {
	
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
    @ExceptionHandler(value = { ConstraintViolationException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseBean> constraintViolationException(ConstraintViolationException ex) {
		ResponseBean respBean = new ResponseBean();
		respBean.setMessage(ex.getMessage());
		respBean.setStatus("Failed");
        return new ResponseEntity<ResponseBean>(respBean, HttpStatus.BAD_REQUEST);
    }

    
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseBean> requestExceptionHandling(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		ResponseBean respBean = new ResponseBean();
		ResponseStatus responseStatusAnnotation = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);

		if (responseStatusAnnotation == null) {
			respBean.setStatus("Error");
			respBean.setMessage("Your input to URL: " 
			+ request.getRequestURL() + " has caused internal error. Please contact us.");
			return new ResponseEntity<ResponseBean>(respBean, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		respBean.setMessage(ex.getMessage());
		respBean.setStatus("Failed");

        return new ResponseEntity<ResponseBean>(respBean, responseStatusAnnotation.value());
	}
    
    @RequestMapping("*")
    public ResponseEntity<ResponseBean> requestHandlingAllNoHandlerFound(Exception exception) {
		ResponseBean respBean = new ResponseBean();
		respBean.setMessage(exception.getMessage());
		respBean.setStatus("Failed");
		return new ResponseEntity<ResponseBean>(respBean, HttpStatus.SERVICE_UNAVAILABLE);
    }
}