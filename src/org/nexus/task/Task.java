package org.nexus.task;

import java.util.function.BooleanSupplier;

import org.nexus.NexusScript;
import org.nexus.handler.gear.Gear;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class Task {

	private Area bankArea;
	private Area actionArea;
	private BooleanSupplier condition;
	private TaskType taskType;
	private Gear preferredGear;
	private String breakType;
	private String breakAfter;
	private String taskID;
	private long timeStartedMilli;
	private int gainedXP = 0;
	
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
	
	public void setCondition(BooleanSupplier condition) {
		this.condition = condition;
	}
	public boolean isCompleted() {
		return condition.getAsBoolean();
	}
	public TaskType getTaskType() {
		return taskType;
	}
	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}
	public Gear getPreferredGear() {
		if(this.preferredGear == null) {
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
	
	public String getBreakAfter() {
		return this.breakAfter;
	}
	public void setBreakAfter(String breakAfter) {
		this.breakAfter = breakAfter;
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
	public long getTimeLeft() {
		return ((timeStartedMilli + (Integer.parseInt(this.breakAfter) * 60 * 1000)) - System.currentTimeMillis())/60000;
	}
	
	public int getGainedXP() {
		return this.gainedXP;
	}
	public void setGainedXP(int gainedXP) {
		this.gainedXP = gainedXP;
	}
	
	public String toString() {
		if(this.taskType != null) {
			return "" + this.taskType;
		}
		return "No TaskType defined";
	}

}
