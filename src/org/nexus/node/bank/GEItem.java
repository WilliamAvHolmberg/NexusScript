package org.nexus.node.bank;

public class GEItem {
	
	private int itemID;
	private int itemAmount;
	
	public GEItem(int itemID, int itemAmount) {
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

	public void setAmount(int newAmount) {
		this.itemAmount = newAmount;
	}
}
