package org.nexus.communication;

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
import org.nexus.objects.RSItem;
import org.nexus.task.TaskType;
import org.nexus.task.WoodcuttingTask;
import org.nexus.utils.WebBank;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class NexHelper implements Runnable {
	private MethodProvider methodProvider;
	private String ip = "192.168.10.127";
	private int port = 2099;
	private long lastLog = 0;

	private Stack<String> messageQueue;
	private final String newTaskMessage = "NEW_TASK";
	private final String disconnectMessage = "DISCONNECT";
	private String respond = "none";

	private String parsedTaskType;
	private String parsedBankArea;
	private String parsedActionArea;
	private String parsedAxeID;
	private String axeName;
	private String treeName;
	private Area bankArea;
	private Area actionArea;
	private RSItem axe;


	public NexHelper(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
		messageQueue = new Stack<String>();
		methodProvider.log("initiated NexHelper");
	}

	@Override
	public void run() {
		methodProvider.log("started NexHelper");
		try {
			Socket socket = new Socket(ip, port);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			methodProvider.log("lets init");
			initializeContactToSocket(out, in);

			String nextRequest;

			while (NexusScript.SHOULD_RUN) {
				methodProvider.log("Ready to take new instructions...last respond:" + respond);
				if (!messageQueue.isEmpty()) {
					nextRequest = messageQueue.pop();
					String[] parsed = nextRequest.split(":");
					String string = parsed[0];
					
					if (string.equals(disconnectMessage)) {
						NexusScript.SHOULD_RUN = false;
						log(out, in);
					} else if (string.equals(newTaskMessage)) {
						methodProvider.log("lets request new task");
						requestTask(out, in);
					} else if (string.equals("task_respond")) {
						if (parsed[1].equals("1")) {
							handleTaskRespond(parsed);
							log(out, in);
						} else {
							// unsuccessful task_respond. lets stop
							NexusScript.SHOULD_RUN = false;
							log(out, in);
						}
					} else {
						log(out, in);
					}
				} else {
					//methodProvider.log("lets log");
					log(out, in);
					Thread.sleep(1000);
				}
			}
		} catch (Exception e) {
			methodProvider.log("wrong");
			methodProvider.log(e.getMessage());
		}

	}

	private void handleTaskRespond(String[] parsed) {
		/**
		 * repond[0] == type of respond respond[1] == successful or not. 1 == successful
		 * respond[2] == task_type respond[3] == bank_area respond[4] == actionarea -
		 * coordinates respond[5] == axeID respond[6] == axeName respond[7] == treeName
		 */
		parsedTaskType = parsed[2];
		parsedBankArea = parsed[3];
		parsedActionArea = parsed[4];
		parsedAxeID = parsed[5];
		axeName = parsed[6];
		treeName = parsed[7];

		if (parsedBankArea.equals("none")) {
			bankArea = null;
		} else {
			bankArea = WebBank.parseCoordinates(parsedBankArea);
		}
		int axeID = Integer.parseInt(parsedAxeID);
		actionArea = WebBank.parseCoordinates(parsedActionArea);
		axe = new RSItem(axeName, axeID);
		NexusScript.currentTask = new WoodcuttingTask(actionArea, bankArea,
				() -> methodProvider.skills.getStatic(Skill.WOODCUTTING) > 99, axe, treeName);

	}

	private void requestTask(PrintWriter out, BufferedReader in) throws IOException {
		out.println("task_request:1");
		respond = in.readLine();
		methodProvider.log("got respond from task_request:" + respond);
		handleRespond(respond);
	}

	public void getNewTask() {
		if (!messageQueue.contains(newTaskMessage)) {
			messageQueue.push(newTaskMessage);
		}
	}

	/*
	 * Method to take care of every log
	 */
	private void log(PrintWriter out, BufferedReader in) throws InterruptedException, IOException {
		if (System.currentTimeMillis() - lastLog > 5000) { // only log every 5 sec
			out.println("log:0");
			respond = in.readLine();
			handleRespond(respond);
			lastLog = System.currentTimeMillis();
		}

	}

	/*
	 * reads the respond from server
	 * if respond is anything else than the 'standard' - "logged:fine"
	 * put respond to messageQueue
	 */
	private void handleRespond(String respond2) {
		if (!respond.equals("logged:fine")) {
			messageQueue.push(respond);
		}
	}

	/*
	 * Initialize contact towards socket
	 * if connection fails, stop script
	 */
	private void initializeContactToSocket(PrintWriter out, BufferedReader in) throws IOException {
		//methodProvider.log("bla");
		out.println("script:1:" + getIP() + ":" + methodProvider.bot.getUsername());
		if (in.readLine().equals("connected:1")) {
			methodProvider.log("NexHelper has been initialized towards Nexus");
		} else {
			methodProvider.log("Connection Towards Nexus failed");
			messageQueue.push(disconnectMessage);
		}
	}

	public String getIP() {
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
