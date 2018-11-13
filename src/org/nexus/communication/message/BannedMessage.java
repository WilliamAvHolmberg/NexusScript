package org.nexus.communication.message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;

import org.nexus.NexusScript;
import org.osbot.rs07.script.MethodProvider;

public class BannedMessage extends NexMessage {

	public BannedMessage(MethodProvider methodProvider, Stack<NexMessage> messageQueue, String respond) {
		super(methodProvider, messageQueue, respond);
	}

	@Override
	public void execute(PrintWriter out, BufferedReader in) throws IOException {
		out.println("banned:1");
		NexusScript.SHOULD_RUN = false;
	}

}
