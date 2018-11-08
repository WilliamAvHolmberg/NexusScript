package org.nexus.provider;

import java.awt.Color;

import org.osbot.rs07.script.MethodProvider;

public class NexPlayer extends MethodProvider{
	
	public boolean isTutorialIslandCompleted() {
        return getWidgets().getWidgetContainingText("Tutorial Island Progress") == null;
    }
	public boolean isDisabledMessageVisible() {
		return getColorPicker().isColorAt(483, 192, new Color(255, 255, 0));
	}

}
