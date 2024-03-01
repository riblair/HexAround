package hexaround.game;

import hexaround.config.CreatureDefinition;
import hexaround.config.GameConfiguration;
import hexaround.game.MoveHandlers.MoveHandler;
import hexaround.game.MoveHandlers.WalkHandler;
import hexaround.required.CreatureName;
import hexaround.required.CreatureProperty;
import hexaround.required.MoveResponse;
import hexaround.required.MoveResult;

import java.awt.*;
import java.util.*;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class HexAroundGame implements IHexAroundGameManager {

    // for keeping track of whose turn it is currently. (Not needed now)
    protected boolean blueTurn;
    protected int turnCounter;
    protected Point blueButterfly;
    protected Point redButterfly;

    // board keeps track of placed pieces with framework <Point, piece>
    protected Board board;

    // legalSpaces keeps track of free spaces for red pieces with framework <Point, Point>
    protected  HashMap<Point, Point> redLegalSpaces;

    // legalSpaces keeps track of free spaces for blue pieces with framework <Point, Point>
    protected HashMap<Point, Point> blueLegalSpaces;

    protected HashMap<CreatureName, CreatureDefinition> definitions;

    public HexAroundGame() {

    }

    public void initGame(GameConfiguration config) {
        definitions = new HashMap<>();
        config.creatures().forEach( (c) -> definitions.put(c.name(),c)); // adds every Creature Definition to the Map

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
    private boolean butterflyCheck() {

        if(this.turnCounter < 4) {
            return true;
        }
        if(this.blueTurn) {
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

//        System.out.printf("Surrounding cells: %d\n", numOccupied);
        return numOccupied == 6;

    }

    private void updateLegalMoves() {

        // new methodology:
        // reset the legal moves list
        // for each occupied square in the set
        //      get all neighbors
        //      for each neighbor
        //          if unoccupied
        //              get all neighbor_neighbors
        //                  if no enemy adjacency
        //                      add to that teams legal move set

        /* reset both sets */
        this.blueLegalSpaces = new HashMap<>();
        this.redLegalSpaces = new HashMap<>();

        Collection<Point> occupiedSquares = this.board.getBoard().keySet();

        for(Point occupiedSquare: occupiedSquares) {
            Collection<Point> neighbors = this.board.getNeighbors(occupiedSquare);
            Creature c = this.board.get(occupiedSquare);
            for(Point neighbor : neighbors) {
                if(!this.board.isOccupied(neighbor) && this.board.enemyAdjacency(c.getTeam(), neighbor)) {
                        if(c.getTeam()) this.blueLegalSpaces.put(neighbor,neighbor);
                        else this.redLegalSpaces.put(neighbor, neighbor);
                    }
                }
            }

        if(this.blueTurn && this.turnCounter == 1) this.redLegalSpaces = this.blueLegalSpaces;
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
        boolean legalCheck3 = true;
        boolean legalCheck4;
        boolean legalCheck5;

        // make sure creature is valid for game config
        legalCheck1 = this.definitions.containsKey(creature);

        if(this.blueTurn) legalCheck2 = this.blueLegalSpaces.containsKey(p);
        else              legalCheck2 = this.redLegalSpaces.containsKey(p);

//        System.out.printf("Its turn %d\n", this.turnCounter);

        // the only case where we ignore legalSpace Map is when its blues first turn, as any placement is valid
        legalCheck2 |= ((this.turnCounter < 2)  && this.blueTurn);

        // if butterfly not placed by turn 4, and they are trying to place it this turn, legalCheck4 == true

        legalCheck4 = this.butterflyCheck() || creature.equals(CreatureName.BUTTERFLY);

        // if butterfly is already placed however, legalCheck5 == false
        if(blueTurn) legalCheck5 = !(creature.equals(CreatureName.BUTTERFLY) && (this.blueButterfly != null));
        else legalCheck5 = !(creature.equals(CreatureName.BUTTERFLY) && (this.redButterfly != null));

        if(!legalCheck1 || !legalCheck2 || !legalCheck3 || !legalCheck4 || !legalCheck5) {
            System.out.printf("""
                    Legal1: %b
                    Legal2: %b
                    Legal3: %b
                    Legal4: %b
                    Legal5: %b
                    """, legalCheck1, legalCheck2, legalCheck3, legalCheck4, legalCheck5);

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

            // check for game over after turn
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
        boolean legalCheck1 = this.butterflyCheck();
        boolean legalCheck2 = c != null;

        if(!legalCheck1 || !legalCheck2) {
            System.out.printf("""
                    Legal1: %b
                    Legal2: %b
                    """, legalCheck1, legalCheck2);
            mr = new MoveResponse(MoveResult.MOVE_ERROR, "Colony is not connected, try again");
            return mr;
        }

        boolean legalCheck3 = c.getDef().name().toString().equals(creature.toString());
        boolean legalCheck4 = c.getTeam() == this.blueTurn;

        if(!legalCheck3 || !legalCheck4) {
            System.out.printf("""
                    Legal3: %b
                    Legal4: %b
                    """, legalCheck3, legalCheck4);
            mr = new MoveResponse(MoveResult.MOVE_ERROR, "Colony is not connected, try again");
            return mr;
        }

        MoveHandler moveHandler = MoveHandler.createMoveHandler(c.getDef());
        moveHandler.initMoveHandler(this.blueTurn, this.board.copyBoard(), c, fromP, toP);

        if(moveHandler.checkLegality()) {
            mr = new MoveResponse(MoveResult.OK, "Legal move");
            this.board.setBoard(moveHandler.getMoveResult());
            this.updateLegalMoves();
            this.handleChangeTurn();

            // check for game over after turn
            mr = this.checkGameOver(mr);

        }
        else {
            // remove the piece from the new spot, and put him back in the old position
//            this.board.remove(toP);
//            this.board.put(fromP, c);
            mr = new MoveResponse(MoveResult.MOVE_ERROR, "Colony is not connected, try again");
        }
        return mr;
    }

    // if no game-over condition is met, return the same response
    // else return a game over message with the corresponding team winning / drawing
    private MoveResponse checkGameOver(MoveResponse mr) {

        MoveResponse winResponse;

        boolean blueWins = this.isButterflySurrounded(false);
//        boolean blueWins2 = playerCannotPlaceOrMoveCheck(false);
        boolean redWins = this.isButterflySurrounded(true);
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
