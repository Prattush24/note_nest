package com.priyanathbhukta.notenest.util;

import java.lang.reflect.UndeclaredThrowableException;
import java.security.PrivateKey;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.priyanathbhukta.notenest.dto.CategoryDto;
import com.priyanathbhukta.notenest.dto.TodoDto;
import com.priyanathbhukta.notenest.dto.TodoDto.StatusDto;
import com.priyanathbhukta.notenest.dto.UserRequest;
import com.priyanathbhukta.notenest.enums.TodoStatus;
import com.priyanathbhukta.notenest.exception.ResourceNotFoundException;
import com.priyanathbhukta.notenest.exception.ValidationException;
import com.priyanathbhukta.notenest.repository.RoleRepository;

@Component
public class Validation {
	
	@Autowired
	private RoleRepository roleRepo;
	
	public void categotyValidation(CategoryDto categoryDto) {
		
		Map<String, Object> error = new LinkedHashMap<>();
		
		if(ObjectUtils.isEmpty(categoryDto)) {
			throw new IllegalArgumentException("category object/JSON should not be null or empty");
		}else {
			//Validation of name field
			if(ObjectUtils.isEmpty(categoryDto.getName())) {
				error.put("name","name field  is empty or null");
			}else {
				if(categoryDto.getName().length() < 3) {
					error.put("name","name is length is minimum 3");
				}
				if(categoryDto.getName().length() > 100) {
					error.put("name","name is length is maximum 100");
				}
			}
			
			// Validation of description field
			if(ObjectUtils.isEmpty(categoryDto.getDescription())) {
				error.put("description","description field  is empty or null");
			}
//			else {
//				if(categoryDto.getDescription().length() < 10) {
//					error.put("description","description is length is minimum 10");
//				}
//				if(categoryDto.getDescription().length() > 100) {
//					error.put("description","description is length is maximum 100");
//				}
//			}
			
			// Validation isActive field
			if(ObjectUtils.isEmpty(categoryDto.getIsActive())) {
				error.put("isActive","isActive field  is empty or null");
			}else {
				if(categoryDto.getIsActive() != Boolean.TRUE.booleanValue() && categoryDto.getIsActive() !=  Boolean.FALSE.booleanValue()) {
					error.put("isActive","invalid value isActive field.");
				}
			}
		}
		if (!error.isEmpty()) {
			throw new ValidationException(error);
		}
		
	}
	
	
	public void todoValidation(TodoDto todo) throws Exception {
		
		StatusDto reqStatus = todo.getStatus();
		Boolean statusFound = false;
		for(TodoStatus st: TodoStatus.values()) {
			if(st.getId().equals(reqStatus.getId())) {
				statusFound = true;
			}
		}
		if(!statusFound) {
			throw new ResourceNotFoundException("invalid status");
		}
	}
	
	public void userValidation(UserRequest userDto){
		
		//1. First name validation
		if(!StringUtils.hasText(userDto.getFirstName())) {
			throw new IllegalArgumentException("First name is invalid");
		}
		//2. Last name validation
		if(!StringUtils.hasText(userDto.getLastName())) {
			throw new IllegalArgumentException("Last name is invalid");
		}
		//3. Email validation using proper email regex
		if(!StringUtils.hasText(userDto.getEmail()) 
				|| !userDto.getEmail().matches(Constants.EMAIL_REGEX)){
			throw new IllegalArgumentException("Email is invalid");
		}
		//4. 3. Mobile number validation using proper indian mobile number regex
		if(!StringUtils.hasText(userDto.getMobNo()) || !userDto.getMobNo().matches(Constants.INDIAN_MOBILE_REGEX)){
			throw new IllegalArgumentException("Mobile number is invalid");
		}
		//5. Password validation 
		/*
		 * At least one upper case English letter, (?=.*?[A-Z])
		 * At least one lower case English letter, (?=.*?[a-z])
		 * At least one digit, (?=.*?[0-9])
		 * At least one special character, (?=.*?[#?!@$%^&*-])
		 * Minimum eight in length .{8,} (with the anchors) */
//		if(!StringUtils.hasText(userDto.getPassword()) || !userDto.getPassword().matches(Constants.PASSWORD_REGEX)) {
//			throw new IllegalArgumentException("Password is invalid");
//		}
		
		String password = userDto.getPassword();
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        } else {
            if (password.length() < Constants.PASSWORD_MIN_LENGTH) {
            	throw new IllegalArgumentException("Password must be at least " + Constants.PASSWORD_MIN_LENGTH + " characters long.");
            }
            if (!Pattern.matches(Constants.PASSWORD_UPPERCASE, password)) {
            	throw new IllegalArgumentException("Password must contain at least one uppercase letter.");
            }
            if (!Pattern.matches(Constants.PASSWORD_LOWERCASE, password)) {
            	throw new IllegalArgumentException("Password must contain at least one lowercase letter.");
            }
            if (!Pattern.matches(Constants.PASSWORD_DIGIT, password)) {
            	throw new IllegalArgumentException("Password must contain at least one digit.");
            }
            if (!Pattern.matches(Constants.PASSWORD_SPECIAL_CHAR, password)) {
            	throw new IllegalArgumentException("Password must contain at least one special character (#?!@$%^&*-).");
            }
        }
		
		//6. Role validation of the corresponding user
		if(CollectionUtils.isEmpty(userDto.getRoles())) {
			throw new IllegalArgumentException("Role is invalid");
		}else {
			
			//getting role ids from database
			List<Integer> roleIds = roleRepo.findAll().stream().map(r->r.getId()).toList();
			//getting role ids which are not in database means invalid role ids
			List<Integer> invalidReqRoleIds = userDto.getRoles()
				.stream()
				.map(r->r.getId())
				.filter(roleId->!roleIds.contains(roleId))
				.toList();
			
			if(!CollectionUtils.isEmpty(invalidReqRoleIds)) {
				throw new IllegalArgumentException("Role id is invalid"+invalidReqRoleIds);
			}
		}
	}
	
}
