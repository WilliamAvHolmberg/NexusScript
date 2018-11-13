package org.nexus.communication.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;

import org.osbot.rs07.script.MethodProvider;

public class ServerLog extends NexMessage{

	public ServerLog(MethodProvider methodProvider, Stack<NexMessage> messageQueue, String respond) {
		super(methodProvider, messageQueue, respond);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(PrintWriter out, BufferedReader in) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
