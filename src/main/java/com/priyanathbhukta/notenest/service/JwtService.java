package com.priyanathbhukta.notenest.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.priyanathbhukta.notenest.entity.User;

public interface JwtService {
	
	public String generateToken(User user);
	
	public String extractUsername(String token);
	
	public Boolean validateToken(String token,UserDetails userDetails);
}
