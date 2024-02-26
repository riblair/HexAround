package hexaround.game;

import hexaround.config.CreatureDefinition;
import hexaround.required.CreatureName;
import hexaround.required.CreatureProperty;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Board {
    private HashMap<Point, Creature> boardMap;
    public Board() {
        this.boardMap = new HashMap<>();
    }
    public Board(HashMap<Point, Creature> boardMap) {
        this.boardMap = boardMap;
    }

    /**
     * Given the x and y-coordinates for a hex, return the name
     * of the creature on that coordinate.
     * @param p
     * @return the name of the creature on p, or null if there
     *  is no creature.
     */

    public Creature getCreatureAt(Point p) {
        // There will be some container that has key as point, and value of creature
        // access container and see if something is there
        return this.boardMap.get(p);
    }

    public Creature getCreatureAt(int x, int y) {
        return this.getCreatureAt(new Point(x,y));
    }

    /**
     * Determine if the creature at the x and y-coordinates has the specified
     * property. You can assume that there will be a creature at the specified
     * location.
     * @param p
     * @param property the property to look for.
     * @return true if the creature at (x, y) has the specified property,
     *  false otherwise.
     */

    public boolean hasProperty(Point p, CreatureProperty property) {
        // call get creature,
        Creature creature = this.getCreatureAt(p);

        CreatureDefinition def = creature.getDef();

        for (CreatureProperty prop : def.properties()) {
            if(prop.toString().equals(property.toString())) return true;
        }
        return false;
    }

    public boolean hasProperty(int x, int y, CreatureProperty property) {
        return this.hasProperty(new Point(x,y), property);
    }

    /**
     * Given the coordinates for two hexes, (x1, y1) and (x2, y2),
     * return whether the piece at (x1, y1) could reach the other
     * hex.
     * You can assume that there will be a piece at (x1, y1).
     * The distance is just the distance between the two hexes. You
     * do not have to do any other checking.
     * @param pFrom
     * @param pTo
     * @return true if the distance between the two hexes is less
     * than or equal to the maximum distance property for the piece
     * at (x1, y1). Return false otherwise.
     */

    public int getDistance(Point pFrom, Point pTo) {
        int yDelta = pTo.y - pFrom.y;
        int xDelta = pTo.x - pFrom.x;

        // two cases, if (+,-) / (-,+) then the distance is yDelta + xDelta
        // else distance is the highest of the two values

        int totalDist = max(abs(xDelta), abs(yDelta));

        if(Integer.signum(yDelta) == Integer.signum(xDelta)) {
            totalDist = abs(yDelta) + abs(xDelta);
        }
        return totalDist;
    }

    public boolean canReach(Point pFrom, Point pTo) {
        Creature creature = this.getCreatureAt(pFrom);
        // get the creatures max distance from definition

        int dist = this.getDistance(pFrom, pTo);
        return dist <= creature.getDef().maxDistance();
    }
    public boolean canReach(int x1, int y1, int x2, int y2) {
        return this.canReach(new Point(x1,y1), new Point(x2,y2));
    }

    public Collection<Point> getNeighbors(Point p) {
        Collection<Point> neighbors = new LinkedList<>();
        neighbors.add(new Point(p.x, p.y-1));
        neighbors.add(new Point(p.x+1, p.y-1));
        neighbors.add(new Point(p.x+1, p.y));
        neighbors.add(new Point(p.x, p.y+1));
        neighbors.add(new Point(p.x-1, p.y+1));
        neighbors.add(new Point(p.x-1, p.y));
        return neighbors;
    }

    // Returns true if no enemy creatures are adjacent, else return false
    public boolean enemyAdjacency(boolean blue, Point unoccupiedPoint) {
        Collection<Point> neighbors = this.getNeighbors(unoccupiedPoint);
        Creature subNCreature;
        for (Point subN : neighbors) {
            subNCreature = this.boardMap.get(subN);
            if (subNCreature != null && subNCreature.getTeam() != blue) return false;
        }
        return true;
    }

    public HashMap<Point, Creature> getBoard() {
        return this.boardMap;
    }

    //creates a deep copy of the current hashmap
    public HashMap<Point, Creature> copy() {
        return this.boardMap; // TODO: make a deep copy
    }

    public int size() {
        return boardMap.size();
    }

    public void put(Point p, Creature c) {
        this.boardMap.put(p,c);
    }

    public Creature get(Point p) {
        return this.boardMap.get(p);
    }

    public void remove(Point p) {
        this.boardMap.remove(p);
    }

}
