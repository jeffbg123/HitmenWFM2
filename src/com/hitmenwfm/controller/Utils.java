package com.hitmenwfm.controller;

import java.util.Date;

public class Utils {
	public static long DateToTimeStamp(Date d) {
		return d.getTime();
	}
	
	public static Date TimestampToDate(long ts) {
		return new Date(ts);
	}
	
	
}
