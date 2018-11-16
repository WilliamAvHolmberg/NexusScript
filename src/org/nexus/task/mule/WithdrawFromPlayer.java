package org.nexus.task.mule;

import java.awt.Graphics2D;
import java.util.function.BooleanSupplier;

import org.nexus.objects.RSItem;
import org.nexus.task.Task;
import org.nexus.task.TaskType;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.MethodProvider;

public class WithdrawFromPlayer extends Mule {

	
	public WithdrawFromPlayer(int world, int itemID, int itemAmount, int startAmount, String tradeName) {
		super(world, itemID, itemAmount, startAmount, tradeName);
		setTaskType(TaskType.WITHDRAW_ITEM_FROM_MULE);
	}
	
	@Override
	public boolean isCompleted(MethodProvider methodProvider) {
		return methodProvider.inventory.getAmount(itemID) >= itemAmount + startAmount;
	}

	@Override
	public void onPaint(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}
}
