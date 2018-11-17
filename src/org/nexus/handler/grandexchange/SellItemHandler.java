package org.nexus.handler.grandexchange;

import java.util.Arrays;
import java.util.Stack;

import org.nexus.NexusScript;
import org.nexus.handler.Handler;
import org.nexus.node.Node;
import org.nexus.node.bank.Withdraw;
import org.nexus.node.ge.BuyItem;
import org.nexus.node.ge.HandleCoins;
import org.nexus.node.ge.SellItem;
import org.nexus.objects.DepositItem;
import org.nexus.objects.DepositItem.DepositType;
import org.nexus.objects.GEItem;
import org.nexus.objects.GESellItem;
import org.nexus.objects.WithdrawItem;
import org.nexus.task.TaskType;
import org.nexus.utils.WebBank;
import org.osbot.rs07.script.MethodProvider;

public class SellItemHandler extends Handler {
	public static Stack<GESellItem> items = new Stack<GESellItem>();
	private GESellItem geItem;
	private WithdrawItem withdrawItem;
	public static HandleCoins handleCoins = new HandleCoins();
	public static BuyItem buyItemNode = new BuyItem();
	public static SellItem sellItemNode = new SellItem();

	public SellItemHandler() {
	}

	public Node getNode() {
		log("in ge handler");
		if (items.isEmpty()) {
			log("items is null??");
			return null;
		} else {
			log("items not null");
			geItem = items.peek();
			log("we are gonna sell " + geItem.getItemName());
			//withdrawItem = BankHandler.getWithdrawItem();
			if(!geItem.hasBeenWithdrawnFromBank()){
				withdrawRequiredItem(geItem);
			}else if(!geItem.hasBeenSold()) {
				if(inventory.contains(geItem.getItemID())) {
					geItem.setHasBeenSold(true);
					log("item has been sold");
					return getNode();
				}else {
					log("lets sell item");
					return sellItemNode.setItem(geItem);
				}
			}else if(grandExchange.isOpen()) {
				if(grandExchange.collect()) {
					log("sale has been completed. lets remove item");
					items.remove(geItem);
				}else {
					return getNode();
				}
			}

			//ta ut item
			//s�lj item
			//make sure coins > before == purchaseIsCompleted

			/*if (purchaseIsCompleted(geItem, withdrawItem)) {
				log("done!");
				SellItemHandler.removeItem(geItem);
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
			}*/
		}
		log("no node found in sell item");
		return null;
	}

	private Node withdrawRequiredItem(GESellItem geItem) {
		if (bank.isOpen()) {
			if (!bank.contains(geItem.getItemID())) {
				geItem.setHasBeenWithdrawn(true);
				return getNode();
			} else {
				return withdraw.setItem(new WithdrawItem(geItem.getItemID(), geItem.getAmount()));
			}
		}
		return withdrawRequiredItem(geItem);
	}

	public static void addItem(GESellItem item) {
		items.add(item);
	}

	public static void removeItem(GEItem item) {
		items.remove(item);
	}

	public static GESellItem getItem() {
		if (items.isEmpty()) {
			return null;
		}
		return items.peek();
	}

	private boolean playerAtGrandExchange() {
		return WebBank.GRAND_EXCHANGE.getArea().contains(myPosition());
	}

}
