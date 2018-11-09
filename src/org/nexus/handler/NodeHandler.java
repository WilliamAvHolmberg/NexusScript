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
import org.nexus.task.TaskType;
import org.nexus.task.mule.Mule;
import org.nexus.task.mule.WithdrawFromMule;
import org.nexus.utils.WebBank;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.MethodProvider;

public class NodeHandler extends Handler {
	private Node node;
	private WoodcuttingHandler woodcuttingHandler;
	private BankHandler bankHandler;
	private GrandExchangeHandler geHandler;
	private GEItem geItem;
	private WithdrawItem withdrawItem;
	private DepositItem depositItem;
	private GearHandler gearHandler;
	private MuleHandler withdrawFromMuleHandler;

	public NodeHandler() {
		this.woodcuttingHandler = new WoodcuttingHandler();
		this.bankHandler = new BankHandler();
		this.geHandler = new GrandExchangeHandler();
		this.gearHandler = new GearHandler();
		this.withdrawFromMuleHandler = new MuleHandler();
	}

	/**
	 * shall execute GE NODES if BUY_LIST is not empty shall execute BANK_NODES if
	 * WITHDRAW_LIST is not empty TODO - shall execute EQUIP_NODES if EQUIP_LIST is
	 * not empty TODO - return TASK_NODES if all above is empty TODO - EquipHandler
	 */
	@Override
	public Node getNode() {
		node = null;
		if (getCurrentTask().getTaskType() == TaskType.WITHDRAW_FROM_MULE) {
			log("task is from mule");
			return withdrawFromMuleHandler.getNode();
		}
		else if (geHandler.getNode() != null) {
			return geHandler.getNode();
		} else if (bankHandler.getNode() != null) {
			return bankHandler.getNode();
		} else if (gearHandler.getNode() != null) {
			return gearHandler.getNode();
		} else {

			switch (getCurrentTask().getTaskType()) {
			case WOODCUTTING:
				return woodcuttingHandler.getNode();
			case DEPOSIT_TO_SLAVE:
				return withdrawFromMuleHandler.getNode();
			default:
				break;

			}
			return node;
		}
	}

	public void init() {
		this.woodcuttingHandler.exchangeContext(getBot());
		this.bankHandler.exchangeContext(getBot());
		this.geHandler.exchangeContext(getBot());
		this.gearHandler.exchangeContext(getBot());
		this.withdrawFromMuleHandler.exchangeContext(getBot());
	}
}
