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
import org.nexus.utils.WebBank;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public abstract class TaskRespond extends NexMessage {

	protected Task newTask;
	protected long currentTime;
	
	public TaskRespond(MethodProvider methodProvider, Stack<NexMessage> messageQueue, String respond) {
		super(methodProvider, messageQueue, respond);
	}

	public BooleanSupplier getBreakCondition(String parsedBreakCondition, String breakAfter, long currentTime) {
		return  () -> System.currentTimeMillis() > currentTime
				+ Double.parseDouble(breakAfter) * 1000 * 60;
	}

}
