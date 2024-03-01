package hexaround.game.MoveHandlers;

import hexaround.game.Creature;

import java.awt.*;
import java.util.HashMap;

public class JumpHandler extends MoveHandler {

    public JumpHandler() {
        super();
    }
    @Override
    public boolean checkLegality() {

    // 4 checks:
    // has to move in a straight line (how tf I check that)
    // if occupied (and not a special type ((implementing later))
    // if distance(from,to) <= creatures max distance, cannot move
    // Check move continuity results


        return false;
    }

    @Override
    public HashMap<Point, Creature> getMoveResult() {
        return null;
    }
}
