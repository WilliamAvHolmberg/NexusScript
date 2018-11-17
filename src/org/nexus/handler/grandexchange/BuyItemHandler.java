package org.nexus.handler.grandexchange;

import java.util.Arrays;
import java.util.Stack;

import org.nexus.NexusScript;
import org.nexus.handler.BankHandler;
import org.nexus.handler.Handler;
import org.nexus.node.Node;
import org.nexus.node.bank.Withdraw;
import org.nexus.node.ge.BuyItem;
import org.nexus.node.ge.HandleCoins;
import org.nexus.objects.DepositItem;
import org.nexus.objects.DepositItem.DepositType;
import org.nexus.objects.GEItem;
import org.nexus.objects.WithdrawItem;
import org.nexus.task.TaskType;
import org.nexus.utils.WebBank;
import org.osbot.rs07.script.MethodProvider;

public class BuyItemHandler extends Handler {
	public static Stack<GEItem> items = new Stack<GEItem>();
	private GEItem geItem;
	private WithdrawItem withdrawItem;
	public static HandleCoins handleCoins = new HandleCoins();
	public static BuyItem buyItemNode = new BuyItem();

	public BuyItemHandler() {
	}

	public Node getNode() {
		log("in ge handler");
		if (items.isEmpty()) {
			log("items is null??");
			return null;
		} else {
			log("items not null");
			geItem = items.peek();
			log(geItem.getTotalPrice());
			withdrawItem = BankHandler.getWithdrawItem();
			if (purchaseIsCompleted(geItem, withdrawItem)) {
				log("done!");
				BuyItemHandler.removeItem(geItem);
			} else if (purchaseAmountIsWrong(geItem, withdrawItem)
					&& NexusScript.currentTask.getTaskType() != TaskType.COMBAT) {
				log("wrong amount");
				geItem.setAmount((int) (withdrawItem.getAmount() - bank.getAmount(geItem.getItemID())));
			} else if (!playerAtGrandExchange()) {
				return walkToAreaNode.setArea(WebBank.GRAND_EXCHANGE.getArea());
			} else if (!inventory.onlyContains(995)) {
				return deposit.setItem(new DepositItem(DepositType.DEPOSIT_ALL_EXCEPT, 995));
			} else if (inventory.getAmount(995) < geItem.getTotalPrice() && !grandExchange.isOpen()) {
				return withdraw.setItem(new WithdrawItem(995, geItem.getTotalPrice()));
			} else {
				return buyItemNode.setItem(geItem);
			}
		}
		log("no node found in ge");
		return null;
	}

	private boolean purchaseAmountIsWrong(GEItem geItem, WithdrawItem withdrawItem) {
		return bank.isOpen() && (withdrawItem.getAmount() - bank.getAmount(geItem.getItemID())) != geItem.getAmount();
	}

	private boolean purchaseIsCompleted(GEItem geItem, WithdrawItem withdrawItem) {
		return bank.isOpen() && bank.getAmount(geItem.getItemID()) >= withdrawItem.getAmount() ||
				inventory.getAmount(geItem.getItemName()) >= withdrawItem.getAmount();
	}

	public static void addItem(GEItem item) {
		items.add(item);
	}

	public static void removeItem(GEItem item) {
		items.remove(item);
	}

	public static GEItem getItem() {
		if (items.isEmpty()) {
			return null;
		}
		return items.peek();
	}

	private boolean playerAtGrandExchange() {
		return WebBank.GRAND_EXCHANGE.getArea().contains(myPosition());
	}

}
