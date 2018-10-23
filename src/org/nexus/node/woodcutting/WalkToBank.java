package org.nexus.node.woodcutting;

import org.nexus.NexusScript;
import org.nexus.node.Node;

public class WalkToBank extends Node{
	
	@Override
	public boolean shallExecute() {
		//inv is full
		//inv does not contain axe
		//player is not in closest bank
		
		return !NexusScript.nodeHandler.getBankArea().contains(methodProvider.myPosition());
	}

	@Override
	public void execute() {
		methodProvider.log("lets walk to webbank");
		methodProvider.walking.webWalk(NexusScript.nodeHandler.getBankArea());		
	}

	@Override
	public String toString() {
		return "Walk To Bank";
	}
}
