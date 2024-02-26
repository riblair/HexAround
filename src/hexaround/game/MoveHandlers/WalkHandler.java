package hexaround.game.MoveHandlers;

import hexaround.game.Creature;

import java.awt.*;
import java.util.HashMap;

public class WalkHandler extends MoveHandler {

    public WalkHandler(boolean tmTurn, HashMap<Point, Creature> bordCopy, Creature creat, Point fromP, Point toP) {
        super(tmTurn, bordCopy, creat, fromP, toP);
    }
    @Override
    public boolean checkLegality() {
        return false;
    }

    @Override
    public HashMap<Point, Creature> getMoveResult() {
        return null;
    }
}
