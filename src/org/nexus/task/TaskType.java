package org.nexus.task;

public enum TaskType {
	WOODCUTTING, WITHDRAW_FROM_MULE, BREAK, DEPOSIT_TO_SLAVE;

	public static TaskType getType(String taskType) {
		switch(taskType) {
		case "WOODCUTTING":
			return WOODCUTTING;
		case "WITHDRAW_FROM_MULE":
			return WITHDRAW_FROM_MULE;
		}
		return null;
	}

}
