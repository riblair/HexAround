package hexaround.game.MoveHandlers;

import hexaround.config.CreatureDefinition;
import hexaround.game.Creature;

import java.awt.*;
import java.util.HashMap;

public abstract class MoveHandler {

    private boolean teamTurn;
    private HashMap<Point, Creature> boardCopy;
    private Creature creature;
    private Point fromPoint;
    private Point toPoint;

    public MoveHandler(boolean tmTurn, HashMap<Point, Creature> bordCopy, Creature creat, Point fromP, Point toP) {
        this.teamTurn = tmTurn;
        this.boardCopy = bordCopy;
        this.creature = creat;
        this.fromPoint = fromP;
        this.toPoint = toP;
    }

    public abstract boolean checkLegality();

    public abstract HashMap<Point, Creature> getMoveResult();

}
