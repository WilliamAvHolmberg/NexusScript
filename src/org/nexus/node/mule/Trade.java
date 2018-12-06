package org.nexus.node.mule;

import org.nexus.NexusScript;
import org.nexus.node.Node;
import org.nexus.task.mule.Mule;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.script.MethodProvider;

import org.nexus.utils.Timing;

public abstract class Trade extends Node{

	protected String playerToTrade;
	protected int itemID;
	protected int itemAmount;



	public void initValues() {
		playerToTrade = ((Mule) NexusScript.currentTask).getMuleName();
		itemID = ((Mule) NexusScript.currentTask).getItemID();
		itemAmount = ((Mule) NexusScript.currentTask).getItemAmount();
	}
	
	

	public void initiateTrade(MethodProvider methodProvider, String name) {
		Player target = getPlayer(methodProvider, name);
		if(target != null) {
			target.interact("Trade with");
			Timing.waitCondition(() -> methodProvider.trade.isCurrentlyTrading(), 25000);
		}
		
	}
	
	public boolean firstTradeScreenIsOkay(MethodProvider methodProvider, int itemID, int itemAmount) {
		if(methodProvider.getTrade().getTheirOffers().getAmount(itemID) >= itemAmount || methodProvider.getTrade().getOurOffers().getAmount(itemID) >= itemAmount ) {
			return true;
		}
		return false;
	}
	
	public void acceptFirstScreen(MethodProvider methodProvider) {
			methodProvider.trade.acceptTrade();
			Timing.waitCondition(() -> !methodProvider.trade.isFirstInterfaceOpen(), 7500);
	}
	
	public void addItemToTrade(MethodProvider methodProvider, int itemID, int itemAmount) {
		if(methodProvider.trade.isFirstInterfaceOpen()) {
			methodProvider.getTrade().offer(itemID, itemAmount);
			Timing.waitCondition(() -> methodProvider.getTrade().getOurOffers().getAmount(995) >= itemAmount, 7500);
		}
	}
	
	
	public Player getPlayer(MethodProvider methodProvider, String name) {
		for(Player player : methodProvider.players.getAll()) {
			if(player.getName().toLowerCase().equals(name.toLowerCase())) {
				return player;
			}
		}
		return null;
	}




}
