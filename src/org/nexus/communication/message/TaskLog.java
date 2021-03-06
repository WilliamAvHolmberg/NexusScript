package org.nexus.communication.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;

import org.nexus.task.ActionTask;
import org.osbot.rs07.script.MethodProvider;

public class TaskLog extends NexMessage {

	ActionTask currentTask;
	public TaskLog(MethodProvider methodProvider, Stack<NexMessage> messageQueue, ActionTask currentTask, String respond) {
		super(methodProvider, messageQueue, respond);
		this.currentTask = currentTask;
	}

	@Override
	public void execute(PrintWriter out, BufferedReader in) throws IOException {
		out.println("TASK_LOG:" + currentTask.getTaskID() + ":" + currentTask.getGainedXP());
		in.readLine(); //will always return ok. Therefor nothing shall be done.
		
	}

}
