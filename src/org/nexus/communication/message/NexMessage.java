package org.nexus.communication.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;

import org.nexus.task.ActionTask;
import org.osbot.rs07.script.MethodProvider;

public abstract class NexMessage {
	
	protected MethodProvider methodProvider;
	protected Stack<NexMessage> messageQueue;
	protected String respond;
	
	protected ActionTask newTask;
	
	public NexMessage(MethodProvider methodProvider, Stack<NexMessage> messageQueue, String respond) {
		this.methodProvider = methodProvider;
		this.messageQueue = messageQueue;
		this.respond = respond;
		methodProvider.log("new mess created: " + respond);
	}
	public abstract void execute(PrintWriter out, BufferedReader in) throws IOException;
	
}
