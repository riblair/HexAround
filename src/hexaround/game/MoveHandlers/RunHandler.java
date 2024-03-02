package hexaround.game.MoveHandlers;

import hexaround.game.Creature;

import java.awt.*;
import java.util.*;

public class RunHandler extends MoveHandler {

    public RunHandler() {
        super();
    }
    @Override
    public boolean checkLegality() {
        // Code is very similar to Walking with the one key exception being the exit condition also requires the distance to be exactly max.
        if(this.creature.getDef().maxDistance() < this.boardCopy.getDistance(this.fromPoint,this.toPoint)) {
            return false;
        }

        Set<Point> visited = new HashSet<>();
        LinkedList<MoveHelper> movementQueue = new LinkedList<>();

        Collection<Point> neighbors = this.boardCopy.getNeighbors(this.fromPoint);

        // this is only checking for unoccupied cells, the while loop for each potential move handles the rest
        for(Point p : neighbors) {
            movementQueue.add(new MoveHelper(this.fromPoint, p, this.creature.getDef().maxDistance()));
        }
        MoveHelper current;
        int goalDist;
        visited.add(this.fromPoint);

        this.boardCopy.remove(this.fromPoint);

        while(!movementQueue.isEmpty()) {
            current = movementQueue.pop();
            visited.add(current.fromP);
            boolean previousOcc = this.boardCopy.isOccupied(current.toP);
            if(!previousOcc) {
                this.boardCopy.put(current.fromP, this.creature);
            }
            goalDist = this.boardCopy.getDistance(current.fromP, this.toPoint);
            boolean legalCheck1 = !visited.contains(current.toP);
            boolean legalCheck2 = this.boardCopy.isWalkable(this.creature, current.fromP,current.toP, this.toPoint);
            boolean legalCheck3 =  goalDist <= current.stepsLeft;


            if(!previousOcc) {
                this.boardCopy.remove(current.fromP);
                this.boardCopy.put(current.toP, this.creature);
            }
            boolean legalCheck4 = this.boardCopy.isBoardConnected();
            if(!previousOcc) {
                this.boardCopy.remove(current.toP);
            }
            if(legalCheck1 && legalCheck2 && legalCheck3 && legalCheck4) {
                if(current.toP.equals(this.toPoint) && current.stepsLeft == 1) {
                    return true;
                }

                neighbors = this.boardCopy.getNeighbors(current.toP);

                // this is only checking for unoccupied cells unless it's the goal the while loop for each potential move handles the rest
                for(Point p : neighbors) {
                    if (!this.boardCopy.isOccupied(p) && !visited.contains(p) || p.equals(this.toPoint))
                        movementQueue.add(new MoveHelper(current.toP, p, current.stepsLeft - 1));
                }
            }
        }
        return false;
    }
}
