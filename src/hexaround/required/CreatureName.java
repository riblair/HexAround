/*
 * Copyright (c) 2023. Gary F. Pollice
 *
 * This files was developed for personal or educational purposes. All rights reserved.
 *
 *  You may use this software for any purpose except as follows:
 *  1) You may not submit this file without modification for any educational assignment
 *      unless it was provided to you as part of starting code that does not require modification.
 *  2) You may not remove this copyright, even if you have modified this file.
 */

package hexaround.required;

/**
 * This enumeration just provides symbols for the different types of creatures
 * that are possible in a game of HexAround. Other than being a name, or symbol,
 * there is no other semantics associated with them. The specific properties of
 * creatures with that name depend upon the specific game configuration.
 */
public enum CreatureName {
    BUTTERFLY("Butterfly"),
    CRAB("Crab"),
    DOVE("Dove"),
    DUCK("Duck"),
    GRASSHOPPER("Grasshopper"),
    HORSE("Horse"),
    HUMMINGBIRD("Hummingbird"),
    RABBIT("Rabbit"),
    SPIDER("Spider"),
    TURTLE("Turtle")
    ;

    private final String name;

    private CreatureName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
