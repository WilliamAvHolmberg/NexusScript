package org.nexus.objects;

public abstract class RequiredItem  {
	private int itemID;
	private int itemAmount;
	private String itemName;
	
	
	public int getItemID() {
		return itemID;
	}
	
	public int getAmount() {
		return itemAmount;
	}
	
	public String getItemName() {
		return itemName;
	}
	
	public void setItemID(int id) {
		this.itemID = id;
	}
	
	public void setAmount(int amount) {
		this.itemAmount = amount;
	}
	
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public String toString() {
		return "Amount:" + itemAmount + "...ID:" + itemID;
	}
}
