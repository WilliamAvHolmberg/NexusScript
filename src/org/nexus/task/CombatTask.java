package org.nexus.task;

import java.awt.Graphics2D;
import java.util.function.BooleanSupplier;

import org.nexus.NexusScript;
import org.nexus.handler.gear.Gear;
import org.nexus.handler.gear.Inventory;
import org.nexus.loot.LootHandler;
import org.nexus.objects.RSItem;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.Skill;

public class CombatTask extends Task {

	private String monsterName;
	private RSItem food;
	private Skill skill;
	private int lootThreshold;
	
	public CombatTask(Area actionArea, Area bankArea, String monsterName, Gear gear, RSItem food, Inventory inventory, Skill skill, int lootThreshold) {
		setActionArea(actionArea);
		setBankArea(bankArea);
		setTaskType(TaskType.COMBAT);
		this.monsterName = monsterName;
		this.gear = gear;
		this.food = food;
		this.inventory = inventory;
		this.skill = skill;
		this.lootThreshold = lootThreshold;
		NexusScript.setTracker(getSkill());
		LootHandler.reset();
	}





	public String getMonsterName() {
		return monsterName;
	}
	
	public int getLootThreshold() {
		return lootThreshold;
	}
	
	
	
	public RSItem getFood() {
		return food;
	}
	


	// TODO - Add different attack styles LATER
	public Skill getSkill() {
		return skill;
	}







	@Override
	public void onPaint(Graphics2D g) {
		g.drawString("XP Gained: " + NexusScript.experienceTracker.getGainedXP(getSkill()), 350, 50);
		g.drawString("XP Per Hour: " + NexusScript.experienceTracker.getGainedXPPerHour(getSkill()), 350, 75);
		g.drawString("Logs Per Hour: " + NexusScript.experienceTracker.getGainedXPPerHour(getSkill()) / 25, 350,
				100);
		g.drawString("Break after level: " + getWantedLevel(), 350, 125);
		g.drawString("Or Break in: " + getTimeLeft() + " minutes", 350, 150);
		g.drawString("Money made: " + LootHandler.valueOfLoot, 350,
				175);
		g.drawString("Money per hour: " + NexusScript.perHour(LootHandler.valueOfLoot), 350,
				200);
		
		g.drawString("Started: " + getTimeStartedMilli(), 250, 250);
		g.drawString("Time til new request " + getTimeLeft() , 250, 270);
	}





	
	

}
