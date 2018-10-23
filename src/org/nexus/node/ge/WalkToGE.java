package org.nexus.node.ge;

import org.nexus.NexusScript;
import org.nexus.node.Node;
import org.nexus.utils.WebBank;

public class WalkToGE extends Node{
	
	@Override
	public boolean shallExecute() {
		//inv is full
		//inv does not contain axe
		//player is not in closest bank
		
		return WebBank.GRAND_EXCHANGE.getArea().contains(methodProvider.myPosition());
	}

	@Override
	public void execute() {
		methodProvider.log("lets walk to webbank");
		methodProvider.walking.webWalk(WebBank.GRAND_EXCHANGE.getArea());		
	}

	@Override
	public String toString() {
		return "Walk To GE";
	}
}
