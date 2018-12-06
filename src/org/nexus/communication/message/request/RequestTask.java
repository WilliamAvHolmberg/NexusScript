package org.nexus.communication.message.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;

import org.nexus.communication.message.DisconnectMessage;
import org.nexus.communication.message.NexMessage;
import org.nexus.communication.message.respond.AgilityRespond;
import org.nexus.communication.message.respond.BreakRespond;
import org.nexus.communication.message.respond.CombatRespond;
import org.nexus.communication.message.respond.MuleRespond;
import org.nexus.communication.message.respond.WoodcuttingRespond;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class RequestTask extends NexRequest{

	public RequestTask(MethodProvider methodProvider, Stack<NexMessage> messageQueue, String respond) {
		super(methodProvider, messageQueue, respond);
	}

	@Override
	public void execute(PrintWriter out, BufferedReader in) throws IOException {
		String string = "skills;";
		for(Skill skill : Skill.values()) {
			string += skill + "," + methodProvider.skills.getStatic(skill) + ";";
		}
		out.println("task_request:1:" + string);
		String respond = in.readLine();
		methodProvider.log("got respond from task_request:" + respond);
		handleRespond(respond);
	}
	
	private void handleRespond(String respond) {
		if (respond.contains("TASK_RESPOND:0") || respond.contains("DISCONNECT")) {
			messageQueue.push(new DisconnectMessage(methodProvider, messageQueue, "Failed to get task"));
		} else {
			methodProvider.log("Task respond!:" + respond);
			String[] parsedRespond = respond.split(":");
			String taskType = parsedRespond[2];
			
			switch (taskType) {
			case "BREAK":
				messageQueue.push(new BreakRespond(methodProvider, messageQueue, respond));
				break;
			case "WOODCUTTING":
				methodProvider.log("respond is not null here!" + respond);
				messageQueue.push(new WoodcuttingRespond(methodProvider, messageQueue, respond));
				break;
			case "COMBAT":
				methodProvider.log("respond is not null here!" + respond);
				messageQueue.push(new CombatRespond(methodProvider, messageQueue, respond));
				break;
			case "MULE_WITHDRAW":
				messageQueue.push(new MuleRespond(methodProvider, messageQueue, respond));
				break;
			case "MULE_DEPOSIT":
				messageQueue.push(new MuleRespond(methodProvider, messageQueue, respond));
				break;
			case "AGILITY":
				messageQueue.push(new AgilityRespond(methodProvider, messageQueue, respond));
				break;
				
			}
		}
	}

}
