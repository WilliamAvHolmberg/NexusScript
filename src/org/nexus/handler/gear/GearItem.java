package org.nexus.handler.gear;

import org.nexus.objects.RSItem;
import org.osbot.rs07.api.ui.EquipmentSlot;

public class GearItem {
	
	private EquipmentSlot slot;
	private RSItem item;
	
	public GearItem(EquipmentSlot slot, RSItem equipment) {
		setSlot(slot);
		setEquipment(equipment);
	}
	public EquipmentSlot getSlot() {
		return slot;
	}
	public void setSlot(EquipmentSlot slot) {
		this.slot = slot;
	}
	public RSItem getItem() {
		return item;
	}
	public void setEquipment(RSItem item) {
		this.item = item;
	}
	
	

}
