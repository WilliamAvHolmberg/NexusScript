package org.nexus.task;

import org.osbot.rs07.script.MethodProvider;

public abstract class Task extends MethodProvider{

	public long timeStarted = System.currentTimeMillis();
	public abstract void onExit();
	public abstract int onLoop() throws InterruptedException;
	public abstract boolean isFinished();
}
