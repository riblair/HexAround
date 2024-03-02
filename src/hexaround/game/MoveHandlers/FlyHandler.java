package hexaround.game.MoveHandlers;

import hexaround.EnumsAndDefinitions.CreatureProperty;

import java.awt.*;
import java.util.Collection;

public class FlyHandler extends MoveHandler {

    public FlyHandler() {
        super();
    }
    @Override
    public boolean checkLegality() {

        // 4 checks:
        boolean legalCheck1; // if distance(from,to) <= creatures max distance, invalid move
        boolean legalCheck2; // if all neighbors are occupied, cannot move
        boolean legalCheck3; // Does not break continuity
        boolean legalCheck4; // if occupied (and not a special type)

        legalCheck1 = this.creature.getDef().maxDistance() >= this.boardCopy.getDistance(this.fromPoint, this.toPoint);

        Collection<Point> neighbors = this.boardCopy.getNeighbors(this.fromPoint);
        int numTouching = 0;
        for(Point p : neighbors) {
            if(boardCopy.isOccupied(p)) numTouching++;
        }
        legalCheck2 = numTouching < 6;

        boolean prevOccupied = this.boardCopy.isOccupied(this.toPoint);

        this.boardCopy.remove(this.fromPoint);
        if(!prevOccupied) this.boardCopy.put(this.toPoint, this.creature);
        legalCheck3 = this.boardCopy.isBoardConnected();

        if(!prevOccupied) this.boardCopy.remove(this.toPoint);
        this.boardCopy.put(this.fromPoint, this.creature);

        if(creature.getDef().properties().contains(CreatureProperty.KAMIKAZE) || creature.getDef().properties().contains(CreatureProperty.SWAPPING)) {
            legalCheck4 = true;
        }
        else {
            legalCheck4 = !this.boardCopy.isOccupied(this.toPoint);
        }

        return legalCheck1 && legalCheck2 && legalCheck3 && legalCheck4;
    }
}
