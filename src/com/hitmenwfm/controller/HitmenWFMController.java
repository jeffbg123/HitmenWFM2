package com.hitmenwfm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HitmenWFMController {
	@RequestMapping("/welcome")
	public ModelAndView helloWorld() {
		String message = "<br><div style='text-align:center;'>"
				+ "<h3>********** Hello World, Spring MVC Tutorial</h3>This message is coming from CrunchifyHelloWorld.java **********</div><br><br>";
		return new ModelAndView("welcome", "message", message);
	}
	
	@RequestMapping(value="/user",method=RequestMethod.POST)
	public @ResponseBody User createUser(@RequestBody User user) throws Exception {
		SqlHelper sh = new SqlHelper();
		sh.InsertUser(user);
		return user;
	}
	
	@RequestMapping(value="/test",method=RequestMethod.POST)
	public @ResponseBody Test createTest(@RequestBody Test test) throws Exception {
		
		return test;
	}
	
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	   public String printHello(ModelMap model) {
	      model.addAttribute("message", "Hello Spring MVC Framework!");
	      return "hello";
	   }
}
