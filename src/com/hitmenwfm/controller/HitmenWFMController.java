package com.hitmenwfm.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HitmenWFMController {
	private static AuthenticationManager am = new SampleAuthenticationManager();
	public String ErrorToJson(String error) {
		return "{ \"message\":\"" + error + "\"}";
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
	
	@RequestMapping(value="/user",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> createUser() throws Exception {
		try {
			SqlHelper sh = new SqlHelper();
			List<User> allUsers = sh.getAllUsers();
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
			
			if(toReturn != null)
				return new ResponseEntity<>(toReturn, HttpStatus.OK);
			
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
			if(user != null)
				return new ResponseEntity<>(user, HttpStatus.OK);
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
			sh.updatePassword(passwordInfo.getUserName(), passwordInfo.getPassword());
			User user = sh.getUserByUsername(passwordInfo.getUserName());
			if(user != null)
				return new ResponseEntity<>(user, HttpStatus.OK);
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
	public @ResponseBody ResponseEntity<?> userLogin(@RequestBody PasswordInfo loginInfo) throws Exception {
		try {
			SqlHelper sh = new SqlHelper();
			User user = sh.getUserByUsername(loginInfo.getUserName());
			if(!loginInfo.getPassword().equals(user.getPassword()))
				return new ResponseEntity<>(ErrorToJson("Login Failed: Wrong username or password"), HttpStatus.BAD_REQUEST);
	        Authentication request = new UsernamePasswordAuthenticationToken(loginInfo.getUserName(), loginInfo.getPassword());
	        Authentication result = am.authenticate(request);
	        SecurityContextHolder.getContext().setAuthentication(result);

		    System.out.println("Successfully authenticated. Security context contains: " +
		              SecurityContextHolder.getContext().getAuthentication());
		    return new ResponseEntity<>(user, HttpStatus.OK);
	      } catch(Exception ex) {
	    	  String errorMessage;
		        errorMessage = ex + " <== error";
		        return new ResponseEntity<>(ErrorToJson(errorMessage), HttpStatus.BAD_REQUEST);
	      }
			
	}
	
	@RequestMapping(value="/user/login",method=RequestMethod.GET)
	public @ResponseBody ResponseEntity<?> userLoginGet() throws Exception {
		if (SecurityContextHolder.getContext().getAuthentication() != null &&
				SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			SqlHelper help = new SqlHelper();
			User u = help.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			return new ResponseEntity<>(u, HttpStatus.OK);
		}
			

        return new ResponseEntity<>(ErrorToJson("User not authenticated"), HttpStatus.UNAUTHORIZED);
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
	public @ResponseBody ResponseEntity<?> createTask(@RequestBody Task task) throws Exception {
		try {
			SqlHelper sh = new SqlHelper();
			sh.InsertTask(task);
			if(task != null)
				return new ResponseEntity<>(task, HttpStatus.OK);
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
	public @ResponseBody ResponseEntity<?> updateTask(@PathVariable int taskid, @RequestBody Task task) throws Exception {
		try {
			SqlHelper sh = new SqlHelper();
			sh.updateTask(taskid, task);
			if(task != null)
				return new ResponseEntity<>(task, HttpStatus.OK);
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
	public @ResponseBody ResponseEntity<?> getTask() throws Exception {
		try {
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
	public @ResponseBody ResponseEntity<?> getTaskByUsernameAndCategory(@PathVariable String username, @PathVariable String category) throws Exception {
		try {
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
	public @ResponseBody ResponseEntity<?> deleteTask(@PathVariable int taskid) throws Exception {
		try {
			SqlHelper sh = new SqlHelper();
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
	public @ResponseBody ResponseEntity<?> markTaskComplete(@PathVariable int taskid) throws Exception {
		try {
			SqlHelper sh = new SqlHelper();
			sh.markTaskComplete(taskid);
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
	public @ResponseBody ResponseEntity<?> getTemplates() throws Exception {
		try {
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
	public @ResponseBody ResponseEntity<?> getTemplateById(@PathVariable int templateId) throws Exception {
		try {
			SqlHelper sh = new SqlHelper();
			Template toReturn = sh.getTemplate(templateId);
			if(toReturn != null)
				return new ResponseEntity<>(toReturn, HttpStatus.OK);
			return new ResponseEntity<>(ErrorToJson("No Template Found"), HttpStatus.BAD_REQUEST);
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
	
	

	//END: /REPORTS
	//--------------------------------------------------------------------------------
}
