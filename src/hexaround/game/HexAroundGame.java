package hexaround.game;

import hexaround.config.CreatureDefinition;
import hexaround.config.GameConfiguration;
import hexaround.game.MoveHandlers.MoveHandler;
import hexaround.required.CreatureName;
import hexaround.required.MoveResponse;
import hexaround.required.MoveResult;

import java.awt.*;
import java.util.*;

public class HexAroundGame implements IHexAroundGameManager {

    // for keeping track of whose turn it is currently.
    protected boolean blueTurn;
    protected int turnCounter;
    protected Point blueButterfly;
    protected Point redButterfly;

    // board keeps track of placed pieces with framework <Point, piece>
    protected Board board;

    // legalSpaces keeps track of free spaces for blue pieces with framework <Point, Point>
    protected HashMap<Point, Point> blueLegalSpaces;
    protected  HashMap<Point, Point> redLegalSpaces;

    // blue/redCreatures keeps track of currently placed creatures for both sides with the Array representing the max & current amount on the board
    protected Map<CreatureName, int[]> blueCreatures;
    protected Map<CreatureName, int[]> redCreatures;

    protected HashMap<CreatureName, CreatureDefinition> definitions;

    public HexAroundGame() {

    }

    public void initGame(GameConfiguration config) {
        definitions = new HashMap<>();
        config.creatures().forEach( (c) -> definitions.put(c.name(),c)); // adds every Creature Definition to the Map

        config.players().forEach( (pc) ->
                {
                    switch (pc.Player()) {
                        case BLUE -> {
                            this.blueCreatures = new HashMap<>();
                            for (CreatureName c : pc.creatures().keySet()) {
                                int[] maxCur = {pc.creatures().get(c), pc.creatures().get(c)};
                                this.blueCreatures.put(c, maxCur);
                            }
                        }
                        case RED -> {
                            this.redCreatures = new HashMap<>();
                            for (CreatureName c : pc.creatures().keySet()) {
                                int[] maxCur = {pc.creatures().get(c), pc.creatures().get(c)};
                                this.redCreatures.put(c, maxCur);
                            }
                        }
                    }
                }
        );

        if(this.blueCreatures == null) {
            this.blueCreatures = this.redCreatures;
        } else if (this.redCreatures == null) {
            this.redCreatures = this.blueCreatures;
        }

        this.board = new Board();
        this.redLegalSpaces = new HashMap<>();
        this.blueLegalSpaces = new HashMap<>();
        this.blueTurn = true;
        this.turnCounter = 1; // game starts on turn 1 not 0,
        this.blueButterfly = null;
        this.redButterfly = null;
    }

    // You cannot take a turn (move / place) after turn 4 without the butterfly being placed
    /**
     *
     * @return true if both conditions are met (butterfly placed before turn 4)
     */
    private boolean butterflyCheck(boolean team) {

        if(this.turnCounter < 4) {
            return true;
        }
        if(team) {
            return this.blueButterfly != null;
        }
        else {
            return this.redButterfly != null;
        }
    }


    private boolean isButterflySurrounded(boolean blue) {
        Point p = blue ? this.blueButterfly : this.redButterfly;

        if(p == null) {
            return false;
        }

        Collection<Point> neighbors = this.board.getNeighbors(p);
        int numOccupied = 0;

        for(Point n : neighbors) {
            if(this.board.isOccupied(n)) numOccupied++;
        }

        return numOccupied == 6;
    }

