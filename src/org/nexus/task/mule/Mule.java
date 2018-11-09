package org.nexus.task.mule;

import java.util.function.BooleanSupplier;

import org.nexus.objects.RSItem;
import org.nexus.task.Task;
import org.nexus.task.TaskType;
import org.osbot.rs07.api.map.Area;

public abstract class Mule extends Task {
	
	protected int world;
	protected int itemID;
	protected int itemAmount;
	protected int startAmount;
	protected String tradeName;
	
	public Mule(int world, int itemID, int itemAmount, int startAmount, String muleName) {
		setWorld(world);
		setItemID(itemID);
		setItemAmount(itemAmount);
		setStartAmount(startAmount);
		setMuleName(muleName);
	}
	


	public int getWorld() {
		return world;
	}

	public void setWorld(int world) {
		this.world = world;
	}

	public int getItemID() {
		return itemID;
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
	}

	public int getItemAmount() {
		return itemAmount;
	}

	public void setItemAmount(int itemAmount) {
		this.itemAmount = itemAmount;
	}

	public int getStartAmount() {
		return startAmount;
	}

	public void setStartAmount(int startAmount) {
		this.startAmount = startAmount;
	}

	public String getMuleName() {
		return tradeName;
	}

	public void setMuleName(String muleName) {
		this.tradeName = muleName;
	}
	



}
