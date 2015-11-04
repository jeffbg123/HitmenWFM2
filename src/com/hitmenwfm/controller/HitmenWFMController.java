package com.hitmenwfm.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
	public @ResponseBody User createUser(@RequestBody User user) throws Exception {
		SqlHelper sh = new SqlHelper();
		sh.InsertUser(user);
		return user;
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
	public @ResponseBody User getUserUsername(@PathVariable String username) throws Exception {
		SqlHelper sh = new SqlHelper();
		return sh.getUserByUsername(username);
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
	public @ResponseBody User userForgot(@RequestBody UserName userName) throws Exception {
		SqlHelper sh = new SqlHelper();
		User user = sh.getUserByUsername(userName.getUserName());
		user.emailForgotPassword();
		return user;
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
	public @ResponseBody User userSetPassword(@RequestBody PasswordInfo passwordInfo) throws Exception {
		SqlHelper sh = new SqlHelper();
		sh.updatePassword(passwordInfo.getUserName(), passwordInfo.getPassword());
		return sh.getUserByUsername(passwordInfo.getUserName());		
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
	public @ResponseBody User userLogin(@RequestBody PasswordInfo loginInfo) throws Exception {
		SqlHelper sh = new SqlHelper();
		User user = sh.getUserByUsername(loginInfo.getUserName());
		if(user.getPassword().equals(loginInfo.getPassword()))
				return user;
		return null;
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
	public @ResponseBody User userUpdate(@RequestBody User user) throws Exception {
		SqlHelper sh = new SqlHelper();
		sh.updateUser(user);
		return user;
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
	public @ResponseBody Task createTask(@RequestBody Task task) throws Exception {
		SqlHelper sh = new SqlHelper();
		sh.InsertTask(task);
		return task;
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
	public @ResponseBody Task updateTask(@PathVariable int taskid, @RequestBody Task task) throws Exception {
		SqlHelper sh = new SqlHelper();
		sh.updateTask(taskid, task);
		return task;		
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
	public @ResponseBody List<Task> getTask() throws Exception {
		SqlHelper sh = new SqlHelper();
		return sh.getAllTasks();
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
	public @ResponseBody List<Task> getTaskByUsernameAndCategory(@PathVariable String username, @PathVariable String category) throws Exception {
		SqlHelper sh = new SqlHelper();
		return sh.getAllTasks(username, category);
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
	public @ResponseBody int deleteTask(@PathVariable int taskid) throws Exception {
		SqlHelper sh = new SqlHelper();
		sh.deleteTask(taskid);
		return taskid;
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
	public @ResponseBody int markTaskComplete(@PathVariable int taskid) throws Exception {
		SqlHelper sh = new SqlHelper();
		sh.markTaskComplete(taskid);
		return taskid;
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
	public @ResponseBody List<Template> getTemplates() throws Exception {
		SqlHelper sh = new SqlHelper();
		return sh.getAllTemplates();
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
	public @ResponseBody Template getTemplateById(@PathVariable int templateId) throws Exception {
		SqlHelper sh = new SqlHelper();
		return sh.getTemplate(templateId);
	}

	//END: /TEMPLATES
	//--------------------------------------------------------------------------------
	
	//--------------------------------------------------------------------------------
	//START: /REPORTS
	
	

	//END: /REPORTS
	//--------------------------------------------------------------------------------
}
