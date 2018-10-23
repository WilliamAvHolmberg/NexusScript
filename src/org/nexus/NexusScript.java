package org.nexus;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.nexus.communication.NexHelper;
import org.nexus.node.Node;
import org.nexus.node.NodeHandler;
import org.nexus.node.bank.BankHandler;
import org.nexus.node.bank.Deposit;
import org.nexus.node.bank.GrandExchangeHandler;
import org.nexus.node.bank.OpenBank;
import org.nexus.node.bank.Withdraw;
import org.nexus.node.ge.BuyItem;
import org.nexus.node.ge.DepositAllButCoins;
import org.nexus.node.ge.WalkToGE;
import org.nexus.node.woodcutting.WalkToBank;
import org.nexus.objects.DepositItem;
import org.nexus.objects.GEItem;
import org.nexus.objects.WithdrawItem;
import org.nexus.task.Task;
import org.nexus.task.WoodcuttingTask;
import org.nexus.utils.grandexchange.RSExchange;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(author = "Nex", info = "", logo = "", name = "NEX", version = 0)
public class NexusScript extends Script {

	public static boolean SHOULD_RUN = true;
	NexHelper helper;
	Thread nexHelperThread;
	List<Node> withdrawNodes;
	List<Node> depositNodes;
	List<Node> geNodes;
	List<Node> nodes;
	private Node currentNode;
	public static NodeHandler nodeHandler;
	public static Task currentTask;
	private String[] stockArr;

	@Override
	public void onStart() {
		// create new NexHelper
		helper = new NexHelper(this);
		// initialize a new thread for NexHelper
		nexHelperThread = new Thread(helper);
		nexHelperThread.start();
		withdrawNodes = new ArrayList<Node>();
		withdrawNodes.add(new OpenBank().init(this));
		withdrawNodes.add(new Withdraw().init(this));
		withdrawNodes.add(new WalkToBank().init(this));
		depositNodes = new ArrayList<Node>();
		depositNodes.add(new OpenBank().init(this));
		depositNodes.add(new Deposit().init(this));
		depositNodes.add(new WalkToBank().init(this));
		geNodes = new ArrayList<Node>();
		geNodes.add(new WalkToGE().init(this));
		geNodes.add(new DepositAllButCoins().init(this));
		geNodes.add(new BuyItem().init(this));

		// init node Handler
		nodeHandler = new NodeHandler(this);

		// BankHandler.addItem(new WithdrawItem(385, 77, "Shark"));
		List<String> itemsToKeep = new ArrayList<String>();
		itemsToKeep.add("Coins");
		BankHandler.addItem(new DepositItem(DepositItem.DepositType.DEPOSIT_ALL_EXCEPT, itemsToKeep));
		// fill NodeHandler with nodes
		// NodeHandler.add(new Node().init(this));

		Area area = new Area(
				new int[][] { { 3199, 3206 }, { 3187, 3206 }, { 3181, 3238 }, { 3197, 3253 }, { 3201, 3252 } });
		currentTask = new WoodcuttingTask(area, null, () -> skills.getStatic(Skill.WOODCUTTING) > 99, "Iron axe", 1349, "Tree");
	}

	@Override
	public int onLoop() throws InterruptedException {
		// SHOULD_RUN is static and can be accessed from anywhere
		// When NexHelper gets an instruction to disconnect it sets SHOULD_RUN to false
		// Shall exit script when SHOULD_RUN is false
		checkIfWeShouldStop();

		if (currentTask != null) { // !currentTask.isCompleted(this)) {
			handleStates();
		} else {
			helper.getNewTask();
		}

		return 600; // amount of milliseconds to wait before each loop. Set to 600
	}

	private void checkIfWeShouldStop() {
		if (!SHOULD_RUN) {
			stop();
		}
	}

	private void handleStates() {
		nodes = getNodes();
		if (nodes != null) {
			for (Node node : nodes) {
				log(node);
				if (node.shallExecute()) {
					currentNode = node;
					node.execute();
					break;
				}
			}
		}
	}

	/**
	 * shall execute GE NODES if BUY_LIST is not empty shall execute BANK_NODES if
	 * WITHDRAW_LIST is not empty TODO - shall execute EQUIP_NODES if EQUIP_LIST is
	 * not empty TODO - return TASK_NODES if all above is empty
	 */
	private List<Node> getNodes() {
		nodes = nodeHandler.getNodes(currentTask);
		GEItem geItem = GrandExchangeHandler.getItem();
		WithdrawItem withdrawItem = BankHandler.getWithdrawItem();
		DepositItem depositItem = BankHandler.getDepositItem();

		if (geItem != null) {
			return getGeNodes(geItem, withdrawItem);
		} else if (depositItem != null) {
			return getDepositNodes(depositItem);
		} else if (withdrawItem != null) {
			return getWithdrawNodes(withdrawItem);
		} else {
			if(nodes != null) {
				log(nodes.size());
			}
			return nodes;
		}
	}

	private List<Node> getGeNodes(GEItem geItem, WithdrawItem withdrawItem) {
		if (purchaseIsCompleted(geItem, withdrawItem)) {
			GrandExchangeHandler.removeItem(geItem);
		} else if (purchaseAmountIsWrong(geItem, withdrawItem)) {
			geItem.setAmount((int) (withdrawItem.getAmount() - bank.getAmount(geItem.getItemID())));
		} else {
			return geNodes;
		}
		return null;
	}

	private List<Node> getWithdrawNodes(WithdrawItem withdrawItem) {
		if (inventory.getAmount(withdrawItem.getItemID()) == withdrawItem.getAmount()) {
			BankHandler.removeItem(withdrawItem);
			return null;
		} else {
			return withdrawNodes;
		}
	}

	private List<Node> getDepositNodes(DepositItem depositItem) {
		checkIfDepositIsCompleted(depositItem);
		// check if bankHandler still contains depositItem
		if (BankHandler.itemsToDeposit.contains(depositItem)) {
			return depositNodes;
		}
		return null;
	}

	private void checkIfDepositIsCompleted(DepositItem depositItem) {
		switch (depositItem.getType()) {
		case DEPOSIT_ALL:
			if (inventory.isEmpty()) {
				BankHandler.removeItem(depositItem);
			}
			break;
		case DEPOSIT_ALL_EXCEPT:
			stockArr = new String[depositItem.getItems().size()];
			if (inventory.isEmptyExcept(depositItem.getItems().toArray((stockArr)))) {
				BankHandler.removeItem(depositItem);
			}
			break;
		default:
			break;
		}
	}

	private boolean purchaseAmountIsWrong(GEItem geItem, WithdrawItem withdrawItem) {
		return bank.isOpen() && (withdrawItem.getAmount() - bank.getAmount(geItem.getItemID())) != geItem.getAmount();
	}

	private boolean purchaseIsCompleted(GEItem geItem, WithdrawItem withdrawItem) {
		return bank.isOpen() && bank.getAmount(geItem.getItemID()) >= withdrawItem.getAmount();
	}

	@Override
	public void onPaint(Graphics2D g) {
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
	}

	@Override
	public void onExit() {
		SHOULD_RUN = false;
	}

}
