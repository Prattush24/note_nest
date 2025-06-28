package com.priyanathbhukta.notenest.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.priyanathbhukta.notenest.dto.UserDto;
import com.priyanathbhukta.notenest.entity.User;
import com.priyanathbhukta.notenest.repository.RoleRepository;
import com.priyanathbhukta.notenest.repository.UserRepository;
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
	
	@Override
	public Boolean register(UserDto userDto) {
		
		validation.userValidation(userDto);
		
		if (userRepo.existsByEmail(userDto.getEmail())) {
		    throw new IllegalArgumentException("User with this email already exists");
		}
		
		if (userRepo.existsByMobNo(userDto.getMobNo())) {
		    throw new IllegalArgumentException("User with this mobile number already exists");
		}

		User user = mapper.map(userDto, User.class);
		User saveUser = userRepo.save(user);
		
		if(!ObjectUtils.isEmpty(saveUser)) {
			return true;
		}
		return false;
	}

	@Override
	public void deleteUserById(Integer id) {
		if (!userRepo.existsById(id)) {
	        throw new IllegalArgumentException("User with ID " + id + " does not exist");
	    }
	    userRepo.deleteById(id);
	}
	
	
	
}
