package org.nexus.node.methods;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.MethodProvider;

public class AreaMethods {

	private MethodProvider methodProvider;
	
	public AreaMethods(MethodProvider methodProvider){
		this.methodProvider = methodProvider;
	}
	
	public static boolean playerInArea(Area area, MethodProvider methodProvider) {
		return area.contains(methodProvider.myPlayer());
	}
}
