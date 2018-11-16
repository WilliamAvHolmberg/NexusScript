package org.nexus.handler.gear;

import java.util.ArrayList;
import java.util.HashMap;

import org.nexus.objects.RSItem;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.script.MethodProvider;

public class Inventory {
	
	private ArrayList<InventoryItem> items;
	
	public Inventory() {
		items = new ArrayList<InventoryItem>();
	}
	
	public void addItem(InventoryItem item) {	
		items.add(item);
	}
	
	public void addItem(RSItem item, int amount) {	
		items.add(new InventoryItem(amount, item));
	}
	
	public ArrayList<InventoryItem> getItems(){
		return items;
	}
	public ArrayList<Integer> getItemIds(){
		ArrayList<Integer> itemIds = new ArrayList<Integer>();
		items.forEach(item -> {
			itemIds.add(item.getItem().getId());
		});
		return itemIds;
	}
	
	public ArrayList<String> getItemNames(){
		ArrayList<String> itemNames = new ArrayList<String>();
		items.forEach(item -> {
			itemNames.add(item.getItem().getName());
		});
		return itemNames;
	}
	
	
	public static boolean inventoryOnlyContainsRequiredItems(MethodProvider methodprovider, Inventory inventory) {
		for(Item item : methodprovider.inventory.getItems()) {
			if(item != null && !inventory.getItemIds().contains(item.getId())) {
				return false;
			}
		}
		return true;
	}
}
