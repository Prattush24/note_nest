package com.priyanathbhukta.notenest.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.priyanathbhukta.notenest.dto.LoginRequest;
import com.priyanathbhukta.notenest.dto.UserRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;


@Tag(name = "User Management",description = "Provides user management operations such as user registration, login authentication, and account deletion.")
@RequestMapping("/api/v1/auth")
public interface AuthControllerEndpoint {
	
	@Operation(summary = "Register a new user",
			description = "Registers a new user account by accepting user details such as name, email, and password. Sends an email verification link upon successful registration.",
			tags = {"User Management"})
	 @PostMapping("/register")
	    public ResponseEntity<?> registerUser(@RequestBody UserRequest userDto, HttpServletRequest request);
	
	@Operation(summary = "Delete a user account by ID",
			description = "Deletes an existing user account from the system using the specified user ID. Typically restricted to admin roles.",
			tags = {"User Management"})
	 @DeleteMapping("/{id}")
	    public ResponseEntity<?> deleteUser(@PathVariable Integer id);
	 
	@Operation(summary = "Authenticate user and generate token",
			description = "Authenticates the user using provided credentials (email and password). Returns a JWT token upon successful login for use in subsequent API requests.",
			tags = {"User Management"})
	 @PostMapping("/login")
	    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest )throws Exception;
}