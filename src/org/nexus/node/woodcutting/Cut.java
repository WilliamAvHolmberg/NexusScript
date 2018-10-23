package org.nexus.node.woodcutting;

import java.util.Arrays;
import java.util.List;

import org.nexus.node.Node;
import org.nexus.utils.Timing;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;

public class Cut extends Node {

	MethodProvider methodProvider;
	RS2Object object;
	private String treeName;

	@Override
	public boolean shallExecute(MethodProvider methodProvider) {
		// TODO invnetory or equipment contains axe
		return !methodProvider.myPlayer().isAnimating(); // hard coded atm TODO later.
	}

	@Override
	public void execute(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
		object = getClosestAvailableTree();
		if (object != null && object.interact("Chop down")) {
			methodProvider.log("Successfully interacted with object");
			Timing.waitCondition(() -> !object.exists() || methodProvider.myPlayer().isAnimating(), 2500);
		} else {
			methodProvider.log("We failed to interact with object");
		}

	}

	/*
	 * Filter that sorts out objects that are available
	 */

	Filter<RS2Object> appropriateObjectFilter = new Filter<RS2Object>() {
		@Override
		public boolean match(RS2Object o) {
			return o.getName().equals(treeName) && o.hasAction("Chop down");
		}
	};

	public RS2Object getClosestAvailableTree() {
		return methodProvider.objects.closest(appropriateObjectFilter);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Cut";
	}

	public Node setTreeName(String name) {
		this.treeName = name;
		return this;
	}

}
