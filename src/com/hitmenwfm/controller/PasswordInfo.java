package com.hitmenwfm.controller;

public class PasswordInfo {
	private String userName;
	private String password;
	private String verificationToken;
	
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setVerificationToken(String verificationToken) {
		this.verificationToken = verificationToken;
	}
	
	public String getVerificationToken() {
		return verificationToken;
	}
}
