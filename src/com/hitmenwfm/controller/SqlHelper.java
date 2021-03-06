package com.hitmenwfm.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
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

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
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
				Utils.GetMD5(user.getuserName(),user.getPassword()),
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

	public Boolean updatePassword(String userName, String newPassword, String verificationToken) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, UnsupportedEncodingException, NoSuchAlgorithmException {
		newPassword = Utils.GetMD5(userName, newPassword);
		Connection conn = getMySqlConnection();
		User user = getUserByUsername(userName);
		if(!verificationToken.equals(Utils.GetMD5(user.getFirstName(), user.getLastName()))) {
			return false;
		}
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
	    return true;
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
	    String simpleProc = "{ call sp_UI_GetAllUsers() }";
	    CallableStatement cs = conn.prepareCall(simpleProc);
	    cs.execute();
	    ResultSet rs1 = cs.getResultSet();
	    while(rs1.next()) {
		    User toAdd = new User(rs1,true);
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
		if(d == null)
			return "NULL";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
	}

	public void updateUser(User user) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, UnsupportedEncodingException, NoSuchAlgorithmException {
		Connection conn = getMySqlConnection();
		String paramString = String.format("'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s'", 
				user.getuserName(), 
				user.getEmail(), 
				Utils.GetMD5(user.getuserName(),  user.getPassword()),
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

	public void InsertTask(Task task) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, AddressException, MessagingException {
		Connection conn = getMySqlConnection();
		String paramString = String.format("'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s'", 
				task.getTaskName(), task.getTaskDescription(), dateToString(Utils.TimestampToDate(task.getStartDate())), dateToString(Utils.TimestampToDate(task.getDueDate())),
				dateToString(Utils.TimestampToDate(task.getCompletedDate())), getUserId(task.getAssignedToUser()), getUserId(task.getAssignedByUser()),
				task.getPatient(), dateToString(Utils.TimestampToDate(task.getCreateDate())), dateToString(Utils.TimestampToDate(task.getUpdateDate())), task.getPatientName());
		paramString = paramString.replace("'NULL'", "NULL");
	    String simpleProc = "{ call sp_ins_task(" + paramString + ") }";
	    CallableStatement cs = conn.prepareCall(simpleProc);
	    cs.execute();
	    conn.close();	
		task.alertAssignedToUserNewTask(this);
	}

	public void updateTask(int taskid, Task task) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Connection conn = getMySqlConnection();
		String paramString = String.format("'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s'", taskid,
				task.getTaskName(), task.getTaskDescription(), dateToString(Utils.TimestampToDate(task.getStartDate())), dateToString(Utils.TimestampToDate(task.getDueDate())),
						dateToString(Utils.TimestampToDate(task.getCompletedDate())), getUserId(task.getAssignedToUser()), getUserId(task.getAssignedByUser()),
				task.getPatient(), dateToString(Utils.TimestampToDate(task.getCreateDate())), dateToString(Utils.TimestampToDate(task.getUpdateDate())), task.getPatientName());
		paramString = paramString.replace("'NULL'", "NULL");
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
		    		rs1.getTimestamp("StartDate"),
		    		rs1.getTimestamp("DueDate"),
		    		rs1.getTimestamp("CompletedDate"),
		    		rs1.getTimestamp("CreateDate"),
		    		rs1.getTimestamp("UpdateDate"),
		    		rs1.getInt("AssignedToUserId"),
		    		getUsernameFromId(rs1.getInt("AssignedToUserId")),
		    		rs1.getInt("AssignedByUserId"),
		    		getUsernameFromId(rs1.getInt("AssignedByUserId")),
		    		rs1.getInt("patientId"),
		    		rs1.getString("PatientName"));
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
			
			else if(category.toUpperCase().equals("COMPLETED") && allTasks.get(i).getCompletedDate() == 0) {
				allTasks.remove(i);
			}
			
			else if(category.toUpperCase().equals("OUTSTANDING") && allTasks.get(i).getCompletedDate() > 0) {
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

	public List<Template> getAllTemplates() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<Template> toReturn = new ArrayList<Template>();
		Connection conn = getMySqlConnection();
	    String simpleProc = "{ call sp_sel_tasktemplate() }";
	    CallableStatement cs = conn.prepareCall(simpleProc);
	    cs.execute();
	    ResultSet rs1 = cs.getResultSet();
	    while(rs1.next()) {
	    	Template toAdd = new Template(rs1);
		    
		    toReturn.add(toAdd);
	    }
	    conn.close();
		
		return toReturn;
	}

	public Template getTemplate(int templateId) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<Template> allTemplates = getAllTemplates();
		for(int i = 0;i<allTemplates.size();i++) {
			if(allTemplates.get(i).getId() == templateId) {
				return allTemplates.get(i);
			}
		}
		return null;
	}

	public List<UserReport> getUserReports() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<UserReport> toReturn = new ArrayList<UserReport>();
		
		List<User> users = getAllUsers();
		List<Task> tasks = getAllTasks();
		
		for(int i = 0;i<users.size();i++) {
			toReturn.add(new UserReport(users.get(i)));
		}
		
		for(int i = 0;i<tasks.size();i++) {
			for(int z = 0;z<toReturn.size();z++) {
				toReturn.get(z).CheckThisTask(tasks.get(i));
			}
		}
		
		for(int i = 0;i<toReturn.size();i++) {
			toReturn.get(i).Calculate();
		}
		
		return toReturn;
	}

	public void insertTemplate(TemplateInsert templateInsert) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, AddressException, MessagingException {
		Connection conn = getMySqlConnection();
	    String simpleProc = "{ call sp_sel_tasksbytemplatename('" + templateInsert.getTemplateName() + "') }";
	    CallableStatement cs = conn.prepareCall(simpleProc);
	    cs.execute();
	    ResultSet rs1 = cs.getResultSet();
	    while(rs1.next()) {
	    	String taskName = rs1.getString("TaskName");
	    	String taskDescription = rs1.getString("TaskDescription");
	    	
			String paramString = String.format("'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s'", 
					taskName, taskDescription, dateToString(Utils.TimestampToDate(templateInsert.getStartDate())), dateToString(Utils.TimestampToDate(templateInsert.getDueDate())),
					dateToString(Utils.TimestampToDate(templateInsert.getCompletedDate())), getUserId(templateInsert.getAssignedToUser()), getUserId(templateInsert.getAssignedByUser()),
					templateInsert.getPatient(), dateToString(Utils.TimestampToDate(templateInsert.getCreateDate())), dateToString(Utils.TimestampToDate(templateInsert.getUpdateDate())),
							templateInsert.getPatientName());
			paramString = paramString.replace("'NULL'", "NULL");
		    String simpleProc2 = "{ call sp_ins_task(" + paramString + ") }";
		    CallableStatement cs2 = conn.prepareCall(simpleProc2);
		    cs2.execute();	
	    }
	    conn.close();
	    
	    String header = "HitmenWFM";
		String body = "A new template of tasks has been assigned to you! Tempalte name = " + templateInsert.getTemplateName();
		String username = "hitmenwfm";
		String password = "hitmenwfmhitmenwfm";
		String emailTo = getUserByUsername(templateInsert.getAssignedToUser()).getEmail();
		GoogleMail.Send(username, password, emailTo, header, body);
	}
}