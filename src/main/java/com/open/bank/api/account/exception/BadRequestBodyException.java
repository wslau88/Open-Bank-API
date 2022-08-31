package com.open.bank.api.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestBodyException extends RuntimeException {
	private static String ERR_MESSAGE = "Bad Request Body";
	
	public BadRequestBodyException() {
		super(ERR_MESSAGE);
	}
	
	public BadRequestBodyException(String message) {
		super(message);
	}
}
