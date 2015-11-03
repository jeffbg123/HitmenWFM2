package com.hitmenwfm.controller;

import java.util.Date;

public class Task {
	private String taskName;
	private String taskDescription;
	private Date startDate;
	private Date dueDate;
	private String assignedToUser;
	private String assignedByUser;
	private String patient;
	
	public String getTaskName() {
		return taskName;
	}
	
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public String getTaskDescription() {
		return taskDescription;
	}
	
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getDueDate() {
		return dueDate;
	}
	
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	public String getAssignedToUser() {
		return assignedToUser;
	}
	
	public void setAssignedToUser(String assignedToUser) {
		this.assignedToUser = assignedToUser;
	}
	
	public String getAssignedByUser() {
		return assignedByUser;
	}
	
	public void setAssignedByUser(String assignedByUser) {
		this.assignedByUser = assignedByUser;
	}
	
	public String getPatient() {
		return patient;
	}
	
	public void setPatient(String patient) {
		this.patient = patient;
	}
}
