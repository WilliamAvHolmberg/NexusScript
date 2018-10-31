package org.nexus.node.bank;

import java.util.ArrayList;
import java.util.List;

import org.nexus.NexusScript;
import org.nexus.handler.BankHandler;
import org.nexus.node.Node;
import org.nexus.objects.DepositItem;
import org.nexus.objects.GEItem;
import org.nexus.objects.WithdrawItem;
import org.nexus.utils.Timing;
import org.osbot.rs07.api.ui.EquipmentSlot;
import org.osbot.rs07.script.MethodProvider;

public class Deposit extends Node {
	private DepositItem depositItem;
	private List<String> itemsToKeep;
	private String[] stockArr;
	private MethodProvider methodProvider;
	private boolean add = false;

	@Override
	public boolean shallExecute(MethodProvider methodProvider) {
		return NexusScript.nodeHandler.getBankArea().contains(methodProvider.myPosition())
				&& methodProvider.bank.isOpen();
	}

	@Override
	public void execute(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
		depositItem = BankHandler.getDepositItem();
		itemsToKeep = depositItem.getItems();
		stockArr = new String[itemsToKeep.size()];
		switch (depositItem.getType()) {
		case DEPOSIT_ALL:
			itemsToKeep = null;
			depositAll();
			break;
		case DEPOSIT_ALL_EXCEPT:
			depositAllExcept(itemsToKeep);	
		default:
			break;

		}
	}

	private void depositAllExcept(List<String> items) {
		methodProvider.log("lets deposaaait");
		if (items != null && !items.isEmpty()) {
			methodProvider.bank.depositAllExcept(items.toArray(stockArr));
		} else {
			methodProvider.bank.depositAll();
		}

	}

	private void depositAll() {
		methodProvider.bank.depositAll();
	}

	@Override
	public String toString() {
		return "Deposit to Bank";
	}

	public Node setItem(DepositItem depositItem) {
		this.depositItem = depositItem;
		return this;
	}

}
