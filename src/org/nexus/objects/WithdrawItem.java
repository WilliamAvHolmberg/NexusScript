package org.nexus.objects;

import org.nexus.handler.gear.Inventory;
import org.osbot.rs07.api.Bank.BankMode;

public class WithdrawItem extends RequiredItem{
	
	private Inventory inventory;
	private BankMode withdrawMode = BankMode.WITHDRAW_ITEM;
	
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
	
	public WithdrawItem(int itemID, int amount, BankMode bankMode) {
		setItemID(itemID);
		setAmount(amount);
		setBankMode(bankMode);
	}

	private void setBankMode(BankMode bankMode) {
		this.withdrawMode = bankMode;
		
	}

	public WithdrawItem setInventory(Inventory inventory) {
		this.inventory = inventory;
		return this;
	}
	

	
	public Inventory getInventory() {
		return inventory;
	}

	public BankMode getWithdrawMode() {
		return withdrawMode;
	}
}
