package org.nexus.node;

import org.osbot.rs07.script.MethodProvider;

public abstract class Node {
	protected MethodProvider methodProvider;
	
	public Node init(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
		return this;
	}
	
	public abstract boolean shallExecute();
	public abstract void execute();
	
	public abstract String toString(); //Node name - eg: CutTree
	
	
	
}
