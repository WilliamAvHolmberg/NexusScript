package org.nexus.task.mule;

import java.awt.Graphics2D;

import org.nexus.task.ActionTask;
import org.nexus.task.TaskType;
import org.osbot.rs07.script.MethodProvider;

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
