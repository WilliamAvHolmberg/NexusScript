package org.nexus.node;

import org.osbot.rs07.script.MethodProvider;

public abstract class Node {
	public abstract boolean shallExecute(MethodProvider methodProvider);
	public abstract void execute(MethodProvider methodProvider);
	
	public abstract String toString(); //Node name - eg: CutTree
	
	public void sleep(int milli) {
		try {
			MethodProvider.sleep(15000);
		}catch(Exception e) {
			
		}
	}
}
