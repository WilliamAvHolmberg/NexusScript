package org.nexus.node.agility;

import org.nexus.node.methods.AreaMethods;
import org.nexus.task.agility.AgilityCourse;
import org.nexus.task.agility.AgilityObstacle;
import org.nexus.task.agility.GnomeData;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;

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

	public static void climbObs(String obsAction, int obsName, MethodProvider methodProvider) {
		RS2Object obs = methodProvider.objects.closest(obsName);

		if (obs != null && lastID != obs.getId()) {
			if (obs.isVisible()) {
				obs.interact(obsAction);
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
