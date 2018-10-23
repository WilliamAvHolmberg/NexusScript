package org.nexus.handler;

import java.util.Arrays;
import java.util.Stack;

import org.nexus.node.Node;
import org.nexus.node.ge.BuyItem;
import org.nexus.node.ge.HandleCoins;
import org.nexus.objects.DepositItem;
import org.nexus.objects.GEItem;
import org.nexus.objects.WithdrawItem;
import org.nexus.utils.WebBank;
import org.osbot.rs07.script.MethodProvider;

public class GrandExchangeHandler extends Handler {
	public static Stack<GEItem> items = new Stack<GEItem>();
	private GEItem geItem;
	private WithdrawItem withdrawItem;
	public static HandleCoins handleCoins = new HandleCoins();
	public static BuyItem buyItemNode = new BuyItem();

	public GrandExchangeHandler(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
	}

	public Node getNode() {
		if (items.isEmpty()) {
			return null;
		} else {
			geItem = items.peek();
			withdrawItem = BankHandler.getWithdrawItem();
			if (purchaseIsCompleted(geItem, withdrawItem)) {
				GrandExchangeHandler.removeItem(geItem);
			} else if (purchaseAmountIsWrong(geItem, withdrawItem)) {
				geItem.setAmount((int) (withdrawItem.getAmount() - methodProvider.bank.getAmount(geItem.getItemID())));
			} else if (!playerAtGrandExchange()) {
				return walkToAreaNode.setArea(WebBank.GRAND_EXCHANGE.getArea());
			} else if (!methodProvider.inventory.onlyContains("Coins") || !methodProvider.inventory.contains("Coins")) {
				return handleCoins;
			} else {
				return buyItemNode.setItem(geItem);
			}
		}
		return null;
	}

	private boolean purchaseAmountIsWrong(GEItem geItem, WithdrawItem withdrawItem) {
		return methodProvider.bank.isOpen()
				&& (withdrawItem.getAmount() - methodProvider.bank.getAmount(geItem.getItemID())) != geItem.getAmount();
	}

	private boolean purchaseIsCompleted(GEItem geItem, WithdrawItem withdrawItem) {
		return methodProvider.bank.isOpen()
				&& methodProvider.bank.getAmount(geItem.getItemID()) >= withdrawItem.getAmount();
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
		return WebBank.GRAND_EXCHANGE.getArea().contains(methodProvider.myPosition());
	}

}
