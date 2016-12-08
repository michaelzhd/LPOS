package edu.sjsu.LPOS.DTO;

public class OrderUpdateDTO {
	Integer orderId;
	String status;
	Object paymentInfo;
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Object getPaymentInfo() {
		return paymentInfo;
	}
	public void setPaymentInfo(Object paymentInfo) {
		this.paymentInfo = paymentInfo;
	}
	
	
}
