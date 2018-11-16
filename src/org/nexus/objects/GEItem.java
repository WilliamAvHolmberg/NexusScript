package org.nexus.objects;

import org.nexus.NexusScript;
import org.nexus.utils.grandexchange.Exchange;
import org.nexus.utils.grandexchange.ExchangeItem;

public class GEItem extends RequiredItem {

	private int itemPrice;
	private ExchangeItem exchangeItem;
	
	public GEItem(int itemID, int itemAmount, String itemName) {
		setItemID(itemID);
		setAmount(itemAmount);
		setItemName(itemName);
		setItemPrice(Exchange.getPrice(itemID, null));
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
