package org.nexus.handler;

import java.util.Stack;

import org.nexus.task.Task;

public class TaskHandler {
	public static Stack<Task> available_tasks = new Stack<Task>();
	
	public static void addTask(Task task) {
		available_tasks.add(task);
	}
	
	public static Task popTask() {
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
			for(Task task : available_tasks) {
				tempString += task;
			}
		}
		return tempString;
	}
}
