package com.priyanathbhukta.notenest.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.priyanathbhukta.notenest.dto.LoginRequest;
import com.priyanathbhukta.notenest.dto.UserRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthControllerEndpoint {
	
	
	 @PostMapping("/")
	    public ResponseEntity<?> registerUser(@RequestBody UserRequest userDto, HttpServletRequest request);
	 
	 @DeleteMapping("/{id}")
	    public ResponseEntity<?> deleteUser(@PathVariable Integer id);
	 
	 @PostMapping("/login")
	    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest )throws Exception;
}
