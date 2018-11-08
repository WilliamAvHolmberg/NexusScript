package org.nexus.handler;

import org.nexus.NexusScript;
import org.nexus.node.Node;
import org.nexus.node.equip.EquipItem;
import org.nexus.node.general.WalkToArea;
import org.nexus.provider.NexProvider;
import org.nexus.task.Task;
import org.nexus.utils.WebBank;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.MethodProvider;

public abstract class Handler extends NexProvider {
	
	public static WalkToArea walkToAreaNode = new WalkToArea();
	public static EquipItem equipItemNode = new EquipItem();
	
	public abstract Node getNode();
	
	public Task getCurrentTask() {
		return NexusScript.currentTask;
	}
	
	public Area getBankArea() {
		if(NexusScript.currentTask == null || NexusScript.currentTask.getBankArea() == null) {
			return WebBank.getNearest(this).getArea();
		}
		return NexusScript.currentTask.getBankArea();
	}
	
	public boolean playerInArea(Area area) {
		return area.contains(myPlayer());
	}
	
	public boolean playerInBank() {
		return getBankArea().contains(myPlayer());
	}
	
	public boolean playerInActionArea() {
		if(getCurrentTask() != null && getCurrentTask().getActionArea() != null) {
			return playerInArea(getCurrentTask().getActionArea());
		}
		return false;
	}
	
}
