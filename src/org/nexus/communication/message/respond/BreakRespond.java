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

public class BreakRespond extends TaskRespond {

	public BreakRespond(MethodProvider methodProvider, Stack<NexMessage> messageQueue, String respond) {
		super(methodProvider, messageQueue, respond);
	}

	@Override
	public void execute(PrintWriter out, BufferedReader in) throws IOException {
		String[] parsed = respond.split(":");
		String currentTaskID = parsed[3];
		String parsedBreakCondition = parsed[4];
		String breakAfter = parsed[5];
		currentTime = System.currentTimeMillis();
		newTask = new Task();
		newTask.setTaskType(TaskType.BREAK);
		newTask.setBreakType(parsedBreakCondition);
		newTask.setCondition(getBreakCondition(parsedBreakCondition, breakAfter, currentTime));
		newTask.setBreakAfter(breakAfter);
		newTask.setTimeStartedMilli(currentTime);
		newTask.setTaskID(currentTaskID);
		TaskHandler.addTask(newTask);
	}


	
	
}
