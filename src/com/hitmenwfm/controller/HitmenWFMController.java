package com.hitmenwfm.controller;

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
	
	@RequestMapping(value="/user",method=RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> createUser(@RequestBody User user) throws Exception {
		SqlHelper sh = new SqlHelper();
		sh.InsertUser(user);
		return new ResponseEntity<String>("User added successfully", HttpStatus.OK);
	}
	
	@RequestMapping(value="/user/{userId}",method=RequestMethod.GET)
	public @ResponseBody User getUserById(@PathVariable int userId) throws Exception {
		SqlHelper sh = new SqlHelper();
		return sh.getUserById(userId);
	}
}
