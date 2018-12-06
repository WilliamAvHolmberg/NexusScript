package org.nexus.task.agility;



import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;


public class VarrockData {
	

	
	public static final Area varrockAgilityArea = new Area(3184, 3421, 3241, 3399);

	private static Area OBSTACLE_AREA_1= new Area(3221,3411,3225,3419);
	private static int OBSTACLE_1_ID = 10586;
	private static String OBSTACLE_1_ACTION = "Climb";
	
	
	private static Area OBSTACLE_AREA_2 = new Area(new Position(3219,3419,3), new Position(3214,3410,3));
	private static int OBSTACLE_2_ID = 10587;
	private static String OBSTACLE_2_ACTION = "Cross";
	
	private static Area OBSTACLE_AREA_3 = new Area(new Position(3208,3414,3), new Position(3201,3419,3));
	private static int OBSTACLE_3_ID = 10642;
	private static String OBSTACLE_3_ACTION = "Leap";
	
	private static Area OBSTACLE_AREA_4 = new Area(new Position(3197,3416,1), new Position(3193,3416,1));
	private static int OBSTACLE_4_ID = 10777;
	private static String OBSTACLE_4_ACTION = "Balance";
	
	private static Area OBSTACLE_AREA_5 = new Area(new Position(3192,3406,3), new Position(3198,3402,3));
	private static int OBSTACLE_5_ID = 10778;
	private static String OBSTACLE_5_ACTION = "Leap";
	
	private static Area OBSTACLE_AREA_6 = new Area(new Position(3189,3395,3), new Position(3208,3399,3));
	private static int OBSTACLE_6_ID = 10779;
	private static String OBSTACLE_6_ACTION = "Leap";
	
	private static Area OBSTACLE_AREA_7 = new Area(new Position(3218,3393,3), new Position(3232,3402,3));
	private static int OBSTACLE_7_ID = 10780;
	private static String OBSTACLE_7_ACTION = "Leap";
	
	private static Area OBSTACLE_AREA_8 = new Area(new Position(3236,3403,3), new Position(3240,3408,3));
	private static int OBSTACLE_8_ID = 10781;
	private static String OBSTACLE_8_ACTION = "Hurdle";
	
	private static Area OBSTACLE_AREA_9 = new Area(new Position(3240,3410,3), new Position(3236,3415,3));
	private static int OBSTACLE_9_ID = 10817;
	private static String OBSTACLE_9_ACTION = "Jump-off";
	
	public static AgilityObstacle[] VARROCK_OBSTACLES = {new AgilityObstacle(OBSTACLE_AREA_1, OBSTACLE_1_ID, OBSTACLE_1_ACTION),
			   new AgilityObstacle(OBSTACLE_AREA_2, OBSTACLE_2_ID, OBSTACLE_2_ACTION),
			   new AgilityObstacle(OBSTACLE_AREA_3, OBSTACLE_3_ID, OBSTACLE_3_ACTION),
			   new AgilityObstacle(OBSTACLE_AREA_4, OBSTACLE_4_ID, OBSTACLE_4_ACTION),
			   new AgilityObstacle(OBSTACLE_AREA_5, OBSTACLE_5_ID, OBSTACLE_5_ACTION),
			   new AgilityObstacle(OBSTACLE_AREA_6, OBSTACLE_6_ID, OBSTACLE_6_ACTION),
			   new AgilityObstacle(OBSTACLE_AREA_7, OBSTACLE_7_ID, OBSTACLE_7_ACTION),
			   new AgilityObstacle(OBSTACLE_AREA_8, OBSTACLE_8_ID, OBSTACLE_8_ACTION),
			   new AgilityObstacle(OBSTACLE_AREA_9, OBSTACLE_9_ID, OBSTACLE_9_ACTION),
			  };

}
