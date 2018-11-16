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
import org.nexus.communication.message.BannedMessage;
import org.nexus.communication.message.DisconnectMessage;
import org.nexus.communication.message.NexMessage;
import org.nexus.communication.message.TaskLog;
import org.nexus.communication.message.request.RequestTask;
import org.nexus.communication.message.respond.BreakRespond;
import org.nexus.communication.message.respond.MuleRespond;
import org.nexus.communication.message.respond.WoodcuttingRespond;
import org.nexus.handler.TaskHandler;
import org.nexus.objects.RSItem;
import org.nexus.provider.NexProvider;
import org.nexus.task.Task;
import org.nexus.task.TaskType;
import org.nexus.task.WoodcuttingTask;
import org.nexus.task.mule.WithdrawFromPlayer;
import org.nexus.utils.Timing;
import org.nexus.utils.WebBank;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class NexHelper extends NexProvider implements Runnable {
	 private String ip = "192.168.10.127";
	//private String ip = "oxnetserver.ddns.net";
	private int port = 43594;
	private long lastLog = 0;

	public static Stack<NexMessage> messageQueue;
	private String respond = "none";

	private Task newTask;
	private NexMessage nextRequest;
	private String[] parsed;
	private String string;
	private String[] parsedRespond;
	private String world;
	private String muleName;
	private long startAmount;

	public NexHelper() {
		messageQueue = new Stack<NexMessage>();
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
			messageQueue.add(new DisconnectMessage(this, messageQueue, "Failed to initialize"));
		}

	}

	private void whileShouldRun(PrintWriter out, BufferedReader in) throws IOException, InterruptedException {
		log("alive");
		while (NexusScript.SHOULD_RUN) {
			checkIfBanned(out, in);
			if (!messageQueue.isEmpty()) {
				log("message queue not empty");
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
		if (nextRequest != null) {
			log(nextRequest);
			nextRequest.execute(out, in);
		} else {
			logToServer(out, in);
		}

	}

	/*
	 * reads the respond from server if respond is anything else than the 'standard'
	 * - "logged:fine" put respond to messageQueue
	 */
	private void handleRespond(String respond) {
		if (!respond.equals("logged:fine")) {
			parsedRespond = respond.split(":");
			switch (parsedRespond[0]) {
			case "SUCCESSFUL_MULE_REQUEST":
				messageQueue.push(new MuleRespond(this, messageQueue, respond));
				break;
			case "UNSUCCESSFULL_MULE_REQUEST":
				messageQueue.push(new DisconnectMessage(this, messageQueue, "Failed to get mule"));
				break;
			default:
				log("no respond found for message:" + respond);
				break;
			}
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
			messageQueue.push(new DisconnectMessage(this, null, "failed to initialize contact"));
		}
	}

	private String getRespond() {
		if (NexusScript.currentTask != null) {
			respond = "log:1:" + NexusScript.currentTask.getTaskID();
		}
		return "log:0";
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

	public void getNewTask() {
		log("lets get new task");
		newTask = TaskHandler.popTask();
		if (newTask != null) {
			NexusScript.currentTask = newTask;
		} else if (!messageQueue.contains("NEW_TASK")) {
			messageQueue.push(new RequestTask(this, messageQueue, null));
		}
	}

	private void checkIfBanned(PrintWriter out, BufferedReader in) throws IOException {
		if (!client.isLoggedIn() && getPlayer().isDisabledMessageVisible()) {
			messageQueue.push(new BannedMessage(this, null, "Player is banned"));
		}
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
