package com.hitmenwfm.controller;

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
	
	
}
