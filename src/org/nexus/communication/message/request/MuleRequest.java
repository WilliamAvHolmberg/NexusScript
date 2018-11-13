package org.nexus.communication.message.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;

import org.nexus.NexusScript;
import org.nexus.communication.message.DisconnectMessage;
import org.nexus.communication.message.NexMessage;
import org.nexus.handler.TaskHandler;
import org.nexus.task.mule.WithdrawFromMule;
import org.osbot.rs07.script.MethodProvider;

public class MuleRequest extends NexRequest{

	public MuleRequest(MethodProvider methodProvider, Stack<NexMessage> messageQueue, String respond) {
		super(methodProvider, messageQueue, respond);
	}

	@Override
	public void execute(PrintWriter out, BufferedReader in) throws IOException {
		String[] nextRequest = respond.split(":");
		String itemID = nextRequest[1];
		String amount = nextRequest[2];
		out.println("mule_request:"+itemID + ":" + amount + ":" + methodProvider.myPlayer().getName() + ":" + methodProvider.worlds.getCurrentWorld());
		String respond = in.readLine();
		methodProvider.log("mule respond: " + respond);
		String[]parsedRespond = respond.split(":");
		if(parsedRespond[0].equals("SUCCESSFUL")) {
			handleSuccessfullMuleRespond(parsedRespond, Integer.parseInt(itemID), Integer.parseInt(amount));
		}else {
			methodProvider.log("no mule available");
			messageQueue.add(new DisconnectMessage(methodProvider, messageQueue, null));
		}
	}
	
	




	private void handleSuccessfullMuleRespond(String[] parsedRespond, int itemID, int amount) {
		
		String muleName = parsedRespond[1];
		String world = parsedRespond[2]; 
		int startAmount = (int) methodProvider.inventory.getAmount(itemID);
		newTask = new WithdrawFromMule(Integer.parseInt(world), itemID, amount, (int) startAmount, muleName.toLowerCase());
		newTask.setCondition(() -> methodProvider.inventory.getAmount(itemID) >= amount + startAmount);
		if(NexusScript.currentTask != null) {
			TaskHandler.addTask(NexusScript.currentTask);
		}
		NexusScript.currentTask = newTask;
		
	}

}
