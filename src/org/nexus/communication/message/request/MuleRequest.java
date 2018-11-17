package org.nexus.communication.message.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;

import org.nexus.NexusScript;
import org.nexus.communication.message.DisconnectMessage;
import org.nexus.communication.message.NexMessage;
import org.nexus.handler.TaskHandler;
import org.nexus.task.mule.DepositToPlayer;
import org.nexus.task.mule.WithdrawFromPlayer;
import org.osbot.rs07.script.MethodProvider;

public class MuleRequest extends NexRequest {

	public MuleRequest(MethodProvider methodProvider, Stack<NexMessage> messageQueue, String respond) {
		super(methodProvider, messageQueue, respond);
	}

	@Override
	public void execute(PrintWriter out, BufferedReader in) throws IOException {
		String[] nextRequest = respond.split(":");
		String muleType = nextRequest[0].toLowerCase();
		String itemID = nextRequest[1];
		String amount = nextRequest[2];
		out.println("mule_request:" + itemID + ":" + amount + ":" + methodProvider.myPlayer().getName() + ":"
				+ methodProvider.worlds.getCurrentWorld() + ":" + muleType);
		String respond = in.readLine();
		methodProvider.log("mule respond: " + respond);
		String[] parsedRespond = respond.split(":");
		if (parsedRespond[0].equals("SUCCESSFUL_MULE_REQUEST")) {
			handleSuccessfullMuleRespond(parsedRespond, Integer.parseInt(itemID), Integer.parseInt(amount));
		} else {
			methodProvider.log("no mule available");
			messageQueue.add(new DisconnectMessage(methodProvider, messageQueue, null));
		}
	}

	private void handleSuccessfullMuleRespond(String[] parsedRespond, int itemID, int amount) {

		String muleName = parsedRespond[1];
		String world = parsedRespond[2];
		String muleType = parsedRespond[3].toLowerCase();
		int startAmount;
		long currentTime = System.currentTimeMillis();
		switch (muleType) {
		case "mule_deposit":
			startAmount = (int) methodProvider.inventory.getAmount(itemID);
			newTask = new DepositToPlayer(Integer.parseInt(world), itemID, amount, (int) startAmount,
					muleName.toLowerCase());
			
			newTask.setTimeStartedMilli(currentTime);
			newTask.setBreakAfter(5);
			NexusScript.currentTask = newTask;
			break;
		case "mule_withdraw":
			startAmount = (int) methodProvider.inventory.getAmount(itemID);
			newTask = new WithdrawFromPlayer(Integer.parseInt(world), itemID, amount, (int) startAmount,
					muleName.toLowerCase());
			newTask.setTimeStartedMilli(currentTime);
			newTask.setBreakAfter(5);
			NexusScript.currentTask = newTask;
			break;
		}
		
		


	}

}
