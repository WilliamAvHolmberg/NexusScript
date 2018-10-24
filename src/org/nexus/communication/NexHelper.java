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
	Stack<String> messageQueue;
	long lastLog = 0;
	private String respond = "none";
	private MethodProvider methodProvider;
	String ip = "192.168.10.127";
	int port = 2099;
	private String newTaskMessage = "new_task";

	public NexHelper(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
		messageQueue = new Stack<String>();
		methodProvider.log("inited com");
	}

	@Override
	public void run() {
		methodProvider.log("started run");
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
					switch (parsed[0]) {
					case "disconnect":
						NexusScript.SHOULD_RUN = false;
						log(out, in);
						break;
					case "new_task":
						methodProvider.log("lets request new task");
						requestTask(out, in);
						break;
					case "task_respond":
						if (parsed[1].equals("1")) {
							handleTaskRespond(parsed);
							log(out,in);
						} else {
							// unsuccessful task_respond. lets stop
							NexusScript.SHOULD_RUN = false;
							log(out, in);
						}
						break;
					default:
						log(out, in);
						break;
					}
				} else {
					methodProvider.log("lets log");
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
		String parsedTaskType = parsed[2];
		String parsedBankArea = parsed[3];
		String parsedActionArea = parsed[4];
		String parsedAxeID = parsed[5];
		String axeName = parsed[6];
		String treeName = parsed[7];
		TaskType taskType = TaskType.getType(parsedTaskType);
		Area bankArea;
		if (parsedBankArea.equals("none")) {
			bankArea = null;
		} else {
			// bankArea = WebBank.parseCoordinates(parsedBankArea);
		}
		// Area actionArea = WebBank.parseCoordinates(parsedActionArea);
		int axeID = Integer.parseInt(parsedAxeID);


		String parsedCoordinates = "";

		// if last, do not add ":"
		for (String coord : parsedActionArea.split("\\{")) {
			String newCoord = coord.replaceAll("\\},", "").replaceAll("\\}", "").replaceAll(" ", "");
			if (newCoord.length() > 3) {
				parsedCoordinates = parsedCoordinates + newCoord + ":";
			}
		}
		Position[] newCoordinates = new Position[parsedCoordinates.split(":").length];
		String[] almostReadyCoordinates = parsedCoordinates.split(":");
		for (int i = 0; i < almostReadyCoordinates.length; i++) {
			String[] parsedCoords = almostReadyCoordinates[i].split(",");
			int coordinate1 = Integer.parseInt(parsedCoords[0]);
			int coordinate2 = Integer.parseInt(parsedCoords[1]);
			if (coordinate1 > 500 && coordinate2 > 500) {
				Position newPos = new Position(coordinate1, coordinate2,0);
				newCoordinates[i] = newPos;
			}
		}
		for(Position coords : newCoordinates) {
			methodProvider.log("Coord:" + coords);
		}
		Area actionArea = new Area(newCoordinates);
		RSItem axe = new RSItem(axeName, axeID);
		NexusScript.currentTask = new WoodcuttingTask(actionArea, null , () -> methodProvider.skills.getStatic(Skill.WOODCUTTING) > 99, axe, treeName);


	}

	private void requestTask(PrintWriter out, BufferedReader in) throws IOException {
		methodProvider.log("sending task request");
		out.println("task_request:1");
		methodProvider.log("sent task request");
		respond = in.readLine();
		methodProvider.log("got respond" + respond);
		handleRespond(respond);
	}

	public void getNewTask() {
		if (!messageQueue.contains(newTaskMessage)) {
			messageQueue.push(newTaskMessage);
		}
	}

	private void log(PrintWriter out, BufferedReader in) throws InterruptedException, IOException {
		if (System.currentTimeMillis() - lastLog > 5000) { // only log every 5 sec
			methodProvider.log("sending log");
			out.println("log:0");
			methodProvider.log("sent log");
			respond = in.readLine();
			methodProvider.log("got respond" + respond);
			handleRespond(respond);
			lastLog = System.currentTimeMillis();
		}

	}

	private void handleRespond(String respond2) {
		// standard message is 'logged:fine'
		// if respond is anything else than logged:fine we can assume it is a new
		// instruction
		if (!respond.equals("logged:fine")) {
			System.out.println("we got a new instructionToQueue:" + respond);
			messageQueue.push(respond);
		}
	}

	private void initializeContactToSocket(PrintWriter out, BufferedReader in) throws IOException {
		methodProvider.log("bla");
		out.println("script:1:" + getIP() + ":" + methodProvider.bot.getUsername());
		if (in.readLine().equals("connected:1")) {
			methodProvider.log("NexHelper has been initialized towards Nexus");
		} else {
			methodProvider.log("Connection Towards Nexus failed");
		}
		methodProvider.log("exit");
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
