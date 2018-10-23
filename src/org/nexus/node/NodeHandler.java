package org.nexus.node;

import java.util.ArrayList;
import java.util.List;

import org.nexus.NexusScript;
import org.nexus.task.Task;
import org.nexus.utils.WebBank;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.MethodProvider;



public class NodeHandler {
	private List<Node> nodes;
	protected MethodProvider methodProvider;
	private WoodcuttingHandler woodcuttingHandler;


	public NodeHandler(MethodProvider methodProvider) {
		this.methodProvider = methodProvider;
		this.woodcuttingHandler = new WoodcuttingHandler(methodProvider);
	}

	public List<Node> getNodes(Task currentTask) {
		nodes = null;
		switch(currentTask.getTaskType()) {
		case WOODCUTTING:
			nodes = woodcuttingHandler.getNodes(currentTask);
			break;
		default:
			break;
		
		}
		return nodes;
	}
	
	public Area getBankArea() {
		if(NexusScript.currentTask == null || NexusScript.currentTask.getBankArea() == null) {
			return WebBank.getNearest(methodProvider).getArea();
		}
		return NexusScript.currentTask.getBankArea();
	}

}
