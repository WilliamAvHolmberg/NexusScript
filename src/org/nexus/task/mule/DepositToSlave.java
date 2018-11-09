package org.nexus.task.mule;

import java.util.function.BooleanSupplier;

import org.nexus.objects.RSItem;
import org.nexus.task.Task;
import org.nexus.task.TaskType;
import org.osbot.rs07.api.map.Area;

public class DepositToSlave extends Mule {

	
	public DepositToSlave(int world, int itemID, int itemAmount, int startAmount, String tradeName) {
		super(world, itemID, itemAmount, startAmount, tradeName);
		setTaskType(TaskType.DEPOSIT_TO_SLAVE);
	}
}
