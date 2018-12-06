package org.nexus.task.mule;

import java.util.function.BooleanSupplier;

import org.nexus.objects.RSItem;
import org.nexus.task.ActionTask;
import org.nexus.task.TaskType;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.MethodProvider;

public abstract class Mule extends ActionTask {
	
	protected int world;
	protected int itemID;
	protected int itemAmount;
	protected int startAmount;
	protected String tradeName;
	protected boolean banked = false;
	protected boolean soldItems = false;
	
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
	
	public boolean getBanked() {
		return banked;
	}
	
	public void setBanked(boolean bool) {
		this.banked = bool;
	}

	public String getMuleName() {
		return tradeName;
	}
	
	public boolean isFinished(MethodProvider methodProvider) {
		if(tradeIsCompleted) {
			methodProvider.log("Trade completed from mess");
			return true;
		}	
		if (getTimeLeft() <= 0) {
			methodProvider.log("no time left");
			return true;
		}
		return false;

	}

	public void setMuleName(String muleName) {
		this.tradeName = muleName;
	}

	public void setSoldItems(boolean bool) {
		this.soldItems = bool;
	}
	public boolean soldItems() {
		return soldItems;
	}
	



}
