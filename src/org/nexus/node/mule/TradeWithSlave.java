package org.nexus.node.mule;

import org.nexus.NexusScript;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.listener.MessageListener;
import org.osbot.rs07.script.MethodProvider;

public class TradeWithSlave extends Trade implements MessageListener {

	@Override
	public boolean shallExecute(MethodProvider methodProvider){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute(MethodProvider methodProvider) {
		initValues();
		if (!methodProvider.getTrade().isCurrentlyTrading()) {
			initiateTrade(methodProvider, playerToTrade);
		} else if (methodProvider.trade.isSecondInterfaceOpen()) {
			acceptFirstScreen(methodProvider);
		} else if (!firstTradeScreenIsOkay(methodProvider, itemID, itemAmount)) {
			methodProvider.log("lets add item");
			methodProvider.log("item id:" + itemID);
			addItemToTrade(methodProvider, itemID, itemAmount);
		} else if (methodProvider.trade.isFirstInterfaceOpen()) {
			acceptFirstScreen(methodProvider);
		}

	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Trade with Slave";
	}
	
	@Override
	public void onMessage(Message message) throws InterruptedException {
		String txt = message.getMessage().toLowerCase();

		if (txt.contains("accepted")) {
			NexusScript.currentTask = null;
		}
		
	}

}
