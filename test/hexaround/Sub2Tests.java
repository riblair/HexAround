package hexaround;

import hexaround.config.*;
import hexaround.game.*;
import hexaround.required.CreatureName;
import hexaround.required.CreatureProperty;
import hexaround.required.MoveResponse;
import hexaround.required.MoveResult;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.io.*;
import java.util.HashMap;

import static hexaround.required.CreatureName.*;
import static org.junit.jupiter.api.Assertions.*;

public class Sub2Tests {
    HexAroundGame gameManager;
    TestHexAround testGameManager;
    String hgcFile = "testConfigurations/FirstConfiguration.hgc";
    String hgcFile2 = "testConfigurations/SecondConfiguration.hgc";

    public void setup () {
        try {
            testGameManager = HexAroundGameBuilder.buildTestGameManager(
                    "testConfigurations/Submission2.hgc");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        testGameManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        testGameManager.placeCreature(CreatureName.BUTTERFLY, 0, 1);

        testGameManager.placeCreature(CreatureName.DOVE, 0, -1);
        testGameManager.placeCreature(CreatureName.TURTLE, -1, 2);

        testGameManager.placeCreature(CreatureName.TURTLE, -1, -1);
        testGameManager.placeCreature(CreatureName.DOVE, 0, 2);

        testGameManager.placeCreature(CreatureName.DOVE, 0, -2);
        testGameManager.placeCreature(CreatureName.DOVE, 1, 1);

        testGameManager.placeCreature(CreatureName.TURTLE, 1, -2);
        testGameManager.placeCreature(CreatureName.TURTLE, 2, 0);
    }

    // not placing
    @Test
    void testInit() throws IOException {
        testGameManager = (TestHexAround) HexAroundGameBuilder.buildTestGameManager(hgcFile);
        Board testBoard = testGameManager.getBoard();
        HashMap<Point, Point> testRed = testGameManager.getTeamLegalSpaces(false);
        HashMap<Point, Point> testBlue = testGameManager.getTeamLegalSpaces(true);
        HashMap<CreatureName, CreatureDefinition> testDefs = testGameManager.getDefinitions();

        assertNotNull(testBoard);
        assertNotNull(testRed);
        assertNotNull(testBlue);
        assertNotNull(testDefs);

        assertTrue(testBoard.getBoard().isEmpty());
        assertTrue(testRed.isEmpty());
        assertTrue(testBlue.isEmpty());
        assertFalse(testDefs.isEmpty()); // there should NOT be 0 entries with this hgc file

    }

    @Test
    void testAddPiece() throws IOException {
        testGameManager = (TestHexAround) HexAroundGameBuilder.buildTestGameManager(hgcFile2);

        testGameManager.placeCreature(BUTTERFLY, 0,0);
        testGameManager.placeCreature(GRASSHOPPER, 1,0);
        testGameManager.placeCreature(HORSE, -1,1);

        assertEquals(testGameManager.getBoard().getCreatureAt(0,0).getDef().name(), BUTTERFLY);
        assertEquals(testGameManager.getBoard().getCreatureAt(1,0).getDef().name(), GRASSHOPPER);
        assertEquals(testGameManager.getBoard().getCreatureAt(-1,1).getDef().name(), HORSE);

        assertNull(testGameManager.getBoard().getCreatureAt(-2,-2));
    }

    @Test
    void testisOccupied() throws IOException {
        testGameManager = (TestHexAround) HexAroundGameBuilder.buildTestGameManager(hgcFile2);

        testGameManager.placeCreature(BUTTERFLY, 0,0);
        testGameManager.placeCreature(GRASSHOPPER, 1,0);
        testGameManager.placeCreature(HORSE, -1,1);

        assertTrue(testGameManager.isOccupied(0,0));
        assertTrue(testGameManager.isOccupied(1,0));
        assertTrue(testGameManager.isOccupied(-1,1));

        assertFalse(testGameManager.isOccupied(-2,-2));
    }

    @Test
    void testHasProperty() throws IOException {
        testGameManager = (TestHexAround) HexAroundGameBuilder.buildTestGameManager(hgcFile2);

        testGameManager.placeCreature(BUTTERFLY, 0,0);
        testGameManager.placeCreature(GRASSHOPPER, 1,0);
        testGameManager.placeCreature(HORSE, -1,1);

        assertTrue(testGameManager.getBoard().hasProperty(0,0, CreatureProperty.WALKING ));
        assertTrue(testGameManager.getBoard().hasProperty(0,0, CreatureProperty.QUEEN ));
        assertFalse(testGameManager.getBoard().hasProperty(0,0, CreatureProperty.JUMPING ));

        assertTrue(testGameManager.getBoard().hasProperty(1,0, CreatureProperty.JUMPING ));
        assertTrue(testGameManager.getBoard().hasProperty(1,0, CreatureProperty.INTRUDING ));
        assertFalse(testGameManager.getBoard().hasProperty(1,0, CreatureProperty.RUNNING ));

        assertTrue(testGameManager.getBoard().hasProperty(-1,1, CreatureProperty.JUMPING ));
        assertFalse(testGameManager.getBoard().hasProperty(-1,1, CreatureProperty.SWAPPING ));
    }

    @Test
    void testCanReach() throws IOException {
        testGameManager = (TestHexAround) HexAroundGameBuilder.buildTestGameManager(hgcFile2);

        testGameManager.placeCreature(BUTTERFLY, 0,0);
        testGameManager.placeCreature(GRASSHOPPER, 1,0);

        // 1 dist butteryfly test in all directions
        assertTrue(testGameManager.getBoard().canReach(0,0, 0,1));
        assertTrue(testGameManager.getBoard().canReach(0,0, 1,0));
        assertTrue(testGameManager.getBoard().canReach(0,0, 1,-1));
        assertTrue(testGameManager.getBoard().canReach(0,0, 0,-1));
        assertTrue(testGameManager.getBoard().canReach(0,0, -1,0));
        assertTrue(testGameManager.getBoard().canReach(0,0, -1,1));

        assertFalse(testGameManager.getBoard().canReach(0,0, -2,1));


        // 3 dist Grasshopper test in all directions
        assertTrue(testGameManager.getBoard().canReach(1,0, 0,3));
        assertTrue(testGameManager.getBoard().canReach(1,0, 1,3));
        assertTrue(testGameManager.getBoard().canReach(1,0, 2,2));
        assertTrue(testGameManager.getBoard().canReach(1,0, 3,1));
        assertTrue(testGameManager.getBoard().canReach(1,0, 4,0));
        assertTrue(testGameManager.getBoard().canReach(1,0, 4,-1));
        assertTrue(testGameManager.getBoard().canReach(1,0, 4,-2));
        assertTrue(testGameManager.getBoard().canReach(1,0, 4,-3));
        assertTrue(testGameManager.getBoard().canReach(1,0, 3,-3));
        assertTrue(testGameManager.getBoard().canReach(1,0, 2,-3));
        assertTrue(testGameManager.getBoard().canReach(1,0, 2,-2));
        assertTrue(testGameManager.getBoard().canReach(1,0, 1,-3));
        assertTrue(testGameManager.getBoard().canReach(1,0, 0,-2));
        assertTrue(testGameManager.getBoard().canReach(1,0, -1,-1));
        assertTrue(testGameManager.getBoard().canReach(1,0, -2,0));
        assertTrue(testGameManager.getBoard().canReach(1,0, -2,1));
        assertTrue(testGameManager.getBoard().canReach(1,0, -2,2));
        assertTrue(testGameManager.getBoard().canReach(1,0, -2,3));
        assertTrue(testGameManager.getBoard().canReach(1,0, -1,3));

        assertFalse(testGameManager.getBoard().canReach(1,0, 3,3));
        assertFalse(testGameManager.getBoard().canReach(1,0, 5,1));
        assertFalse(testGameManager.getBoard().canReach(1,0, 3,-4));
        assertFalse(testGameManager.getBoard().canReach(1,0, 0,-3));
        assertFalse(testGameManager.getBoard().canReach(1,0, -2,-1));
        assertFalse(testGameManager.getBoard().canReach(1,0, 1,-4));
    }

    @Test
    void testMoveResponsesOnPlacement() throws IOException {
        testGameManager = (TestHexAround) HexAroundGameBuilder.buildTestGameManager(hgcFile2);

        MoveResponse r = testGameManager.placeCreature(BUTTERFLY, 0,0);

        assertEquals(r.moveResult(), MoveResult.OK);
        assertEquals(r.message(), "Legal move");


        r = testGameManager.placeCreature(GRASSHOPPER, -12,-3); // ILLEGAL MOVE breaking 2nd legal clause

        assertEquals(r.moveResult(), MoveResult.MOVE_ERROR);
        r = testGameManager.placeCreature(HORSE, -1,1);

        assertEquals(r.moveResult(), MoveResult.OK);
        assertEquals(r.message(), "Legal move");

        r = testGameManager.placeCreature(DOVE, -1,1); // ILLEGAL MOVE breaking 1st legal clause

        assertEquals(r.moveResult(), MoveResult.MOVE_ERROR);
    }

    @Test
    void testPlacements() throws IOException {
        testGameManager = (TestHexAround) HexAroundGameBuilder.buildTestGameManager(hgcFile2);

        MoveResponse r;
        r = testGameManager.placeCreature(BUTTERFLY, 0,0);
        r = testGameManager.placeCreature(BUTTERFLY, 0,1);
        r = testGameManager.placeCreature(GRASSHOPPER, -1,0);
        r = testGameManager.placeCreature(GRASSHOPPER, -1,2);
        r = testGameManager.placeCreature(GRASSHOPPER, -1,1); // ILLEGAL!
        assertEquals(r.moveResult(), MoveResult.MOVE_ERROR);

        r = testGameManager.placeCreature(GRASSHOPPER, 1,-1);
        assertTrue(testGameManager.getBoard().get(new Point(1,-1)).getTeam()); // this should be a blue piece

        r = testGameManager.placeCreature(GRASSHOPPER, 1,0); // ILLEGAL!
        assertEquals(r.moveResult(), MoveResult.MOVE_ERROR);

        r = testGameManager.placeCreature(GRASSHOPPER, 1,1);
        r = testGameManager.placeCreature(GRASSHOPPER, -2,1);

        assertEquals(testGameManager.getBoard().size(), 7);

        // no pieces should be placed here, as these were illegally placed
        assertNull(testGameManager.getBoard().getCreatureAt(-1,1));
        assertNull(testGameManager.getBoard().getCreatureAt(1,0));

        assertTrue(testGameManager.getBoard().get(new Point(-2,1)).getTeam()); // this should be a blue piece
    }

    @Test
    void testLegalMoveCreature() throws IOException {
        testGameManager = (TestHexAround) HexAroundGameBuilder.buildTestGameManager(hgcFile2);

        MoveResponse r;
        r = testGameManager.placeCreature(BUTTERFLY, 0,0);
        r = testGameManager.placeCreature(BUTTERFLY, 0,1);

        r = testGameManager.placeCreature(GRASSHOPPER, -1,0);
        r = testGameManager.placeCreature(GRASSHOPPER, -1,2);

        r = testGameManager.placeCreature(GRASSHOPPER, 1,-1);
        r = testGameManager.placeCreature(GRASSHOPPER, 1,1);

        r = testGameManager.placeCreature(GRASSHOPPER, -2,1);

        r = testGameManager.moveCreature(GRASSHOPPER, -1,2, -1,1);

        assertEquals(MoveResult.OK, r.moveResult());
        assertEquals(r.message(), "Legal move");
        assertEquals(testGameManager.getBoard().size(), 7);
        assertNull(testGameManager.getBoard().getCreatureAt(-1,2));
        assertEquals(testGameManager.getBoard().getCreatureAt(-1,1).getDef().name(), GRASSHOPPER);

    }

    @Test
    void testIllegalMoveCreatures() throws IOException {
        testGameManager = (TestHexAround) HexAroundGameBuilder.buildTestGameManager(hgcFile2);

        MoveResponse r;
        r = testGameManager.placeCreature(BUTTERFLY, 0,0);
        r = testGameManager.placeCreature(BUTTERFLY, 0,1);
        r = testGameManager.placeCreature(GRASSHOPPER, -1,0);
        r = testGameManager.placeCreature(GRASSHOPPER, -1,2);
        r = testGameManager.placeCreature(GRASSHOPPER, 1,-1);
        r = testGameManager.placeCreature(GRASSHOPPER, 1,1);
        r = testGameManager.placeCreature(GRASSHOPPER, -2,1);

        r = testGameManager.moveCreature(BUTTERFLY, -10,1, -2,0); // not a creature!
        assertEquals(r.moveResult(), MoveResult.MOVE_ERROR);
        assertEquals(r.message(), "Colony is not connected, try again");

        r = testGameManager.moveCreature(GRASSHOPPER, -2,1, -5,0); // too far!
        assertEquals(r.moveResult(), MoveResult.MOVE_ERROR);
        assertEquals(r.message(), "Colony is not connected, try again");

        r = testGameManager.moveCreature(GRASSHOPPER, -2,1, -3,1); // disconnection!
        assertEquals(MoveResult.MOVE_ERROR, r.moveResult());
        assertEquals(r.message(), "Colony is not connected, try again");
        assertEquals(testGameManager.getBoard().size(), 7);
        assertNull(testGameManager.getBoard().getCreatureAt(-3,-1));
        assertEquals(testGameManager.getBoard().getCreatureAt(-2,1).getDef().name(), GRASSHOPPER);


    }

    @Test
    void testPlaceMoveCombos() throws IOException {
        testGameManager = (TestHexAround) HexAroundGameBuilder.buildTestGameManager(hgcFile2);

        MoveResponse r;
        r = testGameManager.placeCreature(BUTTERFLY, 0,0);
        r = testGameManager.placeCreature(BUTTERFLY, 0,1);
        r = testGameManager.moveCreature(BUTTERFLY, 0,0, 0, -1); // illegal disconnection

        assertEquals(r.moveResult(), MoveResult.MOVE_ERROR);
        assertEquals(r.message(), "Colony is not connected, try again");
        assertEquals(testGameManager.getBoard().size(), 2);
        assertNull(testGameManager.getBoard().getCreatureAt(0,-1));
        assertEquals(testGameManager.getBoard().getCreatureAt(0,0).getDef().name(), BUTTERFLY);

        r = testGameManager.moveCreature(BUTTERFLY, 0,0, -1, 1); // legal move

        assertEquals(r.moveResult(), MoveResult.OK);
        assertEquals(r.message(), "Legal move");

        r = testGameManager.placeCreature(GRASSHOPPER, 0,0); // illegal placement
        assertEquals(r.moveResult(), MoveResult.MOVE_ERROR);

        r = testGameManager.placeCreature(GRASSHOPPER, -1,2); // illegal placement
        assertEquals(r.moveResult(), MoveResult.MOVE_ERROR);

        r = testGameManager.placeCreature(GRASSHOPPER, 1,1);
        assertFalse(testGameManager.getBoard().get(new Point(1,1)).getTeam()); // should be a red dude

    }

    @Test
    void testButterflyLogic() throws IOException {
        testGameManager = (TestHexAround) HexAroundGameBuilder.buildTestGameManager(hgcFile2);

        MoveResponse r;
        //turn 1
        assertEquals(testGameManager.placeCreature(GRASSHOPPER, 0,0).moveResult(), MoveResult.OK);
        assertEquals(testGameManager.placeCreature(GRASSHOPPER, 0,1).moveResult(), MoveResult.OK);
        //turn 2
        assertEquals(testGameManager.placeCreature(GRASSHOPPER, -1,0).moveResult(), MoveResult.OK);
        assertEquals(testGameManager.placeCreature(GRASSHOPPER, 1,1).moveResult(), MoveResult.OK);
        // turn 3
        assertEquals(testGameManager.moveCreature(GRASSHOPPER, -1,0, -1, 1).moveResult(), MoveResult.OK);
        assertEquals(testGameManager.moveCreature(GRASSHOPPER, 1,1, 1,0).moveResult(),  MoveResult.OK);
        // turn 4 (butterfly needs to be placed)
        // should not be able to place other creature
        assertEquals(testGameManager.placeCreature(GRASSHOPPER, -1,0).moveResult(), MoveResult.MOVE_ERROR);
        // should not be able to move other creature
        assertEquals(testGameManager.moveCreature(GRASSHOPPER, -1,1, -1, 0).moveResult(), MoveResult.MOVE_ERROR);
        // finally places butterfly
        assertEquals(testGameManager.placeCreature(BUTTERFLY, -2,2).moveResult(), MoveResult.OK);
        // repeat tests for RED
        assertEquals(testGameManager.placeCreature(GRASSHOPPER, 1,1).moveResult(), MoveResult.MOVE_ERROR);
        assertEquals(testGameManager.moveCreature(GRASSHOPPER, 1,0, 1, 1).moveResult(), MoveResult.MOVE_ERROR);
        assertEquals(testGameManager.placeCreature(BUTTERFLY, 0,2).moveResult(), MoveResult.OK);

        Point blueButterfly = new Point(-2, 2);
        Point redButterfly = new Point(0, 2);
        assertEquals(testGameManager.getButterfly(true), blueButterfly);
        assertEquals(testGameManager.getButterfly(false), redButterfly);
    }

    @Test
    void testMoveWrongColorPiece() throws IOException {
        this.setup();

        assertEquals(testGameManager.moveCreature(CreatureName.TURTLE, -1, -1, -1, 0).moveResult(), MoveResult.OK);
        assertFalse(testGameManager.getCurrentTeamTurn());
        // this moves a creature of the wrong color!
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(TURTLE, 1, -2, 1, -1).moveResult());

    }

}
