package org.nexus.node.woodcutting;

import org.nexus.NexusScript;
import org.nexus.node.Node;
import org.osbot.rs07.api.map.Area;

public class WalkToAction extends Node{

	@Override
	public boolean shallExecute() {	
		return !methodProvider.myPlayer().isAnimating() && !methodProvider.myPlayer().isMoving() && !NexusScript.currentTask.getActionArea().contains(methodProvider.myPosition());
	}

	@Override
	public void execute() {
		methodProvider.log("lets walk to actionArea");
		methodProvider.walking.webWalk(NexusScript.currentTask.getActionArea());		
	}

	@Override
	public String toString() {
		return "WalkToActionArea";
	}



}
