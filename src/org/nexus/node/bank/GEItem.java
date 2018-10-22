package org.nexus.node.bank;

import org.nexus.NexusScript;
import org.nexus.utils.Exchange;
import org.nexus.utils.ExchangeItem;

public class GEItem extends RequiredItem {

	private int itemPrice;
	private ExchangeItem exchangeItem;
	
	public GEItem(int itemID, int itemAmount, String itemName) {
		setItemID(itemID);
		setAmount(itemAmount);
		setItemName(itemName);
		setItemPrice(Exchange.getPrice(itemID));
	}

	private void setItemPrice(int price) {
		this.itemPrice = price;
	}
	
	public int getItemPrice() {
		return itemPrice;
	}

}
