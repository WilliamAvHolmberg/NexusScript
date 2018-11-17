package org.nexus.task.agility;

import java.util.ArrayList;
import java.util.List;

import org.osbot.rs07.api.map.Area;

public enum AgilityCourse {
	
	GNOME(GnomeData.gnomeAgilityArea, GnomeData.GNOME_OBSTACLES, 1);
	
	private Area courseArea;
	private List<AgilityObstacle> AgilityObstacle;
	private int requiredLevel;
	AgilityCourse(Area courseArea, AgilityObstacle[] obstacles, int requiredLevel){
		this.courseArea = courseArea;
		this.AgilityObstacle = new ArrayList<AgilityObstacle>();
		for(AgilityObstacle obstacle : obstacles){
			this.AgilityObstacle.add(obstacle);
		}
		this.requiredLevel = requiredLevel;
	}
	
	public List<AgilityObstacle> getObstacles(){
		return AgilityObstacle;
	}

	public int getRequiredLevel() {
		return requiredLevel;
	}

	public Area getArea() {
		return courseArea;
	}

}
