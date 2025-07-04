package com.priyanathbhukta.notenest.controller;

import org.modelmapper.internal.bytebuddy.asm.Advice.OffsetMapping.ForOrigin.Renderer.ForReturnTypeName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.priyanathbhukta.notenest.dto.LoginRequest;
import com.priyanathbhukta.notenest.dto.LoginResponse;
import com.priyanathbhukta.notenest.dto.UserRequest;
import com.priyanathbhukta.notenest.endpoint.AuthControllerEndpoint;
import com.priyanathbhukta.notenest.schedular.NotesSchedular;
import com.priyanathbhukta.notenest.service.AuthService;
import com.priyanathbhukta.notenest.util.CommonUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController

public class AuthController implements AuthControllerEndpoint {

    private final NotesSchedular notesSchedular;
	
	@Autowired
	private AuthService authService;

    AuthController(NotesSchedular notesSchedular) {
        this.notesSchedular = notesSchedular;
    }
	
//    @PostMapping("/")
//	public ResponseEntity<?> registerUser(@RequestBody UserDto userDto){
//		Boolean register = userService.register(userDto);
//		if(register) {
//			return CommonUtil.createBuildResponse("Register Successfully", HttpStatus.CREATED);
//		}else {
//			return CommonUtil.createErrorResponseMessage("Registration Failed", HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
    
    @Override
    public ResponseEntity<?> registerUser(UserRequest userDto, HttpServletRequest request){
        try {
        	log.info("AuthController : registerUser() : Execution Start");
        	String url = CommonUtil.geturl(request);
            Boolean register = authService.register(userDto,url);
            if(register) {
            	log.info("AuthController :: registerUser() : Execution End");
                return CommonUtil.createBuildResponse("Register Successfully", HttpStatus.CREATED);
            } else {
            	log.info("Error : {}","Registration Failed");
                return CommonUtil.createErrorResponseMessage("Registration Failed", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IllegalArgumentException ex) {
            return CommonUtil.createErrorResponseMessage(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
        	log.info("Error : {}","Something went wrong");
            return CommonUtil.createErrorResponseMessage("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
   @Override
    public ResponseEntity<?> deleteUser( Integer id) {
        try {
            authService.deleteUserById(id);
            return CommonUtil.createBuildResponse("User deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return CommonUtil.createErrorResponseMessage(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return CommonUtil.createErrorResponseMessage("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest )throws Exception{
    	
    	LoginResponse loginResponse = authService.login(loginRequest);
    	if(ObjectUtils.isEmpty(loginResponse)) {
    		return CommonUtil.createErrorResponseMessage("invalid credentials", HttpStatus.BAD_REQUEST);
    	}
        return CommonUtil.createBuildResponse(loginResponse, HttpStatus.OK);
    }

}
