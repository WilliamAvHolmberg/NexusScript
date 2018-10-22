package org.nexus.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.nexus.utils.json.JsonObject;
import org.nexus.utils.json.JsonObject.Member;

public class Exchange {
	private final static String RSBUDDY_URL = "https://rsbuddy.com/exchange/summary.json";

	public static int getPrice(int item) {
		int price = 0;

		try {
			URL url = new URL(RSBUDDY_URL);
			BufferedReader jsonFile = new BufferedReader(new InputStreamReader(url.openStream()));
			
			JsonObject priceJSON = JsonObject.readFrom(jsonFile.readLine());
			Iterator<Member> iterator = priceJSON.iterator();
			
			while (iterator.hasNext()) {
				JsonObject itemJSON = priceJSON.get(iterator.next().getName()).asObject();
				int itemID = itemJSON.get("id").asInt();
				if (item == itemID) {
					price = itemJSON.get("buy_average").asInt();
					break;
				}
			}
		} catch (Exception e) {
		}
		return price;
	}
}
