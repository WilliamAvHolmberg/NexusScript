package org.nexus.node;

import org.osbot.rs07.script.MethodProvider;

public abstract class Node {
	public abstract boolean shallExecute(MethodProvider methodProvider);
	public abstract void execute(MethodProvider methodProvider);
	
	public abstract String toString(); //Node name - eg: CutTree
	
	
}
