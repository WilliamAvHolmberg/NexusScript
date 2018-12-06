package org.nexus.node.ge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.nexus.handler.grandexchange.BuyItemHandler;
import org.nexus.handler.grandexchange.SellItemHandler;
import org.nexus.node.Node;
import org.nexus.objects.GEItem;
import org.nexus.objects.GESellItem;
import org.osbot.rs07.api.GrandExchange;
import org.osbot.rs07.api.GrandExchange.Box;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.input.mouse.WidgetDestination;
import org.osbot.rs07.script.MethodProvider;
import org.nexus.utils.Timing;

public class SellItem extends Node {

	private GESellItem item;
	private int itemID;
	private int itemAmount;
	private String itemName;
	private int itemPrice;
	
	private List<Box> relevantBoxes;
	private List<Box> availableBoxes;
	private HashMap<Box, Integer[]> hash = new HashMap<Box, Integer[]>();
	private Box boxToUse;
	private MethodProvider methodProvider;

	@Override
	public boolean shallExecute(MethodProvider methodProvider) {
		return methodProvider.inventory.onlyContains(995);
	}

	@Override
	public void execute(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
		hash.put(Box.BOX_1, new Integer[] { 465, 7 });
		hash.put(Box.BOX_2, new Integer[] { 465, 8 });
		hash.put(Box.BOX_3, new Integer[] { 465, 9 });
		hash.put(Box.BOX_4, new Integer[] { 465, 10 });
		hash.put(Box.BOX_5, new Integer[] { 465, 11 });
		hash.put(Box.BOX_6, new Integer[] { 465, 12 });
		itemID = item.getItemID();
		if(methodProvider.inventory.contains(itemID + 1)) {
			itemID = itemID + 1;
		}
		itemAmount = item.getAmount();
		itemName = item.getItemName();
		itemPrice = item.getItemPrice();
		methodProvider.log("lets sell item:" + itemID);
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
		methodProvider.grandExchange.sellItem(itemID, 1, itemAmount);
		Timing.waitCondition(() -> !relevantBoxes.isEmpty(), 5000);
		claimItem();
		SellItemHandler.items.remove(item);
	}

	private void handleBoxes(List<Box> relevantBoxes, int itemID) {
		for (Box box : relevantBoxes) {
			methodProvider.log(methodProvider.grandExchange.getStatus(box));
			methodProvider.log("Item ID:" + methodProvider.grandExchange.getItemId(box));
			switch (methodProvider.grandExchange.getStatus(box)) {
			case FINISHED_SALE:
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
	
	public Node setItem(GESellItem geItem) {
		this.item = geItem;
		return this;
	}

}
