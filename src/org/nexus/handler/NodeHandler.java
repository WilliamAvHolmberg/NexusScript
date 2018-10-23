package org.nexus.handler;

import java.util.ArrayList;
import java.util.List;

import org.nexus.NexusScript;
import org.nexus.handler.gear.GearHandler;
import org.nexus.node.Node;
import org.nexus.objects.DepositItem;
import org.nexus.objects.GEItem;
import org.nexus.objects.WithdrawItem;
import org.nexus.task.Task;
import org.nexus.utils.WebBank;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.MethodProvider;

public class NodeHandler extends Handler {
	private Node node;
	protected MethodProvider methodProvider;
	private WoodcuttingHandler woodcuttingHandler;
	private BankHandler bankHandler;
	private GrandExchangeHandler geHandler;
	private GEItem geItem;
	private WithdrawItem withdrawItem;
	private DepositItem depositItem;
	private GearHandler gearHandler;

	public NodeHandler(MethodProvider methodProvider) {
		super.methodProvider = methodProvider;
		this.methodProvider = methodProvider;
		this.woodcuttingHandler = new WoodcuttingHandler(methodProvider);
		this.bankHandler = new BankHandler(methodProvider);
		this.geHandler = new GrandExchangeHandler(methodProvider);
		this.gearHandler = new GearHandler(methodProvider);
	}

	/**
	 * shall execute GE NODES if BUY_LIST is not empty shall execute BANK_NODES if
	 * WITHDRAW_LIST is not empty TODO - shall execute EQUIP_NODES if EQUIP_LIST is
	 * not empty TODO - return TASK_NODES if all above is empty TODO - EquipHandler
	 */
	@Override
	public Node getNode() {
		node = null;
		if (geHandler.getNode() != null) {
			return geHandler.getNode();
		} else if (bankHandler.getNode() != null) {
			return bankHandler.getNode();
		} else if (gearHandler.getNode() != null) {
			return gearHandler.getNode();
		} else {

			switch (getCurrentTask().getTaskType()) {
			case WOODCUTTING:
				return woodcuttingHandler.getNode();
			default:
				break;

			}
			return node;
		}
	}
}
