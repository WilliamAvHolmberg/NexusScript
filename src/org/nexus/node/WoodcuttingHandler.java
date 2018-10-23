package org.nexus.node;

import java.util.ArrayList;
import java.util.List;

import org.nexus.node.bank.BankHandler;
import org.nexus.node.woodcutting.Cut;
import org.nexus.node.woodcutting.WalkToAction;
import org.nexus.objects.DepositItem;
import org.nexus.objects.WithdrawItem;
import org.nexus.task.Task;
import org.nexus.task.WoodcuttingTask;
import org.nexus.utils.WebBank;
import org.osbot.rs07.api.Bank;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class WoodcuttingHandler {

	private ArrayList<Node> walkToActionNodes;
	private ArrayList<Node> cutTreeNodes;
	List<String> itemsToWithdraw = new ArrayList<String>();
	List<String> itemsToKeep = new ArrayList<String>();
	private WebBank webBank;
	private MethodProvider methodProvider;


	public WoodcuttingHandler(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
		walkToActionNodes = new ArrayList<Node>();
		cutTreeNodes = new ArrayList<Node>();
		walkToActionNodes.add(new WalkToAction().init(methodProvider));
		cutTreeNodes.add(new Cut().init(methodProvider));
	}

	public List<Node> getNodes(Task currentTask) {
		WoodcuttingTask wcTask = (WoodcuttingTask) currentTask;
		int axeID = wcTask.getAxeID();
		String axeName = wcTask.getAxeName();
		if (!methodProvider.inventory.contains(axeID) || methodProvider.equipment.isWieldingWeapon(axeID)) {
			BankHandler.addItem(new WithdrawItem(axeID, 1, axeName));
			return null;
		} else if (1 == 2 && canWieldAxe(axeName) && !methodProvider.equipment.isWieldingWeapon(axeID)) {
			// GearHandler.addItem(Iron Axe
			return null;
		} else if (methodProvider.inventory.isFull()) {
			itemsToKeep = new ArrayList<String>();
			itemsToKeep.add(axeName);
			BankHandler.addItem(new DepositItem(DepositItem.DepositType.DEPOSIT_ALL_EXCEPT, itemsToKeep));
			return null;
		} else if(currentTask.getActionArea().contains(methodProvider.myPosition())){
			return cutTreeNodes;
		}else {
			return walkToActionNodes;
		}
	}

	private boolean canWieldAxe(String axe) {
		switch (axe) {
		case "Bronze axe":
		case "Iron axe":
			return true;
		case "Mithril axe":
			if (methodProvider.getSkills().getStatic(Skill.ATTACK) >= 20) {
				return true;
			}
		}
		return false;
	}

}
