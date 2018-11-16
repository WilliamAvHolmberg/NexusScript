package org.nexus.node.general;

import java.util.List;

import org.nexus.NexusScript;
import org.nexus.node.Node;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.event.webwalk.PathPreferenceProfile;
import org.osbot.rs07.script.MethodProvider;

public class WalkToArea extends Node{
	
	private Area area;
	@Override
	public boolean shallExecute(MethodProvider methodProvider) {
		//inv is full
		//inv does not contain axe
		//player is not in closest bank
		
		return !NexusScript.nodeHandler.getBankArea().contains(methodProvider.myPosition());
	}

	@Override
	public void execute(MethodProvider methodProvider) {
		methodProvider.log("lets walk to webbank");
		methodProvider.walking.webWalk(area);	

		WebWalkEvent event = new WebWalkEvent(area);

		event.setPathPreferenceProfile(PathPreferenceProfile.DEFAULT.setAllowTeleports(true));
		methodProvider.execute(event);
	}

	@Override
	public String toString() {
		return "Walk To Location";
	}

	public Node setArea(Area area) {
		this.area = area;
		return this;
	}
}
