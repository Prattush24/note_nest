package com.priyanathbhukta.notenest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.ThrowableCauseExtractor;
import org.springframework.stereotype.Service;

import com.priyanathbhukta.notenest.dto.PasswordChngRequest;
import com.priyanathbhukta.notenest.entity.User;
import com.priyanathbhukta.notenest.repository.UserRepository;
import com.priyanathbhukta.notenest.service.UserService;
import com.priyanathbhukta.notenest.util.CommonUtil;


@Service
public class UserServiceImpl implements UserService{
	
	@Autowired		
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepo;
	
	
	
	@Override
	public void changePassword(PasswordChngRequest passwordRequest) {
		User loggedInUser = CommonUtil.getLoggedInUser();
		if(!passwordEncoder.matches(passwordRequest.getOldPassword(), loggedInUser.getPassword())) {
			throw new IllegalArgumentException("Old Passwor dis incorrect!!!!");
		}
		String encodePassword = passwordEncoder.encode(passwordRequest.getNewPassword());
		loggedInUser.setPassword(encodePassword);
		userRepo.save(loggedInUser);
	}

}
