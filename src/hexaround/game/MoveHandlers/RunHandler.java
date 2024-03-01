package hexaround.game.MoveHandlers;

import hexaround.game.Creature;

import java.awt.*;
import java.util.HashMap;

public class RunHandler extends MoveHandler {

    public RunHandler() {
        super();
    }
    @Override
    public boolean checkLegality() {
        // this piece moves in a straight line, implement later


        return false;
    }

    @Override
    public HashMap<Point, Creature> getMoveResult() {
        return null;
    }
}
