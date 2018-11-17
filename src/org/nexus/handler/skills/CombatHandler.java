 package org.nexus.handler.skills;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.nexus.handler.BankHandler;
import org.nexus.handler.Handler;
import org.nexus.handler.gear.Gear;
import org.nexus.handler.gear.GearHandler;
import org.nexus.handler.gear.GearItem;
import org.nexus.handler.gear.Inventory;
import org.nexus.handler.gear.InventoryItem;
import org.nexus.node.Node;
import org.nexus.node.combat.Eat;
import org.nexus.node.combat.Fight;
import org.nexus.node.combat.LootNode;
import org.nexus.node.woodcutting.Cut;
import org.nexus.objects.DepositItem;
import org.nexus.objects.RSItem;
import org.nexus.objects.WithdrawItem;
import org.nexus.objects.DepositItem.DepositType;
import org.nexus.task.CombatTask;
import org.nexus.task.Task;
import org.nexus.task.WoodcuttingTask;
import org.nexus.utils.WebBank;
import org.osbot.rs07.api.Bank;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class CombatHandler extends Handler {

	public static Fight fightNode = new Fight();
	public static Eat eatNode = new Eat();
	public static LootNode lootNode = new LootNode();
	List<String> itemsToWithdraw = new ArrayList<String>();
	List<Integer> itemsToKeep = new ArrayList<Integer>();

	public CombatHandler() {
		fightNode = new Fight();
		eatNode = new Eat();
		lootNode = new LootNode();
	}

	public Node getNode() {
		// TODO, WALK TO BANK: FIX INVENTORY:: ETC :: NOW LETS PLAY A FIFA GAME

		CombatTask combatTask = (CombatTask) getCurrentTask();
		GearItem itemToEquip = getGearItemToEquip(combatTask.getGear());
		if (itemToEquip != null && inventory.isFull()) {
			BankHandler.addItem(new DepositItem(DepositType.DEPOSIT_ALL_EXCEPT, itemToEquip.getItem().getId()));
		}else if(itemToEquip != null) {
			GearHandler.addItem(itemToEquip);
			return null;
		}else if (!playerInActionArea()) {
			return handleOutOfActionArea(combatTask);
		}else if (inventory.isFull() && !inventory.contains(combatTask.getFood().getId())) { //TODO, maybe add method, getInventory at bank
			BankHandler.addItem(new DepositItem(DepositItem.DepositType.DEPOSIT_ALL_EXCEPT, combatTask.getInventory().getItemIds()));
		}else if (Eat.shallEat(this)) {
			return handleShallEat(combatTask);
		} else if (LootNode.valueableDropAvailable(this, combatTask.getLootThreshold(), combatTask.getActionArea()) != null){
			return handleLoot(combatTask);
		} else if (!myPlayer().isAnimating()) {
			return fightNode.setCombatTask(combatTask);
		}
		return null;
	}

	private Node handleLoot(CombatTask combatTask) {
		GroundItem item = LootNode.valueableDropAvailable(this, combatTask.getLootThreshold(),combatTask.getActionArea());
		if(inventory.isFull() && inventory.contains(combatTask.getFood().getId())) {
			return eatNode.setFood(combatTask.getFood());
		}else if (!inventory.isFull() || (item.getDefinition().getNotedId() == -1 && inventory.contains(item.getId()))) {
			return lootNode.setItemAndArea(item, combatTask.getActionArea());
		} else {

		}
		return null;
	}

	private Node handleOutOfActionArea(CombatTask combatTask) {
		InventoryItem itemToWithdraw = getItemToWithdraw(combatTask.getInventory());
		 if (shouldDepositUnnecessaryItems(combatTask)) {
			itemsToKeep = new ArrayList<Integer>();
			for (InventoryItem item : combatTask.getInventory().getItems()) {
				itemsToKeep.add(item.getItem().getId());
			}
			BankHandler.addItem(new DepositItem(DepositItem.DepositType.DEPOSIT_ALL_EXCEPT, itemsToKeep));
			return null;
		}else if (Eat.getCurrentHealth(this) != skills.getStatic(Skill.HITPOINTS) && inventory.contains(combatTask.getFood().getId())) {
			return handleShallEat(combatTask);
		}  else if (itemToWithdraw != null) {
			int amountToWithdraw = (int) (itemToWithdraw.getAmount());
			BankHandler.addItem(new WithdrawItem(itemToWithdraw.getItem().getId(), amountToWithdraw,
					itemToWithdraw.getItem().getName()).setInventory(combatTask.getInventory()));
			return null;
		}else {
			log("helloo");
			return walkToAreaNode.setArea(combatTask.getActionArea());
		}
	}

	private InventoryItem getItemToWithdraw(Inventory inventory) {
		for (InventoryItem item : inventory.getItems()) {
			if (getInventory().getAmount(item.getItem().getId()) < item.getAmount()) {
				return item;
			}
		}
		return null;
	}

	private Node handleShallEat(CombatTask combatTask) {
		if (combatTask.getFood() == null) {
			log("we got no food. lets walk to bank :) ");
			return walkToAreaNode.setArea(WebBank.getNearest(this).getArea());
		} else if (!inventory.contains(combatTask.getFood().getId())) {
			BankHandler.addItem(new WithdrawItem(combatTask.getFood(), 1));
			return null;
		} else {
			return eatNode.setFood(combatTask.getFood());
		}
	}

	private GearItem getGearItemToEquip(Gear gear) {
		for (Entry<EquipmentSlot, GearItem> entry : gear.getGear().entrySet()) {
			EquipmentSlot key = entry.getKey();
			GearItem value = entry.getValue();
			if (key != null && value != null && value.getItem() != null
					&& !equipment.isWearingItem(key, value.getItem().getId())) {
				log("not wearing:" + value.getItem().getName());
				return value;
			}
		}
		return null;
	}

	private boolean shouldDepositUnnecessaryItems(CombatTask combatTask) {
		for(Item item : inventory.getItems()) {
			if (item != null && !combatTask.getInventory().getItemIds().contains(item.getId())){
				return true;
			}
		}
		return false;
	}

}
