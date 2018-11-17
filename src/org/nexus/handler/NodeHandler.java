package org.nexus.handler;

import java.util.ArrayList;
import java.util.List;

import org.nexus.NexusScript;
import org.nexus.handler.gear.GearHandler;
import org.nexus.handler.mule.MuleHandler;
import org.nexus.node.Node;
import org.nexus.objects.DepositItem;
import org.nexus.objects.GEItem;
import org.nexus.objects.WithdrawItem;
import org.nexus.task.Task;
import org.nexus.task.TaskType;
import org.nexus.task.mule.Mule;
import org.nexus.task.mule.WithdrawFromPlayer;
import org.nexus.utils.WebBank;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.MethodProvider;

public class NodeHandler extends Handler {
	private Node node;
	private WoodcuttingHandler woodcuttingHandler;
	private BankHandler bankHandler;
	private BuyItemHandler geBuyItemHandler;
	private SellItemHandler geSellItemHandler;
	private GEItem geItem;
	private WithdrawItem withdrawItem;
	private DepositItem depositItem;
	private GearHandler gearHandler;
	private MuleHandler withdrawFromMuleHandler;
	private CombatHandler combatHandler;

	public NodeHandler() {
		this.woodcuttingHandler = new WoodcuttingHandler();
		this.combatHandler = new CombatHandler();
		this.bankHandler = new BankHandler();
		this.geBuyItemHandler = new BuyItemHandler();
		this.gearHandler = new GearHandler();
		this.withdrawFromMuleHandler = new MuleHandler();
		this.geSellItemHandler = new SellItemHandler();
	}

	/**
	 * shall execute GE NODES if BUY_LIST is not empty shall execute BANK_NODES if
	 * WITHDRAW_LIST is not empty TODO - shall execute EQUIP_NODES if EQUIP_LIST is
	 * not empty TODO - return TASK_NODES if all above is empty TODO - EquipHandler
	 */
	@Override
	public Node getNode() {
		node = null;
		if (getCurrentTask().getTaskType() == TaskType.WITHDRAW_ITEM_FROM_MULE || trade.isCurrentlyTrading() ) {
			log("task is from mule");
			return withdrawFromMuleHandler.getNode();
		} else if (geSellItemHandler.getNode() != null) {
			return geSellItemHandler.getNode();
		} else if (geBuyItemHandler.getNode() != null) {
			return geBuyItemHandler.getNode();
		} else if (bankHandler.getNode() != null) {
			return bankHandler.getNode();
		} else if (gearHandler.getNode() != null) {
			return gearHandler.getNode();
		} else {

			switch (getCurrentTask().getTaskType()) {
			case WOODCUTTING:
				return woodcuttingHandler.getNode();
			case COMBAT:
				return combatHandler.getNode();
			case DEPOSIT_ITEM_TO_PLAYER:
			case PREPARE_FOR_MULE_DEPOSIT:
				return withdrawFromMuleHandler.getNode();

			default:
				break;

			}
			return node;
		}
	}

	public void init() {
		this.woodcuttingHandler.exchangeContext(getBot());
		this.combatHandler.exchangeContext(getBot());
		this.bankHandler.exchangeContext(getBot());
		this.geBuyItemHandler.exchangeContext(getBot());
		this.gearHandler.exchangeContext(getBot());
		this.withdrawFromMuleHandler.exchangeContext(getBot());
		this.geSellItemHandler.exchangeContext(getBot());
	}
}
