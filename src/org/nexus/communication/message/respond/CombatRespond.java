package org.nexus.communication.message.respond;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;

import org.nexus.communication.message.NexMessage;
import org.nexus.handler.TaskHandler;
import org.nexus.handler.gear.Gear;
import org.nexus.handler.gear.Inventory;
import org.nexus.handler.gear.InventoryItem;
import org.nexus.objects.RSItem;
import org.nexus.task.CombatTask;
import org.nexus.task.Task;

import org.nexus.utils.WebBank;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class CombatRespond extends TaskRespond {


	public CombatRespond(MethodProvider methodProvider, Stack<NexMessage> messageQueue, String respond) {
		super(methodProvider, messageQueue, respond);
		methodProvider.log("wc res is created. is res null?: " + respond == null);
		
	}

	@Override
	public void execute(PrintWriter out, BufferedReader in) throws IOException {
		if(respond != null) {
		methodProvider.log(respond);
		}else {
			methodProvider.log("respond is null");
		}
		String[] parsed = respond.split(":");
		String currentTaskID = parsed[3];
		String parsedBankArea = parsed[4];
		String parsedActionArea = parsed[5];
		String monsterName = parsed[6];
		String parsedBreakCondition = parsed[7];
		//parsed 7 is our break condition. Will always be set to time for now
		String breakAfter = parsed[8];
		
		ArrayList<String> listOfParsedGear = new ArrayList<String>();
		String parsedHelm = parsed[9];
		String parsedCape = parsed[10];
		String parsedAmulet = parsed[11];
		String parsedWeapon = parsed[12];
		String parsedChest = parsed[13];
		String parsedShield = parsed[14];
		String parsedLegs = parsed[15];
		String parsedGloves = parsed[16];
		String parsedBoots = parsed[17];
		String parsedRing = parsed[18];
		String parsedAmmo = parsed[19];
		listOfParsedGear.add(parsedHelm);
		listOfParsedGear.add(parsedCape);
		listOfParsedGear.add(parsedAmulet);
		listOfParsedGear.add(parsedWeapon);
		listOfParsedGear.add(parsedChest);
		listOfParsedGear.add(parsedShield);
		listOfParsedGear.add(parsedLegs);
		listOfParsedGear.add(parsedGloves);
		listOfParsedGear.add(parsedBoots);
		listOfParsedGear.add(parsedRing);
		listOfParsedGear.add(parsedAmmo);
		
		String parsedFood = parsed[21];
		String parsedInventory = parsed[22];
		String parsedLootThreshold = parsed[23];
		String parsedSkill = parsed[24];
		String parsedlevelGoal = parsed[25];
		
		
		int lootThreshold = Integer.parseInt(parsedLootThreshold);
		RSItem food = null;
		methodProvider.log(parsedFood);
		if(!parsedFood.toLowerCase().equals("none")) {
			food = new RSItem(parsedFood.split(",")[0], Integer.parseInt(parsedFood.split(",")[1]));
		}
		
		Gear gear = getGear(parsed, listOfParsedGear);
		
		Area bankArea = null;
		if (!parsedBankArea.equals("none")) {
			bankArea = WebBank.parseCoordinates(parsedBankArea);
		}
		currentTime = System.currentTimeMillis();
		Area actionArea = WebBank.parseCoordinates(parsedActionArea);
		Inventory inv = new Inventory();
		inv = getInventory(parsedInventory);
		
		newTask = new CombatTask(actionArea, bankArea, monsterName, gear, food, inv, Skill.valueOf(parsedSkill.toUpperCase()), lootThreshold );
		newTask.setTimeStartedMilli(currentTime);
		newTask.setTaskID(currentTaskID);
		setBreakConditions(newTask, parsedBreakCondition, breakAfter, parsedlevelGoal);
		TaskHandler.addTask(newTask);
		methodProvider.log("created task");
	}

	private void setBreakConditions(Task newTask, String parsedBreakCondition, String breakAfter,
			String parsedlevelGoal) {
		if(parsedBreakCondition.toLowerCase().contains("time_or_level")) {
			newTask.setBreakAfter((int)Double.parseDouble(breakAfter));
			methodProvider.log(breakAfter);
			methodProvider.log("we set condition");
			newTask.setWantedLevel((int)Double.parseDouble(parsedlevelGoal));
			methodProvider.log("we set wanted Level");
		}else if(parsedBreakCondition.toLowerCase().contains("time")) {
			newTask.setBreakAfter((int)Double.parseDouble(breakAfter));
		}else if(parsedBreakCondition.toLowerCase().contains("level")) {
			methodProvider.log(breakAfter);
			methodProvider.log("we set condition");
			newTask.setWantedLevel((int)Double.parseDouble(parsedlevelGoal));
			methodProvider.log("we set wanted Level");
		}
		
	}

	private Inventory getInventory(String parsedInventory) {
		Inventory inv = new Inventory();
		for(String parsedInvItem : parsedInventory.split(";")) {
			if(parsedInvItem.length() > 2) {
				String[] moreParsed = parsedInvItem.split(",");
				String itemName = moreParsed[0];
				int itemId = Integer.parseInt(moreParsed[1]);
				int itemAmount = Integer.parseInt(moreParsed[2]);
				InventoryItem newItem = new InventoryItem(itemAmount, new RSItem(itemName, itemId));
				inv.addItem(newItem);
			}
		}
		return inv;
	}

	private Gear getGear(String[] parsed, ArrayList<String> listOfParsedGear) {
		Gear gear = new Gear();
		String unParsedItem;
		String itemName;
		int itemID;
		for(int i = 0; i<EquipmentSlot.values().length; i++) {
			unParsedItem = listOfParsedGear.get(i);
			if(!unParsedItem.toLowerCase().equals("none")) {
				itemName = unParsedItem.split(",")[0];
				itemID = Integer.parseInt(unParsedItem.split(",")[1]);
				methodProvider.log("slot:" + EquipmentSlot.values()[i] + "   itemName:" + itemName + "    itemID:" + itemID);
				gear.addGear(EquipmentSlot.values()[i], new RSItem(itemName, itemID));
			}
		}
		return gear;		
	}
}
