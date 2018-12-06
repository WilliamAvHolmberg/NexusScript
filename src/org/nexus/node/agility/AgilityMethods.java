package org.nexus.node.agility;

import org.nexus.node.methods.AreaMethods;
import org.nexus.task.agility.AgilityCourse;
import org.nexus.task.agility.AgilityObstacle;
import org.nexus.task.agility.GnomeData;
import org.nexus.utils.grandexchange.Exchange;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.input.mouse.ClickMouseEvent;
import org.osbot.rs07.input.mouse.MiniMapTileDestination;
import org.osbot.rs07.input.mouse.RectangleDestination;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;

import org.nexus.utils.Timing;

public class AgilityMethods {

	private static int lastID;
	private static int failCheck;

	public static boolean playerInAgilityArea(AgilityCourse assignment, MethodProvider methodProvider) {
		if (assignment != null) {
			for (AgilityObstacle obstacle : assignment.getObstacles()) {
				if (AreaMethods.playerInArea(obstacle.getArea(), methodProvider)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean playerInGnomeAgilityArea(MethodProvider methodProvider){
		for (AgilityObstacle obstacle : GnomeData.GNOME_OBSTACLES) {
			if (AreaMethods.playerInArea(obstacle.getArea(), methodProvider)) {
				return true;
			}
		}
		return false;
	}
	
	public static Area getCurrentArea(MethodProvider methodProvider, AgilityCourse course) {
		for(AgilityObstacle obs : course.getObstacles()) {
			if(obs.getArea().contains(methodProvider.myPosition())) {
				return obs.getArea();
			}
		}
		return null;
	} 
	
	public static Area getCurrentArea(MethodProvider methodProvider, AgilityCourse course, Entity item) {
		for(AgilityObstacle obs : course.getObstacles()) {
			if(obs.getArea().contains(item.getPosition())) {
				return obs.getArea();
			}
		}
		return null;
	} 
	
	public static GroundItem getMarkOfGrace(MethodProvider methodProvider, AgilityCourse course) {
		for (GroundItem item : methodProvider.groundItems.getAll()) {
			if (item.getName().equals("Mark of grace") && 
					getCurrentArea(methodProvider, course, item) != null && 
					getCurrentArea(methodProvider, course, methodProvider.myPlayer()) != null && 
					getCurrentArea(methodProvider, course, item).equals(getCurrentArea(methodProvider, course, methodProvider.myPlayer())) ) {
				return item;
			}
		}
		return null;
	}
	
	public static boolean clickMiniMapPosition(MethodProvider script, Position position)
            {
        return script.mouse.click(new RectangleDestination(
                script.bot, new MiniMapTileDestination(
                script.bot, position).getBoundingBox()));
}

	public static void climbObs(String obsAction, int obsName, MethodProvider methodProvider) {
		RS2Object obs = methodProvider.objects.closest(obsName);

		if (obs != null && lastID != obs.getId()) {
			methodProvider.log("distance: " + obs.getPosition().distance(methodProvider.myPosition()));
			if (obs.getPosition().distance(methodProvider.myPosition()) > 4) {	
				if(!clickMiniMapPosition(methodProvider, obs.getPosition())) {
					methodProvider.walking.walk(obs.getPosition());
				}
			}else if (obs.isVisible()) {
				obs.interact(obsAction);
				Timing.waitCondition(() ->!methodProvider.myPlayer().isAnimating() &&
										!methodProvider.myPlayer().isMoving(), 3000);
				
				lastID = obs.getId();
				methodProvider.log(lastID);
				failCheck = 0;
				methodProvider.camera.moveYaw(methodProvider.camera.getYawAngle() + 30);
			} else {
				methodProvider.camera.toEntity(obs);
				methodProvider.camera.moveYaw(methodProvider.camera.getYawAngle() + 30);
				methodProvider.log("Moving camera to obs");
			}
		} else {
			if (failCheck >= 5) {
				lastID = 0;
			}
			failCheck++;
		}

	}

}
