package org.nexus.communication.message.respond;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;
import java.util.function.BooleanSupplier;

import org.nexus.NexusScript;
import org.nexus.communication.message.NexMessage;
import org.nexus.handler.TaskHandler;
import org.nexus.handler.gear.Gear;
import org.nexus.handler.gear.Inventory;
import org.nexus.objects.RSItem;
import org.nexus.task.CombatTask;
import org.nexus.task.ActionTask;
import org.nexus.task.TaskType;
import org.nexus.task.WoodcuttingTask;
import org.nexus.task.agility.AgilityTask;
import org.nexus.utils.WebBank;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class AgilityRespond extends TaskRespond {

	public AgilityRespond(MethodProvider methodProvider, Stack<NexMessage> messageQueue, String respond) {
		super(methodProvider, messageQueue, respond);
		
	}

	@Override
	public void execute(PrintWriter out, BufferedReader in) throws IOException {
		if(respond != null) {
		methodProvider.log(respond);
		}else {
			methodProvider.log("respond is null");
		}
		String[] parsed = respond.split(":");
		String currentTaskID = parsed[3];
		String parsedBreakCondition = parsed[4];
		String breakAfter = parsed[5];
		String parsedInventory = parsed[6];
		String parsedlevelGoal = parsed[7];

		

		currentTime = System.currentTimeMillis();
		Inventory inv = new Inventory();
		inv = getInventory(parsedInventory);
		
		newTask = new AgilityTask();
		newTask.setTimeStartedMilli(currentTime);
		newTask.setTaskID(currentTaskID);
		setBreakConditions(newTask, parsedBreakCondition, breakAfter, parsedlevelGoal);
		TaskHandler.addTask(newTask);
		methodProvider.log("created task");
	}
}
