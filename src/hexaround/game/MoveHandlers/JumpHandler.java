package hexaround.game.MoveHandlers;

import hexaround.EnumsAndDefinitions.CreatureProperty;

public class JumpHandler extends MoveHandler {

    public JumpHandler() {
        super();
    }
    @Override
    public boolean checkLegality() {

        // 4 checks:
        boolean legalCheck1; // if distance(from,to) <= creatures max distance, cannot move
        boolean legalCheck2; // has to move in a straight line
        boolean legalCheck3; // Does not break move continuity results
        boolean legalCheck4; // if occupied (and not a special type)

        legalCheck1 = this.creature.getDef().maxDistance() >= this.boardCopy.getDistance(this.fromPoint, this.toPoint);

        int dy = (this.toPoint.y-this.fromPoint.y);
        int dx = (this.toPoint.x-this.fromPoint.x);

        legalCheck2 = dy == 0 || dx == 0 || Math.abs(dy) == Math.abs(dx);

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
