package org.nexus.task.agility;



import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;


public class FaladorData {
	

	public static final Area faladorAgilityArea = new Area(3016, 3369, 3057, 3333);

	private static Area OBSTACLE_AREA_1= new Area(3035,3341,3041,3339);
	private static int OBSTACLE_1_ID = 10833;
	private static String OBSTACLE_1_ACTION = "Climb";
	
	
	
	private static Area OBSTACLE_AREA_2 = new Area(new Position(3036,3343,3), new Position(3040,3342,3));
	private static int OBSTACLE_2_ID = 10834;
	private static String OBSTACLE_2_ACTION = "Cross";
	
	private static Area OBSTACLE_AREA_3 = new Area(new Position(3051,3349,3), new Position(3045,3341,3));
	private static int OBSTACLE_3_ID = 10836;
	private static String OBSTACLE_3_ACTION = "Cross";
	
	private static Area OBSTACLE_AREA_4 = new Area(new Position(3051,3356,3), new Position(3048,3358,3));
	private static int OBSTACLE_4_ID = 11161;
	private static String OBSTACLE_4_ACTION = "Jump";
	
	private static Area OBSTACLE_AREA_5 = new Area(new Position(3048,3361,3), new Position(3045,3367,3));
	private static int OBSTACLE_5_ID = 11360;
	private static String OBSTACLE_5_ACTION = "Jump";
	
	private static Area OBSTACLE_AREA_6 = new Area(new Position(3042,3360,3), new Position(3034,3364,3));
	private static int OBSTACLE_6_ID = 11361;
	private static String OBSTACLE_6_ACTION = "Cross";
	
	private static Area OBSTACLE_AREA_7 = new Area(new Position(3026,3355,3), new Position(3029,3352,3));
	private static int OBSTACLE_7_ID = 11364;
	private static String OBSTACLE_7_ACTION = "Cross";
	
	private static Area OBSTACLE_AREA_8 = new Area(new Position(3021,3353,3), new Position(3015,3356,3));
	private static int OBSTACLE_8_ID = 11365;
	private static String OBSTACLE_8_ACTION = "Jump";
	
	private static Area OBSTACLE_AREA_9 = new Area(new Position(3022,3343,3), new Position(3016,3349,3));
	private static int OBSTACLE_9_ID = 11366;
	private static String OBSTACLE_9_ACTION = "Jump";
	
	private static Area OBSTACLE_AREA_10 = new Area(new Position(3014,3346,3), new Position(3011,3343,3));
	private static int OBSTACLE_10_ID = 11367;
	private static String OBSTACLE_10_ACTION = "Jump";
	

	private static Area OBSTACLE_AREA_11 = new Area(new Position(3013,3342,3), new Position(3009,3335,3));
	private static int OBSTACLE_11_ID = 11368;
	private static String OBSTACLE_11_ACTION = "Jump";
	
	private static Area OBSTACLE_AREA_12 = new Area(new Position(3012,3333,3), new Position(3018,3331,3));
	private static int OBSTACLE_12_ID = 11370;
	private static String OBSTACLE_12_ACTION = "Jump";
	
	private static Area OBSTACLE_AREA_13 = new Area(new Position(3019,3332,3), new Position(3024,3335,3));
	private static int OBSTACLE_13_ID = 11371;
	private static String OBSTACLE_13_ACTION = "Jump";
	
	public static AgilityObstacle[] FALADOR_OBSTACLES = {new AgilityObstacle(OBSTACLE_AREA_1, OBSTACLE_1_ID, OBSTACLE_1_ACTION),
			   new AgilityObstacle(OBSTACLE_AREA_2, OBSTACLE_2_ID, OBSTACLE_2_ACTION),
			   new AgilityObstacle(OBSTACLE_AREA_3, OBSTACLE_3_ID, OBSTACLE_3_ACTION),
			   new AgilityObstacle(OBSTACLE_AREA_4, OBSTACLE_4_ID, OBSTACLE_4_ACTION),
			   new AgilityObstacle(OBSTACLE_AREA_5, OBSTACLE_5_ID, OBSTACLE_5_ACTION),
			   new AgilityObstacle(OBSTACLE_AREA_6, OBSTACLE_6_ID, OBSTACLE_6_ACTION),
			   new AgilityObstacle(OBSTACLE_AREA_7, OBSTACLE_7_ID, OBSTACLE_7_ACTION),
			   new AgilityObstacle(OBSTACLE_AREA_8, OBSTACLE_8_ID, OBSTACLE_8_ACTION),
			   new AgilityObstacle(OBSTACLE_AREA_9, OBSTACLE_9_ID, OBSTACLE_9_ACTION),
			   new AgilityObstacle(OBSTACLE_AREA_10, OBSTACLE_10_ID, OBSTACLE_10_ACTION),
			   new AgilityObstacle(OBSTACLE_AREA_11, OBSTACLE_11_ID, OBSTACLE_11_ACTION),
			   new AgilityObstacle(OBSTACLE_AREA_12, OBSTACLE_12_ID, OBSTACLE_12_ACTION),
			   new AgilityObstacle(OBSTACLE_AREA_13, OBSTACLE_13_ID, OBSTACLE_13_ACTION),
			  };

}
