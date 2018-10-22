package org.nexus.node;

import java.util.ArrayList;
import java.util.List;

public class NodeHandler {
	public static List<Node> nodes = new ArrayList<Node>();
	
	public static void add(Node node) {
		nodes.add(node);
	}
	
	public static void remove(Node node) {
		nodes.remove(node);
	}
	
	public static List<Node> getRelevantNodes(){
		//if GEHandler contains item, return GE nodes
		//if BankHandler contains item, return BankNodes
		//if GearHandler contains item, return GeNodes
		//etc
		return null;
	}

}
