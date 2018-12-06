package org.nexus.node.agility;

import org.nexus.node.Node;
import org.nexus.node.methods.AreaMethods;
import org.nexus.task.agility.AgilityCourse;
import org.nexus.task.agility.AgilityObstacle;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.ConditionalSleep;

import org.nexus.utils.Timing;
public class AgilityCourseNode extends Node {

	private AgilityCourse agilityCourse;

	@Override
	public boolean shallExecute(MethodProvider methodProvider) {
		return true;
	}

	@Override
	public void execute(MethodProvider methodProvider) {

		for (AgilityObstacle obstacle : agilityCourse.getObstacles()) {
			if (AreaMethods.playerInArea(obstacle.getArea(), methodProvider)) {
				AgilityMethods.climbObs(obstacle.getAction(), obstacle.getID(), methodProvider);
				new ConditionalSleep(2000, 600) {
					@Override
					public boolean condition() throws InterruptedException {
						return AgilityMethods.playerInAgilityArea(agilityCourse, methodProvider);
					}
				}.sleep();
			}
		}
	}
	
	
	public Node setCourse(AgilityCourse course) {
		this.agilityCourse = course;
		return this;
	}

	@Override
	public String toString() {
		return "GnomeCourse";
	}


}
