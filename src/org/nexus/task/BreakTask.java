package org.nexus.task;

import java.awt.Graphics2D;
import java.util.function.BooleanSupplier;

import org.nexus.NexusScript;
import org.nexus.handler.gear.Gear;
import org.nexus.handler.gear.Inventory;
import org.nexus.loot.LootHandler;
import org.nexus.objects.RSItem;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.ui.Skill;

public class BreakTask extends Task {

	@Override
	public void onPaint(Graphics2D g) {
		g.drawString("Current Task: break. We are waiting for our next task.", 350, 50);
		if (getBreakType() != null && getBreakAfter() > 0) {
			g.drawString("Break is done in: " + getTimeLeft() + " minutes", 350, 125);
		}
	}

}
