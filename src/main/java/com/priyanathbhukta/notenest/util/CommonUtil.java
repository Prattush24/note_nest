package com.priyanathbhukta.notenest.util;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import com.priyanathbhukta.notenest.config.security.CustomUserDetails;
import com.priyanathbhukta.notenest.dto.UserResponse;
import com.priyanathbhukta.notenest.entity.User;
import com.priyanathbhukta.notenest.handler.GenericResponse;

import jakarta.servlet.http.HttpServletRequest;

public class CommonUtil {
	
	public static ResponseEntity<?> createBuildResponse(Object data, HttpStatus status){
		
		GenericResponse response = GenericResponse.builder()
				.responseStatus(status)
				.status("success")
				.message("successfully get data")
				.data(data)
				.build();
		
		return response.create();
	}
	
	public static ResponseEntity<?> createBuildResponseMessage(String message ,HttpStatus status){
			
		GenericResponse response = GenericResponse.builder()
				.responseStatus(status)
				.status("success")
				.message(message)
				.build();
		
		return response.create();
	}
	
	public static ResponseEntity<?> createErrorResponse(Object data, HttpStatus status){
			
		GenericResponse response = GenericResponse.builder()
				.responseStatus(status)
				.status("failed")
				.message("failed")
				.data(data)
				.build();
		
		return response.create();
	}
	
	public static ResponseEntity<?> createErrorResponseMessage(String message, HttpStatus status){
		
		GenericResponse response = GenericResponse.builder()
				.responseStatus(status)
				.status("failed")
				.message(message)
				.build();
		
		return response.create();
	}

	public static String getContentType(String originalFileName) {
		
		String extension = FilenameUtils.getExtension(originalFileName);

	    switch (extension) {
	        case "pdf":
	            return "application/pdf";
	        case "xlsx":
	            return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	        case "txt":
	            return "text/plain";
	        case "png":
	            return "image/png";
	        case "jpeg":
	            return "image/jpeg";
	        default:
	            return "application/octet-stream";
	    }
		
	}

	public static String geturl(HttpServletRequest request) {
		String apiUrl = request.getRequestURL().toString();
	    apiUrl = apiUrl.replace(request.getServletPath(), "");
		return apiUrl;
	}
	
	public static User getLoggedInUser(){
		try {
			CustomUserDetails logUser =(CustomUserDetails)SecurityContextHolder
					.getContext().getAuthentication().getPrincipal();
			return logUser.getUser();
		} catch (Exception e) {
			throw e;
		}
		
	}
	
}
