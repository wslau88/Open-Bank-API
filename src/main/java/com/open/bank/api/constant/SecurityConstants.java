package com.open.bank.api.constant;

public class SecurityConstants {
	
	public static final String SECRET = "SECRET_KEY";
	public static final long EXPIRATION_TIME = 1800_000; // 30 mins
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/api/v1/auth/registry/user/login";
	
}
