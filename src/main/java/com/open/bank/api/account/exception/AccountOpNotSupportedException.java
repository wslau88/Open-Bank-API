package com.open.bank.api.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountOpNotSupportedException extends RuntimeException {
	public AccountOpNotSupportedException() {
		super();
	}
	
	public AccountOpNotSupportedException(String message) {
		super(message);
	}
}
