package org.nexus.node.combat;

import java.util.Arrays;
import java.util.List;

import org.nexus.node.Node;
import org.nexus.node.combat.methods.AttackStyle;
import org.nexus.task.CombatTask;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;

import org.nexus.utils.Timing;

public class Fight extends Node {

	MethodProvider methodProvider;
	NPC target;
	private String targetName;
	private AttackStyle attackStyle = new AttackStyle();
	private CombatTask combatTask;

	@Override
	public boolean shallExecute(MethodProvider methodProvider) {
		// TODO invnetory or equipment contains axe
		return !methodProvider.myPlayer().isAnimating(); // hard coded atm TODO later.
	}

	@Override
	public void execute(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
		methodProvider.log("in fight");
		if (combatTask != null && !attackStyle.rightStyle(combatTask.getSkill(), methodProvider)) {
			attackStyle.changeStyle(combatTask.getSkill(), methodProvider);
		} else if (playerIsAttacking() && interactingNpcIsAvailable()) {
			methodProvider.log("We are already attacking");
			combatSleep();
			// sleep untill drop has been dropped
			Timing.sleep(1200);
		} else if (underAttack() && getInteractingNPC() != null && !playerIsAttacking()
				&& getInteractingNPC().hasAction("Attack")) {
			// attack entity that is attacking us
			methodProvider.log("We are under attack and not fighting back");
			attackExistingTarget();
		} else {
			// find new target and attack
			methodProvider.log("lets atk");
			attackNewTarget();
		}
	}

	public boolean interactingNpcIsAvailable() {
		target = (NPC) methodProvider.myPlayer().getInteracting();
		// return false if target does not exist.
		// If we are not interacting an npc, then we are not attacking an npc.
		if (target == null) {
			return false;
		}
		// return false if the target that we are trying to attack is
		// interacting with another player
		// We can not attack the npc if it is already in combat with someone
		// else
		else if (target.getInteracting() != methodProvider.myPlayer()) {
			return false;
		}
		return true;
	}

	/**
	 * This method finds the closest NPC and then attacks it Script is put to sleep
	 * as long as player is still moving, still attacking or target has died
	 */
	public void attackNewTarget() {
		methodProvider.log("lets attack a new target");
		target = getTarget();
		if (target != null && !methodProvider.map.canReach(target)) {
			methodProvider.getWalking().webWalk(target.getPosition());
		}else if (target != null && target.interact("Attack")) {
			methodProvider.log("lets sleep");
			// combatSleep.sleep();
			// Sleep until player is interacting npc or npc is interacting
			// someone else than our player
			// perhaps change so target.isUnderAttack?
			Timing.waitCondition(() -> target.isUnderAttack(), 150, 5000);
		} else if (target != null && !target.isOnScreen()) {
			methodProvider.walking.webWalk(target.getPosition());
		} else {
			methodProvider.log("out of range or null");
		}
	}

	/**
	 * This method finds the current target that is attacking our NPC and then
	 * attacks it Script is put to sleep as long as player is still moving, still
	 * attacking or target has died
	 */
	public void attackExistingTarget() {
		methodProvider.log("lets attack the current target");
		target = getInteractingNPC();
		if (target != null && target.interact("Attack")) {
			methodProvider.log("lets sleep");
			// Sleep a second after click to not spam click
			Timing.sleep(1000);
			combatSleep();
		}
	}

	/**
	 * Shall break combat sleep if player is not attacking npc is null npc hp is 0
	 * have to eat
	 */
	public void combatSleep() {
		Timing.waitCondition(() -> (Eat.shallEat(methodProvider) || !playerIsAttacking()) || getInteractingNPC() == null
				|| getInteractingNPC().getHealthPercent() == 0, 3000);
	}

	/**
	 * @return if player is under attack
	 */
	public boolean underAttack() {
		return methodProvider.myPlayer().isUnderAttack();
	}

	public NPC getInteractingNPC() {
		return methodProvider.getNpcs().closest(npc -> npc.isInteracting(methodProvider.myPlayer()));
	}

	/**
	 * 
	 * @return if player is attacking
	 */
	public boolean playerIsAttacking() {
		return methodProvider.combat.isFighting();
	}

	/*
	 * Filter that sorts out objects that are available
	 */

	public NPC getTarget() {
		if (methodProvider.myPlayer().isUnderAttack()) {
			methodProvider.log("we already have a target");
			return methodProvider.npcs.closest(attackedByFilter);
		}
		methodProvider.log("we need a new target");
		return methodProvider.npcs.closest(appropriateTargetFilter);

	}

	/*
	 * Filter that sorts out all npcs that are attacking or trying to attack our
	 * player
	 */
	Filter<NPC> attackedByFilter = new Filter<NPC>() {
		@Override
		public boolean match(NPC n) {
			return n.isInteracting(methodProvider.myPlayer());
		}
	};
	Filter<NPC> appropriateTargetFilter = new Filter<NPC>() {
		@Override
		public boolean match(NPC n) {
			return combatTask.getActionArea().contains(n) && targetName.equals(n.getName()) && n.isAttackable() && n.getHealthPercent() >= 50;
		}
	};

	public List<NPC> getInteractingNPCs() {
		return methodProvider.npcs.filter(attackedByFilter);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Attack NPC";
	}

	public Node setCombatTask(CombatTask task) {
		this.combatTask = task;
		this.targetName = task.getMonsterName();
		return this;
	}

}
