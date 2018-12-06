package org.nexus.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.nexus.NexusScript;
import org.nexus.handler.gear.Inventory;
import org.nexus.node.Node;
import org.nexus.node.bank.Deposit;
import org.nexus.node.bank.OpenBank;
import org.nexus.node.bank.Withdraw;
import org.nexus.node.mule.CheckIfWeShallSellItems;
import org.nexus.node.walking.WalkToArea;
import org.nexus.objects.DepositItem;
import org.nexus.objects.DepositItem.DepositType;
import org.nexus.objects.WithdrawItem;
import org.nexus.task.ActionTask;
import org.osbot.rs07.Bot;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.script.MethodProvider;

public class BankHandler extends Handler {
	public static Stack<WithdrawItem> itemsToWithdraw = new Stack<WithdrawItem>();
	public static Stack<DepositItem> itemsToDeposit = new Stack<DepositItem>();
	public static OpenBank openBankNode = new OpenBank();
	public static Withdraw withdrawNode = new Withdraw();
	public static Deposit depositNode = new Deposit();
	public static CheckIfWeShallSellItems checkIfSellItemNode = new CheckIfWeShallSellItems();

	private int[] stockArr;

	@Override
	public MethodProvider exchangeContext(Bot arg0) {
		return super.exchangeContext(arg0);
	}
	@Override
	public Node getNode() {
		if (!itemsToDeposit.isEmpty()) {
			return getDepositNode(itemsToDeposit.peek());
		} else if (!itemsToWithdraw.isEmpty()) {
			return getWithdrawNode(itemsToWithdraw.peek());
		}
		return null;
	}

	private Node getWithdrawNode(WithdrawItem withdrawItem) {
		if(withdrawItem.getInventory() != null && !Inventory.inventoryOnlyContainsRequiredItems(this, withdrawItem.getInventory())) {
			return getDepositNode(new DepositItem(DepositType.DEPOSIT_ALL_EXCEPT, withdrawItem.getInventory().getItemIds()));
		}else if (inventory.getAmount(withdrawItem.getItemID()) == withdrawItem.getAmount()) {
			BankHandler.removeItem(withdrawItem);
			log("lets remove item");
			return null;
		} else if (!playerInBank()) {
			return walkToAreaNode.setArea(getBankArea());
		} else if (!bankIsOpen()) {
			return openBankNode;
		} else if(CheckIfWeShallSellItems.getTimeTilNextCheckInMinutes() < 0) {
			log("lets check items for some stupid reason.");
			log(System.currentTimeMillis());
			log(CheckIfWeShallSellItems.getNextCheckInMilli());
			return checkIfSellItemNode;
		}	else {
			return withdrawNode.setItem(withdrawItem);
		}
	}

	private boolean bankIsOpen() {
		return bank.isOpen();
	}

	public static boolean bankIsOpen(MethodProvider methodProvider) {
		return methodProvider.bank.isOpen();
	}

	public Node getDepositNode(DepositItem depositItem) {

		checkIfDepositIsCompleted(depositItem);
		// check if bankHandler still contains depositItem
		if (!BankHandler.itemsToDeposit.contains(depositItem)) {
			return null;
		} else if (!playerInBank()) {
			return walkToAreaNode.setArea(getBankArea());
		} else if (!bankIsOpen()) {
			log("in bank");
			return openBankNode;
		} else {
			log("lets deposit");
			return depositNode.setItem(depositItem);
		}
	}

	private void checkIfDepositIsCompleted(DepositItem depositItem) {
		switch (depositItem.getType()) {
		case DEPOSIT_ALL:
			if (inventory.isEmpty()) {
				BankHandler.removeItem(depositItem);
			}
			break;
		case DEPOSIT_ALL_EXCEPT:
			stockArr = depositItem.getItems().stream().mapToInt(i->i).toArray();
			if (inventory.isEmptyExcept(stockArr) ) {
				BankHandler.removeItem(depositItem);
			}
			break;
		default:
			break;
		}
	}



	public static void addItem(WithdrawItem item) {
		itemsToWithdraw.add(item);
	}

	public static void removeItem(WithdrawItem item) {
		itemsToWithdraw.remove(item);
	}

	public static WithdrawItem getWithdrawItem() {
		if (itemsToWithdraw.isEmpty()) {
			return null;
		}
		return itemsToWithdraw.peek();
	}

	public static void addItem(DepositItem item) {
		itemsToDeposit.add(item);
	}

	public static void removeItem(DepositItem item) {
		itemsToDeposit.remove(item);
	}

	public static DepositItem getDepositItem() {
		if (itemsToDeposit.isEmpty()) {
			return null;
		}
		return itemsToDeposit.peek();
	}

}
