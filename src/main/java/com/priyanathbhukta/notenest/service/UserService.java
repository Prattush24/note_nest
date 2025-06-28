package com.priyanathbhukta.notenest.service;

import com.priyanathbhukta.notenest.dto.UserDto;

public interface UserService {
	public Boolean register(UserDto userDto);
	
	public void deleteUserById(Integer id);
}
