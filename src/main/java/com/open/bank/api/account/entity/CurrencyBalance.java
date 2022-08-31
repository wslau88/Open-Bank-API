package com.open.bank.api.account.entity;

import java.beans.JavaBean;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JavaBean
@JsonPropertyOrder({ "availableBalance", "currency" })
public class CurrencyBalance {
	private BigDecimal availableBalance;
	
	private String currency;
	
	public CurrencyBalance(BigDecimal availableBalance, String currency) {
		this.availableBalance = availableBalance;
		this.currency = currency;
	}
	
	public BigDecimal getAvailableBalance() {
		return this.availableBalance;
	}
	
	public void settAvailableBalance(BigDecimal availableBalance) {
		this.availableBalance = availableBalance;
	}

	public String getCurrency() {
		return this.currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
