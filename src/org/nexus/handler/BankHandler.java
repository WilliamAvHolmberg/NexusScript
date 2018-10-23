package org.nexus.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.nexus.NexusScript;
import org.nexus.node.Node;
import org.nexus.node.bank.Deposit;
import org.nexus.node.bank.OpenBank;
import org.nexus.node.bank.Withdraw;
import org.nexus.node.general.WalkToArea;
import org.nexus.objects.DepositItem;
import org.nexus.objects.WithdrawItem;
import org.nexus.task.Task;
import org.osbot.rs07.script.MethodProvider;

public class BankHandler extends Handler {
	public static Stack<WithdrawItem> itemsToWithdraw = new Stack<WithdrawItem>();
	public static Stack<DepositItem> itemsToDeposit = new Stack<DepositItem>();
	public static OpenBank openBankNode = new OpenBank();
	public static Withdraw withdrawNode = new Withdraw();
	public static Deposit depositNode = new Deposit();

	private String[] stockArr;

	public BankHandler(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
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
		if (methodProvider.inventory.getAmount(withdrawItem.getItemID()) == withdrawItem.getAmount()) {
			BankHandler.removeItem(withdrawItem);
			return null;
		} else if (!playerInBank()) {
			return walkToAreaNode.setArea(getBankArea());
		} else if (!bankIsOpen()) {
			return openBankNode;
		} else {
			return withdrawNode.setItem(withdrawItem);
		}
	}

	private boolean bankIsOpen() {
		return methodProvider.bank.isOpen();
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
			return openBankNode;
		} else {
			return depositNode.setItem(depositItem);
		}
	}

	private void checkIfDepositIsCompleted(DepositItem depositItem) {
		switch (depositItem.getType()) {
		case DEPOSIT_ALL:
			if (methodProvider.inventory.isEmpty()) {
				BankHandler.removeItem(depositItem);
			}
			break;
		case DEPOSIT_ALL_EXCEPT:
			stockArr = new String[depositItem.getItems().size()];
			if (methodProvider.inventory.isEmptyExcept(depositItem.getItems().toArray((stockArr)))) {
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
