package org.nexus.communication;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
import java.util.Stack;
import java.util.function.BooleanSupplier;

import org.nexus.NexusScript;
import org.nexus.handler.TaskHandler;
import org.nexus.objects.RSItem;
import org.nexus.provider.NexProvider;
import org.nexus.task.Task;
import org.nexus.task.TaskType;
import org.nexus.task.WoodcuttingTask;
import org.nexus.utils.WebBank;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class NexHelper extends NexProvider implements Runnable {
	//private String ip = "oxnetserver.ddns.net";
	private String ip = "nexus.no-ip.org";
	private int port = 43594;
	private long lastLog = 0;

	public static Stack<String> messageQueue;
	private String respond = "none";

	private String parsedBankArea;
	private String parsedActionArea;
	private String parsedAxeID;
	private String axeName;
	private String treeName;
	private String parsedBreakCondition;
	private String breakAfter;
	private String taskType;
	private Area bankArea;
	private Area actionArea;
	private RSItem axe;
	private BooleanSupplier breakCondition;
	private long currentTime;
	private String currentTaskID;
	private int axeID;
	private Task newTask;
	private String nextRequest;
	private String[] parsed;
	private String string;
	private String[] parsedRespond;

	public NexHelper() {
		messageQueue = new Stack<String>();
	}

	@Override
	public void run() {
		log("started NexHelper");
		try {
			Socket socket = new Socket(ip, port);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			log("lets init");
			initializeContactToSocket(out, in);

			whileShouldRun(out, in); // main loop, always run while script should be running

		} catch (Exception e) {
			log("Fail");
			log(e);
			disconnect();
		}

	}

	private void whileShouldRun(PrintWriter out, BufferedReader in) throws IOException, InterruptedException {
		log("alive");
		while (NexusScript.SHOULD_RUN) {
			checkIfBanned(out, in);
			if (!messageQueue.isEmpty()) {
				handleMessageQueue(out, in);
			} else {
				// log("lets log");
				logToServer(out, in);
				Thread.sleep(1000);
			}
		}

	}

	private void handleMessageQueue(PrintWriter out, BufferedReader in) throws InterruptedException, IOException {
		nextRequest = messageQueue.pop();
		log(nextRequest);
		parsed = nextRequest.split(":");
		string = parsed[0];

		switch (string) {
		case "DISCONNECT":
			NexusScript.SHOULD_RUN = false;
			logToServer(out, in);
			break;
		case "BANNED":
			sendBannedMessage(out, in);
			break;
		case "NEW_TASK":
			log("lets request new task");
			requestTask(out, in);
			break;
		case "task_respond":
			handleTaskRespond(parsed);
			logToServer(out, in);
			break;
		case "TASK_LOG":
			log("lets update task log");
			logToServer(nextRequest, out, in);
			break;
		case "MULE_WITHDRAW":
			log("lets send mule request");
			handleMuleRequest(nextRequest, out, in);
		default:
			logToServer(out, in);
			break;
		}

	}

	private void handleMuleRequest(String nextRequest, PrintWriter out, BufferedReader in) throws IOException {
		String itemID = nextRequest.split(":")[1];
		String amount = nextRequest.split(":")[2];
		out.println("mule_request:"+itemID + ":" + amount + ":" + myPlayer().getName() + ":" + worlds.getCurrentWorld());
		respond = in.readLine();
		parsedRespond = respond.split(":");
		if(parsedRespond[0].equals("SUCCESSFUL")) {
			log("good response");
		}else {
			log("no mule available");
			disconnect();
		}
	}

	/*
	 * Initialize contact towards socket if connection fails, stop script
	 */
	private void initializeContactToSocket(PrintWriter out, BufferedReader in) throws IOException {
		log("bla");
		out.println("script:1:" + getIP() + ":" + getBot().getUsername());
		respond = in.readLine();
		if (respond.equals("connected:1")) {
			log("NexHelper has been initialized towards Nexus");
		} else {
			log("Connection Towards Nexus failed");
			disconnect();
		}
	}
	
	private void disconnect() {
		messageQueue.push("DISCONNECT");
	}

	private String getRespond() {
		if (NexusScript.currentTask != null) {
			respond = "log:1:" + NexusScript.currentTask.getTaskID();
		}
		return "log:0";
	}

	/*
	 * reads the respond from server if respond is anything else than the 'standard'
	 * - "logged:fine" put respond to messageQueue
	 */
	private void handleRespond(String respond2) {
		if (!respond.equals("logged:fine")) {
			messageQueue.push(respond);
		}
	}

	public void createLog(Task currentTask) {
		if (currentTask != null) {
			messageQueue.push("task_log:" + currentTask.getTaskID() + ":" + currentTask.getGainedXP());
		}
	}

	/*
	 * Method to take care of every log
	 */
	private void logToServer(PrintWriter out, BufferedReader in) throws InterruptedException, IOException {
		if (System.currentTimeMillis() - lastLog > 5000) { // only log every 5 sec
			respond = getRespond();
			out.println(respond);
			respond = in.readLine();
			handleRespond(respond);
			lastLog = System.currentTimeMillis();
		}

	}

	private void logToServer(String message, PrintWriter out, BufferedReader in) throws IOException {
		respond = message;
		out.println(respond);
		respond = in.readLine();
		handleRespond(respond);
		lastLog = System.currentTimeMillis();
	}

	private void requestTask(PrintWriter out, BufferedReader in) throws IOException {
		String string = "skills;";
		for(Skill skill : Skill.values()) {
			string += skill + "," + skills.getStatic(skill) + ";";
		}
		log(string);
		out.println("task_request:1:" + string);
		respond = in.readLine();
		log("got respond from task_request:" + respond);
		handleRespond(respond);
	}

	public void getNewTask() {
		log("lets get new task");
		newTask = TaskHandler.popTask();
		if (newTask != null) {
			NexusScript.currentTask = newTask;
		} else if (!messageQueue.contains("NEW_TASK")) {
			messageQueue.push("NEW_TASK");
		}
	}

	private void handleTaskRespond(String[] parsed) {
		if (parsed[1].equals("0")) {
			handleTaskRespond(parsed);
			NexusScript.SHOULD_RUN = false;
		} else {
			taskType = parsed[2];

			switch (taskType) {
			case "WOODCUTTING":
				handleWoodCuttingRespond(parsed);
				break;
			case "BREAK":
				handleBreakRespond(parsed);
				break;

			}
		}

	}

	private void handleWoodCuttingRespond(String[] parsed) {
		currentTaskID = parsed[3];
		parsedBankArea = parsed[4];
		parsedActionArea = parsed[5];
		parsedAxeID = parsed[6];
		axeName = parsed[7];
		treeName = parsed[8];
		parsedBreakCondition = parsed[9];
		breakAfter = parsed[10];
		breakCondition = getBreakCondition(parsedBreakCondition, breakAfter);
		if (parsedBankArea.equals("none")) {
			bankArea = null;
		} else {
			bankArea = WebBank.parseCoordinates(parsedBankArea);
		}
		axeID = Integer.parseInt(parsedAxeID);
		actionArea = WebBank.parseCoordinates(parsedActionArea);
		axe = new RSItem(axeName, axeID);
		log("Axe:" + axe.getId() + ":" + axe.getName());
		log("Break in: "
				+ (((currentTime + (Integer.parseInt(breakAfter) * 1000 * 60) - currentTime)) / 60000 + "minutes"));
		newTask = new WoodcuttingTask(actionArea, bankArea, breakCondition, axe, treeName);
		newTask.setBreakType(parsedBreakCondition);
		newTask.setBreakAfter(breakAfter);
		newTask.setTimeStartedMilli(currentTime);
		newTask.setTaskID(currentTaskID);
		TaskHandler.addTask(newTask);

	}

	private void handleBreakRespond(String[] parsed) {
		currentTaskID = parsed[3];
		parsedBreakCondition = parsed[4];
		breakAfter = parsed[5];
		newTask = new Task();
		newTask.setTaskType(TaskType.BREAK);
		newTask.setBreakType(parsedBreakCondition);
		newTask.setCondition(getBreakCondition(parsedBreakCondition, breakAfter));
		newTask.setBreakAfter(breakAfter);
		newTask.setTimeStartedMilli(currentTime);
		newTask.setTaskID(currentTaskID);
		TaskHandler.addTask(newTask);

	}

	private BooleanSupplier getBreakCondition(String parsedBreakCondition, String breakAfter) {
		if (parsedBreakCondition.equals("LEVEL")) {
			return breakCondition = () -> skills.getStatic(Skill.WOODCUTTING) > Integer
					.parseInt(breakAfter);
		} else if (parsedBreakCondition.equals("TIME")) {
			currentTime = System.currentTimeMillis();
			return breakCondition = () -> System.currentTimeMillis() > currentTime
					+ Integer.parseInt(breakAfter) * 1000 * 60;
		}
		return null;
	}

	private void checkIfBanned(PrintWriter out, BufferedReader in) throws IOException {
		if (getPlayer().isDisabledMessageVisible()) {
			sendBannedMessage(out, in);
		}
	}

	private void sendBannedMessage(PrintWriter out, BufferedReader in) throws IOException {
		out.println("banned:1");
		respond = in.readLine();
		messageQueue.push(respond);
	}

	

	public static String getIP() {
		URL url;
		try {
			url = new URL("http://checkip.amazonaws.com/");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			return br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "not_found";

	}

}
