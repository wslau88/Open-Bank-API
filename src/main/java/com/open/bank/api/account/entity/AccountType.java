package com.open.bank.api.account.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ACCOUNT_TYPE")
public class AccountType {

	@Id
	@Column(name = "ACCOUNT_TYPE_CODE")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private String accountTypeCode;
	
	@Column(name = "ACCOUNT_TYPE_DESC")
	private String accountTypeDesc;
	
	public AccountType() {
		
	}
	
	public AccountType(String accountTypeCode, String accountTypeDesc) {
		this.accountTypeCode = accountTypeCode;
		this.accountTypeDesc = accountTypeDesc;
	}
	
	public String getAccountTypeCode() {
		return this.accountTypeCode;
	}
	
	public void setAccountTypeCode(String accountTypeCode) {
		this.accountTypeCode = accountTypeCode;
	}
	
	public String getAccountTypeDesc() {
		return this.accountTypeDesc;
	}
	
	public void setAccountTypeDesc(String accountTypeDesc) {
		this.accountTypeDesc = accountTypeDesc;
	}
	
}
