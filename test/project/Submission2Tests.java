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

package project;

import hexaround.game.*;
import hexaround.required.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import hexaround.util.*;

import java.io.*;
import java.util.stream.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class Submission2Tests extends BaseTestMaster {
    private static boolean firstTest = true;

    private final int TEST_POINTS = 20;
    private static IHexAroundGameManager manager;
    @AfterAll
    static void testBreakdown() {
        firstTest = true;
    }

    @BeforeEach
    public void setup () {
        try {
            manager = HexAroundGameBuilder.buildGameManager(
                "testConfigurations/Submission2.hgc");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        manager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        manager.placeCreature(CreatureName.BUTTERFLY, 0, 1);
        manager.placeCreature(CreatureName.DOVE, 0, -1);
        manager.placeCreature(CreatureName.TURTLE, -1, 2);
        manager.placeCreature(CreatureName.TURTLE, -1, -1);
        manager.placeCreature(CreatureName.DOVE, 0, 2);
        manager.placeCreature(CreatureName.DOVE, 0, -2);
        manager.placeCreature(CreatureName.DOVE, 1, 1);
        manager.placeCreature(CreatureName.TURTLE, 1, -2);
        manager.placeCreature(CreatureName.TURTLE, 2, 0);
    }

    public Submission2Tests() {
        if (firstTest) {
            testReporter.startNewTestGroup("Submission 2", TEST_POINTS);
        }
        firstTest = false;
    }

    @ParameterizedTest
    @MethodSource("connectivitySupplier")
    void connectivityTest(String testName, int testValue, MoveResult expected,
                          CreatureName creature, int x1, int y1, int x2, int y2) {
        startTest(testName, testValue);
        MoveResponse response = manager.moveCreature(creature, x1, y1, x2, y2);
        assertEquals(expected, response.moveResult());
        markTestPassed();
    }

    static Stream<Arguments> connectivitySupplier() {
        return Stream.of(
            arguments("Valid TURTLE move", 5, MoveResult.OK, CreatureName.TURTLE, -1, -1, -1, 0),
            arguments("Invalid BUTTERFLY move", 5, MoveResult.MOVE_ERROR, CreatureName.BUTTERFLY,
                0, 0, 1, -1),
            arguments("Valid DOVE move", 5, MoveResult.OK, CreatureName.DOVE, 0, -2, 0, 3),
            arguments("Invalid DOVE move", 5, MoveResult.MOVE_ERROR, CreatureName.DOVE,
                0, -1, 0, 3)
        );
    }
}
