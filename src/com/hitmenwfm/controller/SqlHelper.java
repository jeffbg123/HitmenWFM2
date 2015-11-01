package com.hitmenwfm.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SqlHelper {
	
	public void InsertUser(User user) throws Exception {
		Connection conn = getMySqlConnection();
		//String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String paramString = String.format("'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s'", 
				user.userName, 
				user.email, 
				user.password,
				user.firstName,
				user.lastName,
				user.middleName,
				user.streetAddressLine1,
				user.streetAddressLine2,
				user.streetAddressLine3,
				user.city,
				user.zip,
				user.state,
				user.homePhone,
				user.cellPhone,
				user.birthDate);
	    String simpleProc = "{ call sp_UI_CreateNewUser(" + paramString + ") }";
	    CallableStatement cs = conn.prepareCall(simpleProc);
	    cs.execute();
	    conn.close();
	}
	
	public static Connection getMySqlConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		String url = "jdbc:mysql://us-cdbr-iron-east-03.cleardb.net:3306/heroku_a072c6d825fcf83";
		Class.forName ("com.mysql.jdbc.Driver").newInstance ();
		Connection conn = DriverManager.getConnection (url, "b63f5d595665c5", "6531134d");
		return conn;
	  }
}