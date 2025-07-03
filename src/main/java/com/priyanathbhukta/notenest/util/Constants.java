package com.priyanathbhukta.notenest.util;

public class Constants {
	public static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
	
	public static final String INDIAN_MOBILE_REGEX = "^\\+91[6-9]\\d{9}$";
	
	public static final String PASSWORD_REGEX = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
	
	public static final String PASSWORD_UPPERCASE = ".*[A-Z].*";
	
    public static final String PASSWORD_LOWERCASE = ".*[a-z].*";
    
    public static final String PASSWORD_DIGIT = ".*\\d.*";
    
    public static final String PASSWORD_SPECIAL_CHAR = ".*[#?!@$%^&*-].*";
    
    public static final int PASSWORD_MIN_LENGTH = 8;
    
    public static final String ROLE_ADMIN = "hasRole('ADMIN')";
    
    public static final String ROLE_ADMIN_USER =  "hasAnyRole('USER','ADMIN')";
    
    public static final String ROLE_USER = "hasRole('USER')";
    
    public static final String DEFAULT_PAGE_NO = "0";
    
    public static final String DEFAULT_PAGE_SIZE= "10";
}
