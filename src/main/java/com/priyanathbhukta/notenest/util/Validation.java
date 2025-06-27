package com.priyanathbhukta.notenest.util;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.priyanathbhukta.notenest.dto.CategoryDto;
import com.priyanathbhukta.notenest.dto.TodoDto;
import com.priyanathbhukta.notenest.dto.TodoDto.StatusDto;
import com.priyanathbhukta.notenest.enums.TodoStatus;
import com.priyanathbhukta.notenest.exception.ResourceNotFoundException;
import com.priyanathbhukta.notenest.exception.ValidationException;

@Component
public class Validation {

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
	
}
