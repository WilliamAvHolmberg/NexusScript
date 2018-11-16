package org.nexus.node.combat;

import org.nexus.node.Node;
import org.nexus.objects.RSItem;
import org.nexus.utils.Timing;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class Eat extends Node {

	private MethodProvider methodProvider;
	private RSItem food;

	@Override
	public boolean shallExecute(MethodProvider methodProvider) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
		if (methodProvider.widgets.closeOpenInterface()) {
			int healthBeforeEat = methodProvider.skills.getDynamic(Skill.HITPOINTS);
			methodProvider.log("lets eat");
			methodProvider.inventory.interact("Eat", food.getName());
			Timing.waitCondition(() -> methodProvider.skills.getDynamic(Skill.HITPOINTS) >= healthBeforeEat + 1, 2500);
		}
	}

	public static int getCurrentHealth(MethodProvider methodProvider) {
		return methodProvider.skills.getDynamic(Skill.HITPOINTS);
	}

	public int getCurrentHealth() {
		return methodProvider.skills.getDynamic(Skill.HITPOINTS);
	}

	public static boolean shallEat(MethodProvider methodProvider) {
		return getCurrentHealth(methodProvider) < getStaticHealth(methodProvider) / 2;
	}

	private static int getStaticHealth(MethodProvider methodProvider) {
		return methodProvider.skills.getStatic(Skill.HITPOINTS);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Eat";
	}

	public Eat setFood(RSItem food) {
		this.food = food;
		return this;
	}

}
