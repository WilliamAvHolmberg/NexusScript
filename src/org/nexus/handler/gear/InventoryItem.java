package org.nexus.handler.gear;

import org.nexus.objects.RSItem;
import org.osbot.rs07.api.ui.EquipmentSlot;

public class InventoryItem {
	
	private int amount;
	private RSItem item;
	
	public InventoryItem(int amount, RSItem item) {
		this.amount = amount;
		this.item = item;
	}
	public int getAmount() {
		return amount;
	}

	public RSItem getItem() {
		return item;
	}

}
