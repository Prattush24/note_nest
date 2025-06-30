package com.priyanathbhukta.notenest.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.priyanathbhukta.notenest.config.security.CustomUserDetails;
import com.priyanathbhukta.notenest.dto.EmailRequest;
import com.priyanathbhukta.notenest.dto.LoginRequest;
import com.priyanathbhukta.notenest.dto.LoginResponse;
import com.priyanathbhukta.notenest.dto.UserRequest;
import com.priyanathbhukta.notenest.entity.AccountStatus;
import com.priyanathbhukta.notenest.entity.User;
import com.priyanathbhukta.notenest.repository.RoleRepository;
import com.priyanathbhukta.notenest.repository.UserRepository;
import com.priyanathbhukta.notenest.service.JwtService;
import com.priyanathbhukta.notenest.service.UserService;
import com.priyanathbhukta.notenest.util.Validation;


@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private Validation validation;
	
	@Autowired
	private ModelMapper mapper;
	
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;
	
	@Override
	public Boolean register(UserRequest userDto, String url) throws Exception {
		
		validation.userValidation(userDto);
		
		if (userRepo.existsByEmail(userDto.getEmail())) {
		    throw new IllegalArgumentException("User with this email already exists");
		}
		
		if (userRepo.existsByMobNo(userDto.getMobNo())) {
		    throw new IllegalArgumentException("User with this mobile number already exists");
		}
		
		User user = mapper.map(userDto, User.class);
		
		AccountStatus status = AccountStatus.builder()
				.isActive(false)
				.verifcationCode(UUID.randomUUID().toString())
				.build();
		user.setStatus(status);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		User saveUser = userRepo.save(user);
		
		if(!ObjectUtils.isEmpty(saveUser)) {
			//logic for sending email
			emailSend(saveUser, url);
			
			return true;
		}
		return false;
	}

	private void emailSend(User saveUser, String url) throws Exception {
		
		String message = "Hi, <b>[[username]]</b>"
				+"<br> Your account has been successfully registered with NoteNest. We’re excited to have you as part of our community."
				+"<br> Click the below link to verify and Acivate your account.<br>"
				+"<a href ='[[url]]'>Verify Account</a> <br><br>"
				+"Thanks <br> NoteNest.com";
		
		message =  message.replace("[[username]]", saveUser.getFirstName());
		message =  message.replace("[[url]]",url+"/api/v1/home/verify?uid="+saveUser.getId()+"&&code="+saveUser.getStatus().getVerifcationCode());
		
		EmailRequest emailRequest = EmailRequest.builder()
				.to(saveUser.getEmail())
				.title("Account Creation Confirmation")
				.subject("Account Created Successfully")
				.message(message)
				.build();
		
		emailService.sendEmail(emailRequest);
		
	}

	@Override
	public void deleteUserById(Integer id) {
		if (!userRepo.existsById(id)) {
	        throw new IllegalArgumentException("User with ID " + id + " does not exist");
	    }
	    userRepo.deleteById(id);
	}

	@Override
	public LoginResponse login(LoginRequest loginRequest) {
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		if(authenticate.isAuthenticated()) {
			CustomUserDetails customUserDetails = (CustomUserDetails)	authenticate.getPrincipal();
			String token = jwtService.generateToken(customUserDetails.getUser());
			LoginResponse loginResponse = LoginResponse.builder()
					.user(mapper.map(customUserDetails.getUser(), UserRequest.class))
					.token(token)
					.build();
			return loginResponse;
		}
		return null;
	}
	
	
	
}
