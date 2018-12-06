package org.nexus.handler;

import java.util.Stack;

import org.nexus.task.ActionTask;

public class TaskHandler {
	public static Stack<ActionTask> available_tasks = new Stack<ActionTask>();
	
	public static void addTask(ActionTask task) {
		available_tasks.add(task);
	}
	
	public static ActionTask popTask() {
		if(available_tasks.isEmpty()) {
			return null;
		}
		return available_tasks.pop();
	}
	
	public static String getTaskList() {
		String tempString = "";
		if(available_tasks.isEmpty()) {
			return "No Tasks in TaskList.";
		}else {
			for(ActionTask task : available_tasks) {
				tempString += task;
			}
		}
		return tempString;
	}
	
}
