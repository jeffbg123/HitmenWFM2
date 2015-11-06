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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class SqlHelper {
	
	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public String handeException(HttpServletResponse httpRes,Throwable ex) {
		httpRes.setStatus(HttpStatus.BAD_REQUEST.value());
        return httpRes.toString();
    }
	
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
				user.getBirthDateMysqlString());
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
		User user = getUserByUsername(userName);
		String paramString = String.format("'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s'", 
				user.getuserName(), 
				user.getEmail(), 
				newPassword,
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
				user.getBirthDateMysqlString());
	    String simpleProc = "{ call sp_UI_UpdateUser(" + paramString + ") }";
	    CallableStatement cs = conn.prepareCall(simpleProc);
	    cs.execute();
	    conn.close();
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
	
	public List<User> getAllUsers() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<User> toReturn = new ArrayList<User>();
		
		Connection conn = getMySqlConnection();
	    String simpleProc = "{ call sp_sel_user() }";
	    CallableStatement cs = conn.prepareCall(simpleProc);
	    cs.execute();
	    ResultSet rs1 = cs.getResultSet();
	    while(rs1.next()) {
		    User toAdd = new User(rs1);
		    toReturn.add(toAdd);
	    }
	    conn.close();
		
		return toReturn;
	}
	
	public String getUsernameFromId(int id) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<User> allUsers = getAllUsers();
		for(int i = 0;i<allUsers.size();i++) {
			if(allUsers.get(i).getId() == id)
				return allUsers.get(i).getuserName();
		}
		return "";
	}
	
	public String dateToString(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
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
				user.getBirthDateMysqlString());
	    String simpleProc = "{ call sp_UI_UpdateUser(" + paramString + ") }";
	    CallableStatement cs = conn.prepareCall(simpleProc);
	    cs.execute();
	    conn.close();		
	}

	public void InsertTask(Task task) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Connection conn = getMySqlConnection();
		String paramString = String.format("'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s'", 
				task.getTaskName(), task.getTaskDescription(), dateToString(Utils.TimestampToDate(task.getStartDate())), dateToString(Utils.TimestampToDate(task.getDueDate())),
				dateToString(Utils.TimestampToDate(task.getCompletedDate())), getUserId(task.getAssignedToUser()), getUserId(task.getAssignedByUser()),
				task.getPatient(), dateToString(Utils.TimestampToDate(task.getCreateDate())), dateToString(Utils.TimestampToDate(task.getUpdateDate())));
	    String simpleProc = "{ call sp_ins_task(" + paramString + ") }";
	    CallableStatement cs = conn.prepareCall(simpleProc);
	    cs.execute();
	    conn.close();		
	}

	public void updateTask(int taskid, Task task) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Connection conn = getMySqlConnection();
		String paramString = String.format("'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s'", taskid,
				task.getTaskName(), task.getTaskDescription(), dateToString(Utils.TimestampToDate(task.getStartDate())), dateToString(Utils.TimestampToDate(task.getDueDate())),
						dateToString(Utils.TimestampToDate(task.getCompletedDate())), getUserId(task.getAssignedToUser()), getUserId(task.getAssignedByUser()),
				task.getPatient(), dateToString(Utils.TimestampToDate(task.getCreateDate())), dateToString(Utils.TimestampToDate(task.getUpdateDate())));
	    String simpleProc = "{ call sp_upd_task(" + paramString + ") }";
	    CallableStatement cs = conn.prepareCall(simpleProc);
	    cs.execute();
	    conn.close();		
	}
	
	public List<Task> getAllTasks() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		List<Task> toReturn = new ArrayList<Task>();
		
		Connection conn = getMySqlConnection();
	    String simpleProc = "{ call sp_sel_task() }";
	    CallableStatement cs = conn.prepareCall(simpleProc);
	    cs.execute();
	    ResultSet rs1 = cs.getResultSet();
	    while(rs1.next()) {
		    Task toAdd = new Task(rs1.getInt("id"),
		    		rs1.getString("TaskName"),
		    		rs1.getString("TaskDescription"),
		    		rs1.getDate("StartDate"),
		    		rs1.getDate("DueDate"),
		    		rs1.getDate("CompletedDate"),
		    		rs1.getDate("CreateDate"),
		    		rs1.getDate("UpdateDate"),
		    		rs1.getInt("AssignedToUserId"),
		    		getUsernameFromId(rs1.getInt("AssignedToUserId")),
		    		rs1.getInt("AssignedByUserId"),
		    		getUsernameFromId(rs1.getInt("AssignedByUserId")),
		    		rs1.getInt("patientId"));
		    toReturn.add(toAdd);
	    }
	    conn.close();
		
		return toReturn;
	}

	public List<Task> getAllTasks(String username, String category) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<Task> allTasks = getAllTasks();
		int userId = getUserId(username);
		for(int i = allTasks.size()-1;i>=0;i--) {
			if(!(allTasks.get(i).getAssignedToUser()).equals(username)) {
				allTasks.remove(i);
			}
			
			if(category.toUpperCase().equals("COMPLETED") && allTasks.get(i).getCompletedDate() == 0) {
				allTasks.remove(i);
			}
			
			if(category.toUpperCase().equals("OUTSTANDING")  && allTasks.get(i).getCompletedDate() != 0) {
				allTasks.remove(i);	
			}
		}
		return allTasks;
	}

	public void deleteTask(int taskid) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Connection conn = getMySqlConnection();
	    String simpleProc = "{ call sp_del_task(" + taskid + ") }";
	    CallableStatement cs = conn.prepareCall(simpleProc);
	    cs.execute();
	    conn.close();
	}
	
	public void deleteTask(Task t) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		deleteTask(t.getTaskId());
	}

	public void markTaskComplete(int taskid) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<Task> allTasks = getAllTasks();
		for(int i = 0;i<allTasks.size();i++) {
			if(allTasks.get(i).getTaskId() == taskid) {
				Task t = allTasks.get(i);
				t.setCompletedDate(Utils.DateToTimeStamp(new Date()));
				updateTask(t.getTaskId(), t);
			}
		}
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