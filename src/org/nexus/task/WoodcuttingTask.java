package org.nexus.task;

import java.awt.Graphics2D;
import java.util.function.BooleanSupplier;

import org.nexus.NexusScript;
import org.nexus.loot.LootHandler;
import org.nexus.objects.RSItem;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.Skill;

public class WoodcuttingTask extends Task {

	private String treeName;
	private RSItem axe;

	public WoodcuttingTask(Area actionArea, Area bankArea, RSItem axe, String treeName) {
		setActionArea(actionArea);
		setBankArea(bankArea);
		setTaskType(TaskType.WOODCUTTING);
		this.treeName = treeName;
		this.axe = axe;
		NexusScript.setTracker(getSkill());
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
	
	public Skill getSkill() {
		return Skill.WOODCUTTING;
	}



	@Override
	public void onPaint(Graphics2D g) {
		g.drawString("XP Gained: " + NexusScript.experienceTracker.getGainedXP(getSkill()), 350, 50);
		g.drawString("XP Per Hour: " + NexusScript.experienceTracker.getGainedXPPerHour(getSkill()), 350, 75);
		g.drawString("Logs Per Hour: " + NexusScript.experienceTracker.getGainedXPPerHour(getSkill()) / 25, 350,
				100);
		if (getBreakType() != null && getBreakAfter() > 0) {
				g.drawString("Break in: " + getTimeLeft() + " minutes", 350, 125);
		}		
	}
}
