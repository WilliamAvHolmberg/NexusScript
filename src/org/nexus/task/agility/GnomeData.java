package org.nexus.task.agility;



import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;


public class GnomeData {
	
	public static final Area gnomeGateArea = new Area(new Position[] {
			new Position(2457, 3378, 0),
			new Position(2456, 3384, 0),
			new Position(2465, 3384, 0),
			new Position(2466, 3376, 0)
	});
	
	public static final Area outsideGnomeAgil = new Area(new Position[] {
			new Position(2464, 3384, 0),
			new Position(2458, 3384, 0),
			new Position(2465, 3388, 0),
			new Position(2468, 3392, 0),
			new Position(2493, 3408, 0),
			new Position(2493, 3449, 0),
			new Position(2406, 3441, 0)
	});
	
	public static final Area gnomeAgilityArea = new Area(new Position[] { new Position(2472,3441,0), new Position(2478,3441,0), new Position(2468,3437,0), new Position(2468,3434,0), new Position(2468,3425,0), new Position(2468,3421,0), new Position(2468,3417,0), new Position(2470,3415,0), new Position(2473,3413,0), new Position(2475,3413,0), new Position(2479,3413,0), new Position(2483,3413,0), new Position(2486,3413,0), new Position(2487,3413,0), new Position(2490,3416,0), new Position(2491,3418,0), new Position(2491,3421,0), new Position(2491,3425,0), new Position(2491,3429,0), new Position(2491,3432,0), new Position(2491,3436,0), new Position(2489,3439,0), new Position(2487,3441,0), new Position(2481,3441,0), new Position(2474,3441,0), new Position(2471,3440,0),});


	private static Area OBSTACLE_AREA_1= new Area(2477,3430,2471,3440);
	private static int OBSTACLE_1_ID = 23145;
	private static String OBSTACLE_1_ACTION = "Walk-across";
	
	
	private static Area OBSTACLE_AREA_2 = new Area(2471,3429,2476,3425);
	private static int OBSTACLE_2_ID = 23134;
	private static String OBSTACLE_2_ACTION = "Climb-over";
	
	private static Area OBSTACLE_AREA_3 = new Area(new Position(2476,3424,1), new Position(2471,3422,1));
	private static int OBSTACLE_3_ID = 23559;
	private static String OBSTACLE_3_ACTION = "Climb";
	
	private static Area OBSTACLE_AREA_4 = new Area(new Position(2477,3418,2), new Position(2472,3421,2));
	private static int OBSTACLE_4_ID = 23557;
	private static String OBSTACLE_4_ACTION = "Walk-on";
	
	private static Area OBSTACLE_AREA_5 = new Area(new Position(2483,3421,2), new Position(2488,3418,2));
	private static int OBSTACLE_5_ID = 23561;
	private static String OBSTACLE_5_ACTION = "Climb-down";
	
	private static Area OBSTACLE_AREA_6 = new Area(2488,3418,2483,3426);
	private static int OBSTACLE_6_ID = 23135;
	private static String OBSTACLE_6_ACTION = "Climb-over";
	
	private static Area OBSTACLE_AREA_7 = new Area(2483,3427,2488,3431);
	private static int OBSTACLE_7_ID = 23139;
	private static String OBSTACLE_7_ACTION = "Squeeze-through";
	
	
	public static AgilityObstacle[] GNOME_OBSTACLES = {new AgilityObstacle(OBSTACLE_AREA_1, OBSTACLE_1_ID, OBSTACLE_1_ACTION),
													   new AgilityObstacle(OBSTACLE_AREA_2, OBSTACLE_2_ID, OBSTACLE_2_ACTION),
													   new AgilityObstacle(OBSTACLE_AREA_3, OBSTACLE_3_ID, OBSTACLE_3_ACTION),
													   new AgilityObstacle(OBSTACLE_AREA_4, OBSTACLE_4_ID, OBSTACLE_4_ACTION),
													   new AgilityObstacle(OBSTACLE_AREA_5, OBSTACLE_5_ID, OBSTACLE_5_ACTION),
													   new AgilityObstacle(OBSTACLE_AREA_6, OBSTACLE_6_ID, OBSTACLE_6_ACTION),
													   new AgilityObstacle(OBSTACLE_AREA_7, OBSTACLE_7_ID, OBSTACLE_7_ACTION),
													  };
}
