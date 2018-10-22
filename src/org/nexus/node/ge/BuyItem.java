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
	private String itemName;
	private int itemPrice;
	
	private List<Box> relevantBoxes;
	private List<Box> availableBoxes;
	private HashMap<Box, Integer[]> hash = new HashMap<Box, Integer[]>();
	private Box boxToUse;

	@Override
	public boolean shallExecute() {
		return methodProvider.inventory.onlyContains(995);
	}

	@Override
	public void execute() {
		hash.put(Box.BOX_1, new Integer[] { 465, 7 });
		hash.put(Box.BOX_2, new Integer[] { 465, 8 });
		hash.put(Box.BOX_3, new Integer[] { 465, 9 });
		hash.put(Box.BOX_4, new Integer[] { 465, 10 });
		hash.put(Box.BOX_5, new Integer[] { 465, 11 });
		hash.put(Box.BOX_6, new Integer[] { 465, 12 });
		itemID = GrandExchangeHandler.getItem().getItemID();
		itemAmount = GrandExchangeHandler.getItem().getAmount();
		itemName = GrandExchangeHandler.getItem().getItemName();
		itemPrice = GrandExchangeHandler.getItem().getItemPrice();
		methodProvider.log("lets buy item:" + itemID);
		if (openGE()) {
			methodProvider.log("ge is open");
			relevantBoxes = getRelevantBoxes(itemID);
			if (relevantBoxes.isEmpty()) {
				createNewOffer(itemID, itemAmount, itemName, itemPrice);
			} else {
				handleBoxes(relevantBoxes, itemID);
			}
		}
	}

	private void createNewOffer(int itemID, int itemAmount, String itemName, int itemPrice) {
		methodProvider.log("lets create new buy offer: " + itemPrice);
		methodProvider.grandExchange.buyItem(itemID, itemName, (int) (itemPrice * 1.2), itemAmount);
		Timing.waitCondition(() -> !relevantBoxes.isEmpty(), 3000);
		
	}

	private void handleBoxes(List<Box> relevantBoxes, int itemID) {
		for (Box box : relevantBoxes) {
			methodProvider.log(methodProvider.grandExchange.getStatus(box));
			methodProvider.log("Item ID:" + methodProvider.grandExchange.getItemId(box));
			switch (methodProvider.grandExchange.getStatus(box)) {
			case FINISHED_BUY:
				claimItem();
				break;
			case PENDING_BUY:
				if (methodProvider.grandExchange.getItemId(box) == itemID) {
					abortUnfinishedOffer(box);
				}
				break;
			default:
				break;
			}
		}

	}

	private void abortUnfinishedOffer(Box box) {
		methodProvider.log("Trying to buy item, lets abandon: " + box);
		RS2Widget widget = methodProvider.widgets.get(hash.get(box)[0], hash.get(box)[1]);
		if (widget != null) {
			widget.interact("Abort offer");
		}
		// TODO sleep until offer is aborted
	}

	private void claimItem() {
		methodProvider.grandExchange.collect();
	}

	/*
	 * Shall return boxes that are either empty or contain our itemID
	 */
	private List<Box> getRelevantBoxes(int itemID) {
		relevantBoxes = new ArrayList<Box>();
		for (Box box : GrandExchange.Box.values()) {
			if (methodProvider.grandExchange.getStatus(box) != GrandExchange.Status.EMPTY
					&& (methodProvider.grandExchange.getStatus(box) == GrandExchange.Status.FINISHED_BUY
							|| methodProvider.grandExchange.getItemId(box) == itemID)) {
				relevantBoxes.add(box);
			}
		}
		return relevantBoxes;
	}

	/*
	 * Shall return boxes that are empty
	 */
	private List<Box> getAvailableBoxes() {
		relevantBoxes = new ArrayList<Box>();
		for (Box box : GrandExchange.Box.values()) {
			if (methodProvider.grandExchange.getStatus(box) == GrandExchange.Status.EMPTY) {
				relevantBoxes.add(box);
			}
		}
		return relevantBoxes;
	}

	private boolean openGE() {
		if (!methodProvider.grandExchange.isOpen()) {
			NPC grandExchangeClerk = methodProvider.getNpcs().closest("Grand Exchange Clerk");
			if (grandExchangeClerk != null) {
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
