package org.nexus.node.mule;

import org.nexus.NexusScript;
import org.nexus.communication.NexHelper;
import org.nexus.communication.message.request.MuleRequest;
import org.nexus.node.Node;
import org.nexus.task.TaskType;
import org.nexus.task.mule.Mule;
import org.nexus.utils.Timing;
import org.osbot.rs07.script.MethodProvider;

public class PrepareForMuleDeposit extends Node {

	Mule currentTask;
	@Override
	public boolean shallExecute(MethodProvider methodProvider) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute(MethodProvider methodProvider) {
		if(!currentTask.soldItems()) {
			methodProvider.log("lets sell items");
		}
		else if (!currentTask.getBanked()) {
			withdrawMoney(methodProvider);
		} else {
			NexHelper.messageQueue.push(new MuleRequest(methodProvider, NexHelper.messageQueue,
					"MULE_DEPOSIT:995:" + methodProvider.inventory.getAmount(995)));
			Timing.waitCondition(() -> NexusScript.currentTask.getTaskType() == TaskType.DEPOSIT_ITEM_TO_PLAYER, 15000);
		}

	}

	private void withdrawMoney(MethodProvider methodProvider) {
		try {
			if (methodProvider.bank.open()) {
				if (methodProvider.bank.getAmount(995) == 0) {
					currentTask.setBanked(true);
				} else {
					methodProvider.bank.withdrawAll(995);
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	public Node setTask(Mule task) {
		currentTask = task;
		return this;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
