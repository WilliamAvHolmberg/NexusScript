package org.nexus.communication.message.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;
import java.util.function.BooleanSupplier;

import org.nexus.communication.message.NexMessage;
import org.osbot.rs07.script.MethodProvider;

public abstract class NexRequest extends NexMessage{

	
	public NexRequest(MethodProvider methodProvider, Stack<NexMessage> messageQueue, String respond) {
		super(methodProvider, messageQueue, respond);
	}

}
