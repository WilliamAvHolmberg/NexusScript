package org.nexus.objects;

import org.nexus.NexusScript;
import org.nexus.utils.grandexchange.Exchange;
import org.nexus.utils.grandexchange.ExchangeItem;

public class GESellItem extends RequiredItem {

	private int itemPrice;
	
	public GESellItem(int itemID, String itemName, int itemPrice) {
		setItemID(itemID);
		setItemName(itemName);
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

}
