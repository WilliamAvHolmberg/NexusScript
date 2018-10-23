package org.nexus.handler.gear;

import java.util.ArrayList;
import java.util.HashMap;

import org.nexus.objects.RSItem;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.script.MethodProvider;

public class Gear {
	
	private HashMap<EquipmentSlot, GearItem> gear;
	
	public Gear() {
		this.gear = new HashMap<EquipmentSlot, GearItem>();
		for(EquipmentSlot slot : EquipmentSlot.values()) {
			gear.putIfAbsent(slot, null);
		}
	}
	
	public void addGear(EquipmentSlot slot, GearItem equipment) {	
		gear.put(slot, equipment);
	}
	
	public void addGear(EquipmentSlot slot, RSItem equipment) {	
		gear.put(slot, new GearItem(slot,equipment));
	}

	public GearItem getGearInSlot(EquipmentSlot slot) {
		return gear.get(slot);
	}
	
	public HashMap<EquipmentSlot, GearItem> getGear(){
		return gear;
	}

}