    private void updateLegalMoves() {

        // reset the legal moves list
        // for each occupied square in the set
        //      get all neighbors
        //      for each neighbor
        //          if unoccupied
        //              get all neighbor_neighbors
        //                  if no enemy adjacency
        //                      add to that teams legal move set

        // ALSO NEED TO UPDATE BUTTERFLY HERE
        /* reset both sets */
        this.blueLegalSpaces = new HashMap<>();
        this.redLegalSpaces = new HashMap<>();

        this.blueButterfly = null;
        this.redButterfly = null;

        HashMap<CreatureName, int[]> resetBlueCount = new HashMap<>();
        HashMap<CreatureName, int[]> resetRedCount = new HashMap<>();

        int[] updated;

        for(CreatureName cn : this.blueCreatures.keySet()) {
            updated = this.blueCreatures.get(cn);
            updated[1] = updated[0];
            resetBlueCount.put(cn, updated);
        }

        for(CreatureName cn : this.redCreatures.keySet()) {
            updated = this.redCreatures.get(cn);
            updated[1] = updated[0];
            resetRedCount.put(cn, updated);
        }

        for(Point occupiedSquare: this.board.getBoard().keySet()) {
            Creature c = this.board.get(occupiedSquare);

            if(c.getDef().name().equals(CreatureName.BUTTERFLY)) {
                if(c.getTeam()) this.blueButterfly = occupiedSquare;
                else this.redButterfly = occupiedSquare;
            }

            // updating placed creature count.
            if(c.getTeam()) {
                updated = resetBlueCount.get(c.getDef().name());
                updated[1]--;
                resetBlueCount.put(c.getDef().name(), updated);
            }
            else {
                updated = resetRedCount.get(c.getDef().name());
                updated[1]--;
                resetRedCount.put(c.getDef().name(), updated);
            }

            // updating legal moves
            Collection<Point> neighbors = this.board.getNeighbors(occupiedSquare);

            for(Point neighbor : neighbors) {
                if(!this.board.isOccupied(neighbor) && this.board.enemyAdjacency(c.getTeam(), neighbor)) {
                        if(c.getTeam()) this.blueLegalSpaces.put(neighbor,neighbor);
                        else this.redLegalSpaces.put(neighbor, neighbor);
                    }
                }
            }

        // this allows red to place their first piece anywhere adjacent to blues first place
        if(this.blueTurn && this.turnCounter == 1) this.redLegalSpaces = this.blueLegalSpaces;

        this.blueCreatures = resetBlueCount;
        this.redCreatures = resetRedCount;
//        System.out.print("Blue Creatures: ");
//
//        for (CreatureName cn: this.blueCreatures.keySet()) {
//            System.out.printf("{ %s, [%d,%d] }, ", cn.toString(), this.blueCreatures.get(cn)[0], this.blueCreatures.get(cn)[1]);
//        }
//        System.out.println();
//        System.out.print("red Creatures: ");
//        for (CreatureName cn: this.redCreatures.keySet()) {
//            System.out.printf(" { %s, [%d,%d] }, ", cn.toString(), this.redCreatures.get(cn)[0], this.redCreatures.get(cn)[1]);
//        }
//        System.out.println();
        // for debugging. implement debug flag?
        //        System.out.println("blueLegalSpaces: " + this.blueLegalSpaces.keySet());
        //        System.out.println("redLegalSpaces: " + this.redLegalSpaces.keySet());

    }

    private void handleChangeTurn() {
        if(!this.blueTurn) {
            this.turnCounter++;
        }
        this.blueTurn = !this.blueTurn;
    }

    private boolean canPlaceCreature(CreatureName creature, boolean bt) {
        if(!this.definitions.containsKey(creature) || (!this.blueCreatures.containsKey(creature) && bt) || (!this.redCreatures.containsKey(creature) && !bt)) {
            return false;
        }

        int placeable = 0;
        if(bt) {
            placeable = this.blueCreatures.get(creature)[1];
        }
        else {
            placeable = this.redCreatures.get(creature)[1];
        }
        return placeable > 0;
    }

    /**
     * Report the legality of placing param creature at param position
     * If Legal, place it and report MoveResponse OK with message "Legal move"
     * If illegal, Move_ERROR with message "??"
     * @param creature
     * @param x
     * @param y
     * @return a response, or null. It is not going to be checked.
     */


    @Override
    public MoveResponse placeCreature(CreatureName creature, int x, int y) {
        Point p = new Point(x,y);
        MoveResponse response;
        // check if legal creature, check if legal position, check butterfly cases
        boolean legalCheck1;
        boolean legalCheck2;
        boolean legalCheck3;
        boolean legalCheck4 = true; // might now be covered by the first boolean check.

        legalCheck1 = this.canPlaceCreature(creature, this.blueTurn);

        if(this.blueTurn) legalCheck2 = this.blueLegalSpaces.containsKey(p);
        else              legalCheck2 = this.redLegalSpaces.containsKey(p);

        // the only case where we ignore legalSpace Map is when its blues first turn, as any placement is valid
        legalCheck2 |= ((this.turnCounter < 2)  && this.blueTurn);

        // if butterfly not placed by turn 4, and they are trying to place it this turn, legalCheck3 == true

        legalCheck3 = this.butterflyCheck(this.blueTurn) || creature.equals(CreatureName.BUTTERFLY);

        //        // if butterfly is already placed however, legalCheck4 == false
        //        if(blueTurn) legalCheck4 = !(creature.equals(CreatureName.BUTTERFLY) && (this.blueButterfly != null));
        //        else legalCheck4 = !(creature.equals(CreatureName.BUTTERFLY) && (this.redButterfly != null));

        if(!legalCheck1 || !legalCheck2 || !legalCheck3 || !legalCheck4) {
            System.out.printf("""
                    Legal1: %b
                    Legal2: %b
                    Legal3: %b
                    Legal4: %b
                    """, legalCheck1, legalCheck2, legalCheck3, legalCheck4);

            response = new MoveResponse(MoveResult.MOVE_ERROR, "Illegal Placement; invalid creature or position!");
        }
        else {
            Creature creature_to_place = new Creature(this.blueTurn, this.definitions.get(creature));

            board.put(p, creature_to_place);

            if(creature_to_place.getDef().name().equals(CreatureName.BUTTERFLY)) {
                if(this.blueTurn) this.blueButterfly = p;
                else this.redButterfly = p;
            }

            this.updateLegalMoves();
            this.handleChangeTurn();

            response = new MoveResponse(MoveResult.OK, "Legal move");

            // check for game over after turn // may need to move this above handle change turn
            response = this.checkGameOver(response);
        }
        return response;
    }

