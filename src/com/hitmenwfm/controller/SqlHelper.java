package com.hitmenwfm.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SqlHelper {
	
	public void InsertUser(User user) throws Exception {
		Connection conn = getMySqlConnection();
		String paramString = String.format("'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s'", 
				user.getuserName(), 
				user.getEmail(), 
				user.getPassword(),
				user.getFirstName(),
				user.getLastName(),
				user.getMiddleName(),
				user.getStreetAddressLine1(),
				user.getStreetAddressLine2(),
				user.getStreetAddressLine3(),
				user.getCity(),
				user.getZip(),
				user.getState(),
				user.getHomePhone(),
				user.getCellPhone(),
				user.getBirthDate());
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

	public User getUserByUsername(String username) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Connection conn = getMySqlConnection();
	    String simpleProc = "{ call sp_UI_GetUserFromUsername('" + username + "') }";
	    CallableStatement cs = conn.prepareCall(simpleProc);
	    cs.execute();
	    ResultSet rs1 = cs.getResultSet();
	    rs1.next();

	    User toReturn = new User(rs1.getString("username"),
	    		rs1.getString("email"),
	    		rs1.getString("password"),
	    		rs1.getString("FirstName"),
	    		rs1.getString("LastName"),
	    		rs1.getString("MiddleName"),
	    		rs1.getString("StreetAddressLine1"),
	    		rs1.getString("StreetAddressLine2"),
	    		rs1.getString("StreetAddressLine3"),
	    		rs1.getString("City"),
	    		rs1.getString("Zip"),
	    		rs1.getString("State"),
	    		rs1.getString("HomePhone"),
	    		rs1.getString("CellPhone"),
	    		rs1.getDate("BirthDate"));
	    conn.close();
	    return toReturn;
	}

	public void updatePassword(String userName, String newPassword) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Connection conn = getMySqlConnection();
		PreparedStatement pstmt = conn.prepareStatement("Update user SET password='" + newPassword + "' where username='" + userName + "'");
		pstmt.executeQuery();
	}
	
	public int getUserId(String username) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Connection conn = getMySqlConnection();
	    String simpleProc = "{ call sp_UI_GetUserFromUsername('" + username + "') }";
	    CallableStatement cs = conn.prepareCall(simpleProc);
	    cs.execute();
	    ResultSet rs1 = cs.getResultSet();
	    rs1.next();
	    return rs1.getInt("id");
	}

	public void updateUser(User user) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Connection conn = getMySqlConnection();
		String paramString = String.format("'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s'", 
				user.getuserName(), 
				user.getEmail(), 
				user.getPassword(),
				user.getFirstName(),
				user.getLastName(),
				user.getMiddleName(),
				user.getStreetAddressLine1(),
				user.getStreetAddressLine2(),
				user.getStreetAddressLine3(),
				user.getCity(),
				user.getZip(),
				user.getState(),
				user.getHomePhone(),
				user.getCellPhone(),
				user.getBirthDate());
	    String simpleProc = "{ call sp_UI_CreateNewUser(" + paramString + ") }";
	    CallableStatement cs = conn.prepareCall(simpleProc);
	    cs.execute();
	    conn.close();		
	}

	public void InsertTask(Task task) {
		// TODO Auto-generated method stub
		
	}

	public void updateTask(int taskid, Task task) {
		// TODO Auto-generated method stub
		
	}

	public List<Task> getAllTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Task> getAllTasks(String username, String category) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteTask(int taskid) {
		// TODO Auto-generated method stub
		
	}

	public void markTaskComplete(int taskid) {
		// TODO Auto-generated method stub
		
	}

	public List<Template> getAllTemplates() {
		// TODO Auto-generated method stub
		return null;
	}

	public Template getTemplate(int templateId) {
		// TODO Auto-generated method stub
		return null;
	}
}