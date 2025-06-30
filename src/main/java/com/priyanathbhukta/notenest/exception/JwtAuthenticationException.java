package com.priyanathbhukta.notenest.exception;

public class JwtAuthenticationException extends RuntimeException{

	public JwtAuthenticationException(String message) {
		super(message);
	}
	
}
