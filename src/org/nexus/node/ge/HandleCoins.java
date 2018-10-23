package org.nexus.node.ge;

import org.nexus.node.Node;
import org.nexus.utils.Timing;
import org.osbot.rs07.script.MethodProvider;

public class HandleCoins extends Node {

	@Override
	public boolean shallExecute(MethodProvider methodProvider) {
		return !methodProvider.inventory.onlyContains(995) || !methodProvider.inventory.contains(995);
	}

	@Override
	public void execute(MethodProvider methodProvider) {
		if (methodProvider.bank.isOpen()) {
			if(!methodProvider.inventory.onlyContains(995)) {
			methodProvider.log("lets deposit all except coins");
			methodProvider.bank.depositAllExcept(995);
			Timing.waitCondition(() -> methodProvider.inventory.onlyContains(995), 2000);
			}else if(!methodProvider.inventory.contains(995)) {
				methodProvider.log("lets withdraw coins");
				methodProvider.bank.withdrawAll(995);
				Timing.waitCondition(() -> methodProvider.inventory.onlyContains(995), 2000);
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
