package hexaround.game.MoveHandlers;

import hexaround.game.Creature;

import java.awt.*;
import java.util.HashMap;

public class FlyHandler extends MoveHandler {

    public FlyHandler(boolean tmTurn, HashMap<Point, Creature> bordCopy, Creature creat, Point fromP, Point toP) {
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
