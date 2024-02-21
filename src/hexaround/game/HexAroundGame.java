package hexaround.game;

import hexaround.config.CreatureDefinition;
import hexaround.config.GameConfiguration;
import hexaround.required.CreatureName;
import hexaround.required.CreatureProperty;
import hexaround.required.MoveResponse;
import hexaround.required.MoveResult;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import static java.lang.Math.abs;
import static java.lang.Math.max;

// TODO: Write a check for colony connectedness
// TODO: Write all of move logic

public class HexAroundGame implements IHexAroundGameManager {

    // for keeping track of whose turn it is currently. (Not needed now)
    protected boolean blueTurn;
    protected int turnCounter;

    // board keeps track of placed pieces with framework <Point, piece>
    protected HashMap<Point, Creature> board;

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

        this.board = new HashMap<>();
        this.redLegalSpaces = new HashMap<>();
        this.blueLegalSpaces = new HashMap<>();
        this.blueTurn = true;
        this.turnCounter = 1; // game starts on turn 1 not 0,
    }

    /**
     * Given the x and y-coordinates for a hex, return the name
     * of the creature on that coordinate.
     * @param x
     * @param y
     * @return the name of the creature on (x, y), or null if there
     *  is no creature.
     */
    public CreatureName getCreatureAt(int x, int y) {
        // There will be some container that has key as point, and value of creature
        // access container and see if something is there
        Creature c =  board.get(new Point(x,y));
        if( c == null) {
            return null;
        }
        return c.getDef().name();
    }

    /**
     * Determine if the creature at the x and y-coordinates has the specified
     * property. You can assume that there will be a creature at the specified
     * location.
     * @param x
     * @param y
     * @param property the property to look for.
     * @return true if the creature at (x, y) has the specified property,
     *  false otherwise.
     */

    public boolean hasProperty(int x, int y, CreatureProperty property) {
        // call get creature,
        CreatureName creature = this.getCreatureAt(x,y);

        CreatureDefinition def = definitions.get(creature);

        for (CreatureProperty prop : def.properties()) {
            if(prop.toString().equals(property.toString())) return true;
        }
        return false;
    }

    /**
     * Given the x and y-coordinate of a hex, determine if there is a
     * piece on that hex on the board.
     * @param x
     * @param y
     * @return true if there is a piece on the hex, false otherwise.
     */

    public boolean isOccupied(int x, int y) {
        return this.getCreatureAt(x,y) != null;
    }

    private int getDistance(int x1, int y1, int x2, int y2) {
        int yDelta = y2 - y1;
        int xDelta = x2 - x1;

        // two cases, if (+,-) / (-,+) then the distance is yDelta + xDelta
        // else distance is the highest of the two values

        int totalDist = max(abs(xDelta), abs(yDelta));

        if(Integer.signum(yDelta) == Integer.signum(xDelta)) {
            totalDist = abs(yDelta) + abs(xDelta);
        }
        return totalDist;
    }

    private Collection<Point> getNeighbors(Point p) {
        Collection<Point> neighbors = new LinkedList<>();
        neighbors.add(new Point(p.x, p.y-1));
        neighbors.add(new Point(p.x+1, p.y-1));
        neighbors.add(new Point(p.x+1, p.y));
        neighbors.add(new Point(p.x, p.y+1));
        neighbors.add(new Point(p.x-1, p.y+1));
        neighbors.add(new Point(p.x-1, p.y));
        return neighbors;
    }

    /**
     * Given the coordinates for two hexes, (x1, y1) and (x2, y2),
     * return whether the piece at (x1, y1) could reach the other
     * hex.
     * You can assume that there will be a piece at (x1, y1).
     * The distance is just the distance between the two hexes. You
     * do not have to do any other checking.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return true if the distance between the two hexes is less
     * than or equal to the maximum distance property for the piece
     * at (x1, y1). Return false otherwise.
     */

    public boolean canReach(int x1, int y1, int x2, int y2) {
        Point p = new Point(x1,y1);
        Creature creature = board.get(p);
        // get the creatures max distance from definition

        int dist = this.getDistance(x1,y1,x2,y2);
        return dist <= creature.getDef().maxDistance();
    }

    // Returns true if no enemy creatures are adjacent, else return false
    private boolean enemyAdjacency(boolean blue, Point unoccupiedPoint) {
        Collection<Point> neighbors = getNeighbors(unoccupiedPoint);
        Creature subNCreature;
        for (Point subN : neighbors) {
            subNCreature = this.board.get(subN);
            if (subNCreature != null && subNCreature.getTeam() != blue) return false;

        }
        return true;
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

        Collection<Point> occupiedSquares =this.board.keySet();

        for(Point occupiedSquare: occupiedSquares) {
            Collection<Point> neighbors = this.getNeighbors(occupiedSquare);
            Creature c = this.board.get(occupiedSquare);
            for(Point neighbor : neighbors) {
                if(!this.isOccupied(neighbor.x,neighbor.y) && this.enemyAdjacency(c.getTeam(), neighbor)) {
                        if(c.getTeam()) this.blueLegalSpaces.put(neighbor,neighbor);
                        else this.redLegalSpaces.put(neighbor, neighbor);
                    }
                }
            }

        if(this.blueTurn && this.turnCounter == 1) this.redLegalSpaces = this.blueLegalSpaces;
        // for debugging. implement debug flag?
        System.out.println("blueLegalSpaces: " + this.blueLegalSpaces.keySet());
        System.out.println("redLegalSpaces: " + this.redLegalSpaces.keySet());

    }

    public boolean isBoardConnected() {
        /* Graph search:
        * things to keep track of -> Occupied_neighbors (search queue), visited (Points)
        * start at any of the points,
        *
        * while occupied_Neighbors not empty
        *   pop of queue, add to visited,
        *   for all neighbors:
        *       if occupied by unvisited node:
        *           add to queue
        *
        * if visited.size == board.size, return true, else return false
        * */

        return false;
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

        // make sure creature is valid for game config
        legalCheck1 = this.definitions.containsKey(creature);

        if(this.blueTurn) legalCheck2 = this.blueLegalSpaces.containsKey(p);
        else              legalCheck2 = this.redLegalSpaces.containsKey(p);

        // the only case where we ignore legalSpace Map is when its blues first turn, as any placement is valid
        legalCheck2 |= ((this.turnCounter < 2)  && this.blueTurn);

        // Here is where we check for butterfly b4 turn 4.
        // legalCheck3 = ...

        if(!legalCheck1 || !legalCheck2 || !legalCheck3) {
            System.out.printf("""
                    Legal1: %b
                    Legal2: %b
                    Legal3: %b
                    """, legalCheck1, legalCheck2, legalCheck3);

            response = new MoveResponse(MoveResult.MOVE_ERROR, "Illegal Placement; invalid creature or position!");
        }
        else {
            Creature creature_to_place = new Creature(this.blueTurn, this.definitions.get(creature));

            board.put(p, creature_to_place);
            this.updateLegalMoves();

            this.handleChangeTurn();

            response = new MoveResponse(MoveResult.OK, "Legal move");

            // by definition of a legal placement, checking if the colony is connected is not necessary.

            // check for game over (impl. later)

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

        //check if a piece with given name is at given from location
        //check if to square is occupied (FOR NOW THIS IS A GOOD CHECK)
        //check if distance from/to > maxDist,
        //move piece on copy board
        // check for connectivity
        // if ANY of these checks fail, return move_error with string
        // otherwise, return OK and update actual board

        // TODO: Write this, update doc
        return null;
    }
}
