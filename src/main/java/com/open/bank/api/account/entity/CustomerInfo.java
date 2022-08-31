package com.open.bank.api.account.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CUSTOMER_INFO")
public class CustomerInfo {

	@Id
	@Column(name = "CUSTOMER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private String customerId;
	
	@Column(name = "USERNAME")
	private String username;
	
	public CustomerInfo() {
		
	}
	
	public CustomerInfo(String customerId, String username) {
		this.customerId = customerId;
		this.username = username;
	}
	
	public String getCustomerId() {
		return this.customerId;
	}
	
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
}
