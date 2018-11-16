package org.nexus.task.mule;

import java.awt.Graphics2D;

import org.nexus.task.TaskType;

public class DepositToPlayer extends Mule {
	public DepositToPlayer(int world, int itemID, int itemAmount, int startAmount, String tradeName) {
		super(world, itemID, itemAmount, startAmount, tradeName);
		setTaskType(TaskType.DEPOSIT_ITEM_TO_PLAYER);
	}

	@Override
	public void onPaint(Graphics2D g) {
		g.drawString("Trade to mule: " + tradeName, 350,150);
		
	}
}
