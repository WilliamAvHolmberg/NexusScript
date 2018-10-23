package org.nexus.task;

import java.util.function.BooleanSupplier;

import org.nexus.objects.RSItem;
import org.osbot.rs07.api.map.Area;

public class WoodcuttingTask extends Task {

	private String treeName;
	private RSItem axe;

	public WoodcuttingTask(Area actionArea, Area bankArea, BooleanSupplier condition, RSItem axe, String treeName) {
		setActionArea(actionArea);
		setBankArea(bankArea);
		setCondition(condition);
		setTaskType(TaskType.WOODCUTTING);
		this.treeName = treeName;
		this.axe = axe;
	}

	public String getAxeName() {
		return axe.getName();
	}

	public String getTreeName() {
		return treeName;
	}

	public int getAxeID() {
		return axe.getId();
	}

	public RSItem getAxe() {
		return axe;
	}
}
