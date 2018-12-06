package org.nexus.communication.message.respond;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;
import java.util.function.BooleanSupplier;

import org.nexus.NexusScript;
import org.nexus.communication.message.NexMessage;
import org.nexus.handler.TaskHandler;
import org.nexus.handler.gear.Inventory;
import org.nexus.handler.gear.InventoryItem;
import org.nexus.objects.RSItem;
import org.nexus.task.ActionTask;
import org.nexus.task.TaskType;
import org.nexus.task.WoodcuttingTask;
import org.nexus.utils.WebBank;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public abstract class TaskRespond extends NexMessage {

	protected ActionTask newTask;
	protected long currentTime;
	
	public TaskRespond(MethodProvider methodProvider, Stack<NexMessage> messageQueue, String respond) {
		super(methodProvider, messageQueue, respond);
	}

	public BooleanSupplier getBreakCondition(String parsedBreakCondition, String breakAfter, long currentTime) {
		return  () -> System.currentTimeMillis() > currentTime
				+ Double.parseDouble(breakAfter) * 1000 * 60;
	}
	
	public Inventory getInventory(String parsedInventory) {
		Inventory inv = new Inventory();
		for(String parsedInvItem : parsedInventory.split(";")) {
			if(parsedInvItem.length() > 2) {
				String[] moreParsed = parsedInvItem.split(",");
				String itemName = moreParsed[0];
				int itemId = Integer.parseInt(moreParsed[1]);
				int itemAmount = Integer.parseInt(moreParsed[2]);
				int buyAmount = Integer.parseInt(moreParsed[3]);
				methodProvider.log("Buy amount for item: " + itemName + ":::" + buyAmount);
				InventoryItem newItem = new InventoryItem(itemAmount, new RSItem(itemName, itemId), buyAmount);
				inv.addItem(newItem);
			}
		}
		return inv;
	}

	public void setBreakConditions(ActionTask newTask, String parsedBreakCondition, String breakAfter,
			String parsedlevelGoal) {
		if(parsedBreakCondition.toLowerCase().contains("time_or_level")) {
			newTask.setBreakAfter(5 + (int)Double.parseDouble(breakAfter));
			methodProvider.log(breakAfter);
			methodProvider.log("we set condition");
			newTask.setWantedLevel((int)Double.parseDouble(parsedlevelGoal));
			methodProvider.log("we set wanted Level");
		}else if(parsedBreakCondition.toLowerCase().contains("time")) {
			newTask.setBreakAfter(5 + (int)Double.parseDouble(breakAfter));
		}else if(parsedBreakCondition.toLowerCase().contains("level")) {
			methodProvider.log(breakAfter);
			methodProvider.log("we set condition");
			newTask.setWantedLevel((int)Double.parseDouble(parsedlevelGoal));
			methodProvider.log("we set wanted Level");
		}
		
	}
}
