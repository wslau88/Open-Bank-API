package com.open.bank.api.account.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ACCOUNTS")
public class Accounts {
	
	@Id
	@Column(name = "ACCOUNT_NUMBER")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private String accountNumber;
	
	@Column(name = "ACCOUNT_STATUS_CODE")
	private String accountStatusCode;
	
	@Column(name = "ACCOUNT_TYPE_CODE")
	private String accountTypeCode;
	
	@Column(name = "CUSTOMER_ID")
	private String customerId;
	
	@Column(name = "OTHER_DETAILS")
	private String otherDetails;
	
	@Column(name = "CREATE_TIME")
	private Timestamp createTime;
	
	@Column(name = "LAST_UPDATE_TIME")
	private Timestamp lastUpdateTime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_ID", insertable = false, updatable = false)
    CustomerInfo customerInfo;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT_TYPE_CODE", insertable = false, updatable = false)
	AccountType accountType;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT_NUMBER", insertable = false, updatable = false)
	List<AccountBalance> accountBalance;
	
	public Accounts() {
		
	}
	
	public Accounts(String accountNumber, String accountTypeCode) {
		this.accountNumber = accountNumber;
		this.accountTypeCode = accountTypeCode;
	}
	
	public String getAccountNumber() {
		return this.accountNumber;
	}
	
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public String getAccountTypeDesc() {
		return this.accountType.getAccountTypeDesc();
	}
	
	public void setAccountTypeDesc(String accountTypeDesc) {
		this.accountType.setAccountTypeDesc(accountTypeDesc);
	}
	
	public List<AccountBalance> getAccountBalance() {
		return this.accountBalance;
	}
	
	public void setAccountBalance(List<AccountBalance> accountBalance) {
		this.accountBalance = accountBalance;
	}
	
}
