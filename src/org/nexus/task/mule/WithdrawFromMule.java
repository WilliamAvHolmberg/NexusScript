package org.nexus.task.mule;

import java.util.function.BooleanSupplier;

import org.nexus.objects.RSItem;
import org.nexus.task.Task;
import org.nexus.task.TaskType;
import org.osbot.rs07.api.map.Area;

public class WithdrawFromMule extends Mule {

	
	public WithdrawFromMule(int world, int itemID, int itemAmount, int startAmount, String tradeName) {
		super(world, itemID, itemAmount, startAmount, tradeName);
		setTaskType(TaskType.WITHDRAW_FROM_MULE);
	}
}
