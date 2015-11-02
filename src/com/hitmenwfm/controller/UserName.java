package com.hitmenwfm.controller;

public class UserName {
	private String UserNameValue;
	
	public UserName(String userName) {
		setUserName(userName);
	}
	
	public String getUserName() {
		return UserNameValue;
	}
	
	public void setUserName(String username) {
		this.UserNameValue = username;
	}
}
