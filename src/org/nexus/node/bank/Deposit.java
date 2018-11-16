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
	private List<Integer> itemsToKeep;
	private int[] stockArr;
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
		itemsToKeep = depositItem.getItems();
		try {
			if (methodProvider.grandExchange.close() && methodProvider.bank.open()) {
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
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void depositAllExcept(List<Integer> items) {
		methodProvider.log("lets deposaaait");
		if (items != null && !items.isEmpty()) {
			stockArr = items.stream().mapToInt(i->i).toArray();
			methodProvider.bank.depositAllExcept(stockArr);
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
