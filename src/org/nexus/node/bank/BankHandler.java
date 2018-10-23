package org.nexus.node.bank;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.nexus.objects.DepositItem;
import org.nexus.objects.WithdrawItem;

public class BankHandler {
	public static Stack<WithdrawItem> itemsToWithdraw = new Stack<WithdrawItem>();
	public static Stack<DepositItem> itemsToDeposit = new Stack<DepositItem>();
	
	public static void addItem(WithdrawItem item) {
		itemsToWithdraw.add(item);
	}
	
	public static void removeItem(WithdrawItem item) {
		itemsToWithdraw.remove(item);
	}
	
	public static WithdrawItem getWithdrawItem() {
		if(itemsToWithdraw.isEmpty()) {
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
		if(itemsToDeposit.isEmpty()) {
			return null;
		}
		return itemsToDeposit.peek();
	}
}
