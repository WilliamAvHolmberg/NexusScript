package org.nexus.node.bank;

import java.util.List;

import org.nexus.NexusScript;
import org.nexus.handler.BankHandler;
import org.nexus.node.Node;
import org.nexus.objects.DepositItem;
import org.nexus.objects.GEItem;
import org.nexus.objects.WithdrawItem;
import org.nexus.utils.Timing;
import org.osbot.rs07.script.MethodProvider;

public class Deposit extends Node {
	private DepositItem depositItem;
	private List<String> itemsToKeep;
	private String[] stockArr;
	private MethodProvider methodProvider;
	@Override
	public boolean shallExecute(MethodProvider methodProvider) {
		return NexusScript.nodeHandler.getBankArea().contains(methodProvider.myPosition()) &&methodProvider.bank.isOpen();
	}

	@Override
	public void execute(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
		depositItem = BankHandler.getDepositItem();
		switch(depositItem.getType()) {
		case DEPOSIT_ALL:
			itemsToKeep = null;
			depositAll();
			break;
		case DEPOSIT_ALL_EXCEPT:
			itemsToKeep = depositItem.getItems();
			stockArr = new String[itemsToKeep.size()];
			depositAllExcept(itemsToKeep);
			break;
		default:
			break;
		
		}
	}

	private void depositAllExcept(List<String> itemsToKeep) {
		if(itemsToKeep != null && !itemsToKeep.isEmpty()) {
		methodProvider.bank.depositAllExcept(itemsToKeep.toArray(stockArr));
		}
		
	}

	private void depositAll() {
		methodProvider.bank.depositAll();
	}

	@Override
	public String toString() {
		return "Withdraw From Bank";
	}

	public Node setItem(DepositItem depositItem) {
		this.depositItem = depositItem;
		return this;
	}

}
