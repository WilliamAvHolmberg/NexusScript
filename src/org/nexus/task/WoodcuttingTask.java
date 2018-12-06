package org.nexus.task;

import java.awt.Graphics2D;
import java.util.function.BooleanSupplier;

import org.nexus.NexusScript;
import org.nexus.loot.Loot;
import org.nexus.loot.LootHandler;
import org.nexus.objects.RSItem;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.Skill;

public class WoodcuttingTask extends ActionTask {

	private String treeName;
	private RSItem axe;

	public WoodcuttingTask(Area actionArea, Area bankArea, RSItem axe, String treeName) {
		setActionArea(actionArea);
		setBankArea(bankArea);
		setTaskType(TaskType.WOODCUTTING);
		this.treeName = treeName;
		this.axe = axe;
		this.skill = Skill.WOODCUTTING;
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



	@Override
	public void onPaint(Graphics2D g) {
		g.drawString("XP Gained: " + NexusScript.experienceTracker.getGainedXP(getSkill()), 350, 50);
		g.drawString("XP Per Hour: " + NexusScript.experienceTracker.getGainedXPPerHour(getSkill()), 350, 75);
		g.drawString("Logs Per Hour: " + NexusScript.perHour(LootHandler.loot.size()), 350,
				100);
		g.drawString("Break after level: " + getWantedLevel(), 350, 125);
		g.drawString("Or Break in: " + getTimeLeft() + " minutes", 350, 150);
		g.drawString("Money made: " + LootHandler.valueOfLoot, 350,
				175);
		g.drawString("Money per hour: " + NexusScript.perHour(LootHandler.valueOfLoot), 350,
				200);
	}



	public Loot getLog() {
		switch(treeName) {
		case "Tree":
			return new Loot(new RSItem("Logs", 1511), 1);
		case "Oak":
			return new Loot(new RSItem("Oak logs", 1521), 1);
		case "Willow":
			return new Loot(new RSItem("Willow logs", 1519), 1);
		case "Maple":
			return new Loot(new RSItem("Maple logs", 1517), 1);
		case "Yew":
			return new Loot(new RSItem("Maple logs", 1517), 1);
		}
		return null;
	}
}
