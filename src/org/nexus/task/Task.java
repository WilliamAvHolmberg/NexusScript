package org.nexus.task;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.function.BooleanSupplier;

import org.nexus.NexusScript;
import org.nexus.handler.gear.Gear;
import org.nexus.handler.gear.GearItem;
import org.nexus.handler.gear.Inventory;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.util.ExperienceTracker;
import org.osbot.rs07.script.MethodProvider;

public abstract class Task {

	private Area bankArea;
	private Area actionArea;
	private TaskType taskType;
	private Gear preferredGear;
	private String breakType;
	protected int breakAfterTime = 0;
	private String taskID;
	protected long timeStartedMilli;
	private int gainedXP = 0;
	public boolean tradeIsCompleted = false;
	private int wantedLevel = 0;
	protected Gear gear;
	protected Inventory inventory;

	public static Gear EMPTY_GEAR = new Gear();

	public Area getBankArea() {
		return bankArea;
	}

	public void setBankArea(Area bankArea) {
		this.bankArea = bankArea;
	}

	public Area getActionArea() {
		return actionArea;
	}

	public void setActionArea(Area actionArea) {
		this.actionArea = actionArea;
	}


	public boolean isCompleted(MethodProvider methodProvider) {
		methodProvider.log(breakAfterTime);
		methodProvider.log(timeStartedMilli);
		if(breakAfterTime > 0 && timeStartedMilli > 0 && getSkill() != null) {
			return getTimeLeft() <= 0 || getWantedLevel() <= methodProvider.getSkills().getStatic(getSkill());
		}else if (breakAfterTime > 0 && timeStartedMilli > 0 && getTimeLeft() <= 0) {
			return true;
		} else if (getSkill() != null && getWantedLevel() <= methodProvider.getSkills().getStatic(getSkill())) {
			return true;
		}
		return false;

	}

	public TaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

	public Gear getPreferredGear() {
		if (this.preferredGear == null) {
			return EMPTY_GEAR;
		}
		return this.preferredGear;
	}

	public void setPreferredGear(Gear gear) {
		this.preferredGear = gear;
	}

	public String getBreakType() {
		return this.breakType;
	}

	public void setBreakType(String parsedBreakCondition) {
		this.breakType = parsedBreakCondition;
	}

	public int getBreakAfter() {
		return this.breakAfterTime;
	}

	public void setBreakAfter(int breakAfter) {
		this.breakAfterTime = breakAfter;
	}

	public long getTimeStartedMilli() {
		return timeStartedMilli;
	}

	public void setTimeStartedMilli(long timeStartedMilli) {
		this.timeStartedMilli = timeStartedMilli;
	}

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public Skill getSkill() {
		return null;
	}

	/**
	 * break after is given in minutes so we have to cast it to millis. When the
	 * calculation is done we have to divide by 60000 to go back to minutes.
	 * 
	 * @return time left of current task
	 */
	public long getTimeLeft() {
		return ((timeStartedMilli + (this.breakAfterTime * 60 * 1000)) - System.currentTimeMillis())
				/ 60000;
	}

	public int getGainedXP() {
		return this.gainedXP;
	}

	public void setGainedXP(int gainedXP) {
		this.gainedXP = gainedXP;
	}

	public String toString() {
		if (this.taskType != null) {
			return "" + this.taskType;
		}
		return "No TaskType defined";
	}

	public abstract void onPaint(Graphics2D g);

	public void setWantedLevel(int wantedLevel) {
		this.wantedLevel = wantedLevel;
	}

	public int getWantedLevel() {
		return wantedLevel;
	}
	
	public Gear getGear() {
		return gear;
	}
	public Inventory getInventory() {
		return inventory;
	}
	
	public ArrayList<Integer> getRequiredItems(){
		ArrayList<Integer> reqItems = new ArrayList<Integer>();
		if(gear != null) {
		for(GearItem item : gear.getGear().values()) {
			if(item != null && item.getItem() != null) {
			reqItems.add(item.getItem().getId());
			}
		}
		}
		if(inventory != null){
		for(int invItem : inventory.getItemIds()) {
			reqItems.add(invItem);
		}
		}
		
		return reqItems;
	}

}
