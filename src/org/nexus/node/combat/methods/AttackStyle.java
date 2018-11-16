package org.nexus.node.combat.methods;

import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class AttackStyle {
	
	
	/**
	 * @return current attack style. 1 == STRENGTH, 0 == ATTACK, 3 == DEFENCE
	 */
	public int attackStyle(MethodProvider methodProvider) {
		return methodProvider.configs.get(43);
	}
	
	public boolean rightStyle(Skill skill, MethodProvider methodProvider ) {
		if (skill.equals(Skill.STRENGTH) && (attackStyle(methodProvider) != 1)) {
			return false;
		} else if (skill.equals(Skill.ATTACK) && (attackStyle(methodProvider) != 0)) {
			return false;
		} else if (skill.equals(Skill.DEFENCE) && (attackStyle(methodProvider) != 3)) {
			return false;
		} else if (skill.equals(Skill.RANGED) && (attackStyle(methodProvider) != 1)) {
			return false;
		}
		return true;
	}

	/**
	 * Widget(593) is for the attack style/combat widget This method should
	 * change attack style to suite the preferred skill
	 */
	public void changeStyle(Skill skill,MethodProvider methodProvider ) {
		if (methodProvider.widgets.isVisible(593)) {
			if (skill.equals(Skill.STRENGTH) && (attackStyle(methodProvider) != 1)) {
				methodProvider.mouse.click(689, 270, false);// click "train
															// strength"
			} else if (skill.equals(Skill.ATTACK) && (attackStyle(methodProvider) != 0)) {
				methodProvider.mouse.click(601, 269, false); // click "train
																// attack"
			} else if (skill.equals(Skill.DEFENCE) && (attackStyle(methodProvider) != 3)) {
				// Check if wearing weapon
				// if not wearing weapon, we press other button in attack styles
				// since there is only three alternatives then
				if (methodProvider.equipment.getItemInSlot(EquipmentSlot.WEAPON.slot) != null) {
					methodProvider.mouse.click(701, 335, false); // click "train
																	// defence"
				} else {
					methodProvider.mouse.click(599, 324, false); // click "train
																	// defence"
				}
				// def"
			} else if (skill.equals(Skill.RANGED) && (attackStyle(methodProvider) != 1)) {
				methodProvider.mouse.click(689, 270, false);// click "train
															// range"
			}

		} else {
			methodProvider.mouse.click(545, 190, false);
		}

	}
}
