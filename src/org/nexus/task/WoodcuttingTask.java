package org.nexus.task;

import java.util.function.BooleanSupplier;

import org.osbot.rs07.api.map.Area;

public class WoodcuttingTask extends Task {

	private String axeName;
	private String treeName;
	private int axeID;
	
	public WoodcuttingTask(Area actionArea, Area bankArea, BooleanSupplier condition, String axeName, int axeID, String treeName) {
		setActionArea(actionArea);
		setBankArea(bankArea);
		setCondition(condition);
		setTaskType(TaskType.WOODCUTTING);
		this.axeName = axeName;
		this.treeName = treeName;
		this.axeID = axeID;
	}
	
	public String getAxeName() {
		return axeName;
	}
	
	public String getTreeName() {
		return treeName;
	}

	public int getAxeID() {
		return axeID;
	}
}
