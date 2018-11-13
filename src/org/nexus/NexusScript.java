package org.nexus;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.nexus.communication.NexHelper;
import org.nexus.communication.message.TaskLog;
import org.nexus.event.LoginEvent;
import org.nexus.event.LoginListener;
import org.nexus.handler.BankHandler;
import org.nexus.handler.GrandExchangeHandler;
import org.nexus.handler.NodeHandler;
import org.nexus.handler.SimpleCacheManager;
import org.nexus.handler.TaskHandler;
import org.nexus.handler.gear.Gear;
import org.nexus.handler.gear.GearHandler;
import org.nexus.node.Node;
import org.nexus.node.bank.Deposit;
import org.nexus.node.bank.OpenBank;
import org.nexus.node.bank.Withdraw;
import org.nexus.node.ge.BuyItem;
import org.nexus.node.ge.HandleCoins;
import org.nexus.node.general.WalkToArea;
import org.nexus.objects.DepositItem;
import org.nexus.objects.GEItem;
import org.nexus.objects.RSItem;
import org.nexus.objects.WithdrawItem;
import org.nexus.provider.NexProvider;
import org.nexus.task.Task;
import org.nexus.task.TaskType;
import org.nexus.task.WoodcuttingTask;
import org.nexus.utils.Timing;
import org.nexus.utils.grandexchange.RSExchange;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(author = "Nex", info = "", logo = "", name = "NEX", version = 0.1)
public class NexusScript extends Script {

	public static boolean SHOULD_RUN = true;
	NexHelper helper;
	Thread nexHelperThread;
	private Node currentNode;
	public static NodeHandler nodeHandler;
	public static Task currentTask;
	private String[] stockArr;
	private Node node;
	private LoginEvent logEvent;
	String username;
	String password;
	NexProvider provider;

	@Override
	public void onStart() {
		provider = new NexProvider();
		provider.exchangeContext(getBot());
		username = bot.getUsername();
		password = getPassword();
		log("lets sleep for 5 seconds for everything to initialize proper");
		sleep(15000);

		logEvent = new LoginEvent(this, username, password, this);
		getBot().getLoginResponseCodeListeners().add(new LoginListener(this));

		helper = new NexHelper();
		helper.exchangeContext(getBot());
		nexHelperThread = new Thread(helper);
		nexHelperThread.start();

		nodeHandler = new NodeHandler();
		nodeHandler.exchangeContext(getBot());
		nodeHandler.init();
		experienceTracker.start(Skill.WOODCUTTING);
	}

	private String getPassword() {
		if (getParameters() != null) {
			String[] params = getParameters().split("_");
			log(params);
			log(getParameters());
			password = params[0];
			return password;
		}
		return "ugot00wned2";
	}

	@Override
	public int onLoop() throws InterruptedException {
		// SHOULD_RUN is static and can be accessed from anywhere
		// When NexHelper gets an instruction to disconnect it sets SHOULD_RUN to false
		// Shall exit script when SHOULD_RUN is false
		checkIfWeShouldStop();

		if (!client.isLoggedIn() && (currentTask == null || currentTask.getTaskType() != TaskType.BREAK)) {
			login();
		} else if (currentTask == null) { // !currentTask.isCompleted(this)) {
			helper.getNewTask();
			log("Lets sleep until we found task");
			Timing.waitCondition(() -> !TaskHandler.available_tasks.isEmpty() || currentTask != null, 15000);
			experienceTracker.start(Skill.WOODCUTTING);
		} else if (currentTask.isCompleted()) {
			handleCompletedTask();
		} else {
			handleStates();
		}

		return 600; // amount of milliseconds to wait before each loop. Set to 600
	}

	private void handleCompletedTask() {
		// log current task data
		if (currentTask.getSkill() != null) {
			currentTask.setGainedXP(experienceTracker.getGainedXP(currentTask.getSkill()));
		}
		// TODO - MONEY MADE
		helper.messageQueue.push(new TaskLog(this, helper.messageQueue, currentTask, "Task Completed"));
		currentTask = null;
	}

	private void checkIfWeShouldStop() {
		if (!SHOULD_RUN) {
			stop();
		}
	}

	private void handleStates() {
		if (currentTask.getTaskType() == TaskType.BREAK && client.isLoggedIn()) {
			logoutTab.logOut();
		} else if (currentTask.getTaskType() != TaskType.BREAK) {
			node = nodeHandler.getNode();
			if (node != null) {
				currentNode = node;
				log("lets execute");
				node.execute(this);
			} else {
				log("no node found");
			}
		}
	}

	private void login() {
		logEvent = new LoginEvent(this, username, password, this);
		execute(logEvent);
	}

	@Override
	public void onPaint(Graphics2D g) {
		g.drawString("Current IP: " + getIP(), 50, 200);
		if (currentNode != null) {
			g.drawString("Current Node:" + currentNode, 50, 50);
		}
		if (!BankHandler.itemsToWithdraw.isEmpty()) {
			g.drawString("Item needed:" + BankHandler.getWithdrawItem(), 50, 75);
		}
		if (!GrandExchangeHandler.items.isEmpty()) {
			g.drawString("Item needed:" + GrandExchangeHandler.getItem(), 50, 100);
		}
		if (!BankHandler.itemsToDeposit.isEmpty()) {
			g.drawString("lets deposit:" + BankHandler.itemsToDeposit.peek().getType(), 50, 100);
		}
		if (!GearHandler.itemsToEquip.isEmpty()) {
			g.drawString("lets equip:" + GearHandler.itemsToEquip.peek().getItem().getName(), 50, 125);
		}

		if (currentTask != null) {
			g.drawString("XP Gained: " + experienceTracker.getGainedXP(currentTask.getSkill()), 350, 50);
			g.drawString("XP Per Hour: " + experienceTracker.getGainedXPPerHour(currentTask.getSkill()), 350, 75);
			g.drawString("Logs Per Hour: " + experienceTracker.getGainedXPPerHour(currentTask.getSkill()) / 25, 350,
					100);
			if (currentTask.getBreakType() != null && currentTask.getBreakAfter() != null) {
				switch (currentTask.getBreakType()) {
				case "LEVEL":
					g.drawString("Break after Level: " + currentTask.getBreakAfter(), 350, 125);
					break;
				case "TIME":
					g.drawString("Break in: " + currentTask.getTimeLeft() + " minutes", 350, 125);
				}
			}
		}

		g.drawString("Tasks: " + TaskHandler.getTaskList(), 350, 150);

	}

	private String getIP() {
		if (SimpleCacheManager.getInstance().get("IP") != null) {
			return (String) SimpleCacheManager.getInstance().get("IP");
		} else {
			return (String) SimpleCacheManager.getInstance().put("IP", NexHelper.getIP());
		}
	}

	public void sleep(int milli) {
		try {
			MethodProvider.sleep(milli);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onMessage(Message message) throws InterruptedException {
		String txt = message.getMessage().toLowerCase();
		
		if (txt.contains("accepted trade.") && currentTask != null && currentTask.getTaskType() == TaskType.DEPOSIT_TO_SLAVE) {
			NexusScript.currentTask = null;
		}
		
	}

	@Override
	public void onExit() {
		SHOULD_RUN = false;
	}

}
