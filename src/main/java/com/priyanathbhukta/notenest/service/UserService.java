package com.priyanathbhukta.notenest.service;

import com.priyanathbhukta.notenest.dto.UserDto;

public interface UserService {
	public Boolean register(UserDto userDto, String url) throws Exception;
	
	public void deleteUserById(Integer id);

}
