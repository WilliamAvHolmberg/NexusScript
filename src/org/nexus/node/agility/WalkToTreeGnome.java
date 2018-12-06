package org.nexus.node.agility;

import org.nexus.node.Node;
import org.nexus.task.agility.GnomeData;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;

import org.nexus.utils.Timing;




public class WalkToTreeGnome extends Node{

	@Override
	public boolean shallExecute(MethodProvider methodProvider) {
		return false;
	}

	@Override
	public void execute(MethodProvider methodProvider) {
		if(!GnomeData.gnomeGateArea.contains(methodProvider.myPlayer())&& !GnomeData.outsideGnomeAgil.contains(methodProvider.myPlayer())) {
			methodProvider.walking.webWalk(new Position(2461, 3380, 0));
		}
		else if(GnomeData.gnomeGateArea.contains(methodProvider.myPlayer()) && methodProvider.myPlayer().getPosition().getY() < 3384) {
			if(continueMessageIsVisible(methodProvider)){
				checkContinue(methodProvider);
			}else{
				openGate(methodProvider);
			}
		}
		else if(methodProvider.myPlayer().getPosition().getY() > 3384 && GnomeData.outsideGnomeAgil.contains(methodProvider.myPlayer()) && !GnomeData.gnomeAgilityArea.contains(methodProvider.myPlayer())) {
			methodProvider.walking.webWalk(new Position(2474, 3438, 0));
		}
		else if(GnomeData.gnomeAgilityArea.contains(methodProvider.myPlayer()) && !AgilityMethods.playerInGnomeAgilityArea(methodProvider)) {
			methodProvider.walking.walk(new Position(2474, 3438, 0));
		}
		else {
			try {
				methodProvider.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void openGate(MethodProvider methodProvider)  {
		RS2Object gate = methodProvider.objects.closest("Gate");
		
		if(gate != null) {
			if(gate.isVisible()) {
				gate.interact("Open");
				Timing.sleep(2500);
			}else {
				methodProvider.camera.toEntity(gate);
			}
		}
		
	}
	
	public void clickContinue(MethodProvider methodProvider) {
		methodProvider.getDialogues().clickContinue();
		try {
			methodProvider.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean continueMessageIsVisible(MethodProvider methodProvider) {
		if (methodProvider.widgets.getWidgetContainingText("Click here to continue") != null) {
			return true;
		}
		return false;
	}

	public void checkContinue(MethodProvider methodProvider) {
		if (continueMessageIsVisible(methodProvider)) {
			clickContinue(methodProvider);
		}
		return;
	}



	@Override
	public String toString() {
		return "Walk to Tree Gnome";
	}
	
	
}
