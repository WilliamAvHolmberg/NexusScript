package org.nexus.utils;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;

public enum WebBank {

    DRAYNOR(Banks.DRAYNOR),
    AL_KHARID(Banks.AL_KHARID),
    LUMBRIDGE(Banks.LUMBRIDGE_UPPER),
    FALADOR_EAST(Banks.FALADOR_EAST),
    FALADOR_WEST(Banks.FALADOR_WEST),
    VARROCK_EAST(Banks.FALADOR_EAST),
    VARROCK_WEST(Banks.VARROCK_WEST),
    SEERS(Banks.CAMELOT),
    CATHERBY(Banks.CATHERBY),
    EDGEVILLE(Banks.EDGEVILLE),
    YANILLE(Banks.YANILLE),
    GNOME_STRONGHOLD(Banks.GNOME_STRONGHOLD),
    ARDOUNGE_NORTH(Banks.ARDOUGNE_NORTH),
    ARDOUNE_SOUTH(Banks.ARDOUGNE_SOUTH),
    CASTLE_WARS(Banks.CASTLE_WARS),
    DUEL_ARENA(Banks.DUEL_ARENA),
    PEST_CONTROL(Banks.PEST_CONTROL),
    CANIFIS(Banks.CANIFIS),
	GRAND_EXCHANGE(Banks.GRAND_EXCHANGE);

    private final Area area;

    WebBank(Area area) {
        this.area = area;
    }

    public static WebBank getNearest(MethodProvider methodProvider) {
        WebBank bank = null;
        int distance = Integer.MAX_VALUE;
        for (WebBank b : WebBank.values()) {
            final int bDistance = b.area.getRandomPosition().distance(methodProvider.myPosition());
            if (bDistance < distance) {
                distance = bDistance;
                bank = b;
            }
        }
        return bank;
    }
    
    public static Area parseCoordinates(String parsedActionArea) {
    	String parsedCoordinates = "";

		// if last, do not add ":"
		for (String coord : parsedActionArea.split("\\{")) {
			String newCoord = coord.replaceAll("\\},", "").replaceAll("\\}", "").replaceAll(" ", "");
			if (newCoord.length() > 3) {
				parsedCoordinates = parsedCoordinates + newCoord + ":";
			}
		}
		Position[] newCoordinates = new Position[parsedCoordinates.split(":").length];
		String[] almostReadyCoordinates = parsedCoordinates.split(":");
		for (int i = 0; i < almostReadyCoordinates.length; i++) {
			String[] parsedCoords = almostReadyCoordinates[i].split(",");
			int coordinate1 = Integer.parseInt(parsedCoords[0]);
			int coordinate2 = Integer.parseInt(parsedCoords[1]);
			if (coordinate1 > 500 && coordinate2 > 500) {
				Position newPos = new Position(coordinate1, coordinate2,0);
				newCoordinates[i] = newPos;
			}
		}
		return new Area(newCoordinates);
    }

    public Position getRandomPosition() {
        return area.getRandomPosition();
    }

    public Area getArea() {
        return area;
    }
}