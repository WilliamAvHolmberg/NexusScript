package org.nexus.node.mule;

import java.util.ArrayList;

import org.nexus.handler.grandexchange.SellItemHandler;
import org.nexus.node.Node;
import org.nexus.objects.GESellItem;
import org.nexus.utils.Timing;
import org.nexus.utils.grandexchange.Exchange;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.script.MethodProvider;

public class CheckIfWeShallSellItems extends Node{

	//used to check when last time we checked items was
	public static int last_check = 0;
	@Override
	public boolean shallExecute(MethodProvider methodProvider) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute(MethodProvider methodProvider) {
		try {
			if(methodProvider.bank.open()) {
				ArrayList<GESellItem> itemsToSell = new ArrayList<GESellItem>();
				int totalValue = 0;
				for(Item item : methodProvider.bank.getItems()) {
					int value = item.getAmount() * Exchange.getPrice(item.getId(), methodProvider);
					if(value > 1000) {
						methodProvider.log("item: " + item.getName() + "   amount: " + item.getAmount());
						methodProvider.log("item_id: " + item.getId() + "   amount: " + item.getAmount());
						methodProvider.log("value " + value);
						methodProvider.log("lets sell item!");
						itemsToSell.add(new GESellItem(item.getId(), item.getName(), item.getAmount(), (int) (value * 0.6)));
						totalValue += value;
					}
				
				}
				if(totalValue > 10000) {
					methodProvider.log("lets sell!");
					itemsToSell.forEach(item -> {
						SellItemHandler.addItem(item);
					});
				}else {
					methodProvider.log("lets not sell. Remove deposit to mule");
				}
				methodProvider.log("total value: " + totalValue);

			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
