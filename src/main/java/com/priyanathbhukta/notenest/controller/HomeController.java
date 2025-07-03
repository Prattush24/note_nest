package com.priyanathbhukta.notenest.controller;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.priyanathbhukta.notenest.dto.PasswordResetRequest;
import com.priyanathbhukta.notenest.endpoint.HomeControllerEndpoint;
import com.priyanathbhukta.notenest.exception.ResourceNotFoundException;
import com.priyanathbhukta.notenest.service.HomeService;
import com.priyanathbhukta.notenest.service.UserService;
import com.priyanathbhukta.notenest.util.CommonUtil;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/v1/home")
public class HomeController implements HomeControllerEndpoint {
	
	Logger log = org.slf4j.LoggerFactory.getLogger(HomeController.class); 
	
	@Autowired
	private HomeService homeService;
	
	@Autowired
	private UserService userService;
	
	@Override
	public  ResponseEntity<?> verifyUserAccount(@RequestParam Integer uid, @RequestParam String code) throws Exception{

		log.info("HomeController :: verifyUserAccount() : Execution Start");

		Boolean verifyAccount = homeService.verifyAccount(uid, code);
		if(verifyAccount) {
			return CommonUtil.createBuildResponseMessage("Account verification successfull", HttpStatus.OK);
		}
		log.info("HomeController :: verifyUserAccount() : Execution End");
		return CommonUtil.createErrorResponseMessage("Invalid verification link", HttpStatus.BAD_REQUEST);
	}
	
	@Override
	public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request) throws Exception{
		
		userService.sendEmailPasswordReset(email,request);
		return CommonUtil.createBuildResponseMessage("Email send Successfully !! Check mail and reset password", HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid, @RequestParam String code) throws Exception{
		userService.verifyPswdResetLink(uid, code);
		return CommonUtil.createBuildResponseMessage("Verified Successfully", HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) throws Exception{
		userService.resetPassword(passwordResetRequest);
		return CommonUtil.createBuildResponseMessage("Password Reset Successfully", HttpStatus.OK);
	}
}
