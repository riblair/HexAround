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
        boolean legalCheck1;
        boolean legalCheck2;
        boolean legalCheck3;
        boolean legalCheck4;

        legalCheck1 = this.creature.getDef().maxDistance() >= this.boardCopy.getDistance(this.fromPoint, this.toPoint);
        legalCheck4 = !this.boardCopy.isOccupied(this.toPoint);

        int dy = (this.toPoint.y-this.fromPoint.y);
        int dx = (this.toPoint.x-this.fromPoint.x);

        legalCheck2 = dy == 0 || dx == 0 || Math.abs(dy) == Math.abs(dx);

        this.boardCopy.remove(this.fromPoint);
        this.boardCopy.put(this.toPoint, this.creature);

        legalCheck3 = this.boardCopy.isBoardConnected();

        this.boardCopy.remove(this.toPoint);
        this.boardCopy.put(this.fromPoint, this.creature);


        System.out.printf("Jumping Creature LegalChecks [%b, %b, %b, %b]\n", legalCheck1,legalCheck2,legalCheck3, legalCheck4);
        return legalCheck1 && legalCheck2 && legalCheck3 && legalCheck4;
    }
}
