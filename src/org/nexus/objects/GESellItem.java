package org.nexus.objects;

import org.nexus.NexusScript;
import org.nexus.utils.grandexchange.Exchange;
import org.nexus.utils.grandexchange.ExchangeItem;

public class GESellItem extends RequiredItem {

	private int itemPrice;
	private boolean hasBeenWithdrawn = false;
	private boolean hasBeenSold = false;
	
	public GESellItem(int itemID, String itemName,int amount,  int itemPrice) {
		setItemID(itemID);
		setItemName(itemName);
		setAmount(amount);
		setItemPrice(itemPrice);
	}

	private void setItemPrice(int price) {
		this.itemPrice = price;
	}
	
	public int getTotalPrice() {
		return (int) (itemPrice * getAmount() * 1.3);
	}
	
	public int getItemPrice() {
		return itemPrice;
	}
	
	public void setHasBeenWithdrawn(boolean hasBeenWithdrawn) {
		this.hasBeenWithdrawn = hasBeenWithdrawn;
	}

	public boolean hasBeenWithdrawnFromBank() {
		return hasBeenWithdrawn;
	}
	
	public void setHasBeenSold(boolean hasBeenSold) {
		this.hasBeenSold = hasBeenSold;
	}

	public boolean hasBeenSold() {
		return hasBeenSold;
	}

}
