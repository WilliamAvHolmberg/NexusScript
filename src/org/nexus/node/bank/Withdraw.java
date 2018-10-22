package org.nexus.node.bank;

import org.nexus.node.Node;
import org.nexus.utils.Timing;

public class Withdraw extends Node {

	private WithdrawItem item;
	int amountRequired;
	int bankAmount;
	int amountRequiredFromGE;
	int invAmountPreWithdraw;
	@Override
	public boolean shallExecute() {
		return methodProvider.bank.isOpen();
	}

	@Override
	public void execute() {
		item = BankHandler.getItem();
		invAmountPreWithdraw = (int) methodProvider.inventory.getAmount(item.getItemID());
		amountRequired = item.getAmount() - invAmountPreWithdraw;
		bankAmount = (int) methodProvider.bank.getAmount(item.getItemID());
		methodProvider.log("Amount of items required:" + amountRequired);
		methodProvider.log("Amount of items in Bank:" + bankAmount);
		methodProvider.log("Amount of items in inventory pre withdraw:" + invAmountPreWithdraw);

		if(bankAmount < amountRequired) {
			amountRequiredFromGE = amountRequired - bankAmount;
			GrandExchangeHandler.addItem(new GEItem(item.getItemID(), amountRequiredFromGE));
			methodProvider.log("Bank does not contain the required amount of our item. Buy:" + amountRequiredFromGE);
		}else {
			methodProvider.bank.withdraw(item.getItemID(), amountRequired);
			Timing.waitCondition(() ->methodProvider.inventory.getAmount(item.getItemID()) - invAmountPreWithdraw == amountRequired, 3000); 
		}
		
	}

	@Override
	public String toString() {
		return "Withdraw From Bank";
	}

}
