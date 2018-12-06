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
import org.nexus.node.agility.AgilityCourseNode;
import org.nexus.node.agility.AgilityMethods;
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
import org.nexus.task.ActionTask;
import org.nexus.task.WoodcuttingTask;
import org.nexus.task.agility.AgilityCourse;
import org.nexus.task.agility.AgilityTask;
import org.nexus.utils.WebBank;
import org.osbot.rs07.api.Bank;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class AgilityHandler extends Handler {

	public static AgilityCourseNode courseNode = new AgilityCourseNode();
	

	public AgilityHandler() {
		
	}

	public Node getNode() {
		AgilityTask agilityTask = (AgilityTask) getCurrentTask();
		AgilityCourse course = agilityTask.getCourse(this);
		if(!AgilityMethods.playerInAgilityArea(course, this)) {
			if(course.getWalkNode() != null) {
				return course.getWalkNode();
			}else {
				return walkToAreaNode.setArea(course.getObstacles().get(0).getArea()); //walk to obstacle nr1 (nr0)
			}
		}else if(AgilityMethods.getMarkOfGrace(this, course) != null) {
			log("lets loot mark of grace@@@@@@");
			return CombatHandler.lootNode.setItemAndArea(AgilityMethods.getMarkOfGrace(this, course), AgilityMethods.getCurrentArea(this, course, myPlayer()));
		}else {
			return courseNode.setCourse(course);
		}
	}


	

}
