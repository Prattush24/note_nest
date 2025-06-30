package com.priyanathbhukta.notenest.service;

import com.priyanathbhukta.notenest.dto.LoginRequest;
import com.priyanathbhukta.notenest.dto.LoginResponse;
import com.priyanathbhukta.notenest.dto.UserRequest;

public interface UserService {
	public Boolean register(UserRequest userDto, String url) throws Exception;
	
	public void deleteUserById(Integer id);

	public LoginResponse login(LoginRequest loginRequest);

}
