package com.priyanathbhukta.notenest.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.priyanathbhukta.notenest.dto.PasswordChngRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User Profile", description = "Provides operations to retrieve user profile details and change the account password.")
@RequestMapping("/api/v1/user")
public interface UserControllerEndpoint {
	
	@Operation(summary = "Get the current user's profile information",
			description = "Retrieves the profile details of the currently authenticated user, such as username, email, and other relevant account information.",
			tags = {"User Profile"})
	@GetMapping("/profile")
	public ResponseEntity<?> getProfile();
	
	@Operation(summary = "Change the password of the current authenticated user",
			description = "Allows the authenticated user to securely change their account password by providing the current password and a new password.",
			tags = {"User Profile"})
	@PostMapping("/chng-pswd")
	public ResponseEntity<?> changePassword(@RequestBody PasswordChngRequest passwordRequest);
}
