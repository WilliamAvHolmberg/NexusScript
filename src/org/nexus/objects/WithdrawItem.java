package org.nexus.objects;

public class WithdrawItem extends RequiredItem{
	
	public WithdrawItem(int itemID, int itemAmount, String itemName) {
		setItemID(itemID);
		setAmount(itemAmount);
		setItemName(itemName);
	}
	
	public WithdrawItem(int itemID, int itemAmount) {
		setItemID(itemID);
		setAmount(itemAmount);
		if(itemID == 995) {
			setItemName("Coins");
		}
	}
}
