package org.nexus.handler;


import org.nexus.NexusScript;
import org.nexus.node.Node;
import org.nexus.node.mule.TradeWithMule;
import org.nexus.node.mule.TradeWithSlave;
import org.nexus.objects.WithdrawItem;
import org.nexus.task.TaskType;
import org.nexus.task.mule.Mule;
import org.nexus.task.mule.WithdrawFromMule;
import org.nexus.utils.WebBank;
import org.osbot.rs07.api.model.Player;


public class MuleHandler extends Handler {

	private Mule currentTask;
	private TradeWithMule tradeWithMule;
	private TradeWithSlave tradeWithSlave;

	public MuleHandler() {
		tradeWithMule = new TradeWithMule();
		tradeWithSlave = new TradeWithSlave();
	}

	public Node getNode() {
		currentTask = (Mule) NexusScript.currentTask;
		if (!playerInArea(WebBank.GRAND_EXCHANGE.getArea())) {
			return walkToAreaNode.setArea(WebBank.GRAND_EXCHANGE.getArea());
		} else if(!trade.isCurrentlyTrading() && currentTask.getTaskType() == TaskType.DEPOSIT_TO_SLAVE && inventory.getAmount(995) < currentTask.getItemAmount()) {
			BankHandler.addItem(new WithdrawItem(995, currentTask.getItemAmount()));
		}
		else if (getMule(currentTask.getMuleName()) != null) {
			log("Mule is available within a distance of:" + getMule(currentTask.getMuleName()).getPosition().distance(myPlayer()));
			if(currentTask.getTaskType() == TaskType.DEPOSIT_TO_SLAVE) {
				return tradeWithSlave;
			}else if(currentTask.getTaskType() == TaskType.WITHDRAW_FROM_MULE) {
				return tradeWithMule;
			}
		} // else return idle, prepare for next tree
		return null;
	}

	private Player getMule(String name) {
		for (Player player : getPlayers().getAll()) {
			if (player.getName().toLowerCase().equals(name.toLowerCase())) {
				return player;
			}
		}
		return null;
	}

}
