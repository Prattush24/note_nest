package com.priyanathbhukta.notenest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.priyanathbhukta.notenest.entity.AccountStatus;
import com.priyanathbhukta.notenest.entity.User;
import com.priyanathbhukta.notenest.exception.ResourceNotFoundException;
import com.priyanathbhukta.notenest.exception.SuccessException;
import com.priyanathbhukta.notenest.repository.UserRepository;
import com.priyanathbhukta.notenest.service.HomeService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class HomerServiceImpl implements HomeService{
	
	@Autowired	
	private UserRepository userRepo;
	
//	@Override
//	public Boolean verifyAccount(Integer userId, String verificationCode) throws Exception {
//		
//		User user = userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("Invalid user"));
//		if(user.getStatus().getVerifcationCode().equals(verificationCode)) {
//			
//			AccountStatus status = user.getStatus();
//			status.setIsActive(true);
//			status.setVerifcationCode(null);
//			
//			userRepo.save(user);
//			
//			return true;
//		}
//		
//		return false;
//	}
	
	@Override
	public Boolean verifyAccount(Integer userId, String verificationCode) throws Exception {
		log.info("HomerServiceImpl : verifyAccount(): Start");
	    User user = userRepo.findById(userId)
	                        .orElseThrow(() -> new ResourceNotFoundException("Invalid user"));

	    AccountStatus status = user.getStatus();
	    
	    if(status.getVerifcationCode() == null) {
	    	log.info("message :Account already verified");
	    	throw new SuccessException("Account already verified");
	    }

	    if (status != null && verificationCode != null && verificationCode.equals(status.getVerifcationCode())) {
	        status.setIsActive(true);
	        status.setVerifcationCode(null);
	        userRepo.save(user);
	        log.info("message :Account verification success");
	        return true;
	    }
	    log.info("HomerServiceImpl : verifyAccount(): End");
	    return false;
	}

}
