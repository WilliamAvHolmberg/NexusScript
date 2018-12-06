package org.nexus.task.agility;

import java.util.ArrayList;
import java.util.List;

import org.nexus.node.Node;
import org.osbot.rs07.api.map.Area;
import org.nexus.node.agility.WalkToTreeGnome;

public enum AgilityCourse {
	
	GNOME(GnomeData.gnomeAgilityArea, GnomeData.GNOME_OBSTACLES, 1, new WalkToTreeGnome()),
	VARROCK(VarrockData.varrockAgilityArea,VarrockData.VARROCK_OBSTACLES, 30, null ),
	FALADOR(FaladorData.faladorAgilityArea,FaladorData.FALADOR_OBSTACLES, 50, null );
	
	private Area courseArea;
	private List<AgilityObstacle> AgilityObstacle;
	private int requiredLevel;
	private Node walkNode = null;
	
	AgilityCourse(Area courseArea, AgilityObstacle[] obstacles, int requiredLevel, Node walkNode){
		this.courseArea = courseArea;
		this.AgilityObstacle = new ArrayList<AgilityObstacle>();
		for(AgilityObstacle obstacle : obstacles){
			this.AgilityObstacle.add(obstacle);
		}
		this.requiredLevel = requiredLevel;
		this.walkNode = walkNode;
		
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
	
	public Node getWalkNode() {
		return walkNode;
	}

}
