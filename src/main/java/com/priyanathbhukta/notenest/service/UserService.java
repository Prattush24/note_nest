package com.priyanathbhukta.notenest.service;

import com.priyanathbhukta.notenest.dto.UserDto;

public interface UserService {
	public Boolean register(UserDto userDto) throws Exception;
	
	public void deleteUserById(Integer id);
}
