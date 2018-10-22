package org.nexus.node.bank;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BankHandler {
	public static Stack<WithdrawItem> items = new Stack<WithdrawItem>();
	
	public static void addItem(WithdrawItem item) {
		items.add(item);
	}
	
	public static void removeItem(WithdrawItem item) {
		items.remove(item);
	}
	
	public static WithdrawItem getItem() {
		if(items.isEmpty()) {
			return null;
		}
		return items.peek();
	}
}
