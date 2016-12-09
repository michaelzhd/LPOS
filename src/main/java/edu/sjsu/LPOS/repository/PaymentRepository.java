package edu.sjsu.LPOS.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import edu.sjsu.LPOS.domain.PaymentInfo;

public interface PaymentRepository extends CrudRepository<PaymentInfo, Integer>{
	List<PaymentInfo> findByUser_id(Integer id);
	PaymentInfo findByCardNumber(String cardNumber);
}
