package com.hitmenwfm.controller;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

public class User implements Serializable   {
	private int id = -1;
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
	private long createTime;
	private int userTypeId;
	private long updateTime;
	private int userDetailsId;
	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="CST")
	private long birthDate;
	
	public User() {
		
	}
	
	public User(String userName, String email, String password, String firstName, String lastName, String middleName,
			String streetAddressLine1, String streetAddressLine2, String streetAddressLine3, String city,
			String state, String zip, String cellPhone, String homePhone, Date birthDate) throws SQLException {
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
		setBirthDate(Utils.DateToTimeStamp(birthDate));
	}

	public User(ResultSet rs1) throws SQLException {
		setId(rs1.getInt("id"));
		setUserName(rs1.getString("username"));
		setEmail(rs1.getString("email"));
		setPassword(rs1.getString("password"));
		setCreateTime(Utils.DateToTimeStamp(rs1.getDate("create_time")));
		setUserTypeId(rs1.getInt("userTypeID"));
		setUpdateTime(Utils.DateToTimeStamp(rs1.getDate("UpdateTime")));
		setUserDetailsID(rs1.getInt("userDetailsID"));
	}
	
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	public void setUserTypeId(int userTypeId) {
		this.userTypeId = userTypeId;
	}
	
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	
	public void setUserDetailsID(int userDetailsId) {
		this.userDetailsId = userDetailsId;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public long getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(long birthDate) {
		this.birthDate = birthDate;
	}
	
	public String getBirthDateMysqlString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(Utils.TimestampToDate(getBirthDate()));
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

	public void emailForgotPassword() throws AddressException, MessagingException {
		String header = "HitmenWFM Forgot Password Reset Link";
		String body = "Placeholder for reset link";
		String username = "hitmenwfm";
		String password = "hitmenwfmhitmenwfm";
		GoogleMail.Send(username, password, email, header, body);
	}
}