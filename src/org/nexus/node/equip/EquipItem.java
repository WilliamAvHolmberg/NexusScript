package org.nexus.node.equip;

import org.nexus.handler.BankHandler;
import org.nexus.handler.gear.GearItem;
import org.nexus.node.Node;
import org.nexus.objects.WithdrawItem;
import org.osbot.rs07.script.MethodProvider;

import org.nexus.utils.Timing;

public class EquipItem extends Node{

	private MethodProvider methodProvider;
	GearItem item;
	
	@Override
	public boolean shallExecute(MethodProvider methodProvider) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
		if(methodProvider.widgets.closeOpenInterface() && methodProvider.inventory.contains(item.getItem().getId())) {
			methodProvider.equipment.equip(item.getSlot(), item.getItem().getId());
			Timing.waitCondition(() -> methodProvider.equipment.isWearingItem(item.getSlot(), item.getItem().getId()), 3000);
		}else {
			BankHandler.addItem(new WithdrawItem(item.getItem().getId(), 1 , item.getItem().getName()));
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Equip item";
	}
	
	public Node setGearItem(GearItem item) {
		this.item = item;
		return this;
	}

}
