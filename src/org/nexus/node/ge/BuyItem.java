package org.nexus.node.ge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.nexus.node.Node;
import org.nexus.node.bank.GrandExchangeHandler;
import org.nexus.utils.Timing;
import org.osbot.rs07.api.GrandExchange;
import org.osbot.rs07.api.GrandExchange.Box;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.input.mouse.WidgetDestination;

public class BuyItem extends Node {

	private int itemID;
	private int itemAmount;
	private List<Box> boxes;
	private HashMap<Box,Integer[]> hash = new HashMap<Box,Integer[]>();

	@Override
	public boolean shallExecute() {
		return methodProvider.inventory.onlyContains(995);
	}

	@Override
	public void execute() {
		hash.put(Box.BOX_1, new Integer[]{465,7});
		itemID = GrandExchangeHandler.getItem().getItemID();
		itemAmount = GrandExchangeHandler.getItem().getAmount();
		methodProvider.log("lets buy item:" + itemID);
		if(openGE()) {
			methodProvider.log("ge is open");
			boxes = getBoxes();
			//if offer exists for item
			if(boxes.isEmpty()) {
				methodProvider.log("lets create new buy offer");
			}else {
				//if offer has not been completed, abort offer
				//if offer has been completed, claim items
				for(Box box : boxes) {
					methodProvider.log(methodProvider.grandExchange.getStatus(box));
					switch(methodProvider.grandExchange.getStatus(box)) {
					case CANCELLING_BUY:
						break;
					case CANCELLING_SALE:
						break;
					case COMPLETING_BUY:
						break;
					case COMPLETING_SALE:
						break;
					case EMPTY:
						break;
					case FINISHED_BUY:
						methodProvider.log("Finished buying item, lets claim: " + box);
						methodProvider.grandExchange.collect();
						break;
					case FINISHED_SALE:
						break;
					case INITIALIZING_BUY:
						break;
					case INITIALIZING_SALE:
						break;
					case PENDING_BUY:
						methodProvider.log("Trying to buy item, lets abandon: " + box);
						RS2Widget widget = methodProvider.widgets.get(hash.get(box)[0], hash.get(box)[1]);
						widget.interact("Abort offer");
						//sleep until offer is aborted
						
						break;
					default:
						break;
					
					}
				}
			}
		}
	}
	
	 private List<Box> getBoxes() {
		boxes = new ArrayList<Box>();
		for(Box box: GrandExchange.Box.values()){
			if(methodProvider.grandExchange.getItemId(box) == itemID) {
				boxes.add(box);
			}
		}
		return boxes;
	}

	private boolean openGE() {
	        if(!methodProvider.grandExchange.isOpen()){
	            NPC grandExchangeClerk = methodProvider.getNpcs().closest("Grand Exchange Clerk");
	            if(grandExchangeClerk != null){
	                boolean didInteraction = grandExchangeClerk.interact("Exchange");
	                Timing.waitCondition(() -> methodProvider.grandExchange.isOpen(), 3000);
	                return openGE();
	            }
	        }
	        return true;
	    }

	@Override
	public String toString() {
		return "Buy Item";
	}

}
