package com.hitmenwfm.controller;

import java.util.Date;

public class Task {
	private int taskId;
	private String taskName;
	private String taskDescription;
	private Date startDate;
	private Date dueDate;
	private Date completedDate;
	private Date createDate;
	private Date updateDate;
	private String assignedToUser;
	private String assignedByUser;
	private String patient;
	
	public Task(int taskId, String taskName, String taskDescription, Date startDate, Date dueDate, Date completedDate, Date createDate, Date updateDate, String assignedToUser, String assignedByUser, String patient) {
		this.taskId = taskId;
		this.taskName = taskName;
		this.taskDescription = taskDescription;
		this.startDate = startDate;
		this.dueDate = dueDate;
		this.completedDate = completedDate;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.assignedToUser = assignedToUser;
		this.assignedByUser = assignedByUser;
		this.patient = patient;
	}
	
	public int getTaskId() {
		return taskId;
	}
	
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	
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
	
	public Date getCompletedDate() {
		return completedDate;
	}
	
	public void setCompletedDate(Date CompletedDate) {
		this.completedDate = CompletedDate;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public Date getUpdateDate() {
		return updateDate;
	}
	
	public void setUpdateDate(Date updatedDate) {
		this.updateDate = updateDate;
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
