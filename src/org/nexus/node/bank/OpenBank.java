package org.nexus.node.bank;

import org.nexus.NexusScript;
import org.nexus.node.Node;
import org.osbot.rs07.script.MethodProvider;
import org.nexus.utils.Timing;

public class OpenBank extends Node{

	@Override
	public boolean shallExecute(MethodProvider methodProvider) {
		return NexusScript.nodeHandler.getBankArea().contains(methodProvider.myPosition()) && !methodProvider.bank.isOpen();
	}

	@Override
	public void execute(MethodProvider methodProvider) {
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
