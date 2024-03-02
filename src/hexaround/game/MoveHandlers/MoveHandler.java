package hexaround.game.MoveHandlers;

import hexaround.config.CreatureDefinition;
import hexaround.game.Board;
import hexaround.game.Creature;
import hexaround.EnumsAndDefinitions.CreatureProperty;

import java.awt.*;
import java.util.HashMap;

/*
* This class is used to provide an abstraction for handling movement of creatures.
* The method that each class must overwrite is the checkLegality method() which utilizes the specific movement type and attributes to determine legality
* Each MoveHandler subclass operates using the same data-points (turn, board, creature, points) and nicely encapsulates the nuisances of each movement type
* */

public abstract class MoveHandler {
    protected boolean teamTurn;
    protected Board boardCopy;
    protected Creature creature;
    protected Point fromPoint;
    protected Point toPoint;

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
    // each subclass must implement with their own logic
    // this method should be implemented in a way where it does not have any side effects. (CANNOT CHANGE BOARD)
    public abstract boolean checkLegality();


    // the results of a move (if legal) will be updated here
    public HashMap<Point, Creature> getMoveResult() {
        this.boardCopy.remove(this.fromPoint);

        if(this.creature.getDef().properties().contains(CreatureProperty.SWAPPING)) {
            Creature swapped = this.boardCopy.getCreatureAt(this.toPoint);
            this.boardCopy.put(this.fromPoint, swapped);
        }

        this.boardCopy.put(this.toPoint, this.creature);
        return this.boardCopy.getBoard();
    }

}
