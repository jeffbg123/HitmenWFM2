package com.hitmenwfm.controller;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Template {
	private int id;
	private String TemplateName;
	private String TemplateDescription;
	
	public Template() {
		
	}
	
	public Template(ResultSet rs1) throws SQLException {
		this.id = rs1.getInt("id");
		this.TemplateName = rs1.getString("TemplateName");
		this.TemplateDescription = rs1.getString("TemplateDescription");
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTemplateName() {
		return TemplateName;
	}
	
	public void setTemplateName(String templateName) {
		this.TemplateName = templateName;
	}
	
	public String getTemplateDescription() {
		return TemplateDescription;
	}
	
	public void setTemplateDescription(String templateDescription) {
		this.TemplateDescription = templateDescription;
	}
}
