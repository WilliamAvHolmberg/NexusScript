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

import org.nexus.NexusScript;
import org.osbot.rs07.script.MethodProvider;

public class NexHelper implements Runnable {
	Stack<String> messageQueue;
	long lastLog = 0;
	private String respond = "none";
	private MethodProvider methodProvider;
	String ip = "0.0.0.0";
	int port = 2099;
	private String newTaskMessage = "new_task";

	public NexHelper(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
		messageQueue = new Stack<String>();
		methodProvider.log("inited com");
	}

	private void log(PrintWriter out, BufferedReader in) throws InterruptedException, IOException {
		if (System.currentTimeMillis() - lastLog > 5000) { // only log every 5 sec
			methodProvider.log("sending log");
			out.println("log:0");
			methodProvider.log("sent log");
			respond = in.readLine();
			methodProvider.log("got respond" + respond);
			// standard message is 'logged:fine'
			// if respond is anything else than logged:fine we can assume it is a new
			// instruction
			if (!respond.equals("logged:fine")) {
				System.out.println("we got a new instructionToQueue:" + respond);
				messageQueue.push(respond);
			}
			lastLog = System.currentTimeMillis();
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

	public void getNewTask() {
		if (!messageQueue.contains(newTaskMessage)) {
			messageQueue.push(newTaskMessage);
		}
	}

}
