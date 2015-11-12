package com.hitmenwfm.controller;

import java.io.Serializable;
import java.util.Date;

public class TemplateInsert implements Serializable {
	private String templateName;
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
	private String patientName;
	
	public TemplateInsert() {
	}
	
	public String getTemplateName() {
		return templateName;
	}
	
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
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
	
	public String getPatientName() {
		return patientName;
	}
	
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
}
