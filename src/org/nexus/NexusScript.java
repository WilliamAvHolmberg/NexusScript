package org.nexus;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.nexus.communication.NexHelper;
import org.nexus.node.Node;
import org.nexus.node.NodeHandler;
import org.nexus.node.bank.BankHandler;
import org.nexus.node.bank.GEItem;
import org.nexus.node.bank.GrandExchangeHandler;
import org.nexus.node.bank.OpenBank;
import org.nexus.node.bank.Withdraw;
import org.nexus.node.bank.WithdrawItem;
import org.nexus.node.ge.BuyItem;
import org.nexus.node.ge.DepositAllButCoins;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(author = "Nex", info = "", logo = "", name = "NEX", version = 0)
public class NexusScript extends Script {

	public static boolean SHOULD_RUN = true;
	NexHelper helper;
	Thread nexHelperThread;
	List<Node> bankNodes;
	List<Node> geNodes;
	private Node currentNode;

	@Override
	public void onStart() {
		// create new NexHelper
		helper = new NexHelper(this);
		// initialize a new thread for NexHelper
		nexHelperThread = new Thread(helper);
		nexHelperThread.start();
		bankNodes = new ArrayList<Node>();
		bankNodes.add(new OpenBank().init(this));
		bankNodes.add(new Withdraw().init(this));
		geNodes = new ArrayList<Node>();
		geNodes.add(new DepositAllButCoins().init(this));
		geNodes.add(new BuyItem().init(this));

		BankHandler.addItem(new WithdrawItem(385, 55));
		// fill NodeHandler with nodes
		// NodeHandler.add(new Node().init(this));
	}

	@Override
	public int onLoop() throws InterruptedException {
		// SHOULD_RUN is static and can be accessed from anywhere
		// When NexHelper gets an instruction to disconnect it sets SHOULD_RUN to false
		// Shall exit script when SHOULD_RUN is false
		if (!SHOULD_RUN) {
			stop();
		}
		GEItem geItem = GrandExchangeHandler.getItem();
		WithdrawItem bankItem = BankHandler.getItem();
		if (geItem != null) {
			if (bank.isOpen()) {
				log("bank is open");
				//if amount(bank) >= bankitem.getAmount, remove self
				
				geItem.setAmount((int) (bankItem.getAmount() - bank.getAmount(geItem.getItemID())));
			}
			if (inventory.getAmount(bankItem.getItemID()) == bankItem.getAmount()) {
				GrandExchangeHandler.removeItem(geItem);
			} else {

				for (Node node : geNodes) {
					if (node.shallExecute()) {
						currentNode = node;
						node.execute();
						break;
					}
				}
			}
		} else if (bankItem != null) {
			if (inventory.getAmount(bankItem.getItemID()) == bankItem.getAmount()) {
				BankHandler.removeItem(bankItem);
			} else {
				for (Node node : bankNodes) {
					if (node.shallExecute()) {
						currentNode = node;
						node.execute();
					}
				}
			}
		}
		// shall execute GE NODES if BUY_LIST is not empty
		// shall execute BANK_NODES if WITHDRAW_LIST is not empty
		// shall execute EQUIP_NODES if EQUIP_LIST is not empty
		// reasonable to remove this logic. Perhaps equip should be done from within
		// main nodes

		// if inventory does not contain item - add item to WITHDRAW_LIST
		// if bank does not contain item - add item to BUY_LIST

		// loop through each node
		// break loop if we executed since we want to restart the loop.
		for (Node node : NodeHandler.nodes) {
			if (node.shallExecute()) {
				node.execute();
				break;
			}
		}
		return 600; // amount of milliseconds to wait before each loop. Set to 600
	}

	@Override
	public void onPaint(Graphics2D g) {
		if (currentNode != null) {
			g.drawString("Current Node:" + currentNode, 50, 50);
		}
		if (!BankHandler.items.isEmpty()) {
			g.drawString("Item needed:" + BankHandler.getItem(), 50, 75);
		}
		if (!GrandExchangeHandler.items.isEmpty()) {
			g.drawString("Item needed:" + GrandExchangeHandler.getItem(), 50, 100);
		}
	}

	@Override
	public void onExit() {
		SHOULD_RUN = false;
	}

}
