package hexaround.game.MoveHandlers;

import hexaround.game.Creature;
import hexaround.required.CreatureProperty;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;

public class FlyHandler extends MoveHandler {

    public FlyHandler() {
        super();
    }
    @Override
    public boolean checkLegality() {

        // 4 checks:
        // if all neighbors are occupied, cannot move
        // if occupied (and not a special type ((implementing later))
        // if distance(from,to) <= creatures max distance, cannot move
        // Check move continuity results


        boolean legalCheck1;
        boolean legalCheck2;
        boolean legalCheck3;
        boolean legalCheck4;

        legalCheck1 = this.creature.getDef().maxDistance() >= this.boardCopy.getDistance(this.fromPoint, this.toPoint);

        if(creature.getDef().properties().contains(CreatureProperty.KAMIKAZE)) {
            legalCheck4 = true;
        }
        else {
            legalCheck4 = !this.boardCopy.isOccupied(this.toPoint);
        }



        Collection<Point> neighbors = this.boardCopy.getNeighbors(this.fromPoint);
        int numTouching = 0;
        for(Point p : neighbors) {
            if(boardCopy.isOccupied(p)) numTouching++;
        }

        legalCheck2 = numTouching < 6;

        // move piece, check for continuity, then move it back
        this.boardCopy.remove(this.fromPoint);
        this.boardCopy.put(this.toPoint, this.creature);

        legalCheck3 = this.boardCopy.isBoardConnected();

        this.boardCopy.remove(this.toPoint);
        this.boardCopy.put(this.fromPoint, this.creature);

        System.out.printf("Flying Creature LegalChecks [%b, %b, %b, %b]\n", legalCheck1,legalCheck2,legalCheck3, legalCheck4);
        return legalCheck1 && legalCheck2 && legalCheck3 && legalCheck4;
    }
}
