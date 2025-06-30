package com.priyanathbhukta.notenest.service;

import com.priyanathbhukta.notenest.dto.PasswordChngRequest;

public interface UserService {
	
	public void changePassword(PasswordChngRequest passwordRequest);
	
}
