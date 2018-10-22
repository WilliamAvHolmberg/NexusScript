package org.nexus.node.bank;

public class WithdrawItem {

	private int itemID;
	private int itemAmount;
	
	public WithdrawItem(int itemID, int itemAmount) {
		this.itemID = itemID;
		this.itemAmount = itemAmount;
	}
	
	public int getItemID() {
		return itemID;
	}
	
	public int getAmount() {
		return itemAmount;
	}
	
	public String toString() {
		return "Amount:" + itemAmount + "...ID:" + itemID;
	}
}
