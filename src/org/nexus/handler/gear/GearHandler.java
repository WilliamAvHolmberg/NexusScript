package org.nexus.handler.gear;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.nexus.handler.Handler;
import org.nexus.node.Node;
import org.nexus.objects.DepositItem;
import org.nexus.objects.WithdrawItem;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.script.MethodProvider;

public class GearHandler extends Handler {

	public static Stack<GearItem> itemsToEquip = new Stack<GearItem>();

	private ArrayList<String> currentEquipedGear;

	private Gear preferredGear;


	
	/**
	 * 
	 * @param param1
	 * @param param2
	 * @return
	 */
	public int bla(int param1, int param2) {
		return param1 + param2;
	}


	@Override
	public Node getNode() {
		if (itemsToEquip.isEmpty()) {
			preferredGear = getCurrentTask().getPreferredGear();
			if (preferredGear == null) {
				return null;
			} else {
				// loop through each gear in preferredgear and check if player is wearing item.
				// If not, add new "ItemsToEquip"
				for (HashMap.Entry<EquipmentSlot, GearItem> entry : preferredGear.getGear().entrySet()) {
					EquipmentSlot slot = entry.getKey();
					GearItem item = entry.getValue();
					if (item != null && !equipment.isWearingItem(slot, item.getItem().getName())) {
						itemsToEquip.push(item);
						log("should see this once");
						return null;
					}
				}
			}
		} else {
			if (isWearing(itemsToEquip.peek())) {
				itemsToEquip.pop();
				return null;
			} else {
				return equipItemNode.setGearItem(itemsToEquip.peek());
			}
		}
		return null;
	}

	private boolean isWearing(GearItem peek) {
		return equipment.isWearingItem(peek.getSlot(), peek.getItem().getId());
	}

	/*
	 * public GearItem getPreferredGearInSlot(EquipmentSlot slot) { for(GearItem
	 * item: gear) { if(item.getSlot() == slot) { return item; } } return null; }
	 */


	

	public static GearItem getWithdrawItem() {
		if (itemsToEquip.isEmpty()) {
			return null;
		}
		return itemsToEquip.peek();
	}

	public static void addItem(GearItem item) {
		itemsToEquip.add(item);
	}

	public static void removeItem(GearItem item) {
		itemsToEquip.remove(item);
	}

}
