package org.nexus.node.bank;

import org.nexus.NexusScript;
import org.nexus.node.Node;
import org.nexus.utils.Timing;

public class OpenBank extends Node{

	@Override
	public boolean shallExecute() {
		return NexusScript.nodeHandler.getBankArea().contains(methodProvider.myPosition()) && !methodProvider.bank.isOpen();
	}

	@Override
	public void execute() {
		methodProvider.log("lets open bank");
		try {
			methodProvider.bank.open();
			Timing.waitCondition(() -> methodProvider.bank.isOpen(), 2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "Open Bank";
	}

}
