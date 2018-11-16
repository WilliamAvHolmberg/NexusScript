package org.nexus.objects;

import org.nexus.handler.gear.Inventory;

public class WithdrawItem extends RequiredItem{
	
	private Inventory inventory;
	
	public WithdrawItem(int itemID, int itemAmount, String itemName) {
		setItemID(itemID);
		setAmount(itemAmount);
		setItemName(itemName);
	}
	
	public WithdrawItem(RSItem item, int itemAmount) {
		setItemID(item.getId());
		setAmount(itemAmount);
		setItemName(item.getName());
	}
	
	public WithdrawItem(int itemID, int itemAmount) {
		setItemID(itemID);
		setAmount(itemAmount);
		if(itemID == 995) {
			setItemName("Coins");
		}
	}
	
	public WithdrawItem setInventory(Inventory inventory) {
		this.inventory = inventory;
		return this;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
}
