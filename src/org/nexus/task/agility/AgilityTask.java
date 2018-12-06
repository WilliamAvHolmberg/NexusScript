package org.nexus.task.agility;

import java.awt.Graphics2D;

import org.nexus.NexusScript;
import org.nexus.loot.LootHandler;
import org.nexus.node.Node;
import org.nexus.task.ActionTask;
import org.nexus.task.TaskType;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class AgilityTask extends ActionTask{
	
	private AgilityCourse course;
	private int gracesLooted = 0;
	
	public AgilityTask() {
		this.skill = Skill.AGILITY;
	}
	public AgilityTask(AgilityCourse course) {
		this.course = course;
		this.skill = Skill.AGILITY;
	}

	

	@Override
	public void onPaint(Graphics2D g) {
		g.drawString("XP Gained: " + NexusScript.experienceTracker.getGainedXP(getSkill()), 350, 50);
		g.drawString("XP Per Hour: " + NexusScript.experienceTracker.getGainedXPPerHour(getSkill()), 350, 75);
		g.drawString("Break after level: " + getWantedLevel(), 350, 125);
		g.drawString("Or Break in: " + getTimeLeft() + " minutes", 350, 150);
		g.drawString("Mark of graces looted: " + gracesLooted, 350,175);
		g.drawString("Money made: " + LootHandler.valueOfLoot, 350,
				200);
		g.drawString("Money per hour: " + NexusScript.perHour(LootHandler.valueOfLoot), 350,
				 250);
		
		g.drawString("Started: " + getTimeStartedMilli(), 250, 275);
		g.drawString("Time til new request " + getTimeLeft() , 250, 300);
		
	}
	
	public AgilityCourse getCourse(MethodProvider methodProvider) {
		if(course != null) {
			return course;
		}else {
			int level = methodProvider.getSkills().getStatic(Skill.AGILITY);
			if(level < 30) {
				return AgilityCourse.GNOME;
			}
			
			if(level < 50) {
				return AgilityCourse.VARROCK;
			}
			
			if(level < 60) {
				return AgilityCourse.FALADOR;
			}
		}
		return AgilityCourse.GNOME;
	}
	
	@Override
	public TaskType getTaskType() {
		return TaskType.AGILITY;
	}
	
	public int getAmountOfGraces() {
		return gracesLooted;
	}
	public void updateMarkOfGraces() {
		gracesLooted++;
	}
	
	

}
