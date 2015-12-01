package com.hitmenwfm.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HitmenWFMController {
	public String ErrorToJson(String error) {
		return "{ \"message\":\"" + error + "\"}";
	}
	
	public Boolean IsAnyoneLoggedIn(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		SecurityContext securityContext =(SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
		if(securityContext == null)
			return false;
		Authentication authentication = securityContext.getAuthentication();
		if (authentication == null) {
			return false;
		}
		return true;
	}
	
	//--------------------------------------------------------------------------------
	//START: /USER
	
	/**
	 * Takes a new user in json format and adds that user to the database with the 
	 * stored procedure sp_UI_CreateNewUser. It returns a the User that was created
	 * 
	 * @param user
	 * @return User
	 * @throws Exception
	 */
	@RequestMapping(value="/user",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> createUser(@RequestBody User user) throws Exception {
		try {
			SqlHelper sh = new SqlHelper();
			sh.InsertUser(user);
			return new ResponseEntity<>(user, HttpStatus.OK);
		}catch(Exception ex){
	        String errorMessage;
	        errorMessage = ex + " <== error";
	        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	    }	
	}
	
	/**
	 * Gets all users
	 * 
	 * 
	 * @param 
	 * @return List<User>
	 * @throws Exception
	 */
	@RequestMapping(value="/user",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> getUsers() throws Exception {
		try {
			SqlHelper sh = new SqlHelper();
			List<User> allUsers = sh.getAllUsers();
			for(int i = 0;i<allUsers.size();i++) {
				allUsers.get(i).setPassword("***");
			}
			return new ResponseEntity<>(allUsers, HttpStatus.OK);
		}catch(Exception ex){
	        String errorMessage;
	        errorMessage = ex + " <== error";
	        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	    }	
	}
	
	/**
	 * Takes a username as a path parameter referring to a username that is already
	 * in the database. It looks up that username, creates a User object from the info it finds, 
	 * and returns that user
	 * 
	 * @param username
	 * @return User
	 * @throws Exception
	 */
	@RequestMapping(value="/user/{username}",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> getUserUsername(@PathVariable String username) throws Exception {
		try {
			SqlHelper sh = new SqlHelper();
			User toReturn = sh.getUserByUsername(username);
			
			if(toReturn != null) {
				toReturn.setPassword("****");
				return new ResponseEntity<>(toReturn, HttpStatus.OK);
			}
			
			return new ResponseEntity<>(ErrorToJson("No User Found"), HttpStatus.BAD_REQUEST);
		}catch(Exception ex){
	        String errorMessage;
	        errorMessage = ex + " <== error";
	        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	    }	
	}
	
	/**
	 * Takes a username in json format and sends an email to the username's associated
	 * email with a link to reset the password. It returns the user.
	 * 
	 * @param userName
	 * @return User
	 * @throws Exception
	 */
	@RequestMapping(value="/user/forgot",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> userForgot(@RequestBody UserNameOnly userName) throws Exception {
		try {
			SqlHelper sh = new SqlHelper();
			User user = sh.getUserByUsername(userName.getUserName());
			user.emailForgotPassword();
			if(user != null) {
				user.setPassword("****");
				return new ResponseEntity<>(user, HttpStatus.OK);
			}
			return new ResponseEntity<>(ErrorToJson("No User Found"), HttpStatus.BAD_REQUEST);
		}catch(Exception ex){
	        String errorMessage;
	        errorMessage = ex + " <== error";
	        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	    }	
	}
	
	/**
	 * This will take JSON for a username and a new password. 
	 * It set the password in the database for that user to the new password. 
	 * It will return the User that has been modified.
	 * 
	 * 
	 * @param PasswordInfo
	 * @return User
	 * @throws Exception
	 */
	@RequestMapping(value="/user/password",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> userSetPassword(@RequestBody PasswordInfo passwordInfo) throws Exception {
		try {
			SqlHelper sh = new SqlHelper();
			Boolean success = sh.updatePassword(passwordInfo.getUserName(), passwordInfo.getPassword(), passwordInfo.getVerificationToken());
			User user = sh.getUserByUsername(passwordInfo.getUserName());
			if(user != null && success)
				return new ResponseEntity<>(user, HttpStatus.OK);
			else if(!success) {
				return new ResponseEntity<>(ErrorToJson("Wrong verification token"), HttpStatus.BAD_REQUEST);				
			}
			return new ResponseEntity<>(ErrorToJson("No User Found"), HttpStatus.BAD_REQUEST);
		}catch(Exception ex){
	        String errorMessage;
	        errorMessage = ex + " <== error";
	        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	    }	
	}
	
	/**
	 *  This will take JSON for a username and password. If they match it will return the User object,
	 *   otherwise it will return an error
	 * 
	 * 
	 * @param PasswordInfo
	 * @return User
	 * @throws Exception
	 */
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> userLogin(@RequestBody PasswordInfo loginInfo, HttpServletRequest request) throws Exception {
		try {
			SqlHelper sh = new SqlHelper();
			User user = sh.getUserByUsername(loginInfo.getUserName());
			if(!Utils.GetMD5(loginInfo.getUserName(),  loginInfo.getPassword()).equals(user.getPassword()))
				return new ResponseEntity<>(ErrorToJson("Login Failed: Wrong username or password"), HttpStatus.BAD_REQUEST);
			login(request, user.getuserName(), user.getPassword());
		    System.out.println("Successfully authenticated");
		    return new ResponseEntity<>(user, HttpStatus.OK);
	      } catch(Exception ex) {
	    	  String errorMessage;
		        errorMessage = ex + " <== error";
		        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	      }
			
	}
	
	public void login(HttpServletRequest request, String userName, String password)
	{
		Collection<GrantedAuthority> a=new ArrayList<GrantedAuthority>();
	      a.add(new GrantedAuthorityImpl("admin"));
		UserDetails user = new org.springframework.security.core.userdetails.User(userName, password, true, true, true, true, a);
		Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), user.getAuthorities());
		 SecurityContext securityContext = SecurityContextHolder.getContext();
		SecurityContextHolder.getContext().setAuthentication(authentication);
		HttpSession session = request.getSession(true);
	    session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);	 
	}
	
	public String MyUsername(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		SecurityContext securityContext =(SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
		Authentication authentication = securityContext.getAuthentication();
		if (authentication == null) {
			return "NULL";
		}
		String username = authentication.getName();
		return username;
	}
	
	/**
	 *  Checks whether or not the user is authenticated
	 *  
	 * 
	 * 
	 * @param 
	 * @return User
	 * @throws Exception
	 */
	@RequestMapping(value="/user/login",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> userLoginGet(HttpServletRequest request) throws Exception {
		try {
			HttpSession session = request.getSession(true);
			SecurityContext securityContext =(SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
			Authentication authentication = securityContext.getAuthentication();
			if (authentication == null) {
				return new ResponseEntity<>(ErrorToJson("No one is authenticated!"), HttpStatus.BAD_REQUEST);
			}
			String username = authentication.getName();
			SqlHelper sh = new SqlHelper();
			User toReturn = sh.getUserByUsername(username);
			return new ResponseEntity<>(toReturn, HttpStatus.OK);
		} catch(Exception ex) {
	    	  String errorMessage;
		        errorMessage = ex + " <== error";
		        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	      }
	}
	
	/**
	 *  This will take JSON for a user and update all of the user info based on the username
	 *  It will return the User that has been updated
	 * 
	 * 
	 * @param User
	 * @return User
	 * @throws Exception
	 */
	@RequestMapping(value="/user/update",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> userUpdate(@RequestBody User user) throws Exception {
		try {
			SqlHelper sh = new SqlHelper();
			sh.updateUser(user);
			if(user != null)
				return new ResponseEntity<>(user, HttpStatus.OK);
			return new ResponseEntity<>(ErrorToJson("No User Found"), HttpStatus.BAD_REQUEST);
		}catch(Exception ex){
	        String errorMessage;
	        errorMessage = ex + " <== error";
	        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	    }	
	}
	
	//END: /USER
	//--------------------------------------------------------------------------------
	
	//--------------------------------------------------------------------------------
	//START: /TASKS
	
	/**
	 *  This will take JSON for a task, create the task, and return the created task
	 * 
	 * 
	 * @param Task
	 * @return Task
	 * @throws Exception
	 */
	@RequestMapping(value="/tasks",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> createTask(@RequestBody Task task,HttpServletRequest request) throws Exception {
		try {
			if(!IsAnyoneLoggedIn(request)) {
				return new ResponseEntity<>(ErrorToJson("No one is logged in!"), HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
			}
			
			SqlHelper sh = new SqlHelper();
			sh.InsertTask(task);
			
			if(task != null) {
				return new ResponseEntity<>(task, HttpStatus.OK);
			}
			return new ResponseEntity<>(ErrorToJson("No Task Found"), HttpStatus.BAD_REQUEST);
		}catch(Exception ex){
	        String errorMessage;
	        errorMessage = ex + " <== error";
	        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	    }	
	}
	
	/**
	 *  This will take a taskid as a path parameter and JSON for a task object. 
	 *  It will update the task with the new task object and return the new Task
	 * 
	 * 
	 * @param int , Task
	 * @return Task
	 * @throws Exception
	 */
	@RequestMapping(value="/tasks/{taskid}",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> updateTask(@PathVariable int taskid, @RequestBody Task task,HttpServletRequest request) throws Exception {
		try {
			if(!IsAnyoneLoggedIn(request)) {
				return new ResponseEntity<>(ErrorToJson("No one is logged in!"), HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
			}
			SqlHelper sh = new SqlHelper();
			sh.updateTask(taskid, task);
			if(task != null) {
				task.alertAssignedToUserUpdatedTask(sh);
				return new ResponseEntity<>(task, HttpStatus.OK);
			}
			return new ResponseEntity<>(ErrorToJson("No Task Found"), HttpStatus.BAD_REQUEST);
		}catch(Exception ex){
	        String errorMessage;
	        errorMessage = ex + " <== error";
	        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	    }	
	}
	
	/**
	 *   takes no parameters/input and returns all of the tasks in the system
	 * 
	 * 
	 * @param NONE
	 * @return List<Task>
	 * @throws Exception
	 */
	@RequestMapping(value="/tasks",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> getTask(HttpServletRequest request) throws Exception {
		try {
			if(!IsAnyoneLoggedIn(request)) {
				return new ResponseEntity<>(ErrorToJson("No one is logged in!"), HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
			}
			SqlHelper sh = new SqlHelper();
			List<Task> toReturn = sh.getAllTasks();
			if(toReturn != null)
				return new ResponseEntity<>(toReturn, HttpStatus.OK);
			return new ResponseEntity<>(ErrorToJson("No Tasks Found"), HttpStatus.BAD_REQUEST);
		}catch(Exception ex){
	        String errorMessage;
	        errorMessage = ex + " <== error";
	        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	    }	
	}
	
	/**
	 *   takes a username and category (all/completed/outstanding) and returns all of the tasks in the system
	 * 		that fit the parameters
	 * 
	 * @param String username, String category (all/completed/outstanding)
	 * @return List<Task>
	 * @throws Exception
	 */
	@RequestMapping(value="/tasks/{username}/{category}",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> getTaskByUsernameAndCategory(@PathVariable String username, @PathVariable String category,HttpServletRequest request) throws Exception {
		try {
			if(!IsAnyoneLoggedIn(request)) {
				return new ResponseEntity<>(ErrorToJson("No one is logged in!"), HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
			}
			SqlHelper sh = new SqlHelper();
			List<Task> toReturn = sh.getAllTasks(username, category);
			if(toReturn != null)
				return new ResponseEntity<>(toReturn, HttpStatus.OK);
			return new ResponseEntity<>(ErrorToJson("No Tasks Found"), HttpStatus.BAD_REQUEST);
		}catch(Exception ex){
	        String errorMessage;
	        errorMessage = ex + " <== error";
	        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	    }	
	}
	
	/**
	 *   This will take a taskid as a path parameter and delete the task from
	 *    the database. It will return the deleted task
	 * 
	 * 
	 * @param int taskid
	 * @return TaskID
	 * @throws Exception
	 */
	@RequestMapping(value="/tasks/{taskid}",method=RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<?> deleteTask(@PathVariable int taskid,HttpServletRequest request) throws Exception {
		try {
			if(!IsAnyoneLoggedIn(request)) {
				return new ResponseEntity<>(ErrorToJson("No one is logged in!"), HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
			}
			SqlHelper sh = new SqlHelper();
			List<Task> t = sh.getAllTasks();
			for(int i = 0;i<t.size();i++) {
				if(t.get(i).getTaskId() == taskid) {
					t.get(i).alertAssignedToUserDeletedTask(sh);
				}
			}
			sh.deleteTask(taskid);
			return new ResponseEntity<>(taskid, HttpStatus.OK);
		}catch(Exception ex){
	        String errorMessage;
	        errorMessage = ex + " <== error";
	        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	    }	
	}
	
	/**
	 *  This will take a taskid as a path parameter and update the task in the database to complete
	 * It returns the Task
	 * 
	 * @param int taskid
	 * @return taskid
	 * @throws Exception
	 */
	@RequestMapping(value="/tasks/{taskid}/complete",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> markTaskComplete(@PathVariable int taskid,HttpServletRequest request) throws Exception {
		try {
			if(!IsAnyoneLoggedIn(request)) {
				return new ResponseEntity<>(ErrorToJson("No one is logged in!"), HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
			}
			SqlHelper sh = new SqlHelper();
			sh.markTaskComplete(taskid);
			List<Task> t = sh.getAllTasks();
			for(int i = 0;i<t.size();i++) {
				if(t.get(i).getTaskId() == taskid) {
					t.get(i).alertAssignedByUserCompletedTask(sh);
				}
			}
			return new ResponseEntity<>(taskid, HttpStatus.OK);
		}catch(Exception ex){
	        String errorMessage;
	        errorMessage = ex + " <== error";
	        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	    }	
	}
	
	//END: /TASKS
	//--------------------------------------------------------------------------------
	
	//--------------------------------------------------------------------------------
	//START: /TEMPLATES
		
	/**
	 *   This will return a list of all of the templates in the database
	 * 
	 * 
	 * @param NONE
	 * @return List<Template>
	 * @throws Exception
	 */
	@RequestMapping(value="/templates",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> getTemplates(HttpServletRequest request) throws Exception {
		try {
			if(!IsAnyoneLoggedIn(request)) {
				return new ResponseEntity<>(ErrorToJson("No one is logged in!"), HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
			}
			SqlHelper sh = new SqlHelper();
			List<Template> toReturn = sh.getAllTemplates();
			if(toReturn != null)
				return new ResponseEntity<>(toReturn, HttpStatus.OK);
			return new ResponseEntity<>(ErrorToJson("No Templates Found"), HttpStatus.BAD_REQUEST);
		}catch(Exception ex){
	        String errorMessage;
	        errorMessage = ex + " <== error";
	        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	    }	
	}
	
	/**
	 *   This will return the template in the database with the template id specified
	 * 
	 * 
	 * @param int
	 * @return Template
	 * @throws Exception
	 */
	@RequestMapping(value="/templates/{templateid}",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> getTemplateById(@PathVariable int templateid,HttpServletRequest request) throws Exception {
		try {
			if(!IsAnyoneLoggedIn(request)) {
				return new ResponseEntity<>(ErrorToJson("No one is logged in!"), HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
			}
			SqlHelper sh = new SqlHelper();
			Template toReturn = sh.getTemplate(templateid);
			if(toReturn != null)
				return new ResponseEntity<>(toReturn, HttpStatus.OK);
			return new ResponseEntity<>(ErrorToJson("No Template Found"), HttpStatus.BAD_REQUEST);
		}catch(Exception ex){
	        String errorMessage;
	        errorMessage = ex + " <== error";
	        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	    }	
	}

	/**
	 *   Takes a template and adds all tasks for that template to the database
	 * 
	 * 
	 * @param Template
	 * @return Template
	 * @throws Exception
	 */
	@RequestMapping(value="/templates",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<?> insertTemplate(@RequestBody TemplateInsert templateInsert,HttpServletRequest request) throws Exception {
		try {
			if(!IsAnyoneLoggedIn(request)) {
				return new ResponseEntity<>(ErrorToJson("No one is logged in!"), HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
			}
			SqlHelper sh = new SqlHelper();
			sh.insertTemplate(templateInsert);
			return new ResponseEntity<>(templateInsert, HttpStatus.OK);
		}catch(Exception ex){
	        String errorMessage;
	        errorMessage = ex + " <== error";
	        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	    }	
	}
	
	//END: /TEMPLATES
	//--------------------------------------------------------------------------------
	
	//--------------------------------------------------------------------------------
	//START: /REPORTS
	
	/**
	 *   This will return a each user's # of outstanding/completed tasks, as well as the average time per completed task
	 * 
	 * 
	 * @param 
	 * @return List<UserReport>
	 * @throws Exception
	 */
	@RequestMapping(value="/reports",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> getReports(HttpServletRequest request) throws Exception {
		try {
			if(!IsAnyoneLoggedIn(request)) {
				return new ResponseEntity<>(ErrorToJson("No one is logged in!"), HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
			}
			SqlHelper sh = new SqlHelper();
			List<UserReport> toReturn = sh.getUserReports();			
			if(toReturn != null)
				return new ResponseEntity<>(toReturn, HttpStatus.OK);
			return new ResponseEntity<>(ErrorToJson("Problem generating reports!"), HttpStatus.BAD_REQUEST);
		}catch(Exception ex){
	        String errorMessage;
	        errorMessage = ex + " <== error";
	        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	    }	
	}
	
	

	//END: /REPORTS
	//--------------------------------------------------------------------------------

	//--------------------------------------------------------------------------------
	//START: /PATIENTS
	//GET /patients?name=XXX
	/**
	 *   This will return a list of all of the patients with a name containing the parameter
	 * 
	 * 
	 * @param name String
	 * @return List<String>
	 * @throws Exception
	 */
	@RequestMapping(value="/patients/name/{name}",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> getPatientsByName(@PathVariable String name,HttpServletRequest request) throws Exception {
		try {
			if(!IsAnyoneLoggedIn(request)) {
				return new ResponseEntity<>(ErrorToJson("No one is logged in!"), HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
			}
			FHIRHelper fh = new FHIRHelper();
			List<String> patientNames = fh.getPatientNames(name);
			if(patientNames != null)
				return new ResponseEntity<>(patientNames, HttpStatus.OK);
			return new ResponseEntity<>(ErrorToJson("Problem getting patients!"), HttpStatus.BAD_REQUEST);
		}catch(Exception ex){
	        String errorMessage;
	        errorMessage = ex + " <== error";
	        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	    }	
	}
	
	//GET /patients/<patient-id>  - for one patient's data
	/**
	 *   This will return the name of the patient with the given ID
	 * 
	 * 
	 * @param int id
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/patients/id/{id}",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> getPatientById(@PathVariable int id,HttpServletRequest request) throws Exception {
		try {
			if(!IsAnyoneLoggedIn(request)) {
				return new ResponseEntity<>(ErrorToJson("No one is logged in!"), HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
			}
			FHIRHelper fh = new FHIRHelper();
			String patientName = fh.getPatientById(id);
			if(patientName != null)
				return new ResponseEntity<>(patientName, HttpStatus.OK);
			return new ResponseEntity<>(ErrorToJson("Problem getting patient!"), HttpStatus.BAD_REQUEST);
		}catch(Exception ex){
	        String errorMessage;
	        errorMessage = ex + " <== error";
	        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	    }	
	}
	
	//GET /fullpatients/id/<patient-id>  - for one patient's data
	/**
	 *   This will return all of the patient info for the patient with the given ID
	 * 
	 * 
	 * @param int id
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/fullpatients/id/{id}",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> getFullPatientById(@PathVariable int id,HttpServletRequest request) throws Exception {
		try {
			if(!IsAnyoneLoggedIn(request)) {
				return new ResponseEntity<>(ErrorToJson("No one is logged in!"), HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
			}
			FHIRHelper fh = new FHIRHelper();
			String patientName = fh.getFullPatientById(id);
			if(patientName != null)
				return new ResponseEntity<>(patientName, HttpStatus.OK);
			return new ResponseEntity<>(ErrorToJson("Problem getting patient!"), HttpStatus.BAD_REQUEST);
		}catch(Exception ex){
	        String errorMessage;
	        errorMessage = ex + " <== error";
	        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	    }	
	}
	
	/**
	 *   This will return a list of all of the patients with a name containing the parameter and all of their information
	 * 
	 * 
	 * @param name String
	 * @return List<String>
	 * @throws Exception
	 */
	@RequestMapping(value="/fullpatients/name/{name}",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> getFullPatientsByName(@PathVariable String name,HttpServletRequest request) throws Exception {
		try {
			if(!IsAnyoneLoggedIn(request)) {
				return new ResponseEntity<>(ErrorToJson("No one is logged in!"), HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
			}
			FHIRHelper fh = new FHIRHelper();
			String patientNames = fh.getFullPatientNames(name);
			if(patientNames != null)
				return new ResponseEntity<>(patientNames, HttpStatus.OK);
			return new ResponseEntity<>(ErrorToJson("Problem getting patients!"), HttpStatus.BAD_REQUEST);
		}catch(Exception ex){
	        String errorMessage;
	        errorMessage = ex + " <== error";
	        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	    }	
	}

	//END: /PATIENTS
	//--------------------------------------------------------------------------------
	
	@RequestMapping(value="/sendalerts",method=RequestMethod.GET)
	public void getPatientById() throws Exception {
		try {
		String header = "HitmenWFM Alert";
		String body = "Placeholder for alert";
		String username = "hitmenwfm";
		String password = "hitmenwfmhitmenwfm";
		GoogleMail.Send(username, password, "jeffbg123@gmail.com", header, body);
		}catch(Exception ex) {
			
		}
	}
	
}
