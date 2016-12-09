package edu.sjsu.LPOS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.LPOS.domain.PaymentInfo;
import edu.sjsu.LPOS.repository.PaymentRepository;

@Service
public class PaymentInfoService {
	@Autowired
	PaymentRepository paymentRepository;
	public List<PaymentInfo> getPaymentInfoByUser_Id(Integer id) {
		return paymentRepository.findByUser_id(id);
	}
	public PaymentInfo getPaymentInfoById(Integer id) {
		return paymentRepository.findOne(id);
	}
	
	public PaymentInfo getPaymentInfoByCardNumber(String cardNumber) {
		return paymentRepository.findByCardNumber(cardNumber);
	}
	
	public PaymentInfo savePaymentInfo(PaymentInfo paymentInfo) {
		return paymentRepository.save(paymentInfo);
	}
	
	public void deletePaymentInfo(Integer id) {
		paymentRepository.delete(id);
		return;
	}
}
