package com.hitmenwfm.controller;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Date;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

public class User   {
	private String userName;
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private String middleName;
	private String streetAddressLine1;
	private String streetAddressLine2;
	private String streetAddressLine3;
	private String city;
	private String state;
	private String zip;
	private String cellPhone;
	private String homePhone;
	private String birthDate;
	
	public User(String userName, String email, String password, String firstName, String lastName, String middleName,
			String streetAddressLine1, String streetAddressLine2, String streetAddressLine3, String city,
			String state, String zip, String cellPhone, String homePhone, String birthDate) throws SQLException {
		setUserName(userName);
		setEmail(email);
		setPassword(password);
		setFirstName(firstName);
		setMiddleName(middleName);
		setLastName(lastName);
		setStreetAddressLine1(streetAddressLine1);
		setStreetAddressLine2(streetAddressLine2);
		setStreetAddressLine3(streetAddressLine3);
		setCity(city);
		setState(state);
		setZip(zip);
		setCellPhone(cellPhone);
		setHomePhone(homePhone);
		setBirthDate(birthDate);
	}

	public String getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setStreetAddressLine1(String streetAddressLine1) {
		this.streetAddressLine1 = streetAddressLine1;
	}
	
	public void setStreetAddressLine2(String streetAddressLine2) {
		this.streetAddressLine2 = streetAddressLine2;
	}
	
	public void setStreetAddressLine3(String streetAddressLine3) {
		this.streetAddressLine3 = streetAddressLine3;
	}
	
	public void setCity(String city) {
		this.city= city;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	
	public String getuserName() {
		return userName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getMiddleName() {
		return middleName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getStreetAddressLine1() {
		return streetAddressLine1;
	}
	
	public String getStreetAddressLine2() {
		return streetAddressLine2;
	}
	
	public String getStreetAddressLine3() {
		return streetAddressLine3;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getState() {
		return state;
	}
	
	public String getZip() {
		return zip;
	}
	
	public String getHomePhone() {
		return homePhone;
	}
	
	public String getCellPhone() {
		return cellPhone;
	}
}