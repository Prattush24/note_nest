package com.priyanathbhukta.notenest.service;

import com.priyanathbhukta.notenest.dto.PasswordChngRequest;
import com.priyanathbhukta.notenest.dto.PasswordResetRequest;
import com.priyanathbhukta.notenest.exception.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
	
	public void changePassword(PasswordChngRequest passwordRequest);

	public void sendEmailPasswordReset(String email, HttpServletRequest request) throws Exception;

	public void verifyPswdResetLink(Integer uid, String code) throws Exception;

	public void resetPassword(PasswordResetRequest passwordResetRequest) throws Exception;
	
}
