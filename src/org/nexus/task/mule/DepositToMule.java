package org.nexus.task.mule;

import java.awt.Graphics2D;

import org.nexus.task.Task;
import org.nexus.task.TaskType;

public class DepositToMule extends Mule {
	public DepositToMule() {
		super(0, 0, 0, 0, null);
		setTaskType(TaskType.PREPARE_FOR_MULE_DEPOSIT);
		this.setTimeStartedMilli(System.currentTimeMillis());
		this.setBreakAfter(10);
	}

	@Override
	public void onPaint(Graphics2D g) {
		g.drawString("Preparing for mule deposit: ", 350,150);
		
	}
}
