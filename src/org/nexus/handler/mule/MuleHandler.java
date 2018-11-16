package org.nexus.handler.mule;

import org.nexus.NexusScript;
import org.nexus.communication.NexHelper;
import org.nexus.communication.message.request.MuleRequest;
import org.nexus.handler.BankHandler;
import org.nexus.handler.Handler;
import org.nexus.node.Node;
import org.nexus.node.mule.DepositItemToPlayer;
import org.nexus.node.mule.PrepareForMuleDeposit;
import org.nexus.node.mule.CheckIfWeShallSellItems;
import org.nexus.node.mule.WithdrawItemFromPlayer;
import org.nexus.objects.DepositItem;
import org.nexus.objects.DepositItem.DepositType;
import org.nexus.objects.WithdrawItem;
import org.nexus.task.TaskType;
import org.nexus.task.mule.Mule;
import org.nexus.task.mule.WithdrawFromPlayer;
import org.nexus.utils.Timing;
import org.nexus.utils.WebBank;
import org.osbot.rs07.api.model.Player;

public class MuleHandler extends Handler {

	private Mule currentTask;
	protected DepositItemToPlayer tradeWithMule;
	protected WithdrawItemFromPlayer tradeWithSlave;
	private PrepareForMuleDeposit prepareForMuleDepositNode = new PrepareForMuleDeposit();
	private CheckIfWeShallSellItems sellEveryItemNode = new CheckIfWeShallSellItems();

	public MuleHandler() {
		tradeWithMule = new DepositItemToPlayer();
		tradeWithSlave = new WithdrawItemFromPlayer();
	}

	public Node getNode() {
		currentTask = (Mule) NexusScript.currentTask;
		if (!playerInArea(WebBank.GRAND_EXCHANGE.getArea())) {
			return walkToAreaNode.setArea(WebBank.GRAND_EXCHANGE.getArea());
		}

		// if inventory is full, deposit everything except 995
		else if (inventory.isFull()) {
			return deposit.setItem(new DepositItem(DepositType.DEPOSIT_ALL_EXCEPT, 995));
		} else {
			switch (currentTask.getTaskType()) {

			case DEPOSIT_ITEM_TO_PLAYER:
				if (!trade.isCurrentlyTrading() && inventory.getAmount(995) < currentTask.getItemAmount()) {
					BankHandler.addItem(new WithdrawItem(995, currentTask.getItemAmount()));
				} else if (getMule(currentTask.getMuleName()) != null) {
					log("Mule is available within a distance of:"
							+ getMule(currentTask.getMuleName()).getPosition().distance(myPlayer()));
					return tradeWithSlave;
				}
				break;
			case WITHDRAW_ITEM_FROM_MULE:
				if (getMule(currentTask.getMuleName()) != null) {
					log("Mule is available within a distance of:"
							+ getMule(currentTask.getMuleName()).getPosition().distance(myPlayer()));
					return tradeWithMule;
				}
				break;
			case PREPARE_FOR_MULE_DEPOSIT:
				if (!currentTask.soldItems()) {
					return sellEveryItemNode;
				} else {
					return prepareForMuleDepositNode.setTask(currentTask);
				}
			}
		}
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
