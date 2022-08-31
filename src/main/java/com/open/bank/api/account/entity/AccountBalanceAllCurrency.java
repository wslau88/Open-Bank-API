package com.open.bank.api.account.entity;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "accountNumber", "balances", "lastUpdateTime" })
public class AccountBalanceAllCurrency {
	
	private String accountNumber;
	
	private List<CurrencyBalance> balances;
	
	@JsonFormat(timezone = "GMT+08:00", pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private ZonedDateTime lastUpdateTime;
	
	public AccountBalanceAllCurrency() {
		
	}
	
	public AccountBalanceAllCurrency(String accountNumber, List<CurrencyBalance> balances, ZonedDateTime lastUpdateTime) {
		this.accountNumber = accountNumber;
		this.balances = balances;
		this.lastUpdateTime = lastUpdateTime;
	}
	
	public String getAccountNumber() {
		return this.accountNumber;
	}
	
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public List<CurrencyBalance> getBalances() {
		return this.balances;
	}
	
	public void setBalances(List<CurrencyBalance> balances) {
		this.balances = balances;
	}

	public ZonedDateTime getLastUpdateTime() {
		return this.lastUpdateTime;
	}
	
	public void setLastUpdateTime(ZonedDateTime lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
