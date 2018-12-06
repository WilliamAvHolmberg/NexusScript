package org.nexus.node.ge;

import org.nexus.NexusScript;
import org.nexus.communication.NexHelper;
import org.nexus.communication.message.request.MuleRequest;
import org.nexus.handler.grandexchange.BuyItemHandler;
import org.nexus.node.Node;
import org.nexus.task.TaskType;
import org.osbot.rs07.script.MethodProvider;

import org.nexus.utils.Timing;
public class HandleCoins extends Node {

	@Override
	public boolean shallExecute(MethodProvider methodProvider) {
		return !methodProvider.inventory.onlyContains(995) || !methodProvider.inventory.contains(995);
	}

	@Override
	public void execute(MethodProvider methodProvider) {
		if (methodProvider.bank.isOpen()) {
			if(!methodProvider.inventory.isEmptyExcept(995)) {
			methodProvider.log("lets deposit all except coins");
			methodProvider.bank.depositAllExcept(995);
			Timing.waitCondition(() -> methodProvider.inventory.onlyContains(995), 2000);
			}else if(!methodProvider.inventory.contains(995) && methodProvider.bank.getAmount(995) >= 1.5*BuyItemHandler.getItem().getAmount() * BuyItemHandler.getItem().getItemPrice()) {
				methodProvider.log("lets withdraw coins");
				methodProvider.bank.withdrawAll(995);
				Timing.waitCondition(() -> methodProvider.inventory.onlyContains(995), 2000);
			}else {
				NexHelper.messageQueue.push(new MuleRequest(methodProvider, NexHelper.messageQueue, "MULE_WITHDRAW:995:" + (int) 1.5 *BuyItemHandler.getItem().getAmount() * BuyItemHandler.getItem().getItemPrice()));
				methodProvider.log("sleep until we got new task");
				Timing.waitCondition(() -> NexusScript.currentTask.getTaskType() == TaskType.WITHDRAW_ITEM_FROM_MULE, 15000);
			}
		}else {
			try {
				methodProvider.bank.open();
				Timing.waitCondition(() -> methodProvider.bank.isOpen(), 2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String toString() {
		return "Deposit All But Coins";
	}

}
