package com.priyanathbhukta.notenest.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.priyanathbhukta.notenest.dto.PasswordResetRequest;

import jakarta.servlet.http.HttpServletRequest;

public interface HomeControllerEndpoint {
	
	@GetMapping("/verify")
	public  ResponseEntity<?> verifyUserAccount(@RequestParam Integer uid, @RequestParam String code) throws Exception;
	
	@GetMapping("/send-email-reset")
	public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request) throws Exception;
	
	@GetMapping("/verify-pswd-link")
	public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid, @RequestParam String code) throws Exception;
	
	@PostMapping("/reset-pswd")
	public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) throws Exception;
}
