package com.hitmenwfm.controller;

import java.io.Serializable;

public class UserNameOnly implements Serializable   {
	private String userName;
	
	public UserNameOnly(){
		
	}
	
	public UserNameOnly(String userName) {
		setUserName(userName);
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String username) {
		this.userName = username;
	}
}
