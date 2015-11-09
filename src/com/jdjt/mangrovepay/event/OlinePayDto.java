package com.jdjt.mangrovepay.event;

public class OlinePayDto {
	String payDiv;
	// 支付密码
	String amount;
	// 客户端ip
	String clientIp;
	public String getPayDiv() {
		return payDiv;
	}
	public void setPayDiv(String payDiv) {
		this.payDiv = payDiv;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}


}
