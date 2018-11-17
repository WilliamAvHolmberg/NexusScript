package org.nexus.task;

public enum TaskType {
	WOODCUTTING, WITHDRAW_ITEM_FROM_MULE, BREAK, COMBAT, DEPOSIT_ITEM_TO_PLAYER, PREPARE_FOR_MULE_DEPOSIT;

	public static TaskType getType(String taskType) {
		switch(taskType) {
		case "WOODCUTTING":
			return WOODCUTTING;
		case "WITHDRAW_FROM_MULE":
			return WITHDRAW_ITEM_FROM_MULE;
		}
		return null;
	}

}
