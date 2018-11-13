package org.nexus.node.bank;

import org.nexus.NexusScript;
import org.nexus.communication.NexHelper;
import org.nexus.communication.message.request.MuleRequest;
import org.nexus.handler.BankHandler;
import org.nexus.handler.GrandExchangeHandler;
import org.nexus.node.Node;
import org.nexus.objects.GEItem;
import org.nexus.objects.WithdrawItem;
import org.nexus.task.TaskType;
import org.nexus.utils.Timing;
import org.osbot.rs07.script.MethodProvider;

public class Withdraw extends Node {

	private WithdrawItem item;
	int amountRequired;
	int bankAmount;
	int amountRequiredFromGE;
	int invAmountPreWithdraw;
	@Override
	public boolean shallExecute(MethodProvider methodProvider) {
		return NexusScript.nodeHandler.getBankArea().contains(methodProvider.myPosition()) && methodProvider.bank.isOpen();
	}

	@Override
	public void execute(MethodProvider methodProvider) {
		item = BankHandler.getWithdrawItem();
		invAmountPreWithdraw = (int) methodProvider.inventory.getAmount(item.getItemID());
		amountRequired = item.getAmount() - invAmountPreWithdraw;
		bankAmount = (int) methodProvider.bank.getAmount(item.getItemID());
		methodProvider.log("Amount of items required:" + amountRequired);
		methodProvider.log("Amount of items in Bank:" + bankAmount);
		methodProvider.log("Amount of items in inventory pre withdraw:" + invAmountPreWithdraw);

		if(bankAmount < amountRequired) {
			amountRequiredFromGE = amountRequired - bankAmount;
			handleBankDoesNotContainItem(methodProvider, item, amountRequiredFromGE);
		}else {
			methodProvider.bank.withdraw(item.getItemID(), amountRequired);
			Timing.waitCondition(() ->methodProvider.inventory.getAmount(item.getItemID()) - invAmountPreWithdraw == amountRequired, 3000); 
		}
		
	}

	private void handleBankDoesNotContainItem(MethodProvider methodProvider, WithdrawItem item, int amountRequiredFromGE) {
		if(item.getItemID() == 995) {
			NexHelper.messageQueue.push(new MuleRequest(methodProvider, NexHelper.messageQueue, "MULE_WITHDRAW:995:" + amountRequiredFromGE));
			methodProvider.log("sleep until we got new task");
			Timing.waitCondition(() -> NexusScript.currentTask.getTaskType() == TaskType.WITHDRAW_FROM_MULE, 15000);
		}else {
			GrandExchangeHandler.addItem(new GEItem(item.getItemID(), amountRequiredFromGE, item.getItemName()));
			methodProvider.log("Bank does not contain the required amount of our item. Buy:" + amountRequiredFromGE);
		}
		
	}

	@Override
	public String toString() {
		return "Withdraw From Bank";
	}

	public Node setItem(WithdrawItem withdrawItem) {
		this.item = withdrawItem;
		return this;
	}

}
