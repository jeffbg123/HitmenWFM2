package com.hitmenwfm.controller;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
	public static long DateToTimeStamp(Date d) {
		if(d == null)
			return 0;
		return d.getTime();
	}
	
	public static Date TimestampToDate(long ts) {
		if(ts == 0)
			return null;
		return new Date(ts);
	}
	
	public static String GetMD5(String username, String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String s = username + password;
		byte[] bytesOfMessage = s.getBytes("UTF-8");
		StringBuffer hexString = new StringBuffer();
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] hash = md.digest(bytesOfMessage);
		for (int i = 0; i < hash.length; i++) {
		    if ((0xff & hash[i]) < 0x10) {
		        hexString.append("0"
		                + Integer.toHexString((0xFF & hash[i])));
		    } else {
		        hexString.append(Integer.toHexString(0xFF & hash[i]));
		    }
		}
		
		return hexString.toString();
	}
	
	
}
