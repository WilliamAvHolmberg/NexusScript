package org.nexus.task;

import java.util.function.BooleanSupplier;

import org.osbot.rs07.api.map.Area;

public abstract class Task {

	private Area bankArea;
	private Area actionArea;
	private BooleanSupplier condition;
	private TaskType taskType;
	
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
}
