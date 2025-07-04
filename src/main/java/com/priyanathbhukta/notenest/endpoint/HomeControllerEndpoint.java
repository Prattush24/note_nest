package com.priyanathbhukta.notenest.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.priyanathbhukta.notenest.dto.PasswordResetRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;



@Tag(name = "User Authentication",description = "Handles user authentication operations such as account verification, password reset requests, password reset link verification, and password updating.")
@RequestMapping("/api/v1/home")
public interface HomeControllerEndpoint {
	
	@Operation(summary = "Verify user account via email link",
			description = "Validates the user's email verification link using the provided user ID and verification code. This completes the account verification process after registration.",
			tags = {"User Authentication"})
	@GetMapping("/verify")
	public  ResponseEntity<?> verifyUserAccount(@RequestParam Integer uid, @RequestParam String code) throws Exception;
	
	@Operation(summary = "Send email with password reset link",
			description = "Sends a password reset email to the specified user email address if the account exists. The email contains a secure link to reset the password.",
			tags = {"User Authentication"})
	@GetMapping("/send-email-reset")
	public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request) throws Exception;
	
	@Operation(summary = "Verify password reset link",
			description = "Verifies the validity of the password reset link using the user ID and reset code. Ensures that the reset link has not expired or been tampered with.",
			tags = {"User Authentication"})
	@GetMapping("/verify-pswd-link")
	public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid, @RequestParam String code) throws Exception;
	
	@Operation(summary = "Reset password using valid reset token",
			description = "Allows the user to set a new password after successful verification of the reset token. Requires a valid reset token and the new password input.",
			tags = {"User Authentication"})
	@PostMapping("/reset-pswd")
	public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) throws Exception;
}