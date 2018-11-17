package org.nexus.node.mule;

import java.util.ArrayList;

import org.nexus.NexusScript;
import org.nexus.handler.grandexchange.SellItemHandler;
import org.nexus.node.Node;
import org.nexus.objects.GESellItem;
import org.nexus.task.Task;
import org.nexus.task.mule.DepositToMule;
import org.nexus.utils.Timing;
import org.nexus.utils.grandexchange.Exchange;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.script.MethodProvider;

public class CheckIfWeShallSellItems extends Node{

	//used to check when last time we checked items was
	public static long last_check = 0;
	private Task currentTask;
	@Override
	public boolean shallExecute(MethodProvider methodProvider) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute(MethodProvider methodProvider) {
		currentTask = NexusScript.currentTask;
		try {
			if(methodProvider.bank.open()) {
				ArrayList<GESellItem> itemsToSell = new ArrayList<GESellItem>();
				int totalValue = 0;
				for(Item item : methodProvider.bank.getItems()) {
					if(item != null && item.getId() != 995) {
					int value = item.getAmount() * Exchange.getPrice(item.getId(), methodProvider);
					if(value > 1000 && (currentTask == null || !currentTask.getRequiredItems().contains(item.getId()))) {
						itemsToSell.add(new GESellItem(item.getId(), item.getName(), item.getAmount(), (int) (value * 0.6)));
						totalValue += value;
					}
					}
				
				}
				if(totalValue > NexusScript.mule_threshold) {
					methodProvider.log("lets sell!");
					itemsToSell.forEach(item -> {
						SellItemHandler.addItem(item);
					});
					NexusScript.currentTask = new DepositToMule();
				}else {
					methodProvider.log("lets not sell. Remove deposit to mule");
				}
				methodProvider.log("total value: " + totalValue);
				last_check = System.currentTimeMillis();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	public static long getNextCheckInMilli() {
		return ((last_check + 3600 * 1000) );

	}
	
	public static long getTimeTilNextCheckInMinutes() {
		return (getNextCheckInMilli() - System.currentTimeMillis())/60000;
	}
	
	public Node setTask(Task task) {
		this.currentTask = task;
		return this;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
