package org.nexus.node.bank;

public class WithdrawItem extends RequiredItem{
	
	public WithdrawItem(int itemID, int itemAmount, String itemName) {
		setItemID(itemID);
		setAmount(itemAmount);
		setItemName(itemName);
	}
}
