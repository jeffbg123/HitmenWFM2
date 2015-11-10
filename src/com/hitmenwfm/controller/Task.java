package com.hitmenwfm.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Task implements Serializable {
	private int taskId;
	private String taskName;
	private String taskDescription;
	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="CST")
	private long startDate = Utils.DateToTimeStamp(new Date());
	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="CST")
	private long dueDate = Utils.DateToTimeStamp(new Date());
	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="CST")
	private long completedDate  = 0;
	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="CST")
	private long createDate = Utils.DateToTimeStamp(new Date());
	//@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="CST")
	private long updateDate = Utils.DateToTimeStamp(new Date());
	private String assignedToUser;
	private String assignedByUser;
	private int assignedToUserId = 0;
	private int assignedByUserId = 0;
	private int patient;
	
	public Task() {
	}
	
	public Task(int taskId, String taskName, String taskDescription, Date startDate, Date dueDate, Date completedDate, Date createDate, Date updateDate, int assignedToUserId, String assignedToUser, int assignedByUserId, String assignedByUser, int patient) {
		this.taskId = taskId;
		this.taskName = taskName;
		this.taskDescription = taskDescription;
		this.startDate = Utils.DateToTimeStamp(startDate);
		this.dueDate = Utils.DateToTimeStamp(dueDate);
		this.completedDate = Utils.DateToTimeStamp(completedDate);
		this.createDate = Utils.DateToTimeStamp(createDate);
		this.updateDate = Utils.DateToTimeStamp(updateDate);
		this.assignedToUserId = assignedToUserId;
		this.assignedByUserId = assignedByUserId;
		this.assignedByUser = assignedByUser;
		this.assignedToUser = assignedToUser;
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
	
	public long getStartDate() {
		return startDate;
	}
	
	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}
	
	public long getCompletedDate() {
		return completedDate;
	}
	
	public void setCompletedDate(long CompletedDate) {
		this.completedDate = CompletedDate;
	}
	
	public long getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}
	
	public long getUpdateDate() {
		return updateDate;
	}
	
	public void setUpdateDate(Date updatedDate) {
		this.updateDate = updateDate;
	}
	
	public long getDueDate() {
		return dueDate;
	}
	
	public void setDueDate(long dueDate) {
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
	
	public int getPatient() {
		return patient;
	}
	
	public void setPatient(int patient) {
		this.patient = patient;
	}
}
