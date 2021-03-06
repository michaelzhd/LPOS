package edu.sjsu.LPOS.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.LPOS.DTO.ResponseDTO;
import edu.sjsu.LPOS.domain.Menu;
import edu.sjsu.LPOS.domain.PaymentInfo;
import edu.sjsu.LPOS.domain.Restaurant;
import edu.sjsu.LPOS.domain.User;
import edu.sjsu.LPOS.service.PaymentInfoService;
import edu.sjsu.LPOS.service.UserService;

@RequestMapping(value="/payment")
@RestController
public class PaymentRestController {
	@Autowired
	UserService userService;
	@Autowired
	PaymentInfoService paymentService;
	
	@RequestMapping(value = "", method = RequestMethod.GET) 
	public ResponseEntity<ResponseDTO> getPaymentInfo (
			HttpServletRequest request) {
		ResponseDTO responseDTO = new ResponseDTO();
		User user = (User) request.getAttribute("user");
		List<PaymentInfo> paymentInfo = paymentService.getPaymentInfoByUser_Id(user.getId());
		responseDTO.setStatus(HttpStatus.OK.name());
		responseDTO.setData(paymentInfo);
	    return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST) 
	public ResponseEntity<ResponseDTO> createPaymentInfo (HttpServletRequest request, @RequestBody PaymentInfo paymentInfo) {
		ResponseDTO response = new ResponseDTO();
		User user = (User) request.getAttribute("user");
		PaymentInfo p = paymentService.getPaymentInfoByCardNumber(paymentInfo.getCardNumber());
		if( p != null) {
			paymentInfo = p;
		}
		//Restaurant customer = entityManager.getReference(Restaurant.class, menu.getRestaurantId());
		paymentInfo.setUser(user);
		paymentService.savePaymentInfo(paymentInfo);
		List<PaymentInfo> result = paymentService.getPaymentInfoByUser_Id(user.getId());
		response.setStatus(HttpStatus.OK.name());
		response.setData(result);
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{paymentId}", method = RequestMethod.PUT) 
	public ResponseEntity<ResponseDTO> updatePaymentInfo (
															HttpServletRequest request, 
															@PathVariable("paymentId") Integer id,
															@RequestBody PaymentInfo paymentInfo) {
		ResponseDTO response = new ResponseDTO();
		User user = (User) request.getAttribute("user");
		PaymentInfo p = paymentService.getPaymentInfoById(id);
		if( p == null) {
			response.setStatus(HttpStatus.NOT_FOUND.name());
			response.setMessage("not fount the paymentid["+id+"]");
			return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
		}
		paymentInfo.setId(p.getId());
		paymentInfo.setUser(user);
		paymentService.savePaymentInfo(paymentInfo);
		response.setStatus(HttpStatus.OK.name());
		response.setData(paymentInfo);
		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{paymentId}", method = RequestMethod.DELETE) 
	public ResponseEntity<ResponseDTO> deletePaymentInfo (
			HttpServletRequest request,
			@PathVariable("paymentId") Integer id) {
		ResponseDTO response = new ResponseDTO();
		User u = (User) request.getAttribute("user");
		paymentService.deletePaymentInfo(id);
		List<PaymentInfo> result = paymentService.getPaymentInfoByUser_Id(u.getId());
		response.setStatus(HttpStatus.OK.name());
		response.setData(result);

		return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
	}
}
