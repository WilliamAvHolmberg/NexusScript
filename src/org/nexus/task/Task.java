package org.nexus.task;

import java.util.function.BooleanSupplier;

import org.nexus.handler.gear.Gear;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.Skill;

public abstract class Task {

	private Area bankArea;
	private Area actionArea;
	private BooleanSupplier condition;
	private TaskType taskType;
	private Gear preferredGear;
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
	
	public abstract Skill getSkill();
}
