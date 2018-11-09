package org.nexus.node.mule;

import org.nexus.NexusScript;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.listener.MessageListener;
import org.osbot.rs07.script.MethodProvider;

public class TradeWithMule extends Trade{

	@Override
	public boolean shallExecute(MethodProvider methodProvider) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute(MethodProvider methodProvider) {
		initValues();
		if(!methodProvider.getTrade().isCurrentlyTrading()) {
			initiateTrade(methodProvider, playerToTrade);
		}else if(firstTradeScreenIsOkay(methodProvider, itemID, itemAmount)) {
			acceptFirstScreen(methodProvider);
		}else if(methodProvider.trade.isSecondInterfaceOpen()) {
			acceptFirstScreen(methodProvider);
		}else {
			methodProvider.log("Waiting for mule to do move.");
		}
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Trade with Mule";
	}



}
