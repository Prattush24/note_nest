package com.priyanathbhukta.notenest.exception;

public class JwtTokenExpiredException extends RuntimeException{

	public JwtTokenExpiredException(String message) {
		super(message);
	}
	
}
