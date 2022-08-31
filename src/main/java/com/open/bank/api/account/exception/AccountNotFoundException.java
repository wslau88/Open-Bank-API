package com.open.bank.api.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountNotFoundException extends RuntimeException {
	private static String ERR_MESSAGE = "Account Not Found";
	
	public AccountNotFoundException() {
		super(ERR_MESSAGE);
	}
	
	public AccountNotFoundException(String message) {
		super(message);
	}
}
