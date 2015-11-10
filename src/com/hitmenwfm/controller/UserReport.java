package com.hitmenwfm.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserReport implements Serializable  {
	public User user;
	public int numCompletedTasks = 0;
	public int numOutstandingTasks = 0;
	public int numTasksAssignedOutstanding = 0;
	public int numTasksAssignedCompleted = 0;
	public long avgTimePerTask = 0;
	private List<Task> taskList = new ArrayList<Task>();
	private List<Task> taskListAssigned = new ArrayList<Task>();
	
	public UserReport() {
		
	}
	
	public UserReport(User u) {
		this.user = u;
	}
	
	public void Calculate() {
		SetAvgTimePerTask();
		CountTasks();
	}
	
	public void SetAvgTimePerTask() {
		long sumTime = 0;
		long countCompleted = 0;
		for(int i = 0;i<taskList.size();i++) {
			if(taskList.get(i).getCompletedDate() != 0) {
				sumTime = taskList.get(i).getCompletedDate() - taskList.get(i).getStartDate();
				countCompleted++;
			}
		}
		
		if(countCompleted > 0)
			avgTimePerTask = sumTime/countCompleted;
	}
	
	public void CountTasks() {
		for(int i = 0;i<taskList.size();i++) {
			if(taskList.get(i).getCompletedDate() != 0)
				numCompletedTasks++;
			else
				numOutstandingTasks++;
		}
		
		for(int i = 0;i<taskListAssigned.size();i++) {
			if(taskListAssigned.get(i).getCompletedDate() != 0)
				numTasksAssignedCompleted++;
			else
				numTasksAssignedOutstanding++;
		}
		
	}
	
	public void AddTaskAssigned(Task t) {
		taskListAssigned.add(t);
	}
	
	public void AddTask(Task t) {
		taskList.add(t);
	}

	public void CheckThisTask(Task task) {
		if(task.getAssignedToUser().equals(user.getuserName()) )
			taskList.add(task);
		if(task.getAssignedByUser().equals(user.getuserName()))
			taskListAssigned.add(task);		
	}
}
