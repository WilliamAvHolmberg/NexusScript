package org.nexus.loot;

import java.util.ArrayList;
import java.util.List;


public class LootHandler {
	
	public static List<Loot> loot = new ArrayList<Loot>();
	public static int valueOfLoot = 0;
	
	/**
	 * Add loot to loot handler
	 * Add price of the loot (itemPrice * amount of item)
	 * @param loot
	 */
	public static void addLoot(Loot lootItem) {
		loot.add(lootItem);
		valueOfLoot += lootItem.getItem().getItemPrice() * lootItem.getAmount();
	}
	public int getValueOfLoot() {
		return valueOfLoot;
	}
	
	public static void reset() {
		loot = new ArrayList<Loot>();
		valueOfLoot = 0;
	}

}
