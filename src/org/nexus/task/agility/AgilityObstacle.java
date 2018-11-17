package org.nexus.task.agility;

import org.osbot.rs07.api.map.Area;

public class AgilityObstacle {
	
	private Area obstacleArea;
	private int obstacleID;
	private String obstacleAction;
	
	public AgilityObstacle(Area obstacleArea, int obstacleID, String obstacleAction){
		this.obstacleArea = obstacleArea;
		this.obstacleID = obstacleID;
		this.obstacleAction = obstacleAction;
	}
	
	public Area getArea(){
		return obstacleArea;
	}
	
	public int getID(){
		return obstacleID;
	}
	
	public String getAction(){
		return obstacleAction;
	}
}
