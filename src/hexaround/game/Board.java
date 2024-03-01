package hexaround.game;

import hexaround.config.CreatureDefinition;
import hexaround.required.CreatureName;
import hexaround.required.CreatureProperty;

import java.awt.*;
import java.io.IOException;
import java.util.*;

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

    public void setBoard(HashMap<Point, Creature> boardMap) {
        this.boardMap = boardMap;
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
        LinkedList<Point> occupied_Neighbors = new LinkedList<>();
        Set<Point> visited = new HashSet<>();

        Set<Point> keySet = this.getBoard().keySet();

        // Ugly way to get an element from the hashset, but that's java...
        for(Point key : keySet) {
            occupied_Neighbors.add(key);
            break;
        }
        Point curKey;
        Collection<Point> neighbors;
        while(!occupied_Neighbors.isEmpty()) {
            curKey = occupied_Neighbors.pop();
            visited.add(curKey);
            neighbors = this.getNeighbors(curKey);
            for( Point neighbor : neighbors) {
                if(this.isOccupied(neighbor.x, neighbor.y) && !visited.contains(neighbor)) {
                    occupied_Neighbors.add(neighbor);
                }
            }
        }
        return visited.size() == this.boardMap.size();
    }

    /**
     * Given the x and y-coordinate of a hex, determine if there is a
     * piece on that hex on the board.
     * @param x
     * @param y
     * @return true if there is a piece on the hex, false otherwise.
     */

    public boolean isOccupied(int x, int y) {
        return this.getCreatureAt(new Point(x,y)) != null;
    }
    public boolean isOccupied(Point p) {return this.getCreatureAt(p) != null;}

    /**
     * Given the x and y-coordinates for a hex, return the name
     * of the creature on that coordinate.
     * @param p
     * @return the name of the creature on p, or null if there
     *  is no creature.
     */

    public Creature getCreatureAt(Point p) { return this.boardMap.get(p); }
    // There will be some container that has key as point, and value of creature
    // access container and see if something is there
    public Creature getCreatureAt(int x, int y) { return this.getCreatureAt(new Point(x,y));}

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

    public boolean hasProperty(int x, int y, CreatureProperty property) { return this.hasProperty(new Point(x,y), property);}

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

    public boolean isWalkable(Point fromP, Point toP) {
        if(this.isOccupied(toP)) return false;

        // for draggability, there must be at least one adjacent cell next to both fromP and toP to drag
        // this problem can be solved mathematically, but this works for the purpose of this assigment. (AGILE!)
        Collection<Point> fN = this.getNeighbors(fromP);
        Collection<Point> tN = this.getNeighbors(toP);

        Collection<Point> shared = new LinkedList<>();

        for(Point p : fN) {
            for(Point p2 : tN) {
                if(p.equals(p2)) {
                    shared.add(p);
                }
            }
        }

        if(shared.size() > 2) {
            System.out.println("Something went wrong! [186 Board]");
        }
        int freeNeighbors = 0;
        for(Point s : shared) {
            if (!this.isOccupied(s)) freeNeighbors++;
        }
        return freeNeighbors > 0;
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
    public Board copyBoard() {
        HashMap<Point, Creature> bCopy = new HashMap<>();
        Set<Point> keySet = this.boardMap.keySet();

        for(Point p: keySet) {
            try {
                    bCopy.put((Point) p.clone(), (Creature) this.boardMap.get(p).clone());
            }
            catch(CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
        return new Board(bCopy);
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
