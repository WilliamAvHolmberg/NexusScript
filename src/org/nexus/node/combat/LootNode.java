package org.nexus.node.combat;


import org.nexus.loot.Loot;
import org.nexus.loot.LootHandler;
import org.nexus.node.Node;
import org.nexus.objects.RSItem;
import org.nexus.utils.Timing;
import org.nexus.utils.grandexchange.Exchange;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

public class LootNode extends Node {

	private MethodProvider methodProvider;
	private GroundItem item;
	private Area area;

	@Override
	public boolean shallExecute(MethodProvider methodProvider) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
		loot(item);

	}

	public void loot(GroundItem item) {
		int amountBeforeLoot = (int) methodProvider.getInventory().getAmount(item.getId());
		if (item != null) {
			item.interact("Take");
			Timing.waitCondition(() -> methodProvider.inventory.getAmount(item.getId()) > amountBeforeLoot, 300, 4000);
			int amountAfterLoot = (int) methodProvider.getInventory().getAmount(item.getId());

			// if successful loot
			if (amountAfterLoot > amountBeforeLoot) {
				methodProvider.log("Successfull loot");

				// calculate the quantity
				int lootAmount = amountAfterLoot - amountBeforeLoot;
				// get iaoxItem
				RSItem rsItem = new RSItem(item.getName(), item.getId());
				// nullcheck if we found the item
				if (rsItem != null) {
					methodProvider.log("item price: " + Exchange.getPrice(item.getId(), methodProvider));
					LootHandler.addLoot(new Loot(rsItem, lootAmount));
					methodProvider.log("item price: " + Exchange.getPrice(rsItem.getId(), methodProvider));

				} else {
					methodProvider.log("could not find item: " + item.getName());
				}
			}
		}
	}

	public static GroundItem valueableDropAvailable(MethodProvider methodProvider, int threshold, Area area) {
		for (GroundItem item : methodProvider.groundItems.getAll()) {
			int price = Exchange.getPrice(item.getId(), methodProvider);
			if (area.contains(item) && price * item.getAmount() > threshold) {
				return item;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Eat";
	}

	public LootNode setItemAndArea(GroundItem item, Area area) {
		this.item = item;
		this.area = area;
		return this;
	}

}
