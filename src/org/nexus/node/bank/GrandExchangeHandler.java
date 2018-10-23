package org.nexus.node.bank;

import java.util.Stack;

import org.nexus.objects.GEItem;

public class GrandExchangeHandler {
public static Stack<GEItem> items = new Stack<GEItem>();
	
	public static void addItem(GEItem item) {
		items.add(item);
	}
	
	public static void removeItem(GEItem item) {
		items.remove(item);
	}
	
	public static GEItem getItem() {
		if(items.isEmpty()) {
			return null;
		}
		return items.peek();
	}
}
