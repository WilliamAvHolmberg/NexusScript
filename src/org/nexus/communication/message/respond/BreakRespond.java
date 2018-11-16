package org.nexus.communication.message.respond;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;
import org.nexus.communication.message.NexMessage;
import org.nexus.handler.TaskHandler;
import org.nexus.task.BreakTask;
import org.nexus.task.TaskType;


import org.osbot.rs07.script.MethodProvider;

public class BreakRespond extends TaskRespond {

	public BreakRespond(MethodProvider methodProvider, Stack<NexMessage> messageQueue, String respond) {
		super(methodProvider, messageQueue, respond);
	}

	@Override
	public void execute(PrintWriter out, BufferedReader in) throws IOException {
		String[] parsed = respond.split(":");
		String currentTaskID = parsed[3];
		String parsedBreakCondition = parsed[4];
		String breakAfter = parsed[5];
		currentTime = System.currentTimeMillis();
		newTask = new BreakTask();
		newTask.setTaskType(TaskType.BREAK);
		newTask.setBreakType(parsedBreakCondition);
		newTask.setBreakAfter((int)Double.parseDouble(breakAfter));
		newTask.setTimeStartedMilli(currentTime);
		newTask.setTaskID(currentTaskID);
		TaskHandler.addTask(newTask);
	}


	
	
}
