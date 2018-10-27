package org.nexus.task;

public enum TaskType {
	WOODCUTTING, BREAK;

	public static TaskType getType(String taskType) {
		switch(taskType) {
		case "WOODCUTTING":
			return WOODCUTTING;
		}
		return null;
	}

}
