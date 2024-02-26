package hexaround.game;

import hexaround.config.CreatureDefinition;
import hexaround.required.CreatureName;

import java.awt.*;
import java.util.HashMap;

public class TestHexAround extends HexAroundGame {

    /*
     * This class is used to access inner variables from the superclass and check them
     * Only used for testing purposes
     * */
    public TestHexAround() {
        super();
    }

    public Board getBoard() {
        return this.board;
    }
    public HashMap<Point, Point> getTeamLegalSpaces(boolean blueTeam) {
        HashMap<Point, Point> team;
        if(blueTeam)
            team = this.blueLegalSpaces;
        else
            team = this.redLegalSpaces;

        return team;
    }
    public HashMap<CreatureName, CreatureDefinition> getDefinitions() {
        return this.definitions;
    }

    public Point getButterfly(boolean bteam) {
        return bteam ? this.blueButterfly : this.redButterfly;
    }

    public boolean getCurrentTeamTurn() { return this.blueTurn; }

}
