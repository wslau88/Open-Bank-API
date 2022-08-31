package com.open.bank.api.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidTxnAmountException extends RuntimeException {
	private static String ERR_MESSAGE = "Invalid Transaction Amount";
	
	public InvalidTxnAmountException() {
		super(ERR_MESSAGE);
	}
	
	public InvalidTxnAmountException(String message) {
		super(message);
	}
}
