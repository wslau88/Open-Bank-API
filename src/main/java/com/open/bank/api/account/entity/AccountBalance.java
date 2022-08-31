package com.open.bank.api.account.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@SuppressWarnings("serial")
class AccountBalanceId implements Serializable {
    private String accountNumber;
    private String currency;

	public AccountBalanceId() {
		
	}

    public AccountBalanceId(String accountNumber, String currency) {
        this.accountNumber = accountNumber;
        this.currency = currency;
    }
    
	public String getAccountNumber() {
		return this.accountNumber;
	}
	
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public String getCurrency() {
		return this.currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}

}

@Entity
@IdClass(AccountBalanceId.class)
@Table(name = "ACCOUNT_BALANCE")
public class AccountBalance {
	
	@Id
	@Column(name = "ACCOUNT_NUMBER")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private String accountNumber;
	
	@Column(name = "CURRENT_BALANCE")
	private BigDecimal currentBalance;
	
	@Column(name = "AVAILABLE_BALANCE")
	private BigDecimal availableBalance;
	
	@Id
	@Column(name = "CURRENCY")
	private String currency;

	@JsonFormat(timezone = "GMT+08:00", pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	@Column(name = "LAST_UPDATE_TIME")
	private ZonedDateTime lastUpdateTime;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT_NUMBER", insertable = false, updatable = false)
    Accounts accounts;
	
	public AccountBalance() {
		
	}
	
	public AccountBalance(
		String accountNumber,
		BigDecimal currentBalance,
		BigDecimal availableBalance,
		String currency,
		ZonedDateTime lastUpdateTime
	) {
		this.accountNumber = accountNumber;
		this.currentBalance = currentBalance;
		this.availableBalance = availableBalance;
		this.currency = currency;
		this.lastUpdateTime = lastUpdateTime;
	}
	
	public String getAccountNumber() {
		return this.accountNumber;
	}
	
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public BigDecimal getCurrentBalance() {
		return this.currentBalance;
	}
	
	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}
	
	public BigDecimal getAvailableBalance() {
		return this.availableBalance;
	}
	
	public void setAvailableBalance(BigDecimal availableBalance) {
		this.availableBalance = availableBalance;
	}

	public String getCurrency() {
		return this.currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public ZonedDateTime getLastUpdateTime() {
		return this.lastUpdateTime;
	}
	
	public void setLastUpdateTime(ZonedDateTime lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
