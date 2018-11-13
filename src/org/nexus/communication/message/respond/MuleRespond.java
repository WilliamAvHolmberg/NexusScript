package org.nexus.communication.message.respond;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;
import java.util.function.BooleanSupplier;

import org.nexus.NexusScript;
import org.nexus.communication.message.NexMessage;
import org.nexus.handler.TaskHandler;
import org.nexus.objects.RSItem;
import org.nexus.task.Task;
import org.nexus.task.TaskType;
import org.nexus.task.WoodcuttingTask;
import org.nexus.task.mule.DepositToSlave;
import org.nexus.utils.WebBank;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class MuleRespond extends TaskRespond {

	public MuleRespond(MethodProvider methodProvider, Stack<NexMessage> messageQueue, String respond) {
		super(methodProvider, messageQueue, respond);
	}

	@Override
	public void execute(PrintWriter out, BufferedReader in) throws IOException {
		String[] parsed = respond.split(":");
		String tradeName = parsed[3];
		int world = Integer.parseInt(parsed[4]);
		int itemID = Integer.parseInt(parsed[5]);
		int itemAmount = Integer.parseInt(parsed[6]);
		int startAmount = (int) methodProvider.inventory.getAmount(itemID);
		newTask = new DepositToSlave(world, itemID, itemAmount, startAmount, tradeName);
		newTask.setTimeStartedMilli(currentTime);
		newTask.setCondition(()-> newTask.tradeIsCompleted);
		TaskHandler.addTask(newTask);
	}
}
