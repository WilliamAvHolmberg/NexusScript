package org.nexus.node.woodcutting;

import java.util.Arrays;
import java.util.List;

import org.nexus.node.Node;
import org.nexus.task.WoodcuttingTask;
import org.nexus.utils.Timing;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;


public class Cut extends Node {

	MethodProvider methodProvider;
	RS2Object object;
	private WoodcuttingTask wcTask;

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
			return wcTask.getActionArea().contains(o) && o.getName().equals(wcTask.getTreeName()) && o.hasAction("Chop down");
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

	public Node setTask(WoodcuttingTask task) {
		this.wcTask = task;
		return this;
	}

}
