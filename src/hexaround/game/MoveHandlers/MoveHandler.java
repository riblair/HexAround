package hexaround.game.MoveHandlers;

import hexaround.config.CreatureDefinition;
import hexaround.game.Board;
import hexaround.game.Creature;
import hexaround.required.CreatureProperty;

import java.awt.*;
import java.util.HashMap;

public abstract class MoveHandler {

    protected boolean teamTurn;
    protected Board boardCopy;
    protected Creature creature;
    protected Point fromPoint;
    protected Point toPoint;

//    public MoveHandler() {
//
//    }

    public void initMoveHandler(boolean tmTurn, Board bordCopy, Creature creat, Point fromP, Point toP) {
        this.teamTurn = tmTurn;
        this.boardCopy = bordCopy;
        this.creature = creat;
        this.fromPoint = fromP;
        this.toPoint = toP;
    }

    // this method assumes each creature has one of Walking, running, flying, jumping as per hexaround.pdf
    public static MoveHandler createMoveHandler(CreatureDefinition definition) {

        MoveHandler moveHandler = null;
        int defIter = 0;

        while(moveHandler == null) {
            switch ((CreatureProperty)definition.properties().toArray()[defIter]) {
                case WALKING:
                    moveHandler = new WalkHandler();
                    break;
                case RUNNING:
                    moveHandler = new RunHandler();
                    break;
                case FLYING:
                    moveHandler = new FlyHandler();
                    break;
                case JUMPING:
                    moveHandler = new JumpHandler();
                    break;
                default:
                    defIter++;
                    break;
            }
        }
        return moveHandler;
    }

    public abstract boolean checkLegality();

    public HashMap<Point, Creature> getMoveResult() {
        this.boardCopy.remove(this.fromPoint);

        if(this.creature.getDef().properties().contains(CreatureProperty.KAMIKAZE)) {
            this.boardCopy.remove(this.fromPoint);
        }

        this.boardCopy.put(this.toPoint, this.creature);

        return this.boardCopy.getBoard();
    }

}
