package org.nexus.handler.skills;

import java.util.ArrayList;
import java.util.List;

import org.nexus.handler.BankHandler;
import org.nexus.handler.Handler;
import org.nexus.handler.gear.GearHandler;
import org.nexus.handler.gear.GearItem;
import org.nexus.node.Node;
import org.nexus.node.agility.AgilityMethods;
import org.nexus.node.combat.LootNode;
import org.nexus.node.woodcutting.Cut;
import org.nexus.objects.DepositItem;
import org.nexus.objects.WithdrawItem;
import org.nexus.task.ActionTask;
import org.nexus.task.WoodcuttingTask;
import org.nexus.utils.WebBank;
import org.osbot.rs07.api.Bank;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class WoodcuttingHandler extends Handler {

	public static Cut cutTreeNode = new Cut();;
	List<String> itemsToWithdraw = new ArrayList<String>();
	List<Integer> itemsToKeep = new ArrayList<Integer>();

	public WoodcuttingHandler() {
		cutTreeNode = new Cut();
	}

	public Node getNode() {
		WoodcuttingTask wcTask = (WoodcuttingTask) getCurrentTask();
		int axeID = wcTask.getAxeID();
		String axeName = wcTask.getAxeName();
		if (!inventory.contains(axeID) && !equipment.isWieldingWeapon(axeID)) {
			log("should not run");
			BankHandler.addItem(new WithdrawItem(axeID, 1, axeName));
			return null;
		} else if (canWieldAxe(axeName) && !equipment.isWieldingWeapon(axeID)) {
			GearHandler.addItem(new GearItem(EquipmentSlot.WEAPON, wcTask.getAxe()));
			return null;
		} else if (inventory.isFull() || playerInBank() && shouldDepositUnnecessaryItems(wcTask)) { //TODO, maybe add method, getInventory at bank
			itemsToKeep = new ArrayList<Integer>();
			if(!equipment.isWieldingWeapon(axeID)) {
				itemsToKeep.add(axeID);
			}
			BankHandler.addItem(new DepositItem(DepositItem.DepositType.DEPOSIT_ALL_EXCEPT, itemsToKeep));
		} else if(!playerInActionArea()){
			return walkToAreaNode.setArea(wcTask.getActionArea());
		}else if(inventory.getEmptySlotCount() >= 2 && LootNode.valueableDropAvailable(this, "Bird nest", wcTask.getActionArea()) != null) {
			log("lets loot birds nest@");
			return CombatHandler.lootNode.setItemAndArea(LootNode.valueableDropAvailable(this, "Bird nest", wcTask.getActionArea()), wcTask.getActionArea());
		}else if(!myPlayer().isAnimating()) {
			return cutTreeNode.setTask(wcTask);
		} //else return idle, prepare for next tree
		return null;
	}

	private boolean shouldDepositUnnecessaryItems(WoodcuttingTask wcTask) {
		int emptySpots = 27;
		if (canWieldAxe(wcTask.getAxeName())) {
			emptySpots = 28;
		}
		return inventory.getEmptySlots() != emptySpots;
	}

	private boolean canWieldAxe(String axe) {
		switch (axe) {
		case "Bronze axe":
		case "Iron axe":
			return true;
		case "Mithril axe":
			if (getSkills().getStatic(Skill.ATTACK) >= 20) {
				return true;
			}
		case "Rune axe":
			if (getSkills().getStatic(Skill.ATTACK) >= 40) {
				return true;
			}
		case "Dragon axe":
			if (getSkills().getStatic(Skill.ATTACK) >= 60) {
				return true;
			}
		}
		return false;
	}

}
