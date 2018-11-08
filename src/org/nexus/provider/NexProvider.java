package org.nexus.provider;

import org.osbot.rs07.script.MethodProvider;

public class NexProvider extends MethodProvider{

	NexPlayer player;
	public NexProvider() {
		player = new NexPlayer();
	}
	
	public NexPlayer getPlayer() {
		player.exchangeContext(getBot());
		return player;
	}
}
