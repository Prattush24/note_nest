package com.priyanathbhukta.notenest.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.priyanathbhukta.notenest.dto.PasswordChngRequest;

public interface UserControllerEndpoint {
	
	@GetMapping("/profile")
	public ResponseEntity<?> getProfile();
	
	@PostMapping("/chng-pswd")
	public ResponseEntity<?> changePassword(@RequestBody PasswordChngRequest passwordRequest);
}
