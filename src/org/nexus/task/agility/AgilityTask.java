package org.nexus.task.agility;

import java.awt.Graphics2D;

import org.nexus.node.Node;
import org.nexus.task.Task;
import org.nexus.task.TaskType;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class AgilityTask extends Task{
	
	private AgilityCourse course;
	private Node walkNode = null;
	
	public AgilityTask() {
		this.skill = Skill.AGILITY;
	}
	public AgilityTask(AgilityCourse course) {
		this.course = course;
		this.skill = Skill.AGILITY;
	}

	public AgilityTask(AgilityCourse course, Node walkNode) {
		this.course = course;
		this.skill = Skill.AGILITY;
		this.walkNode = walkNode;
	}
	
	public Node getWalkNode() {
		return walkNode;
	}
	@Override
	public void onPaint(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}
	
	public AgilityCourse getCourse(MethodProvider methodProvider) {
		if(course != null) {
			return course;
		}else {
			int level = methodProvider.getSkills().getStatic(Skill.AGILITY);
			if(level < 99) {
				return AgilityCourse.GNOME;
			}
		}
		return AgilityCourse.GNOME;
	}
	
	@Override
	public TaskType getTaskType() {
		return TaskType.AGILITY;
	}
	
	

}
