package com.jdjt.mangrovepay.event;

public class CardPayDto {
	String cardNo;
//	支付密码	
	String payPwd;	
//	花费金额	
	String amount;
	
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getPayPwd() {
		return payPwd;
	}
	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}	
	
}