    /**
     * Check if creature at from point can reach (distance-wise)
     * @param creature
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @return
     */

    @Override
    public MoveResponse moveCreature(CreatureName creature, int fromX, int fromY, int toX, int toY) {

        MoveResponse mr;
        Point fromP = new Point(fromX, fromY);
        Point toP = new Point(toX, toY);
        Creature c = this.board.get(fromP);

        /* All checks within this section are used irrespective of attributes */

        // if its turn 4 and they are trying to move a creature w/out butterfly placed, we throw an invalid move response
        boolean legalCheck1 = this.butterflyCheck(this.blueTurn);
        boolean legalCheck2 = c != null;

        if(!legalCheck1 || !legalCheck2) {
            System.out.printf("""
                    Legal1: %b
                    Legal2: %b
                    """, legalCheck1, legalCheck2);
            mr = new MoveResponse(MoveResult.MOVE_ERROR, "Move Error: (Butterfly not placed? Creature not at Square?)");
            return mr;
        }

        boolean legalCheck3 = c.getDef().name().toString().equals(creature.toString());
        boolean legalCheck4 = c.getTeam() == this.blueTurn;
        boolean legalCheck5 = this.board.getDistance(fromP,toP) > 0;

        if(!legalCheck3 || !legalCheck4 || !legalCheck5) {
            System.out.printf("""
                    Legal3: %b
                    Legal4: %b
                    Legal5: %b
                    """, legalCheck3, legalCheck4, legalCheck5);
            mr = new MoveResponse(MoveResult.MOVE_ERROR, "Other Move Error (Correct creature called?, Correct Turn/Team?, Distance > 0?)");
            return mr;
        }

        MoveHandler moveHandler = MoveHandler.createMoveHandler(c.getDef());
        moveHandler.initMoveHandler(this.blueTurn, this.board.copyBoard(), c, fromP, toP);

        if(moveHandler.checkLegality()) {
            mr = new MoveResponse(MoveResult.OK, "Legal move");
            this.board.setBoard(moveHandler.getMoveResult());
            this.updateLegalMoves();
            this.handleChangeTurn();

            // check for game over after turn // may need to move this above handle change turn
            mr = this.checkGameOver(mr);
        }
        else {
            mr = new MoveResponse(MoveResult.MOVE_ERROR, "Colony is not connected, try again");
        }
        return mr;
    }

    // if no game-over condition is met, return the same response
    // else return a game over message with the corresponding team winning / drawing
    // ASSUMES THAT THE TURN HAS CHANGED. THIS SHOULD RUN AFTER HandleGameOver()
    private MoveResponse checkGameOver(MoveResponse mr) {
        MoveResponse winResponse;

        boolean blueWins = this.isButterflySurrounded(false);
//        boolean blueWins2 = playerCannotPlaceOrMoveCheck(false);
        boolean redWins = this.isButterflySurrounded(true);

        blueWins |= this.butterflyCheck(false) && this.redLegalSpaces.size() == 0 && !this.blueTurn;
        redWins |= this.butterflyCheck(true) && this.blueLegalSpaces.size() == 0 && this.blueTurn;;
//        boolean redWins2 = playerCannotPlaceOrMoveCheck(false);

        if(blueWins && redWins) { // tie
            winResponse = new MoveResponse(MoveResult.DRAW, "The game is tied!");
        }
        else if(blueWins) { // blue wins
            winResponse = new MoveResponse(MoveResult.BLUE_WON, "Blue is the winner!");
        }
        else if(redWins) {// red wins
            winResponse = new MoveResponse(MoveResult.RED_WON, "Red is the winner!");
        }
        else { // neither win
            winResponse = mr;
        }

        return winResponse;
    }


}
