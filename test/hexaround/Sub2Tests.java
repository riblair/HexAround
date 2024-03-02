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
    String hgcFile3 ="testConfigurations/Submission2.hgc";
    String hgcFile4 = "testConfigurations/Level1.hgc";

    String hgcFile5 = "testConfigurations/thirdConfig.hgc";

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

    public void setup1 () {
        try {
            testGameManager = HexAroundGameBuilder.buildTestGameManager(hgcFile4);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        testGameManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        testGameManager.placeCreature(CreatureName.BUTTERFLY, 0, 1);

        testGameManager.placeCreature(CreatureName.DOVE, 0, -1);
        testGameManager.placeCreature(CreatureName.CRAB, -1, 2);

        testGameManager.placeCreature(CreatureName.HORSE, -1, -1);
        testGameManager.placeCreature(CreatureName.DOVE, 0, 2);

        testGameManager.placeCreature(CreatureName.DOVE, 0, -2);
        testGameManager.placeCreature(CreatureName.DOVE, 1, 1);

        testGameManager.placeCreature(CreatureName.GRASSHOPPER, 1, -2);
        testGameManager.placeCreature(CreatureName.GRASSHOPPER, 2, 0);
    }

    public void setup2 () {
        try {
            testGameManager = HexAroundGameBuilder.buildTestGameManager(hgcFile5);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        testGameManager.placeCreature(CreatureName.BUTTERFLY, 0, 0);
        testGameManager.placeCreature(CreatureName.BUTTERFLY, 0, 1);

        testGameManager.placeCreature(CreatureName.DOVE, 0, -1);
        testGameManager.placeCreature(CreatureName.CRAB, -1, 2);

        testGameManager.placeCreature(CreatureName.HORSE, -1, -1);
        testGameManager.placeCreature(CreatureName.DOVE, 0, 2);

        testGameManager.placeCreature(CreatureName.DOVE, 0, -2);
        testGameManager.placeCreature(CreatureName.DOVE, 1, 1);

        testGameManager.placeCreature(CreatureName.CRAB, 1, -2);
        testGameManager.placeCreature(CreatureName.CRAB, 2, 0);
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
        testGameManager.placeCreature(GRASSHOPPER, -1,1);

        assertEquals(testGameManager.getBoard().getCreatureAt(0,0).getDef().name(), BUTTERFLY);
        assertEquals(testGameManager.getBoard().getCreatureAt(1,0).getDef().name(), GRASSHOPPER);
        assertEquals(testGameManager.getBoard().getCreatureAt(-1,1).getDef().name(), GRASSHOPPER);

        assertNull(testGameManager.getBoard().getCreatureAt(-2,-2));
    }

    @Test
    void testisOccupied() throws IOException {
        testGameManager = (TestHexAround) HexAroundGameBuilder.buildTestGameManager(hgcFile2);

        testGameManager.placeCreature(BUTTERFLY, 0,0);
        testGameManager.placeCreature(GRASSHOPPER, 1,0);
        testGameManager.placeCreature(GRASSHOPPER, -1,1);

        assertTrue(testGameManager.getBoard().isOccupied(0,0));
        assertTrue(testGameManager.getBoard().isOccupied(1,0));
        assertTrue(testGameManager.getBoard().isOccupied(-1,1));

        assertFalse(testGameManager.getBoard().isOccupied(-2,-2));
    }

    @Test
    void testHasProperty() throws IOException {
        testGameManager = (TestHexAround) HexAroundGameBuilder.buildTestGameManager(hgcFile2);

        testGameManager.placeCreature(BUTTERFLY, 0,0);
        testGameManager.placeCreature(GRASSHOPPER, 1,0);
        testGameManager.placeCreature(GRASSHOPPER, -1,1);

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
        r = testGameManager.placeCreature(GRASSHOPPER, -1,1);

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

        r = testGameManager.moveCreature(GRASSHOPPER, -1,2, -1,1); // grasshopper movement not supported

        assertEquals(MoveResult.OK, r.moveResult());
        assertEquals(r.message(), "Legal move");
        assertEquals(testGameManager.getBoard().size(), 7);
        assertNull(testGameManager.getBoard().getCreatureAt(-1,2));
        assertEquals(testGameManager.getBoard().getCreatureAt(-1,1).getDef().name(), GRASSHOPPER);

    }

    @Test
    void testIllegalMoveCreatures() throws IOException { // wont work because movement specifics not implemented
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

        r = testGameManager.moveCreature(GRASSHOPPER, -2,1, -5,0); // too far!
        assertEquals(r.moveResult(), MoveResult.MOVE_ERROR);

        r = testGameManager.moveCreature(GRASSHOPPER, -2,1, -3,1); // disconnection!
        assertEquals(MoveResult.MOVE_ERROR, r.moveResult());
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

        assertEquals(MoveResult.MOVE_ERROR, r.moveResult());
        assertEquals(r.message(), "Colony is not connected, try again");

        assertEquals(testGameManager.getBoard().size(), 2);
        assertNull(testGameManager.getBoard().getCreatureAt(0,-1));
        assertEquals(testGameManager.getBoard().getCreatureAt(0,0).getDef().name(), BUTTERFLY);


        //cannot move wrong creature
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(DOVE, 0, 0,-1, 1).moveResult());
        r = testGameManager.moveCreature(BUTTERFLY, 0,0, -1, 1);

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
        assertEquals(MoveResult.OK, testGameManager.placeCreature(GRASSHOPPER, 1,1).moveResult());
        // turn 3
        assertEquals(testGameManager.moveCreature(GRASSHOPPER, -1,0, -1, 1).moveResult(), MoveResult.OK); // cannot move grasshopper yet
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

    @Test
    void testIsWalkable() throws IOException {
        this.setup();
        // true cases
        assertTrue(testGameManager.getBoard().isWalkable(testGameManager.getBoard().get(new Point(-1,2)), new Point(-1,2), new Point(-1, 1), new Point(-1, 1)));
        assertTrue(testGameManager.getBoard().isWalkable(testGameManager.getBoard().get(new Point(1,-2)), new Point(1,-2), new Point(1, -1), new Point(1, -1)));
        assertTrue(testGameManager.getBoard().isWalkable(testGameManager.getBoard().get(new Point(2,0)), new Point(2,0), new Point(1, 0), new Point(1, 0)));
        // false cases
        assertFalse(testGameManager.getBoard().isWalkable(testGameManager.getBoard().get(new Point(1,1)), new Point(1,1), new Point(1, 0), new Point(1, 0)));
        assertFalse(testGameManager.getBoard().isWalkable(testGameManager.getBoard().get(new Point(2,0)), new Point(2,0), new Point(1, 1), new Point(1, 1)));
    }
    @Test
    void testFlying() throws IOException {
        this.setup();
        // true cases
        assertEquals(MoveResult.OK, testGameManager.moveCreature(DOVE,0,-2,-1,0).moveResult()); // blue legal move
        assertTrue(testGameManager.getBoard().isOccupied(-1,0));
        assertFalse(testGameManager.getBoard().isOccupied(0,-2));
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(DOVE,0,2,-1,0).moveResult()); // illegal cause occupied (unsure why failing)
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(DOVE,0,2,-3,0).moveResult()); // illegal cause too far
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(DOVE,0,2,-3,0).moveResult()); // illegal cause too far
        assertEquals(MoveResult.OK, testGameManager.moveCreature(DOVE,0,2,-1,1).moveResult()); // red legal move
        assertTrue(testGameManager.getBoard().isOccupied(-1,1));
        assertFalse(testGameManager.getBoard().isOccupied(0,2));
    }

    //designed to test the case where a flying creature cannot move because it is surrounded
    @Test
    void testFlying2() throws IOException {
        this.setup();
        // true cases
        assertEquals(MoveResult.OK, testGameManager.placeCreature(TURTLE, -2, 0).moveResult());
        assertEquals(MoveResult.OK, testGameManager.placeCreature(TURTLE, 2, -1).moveResult());
        assertEquals(MoveResult.OK, testGameManager.placeCreature(TURTLE, -2, 1).moveResult());
        assertEquals(MoveResult.OK, testGameManager.moveCreature(DOVE,0,2,-1,1).moveResult()); // red legal move
        assertEquals(MoveResult.OK, testGameManager.moveCreature(DOVE,0,-2,-1,0).moveResult()); // this dove is now surrounded
        assertEquals(MoveResult.OK, testGameManager.placeCreature(DOVE, 0,2).moveResult());
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(DOVE,-1,0,0,-2).moveResult()); // this dove cannot move
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(DOVE,-1,0,1,-1).moveResult()); // this dove cannot move
        assertEquals(MoveResult.OK, testGameManager.placeCreature(DOVE, 0,-2).moveResult()); // random legal move from blue to confirm working.
    }

    // tests walkable creatures ability to traverse with both challenging legal moves, and testing edge cases for non-legal moves
    @Test
    void testWalking() throws IOException {
        this.setup();
        // legal moves
        assertEquals(MoveResult.OK, testGameManager.moveCreature(TURTLE, -1, -1, -1, 0).moveResult());
        assertEquals(MoveResult.OK, testGameManager.moveCreature(TURTLE, 2, 0, 2, -2).moveResult());

        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(TURTLE, -1, 0, -1, 3).moveResult()); // out of range
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(TURTLE, -1, 0, -2, 1).moveResult()); // disconnection
        assertEquals(MoveResult.OK, testGameManager.moveCreature(TURTLE, -1, 0, -2, 3).moveResult()); // legal move!

        assertEquals(MoveResult.OK, testGameManager.placeCreature(DOVE, 1,2).moveResult());

        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(TURTLE, -1, -2, 1, -1).moveResult()); // Not Walkable! (sliding-wise)
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(TURTLE, -1, -2, 1, -3).moveResult()); // disconnection
    }

    @Test
    void testJumping() throws IOException {
        testGameManager = (TestHexAround) HexAroundGameBuilder.buildTestGameManager(hgcFile2);
        assertEquals(MoveResult.OK, testGameManager.placeCreature(BUTTERFLY, 0, 0).moveResult());
        assertEquals(MoveResult.OK, testGameManager.placeCreature(BUTTERFLY, 0, 1).moveResult());
        assertEquals(MoveResult.OK, testGameManager.placeCreature(GRASSHOPPER, 0, -1).moveResult());
        assertEquals(MoveResult.OK, testGameManager.placeCreature(GRASSHOPPER, -1, 2).moveResult());

        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(GRASSHOPPER, 0, -1, -1,1).moveResult());
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(GRASSHOPPER, 0, -1, 2,-1).moveResult());
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(GRASSHOPPER, 0, -1, 0,3).moveResult());
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(GRASSHOPPER, 0, -1, 0,1).moveResult());
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(GRASSHOPPER, 0, -1, 1,1).moveResult());

        assertEquals(MoveResult.OK, testGameManager.moveCreature(GRASSHOPPER, 0, -1, 0,2).moveResult());

        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(GRASSHOPPER, -1, 2, 1,1).moveResult());
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(GRASSHOPPER, -1, 2, 1,-1).moveResult());
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(GRASSHOPPER, -1, 2, 0,-1).moveResult());

        assertEquals(MoveResult.OK, testGameManager.moveCreature(GRASSHOPPER, -1, 2, 1,0).moveResult());
    }

    @Test
    void testBlueWinner() throws IOException {
        this.setup();

        assertEquals(MoveResult.OK, testGameManager.moveCreature(DOVE, 0, -2, -1,1).moveResult());
        assertEquals(MoveResult.BLUE_WON, testGameManager.moveCreature(TURTLE, 2, 0, 1,0).moveResult());
    }
    @Test
    void testRedWinner() throws IOException {
        this.setup();
        // legal moves
        assertEquals(MoveResult.OK, testGameManager.moveCreature(DOVE, 0, -2, -1,0).moveResult());
        assertEquals(MoveResult.OK, testGameManager.moveCreature(TURTLE, -1, 2, -1,1).moveResult());

        assertEquals(MoveResult.OK, testGameManager.moveCreature(TURTLE, 1, -2, 1,-1).moveResult());
        assertEquals(MoveResult.RED_WON, testGameManager.moveCreature(TURTLE, 2, 0, 1,0).moveResult());
    }

    @Test
    void testTie() throws IOException {
        this.setup();
        // legal moves
        assertEquals(MoveResult.OK, testGameManager.moveCreature(TURTLE, -1, -1, -1,0).moveResult());
        assertEquals(MoveResult.OK, testGameManager.placeCreature(DOVE, 1,2).moveResult());

        assertEquals(MoveResult.OK, testGameManager.moveCreature(DOVE, 0, -2, -1,1).moveResult());
        assertEquals(MoveResult.OK, testGameManager.placeCreature(DOVE, 0,3).moveResult());

        assertEquals(MoveResult.OK, testGameManager.moveCreature(TURTLE, 1, -2, 1,-1).moveResult());
        assertEquals(MoveResult.DRAW, testGameManager.moveCreature(TURTLE, 2, 0, 1,0).moveResult());
    }

    @Test
    void testCreatureLimits() throws IOException {
        testGameManager = HexAroundGameBuilder.buildTestGameManager(hgcFile3);

        assertEquals(MoveResult.OK, testGameManager.placeCreature(BUTTERFLY, 0, 0).moveResult());
        assertEquals(MoveResult.OK, testGameManager.placeCreature(BUTTERFLY, 0, 1).moveResult());

        // test placing butterfly again!
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.placeCreature(BUTTERFLY, 0, -1).moveResult());
        // test placing a creature not in the definition
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.placeCreature(CRAB, 0, -1).moveResult());

        // place creatures until we have no more of a type left
        assertEquals(MoveResult.OK, testGameManager.placeCreature(DOVE, 0, -1).moveResult());
        assertEquals(MoveResult.OK, testGameManager.placeCreature(TURTLE, -1, 2).moveResult());

        assertEquals(MoveResult.OK, testGameManager.placeCreature(DOVE, 0, -2).moveResult());
        assertEquals(MoveResult.OK, testGameManager.placeCreature(TURTLE, 0, 2).moveResult());

        assertEquals(MoveResult.OK, testGameManager.placeCreature(DOVE, 0, -3).moveResult());
        assertEquals(MoveResult.OK, testGameManager.placeCreature(TURTLE, 0, 3).moveResult());

        //out of doves!
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.placeCreature(DOVE, 0, -4).moveResult());
        assertEquals(MoveResult.OK, testGameManager.placeCreature(TURTLE, 0, -4).moveResult());
        //out of turtles!
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.placeCreature(TURTLE, 0, 4).moveResult());
        assertEquals(MoveResult.OK, testGameManager.placeCreature(DOVE, 0, 4).moveResult());
    }

    /* side effect of testing config with only blue defined */
    @Test
    void testRunning() throws IOException {
        testGameManager = (TestHexAround) HexAroundGameBuilder.buildTestGameManager(hgcFile4);

        assertEquals(MoveResult.OK, testGameManager.placeCreature(BUTTERFLY, 0, 0).moveResult());
        assertEquals(MoveResult.OK, testGameManager.placeCreature(BUTTERFLY, 0, 1).moveResult());

        assertEquals(MoveResult.OK, testGameManager.placeCreature(HORSE, 0, -1).moveResult());
        assertEquals(MoveResult.OK, testGameManager.placeCreature(HORSE, 0, 2).moveResult());

        // invalid moves with the horse
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(HORSE, 0, -1, -1,0).moveResult()); // too short!
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(HORSE, 0, -1, -1,1).moveResult()); // too short!
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(HORSE, 0, -1, -1,2).moveResult()); // too short!
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(HORSE, 0, -1, 0,3).moveResult()); // too far!
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(HORSE, 0, -1, -2, 1).moveResult()); // disconnection

        assertEquals(MoveResult.OK, testGameManager.moveCreature(HORSE, 0, -1, -1,3).moveResult()); // just right!

    }

    @Test
    void testKamikazeFlying() throws IOException {
        this.setup1();
        // true cases
        assertEquals(10, testGameManager.getBoard().size());
        assertEquals(MoveResult.OK, testGameManager.moveCreature(DOVE, 0, -2, -1,-1).moveResult()); // capture ally piece
        assertEquals(testGameManager.getBoard().get(new Point(-1,-1)).getDef().name(),DOVE);
        assertEquals(9, testGameManager.getBoard().size());
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(DOVE, 1, 1, 2,0).moveResult()); // would cause disconnection
        assertEquals(MoveResult.OK, testGameManager.moveCreature(DOVE, 0, 2, 0,-1).moveResult()); // Capture enemy piece
        assertEquals(testGameManager.getBoard().get(new Point(0,-1)).getDef().name(),DOVE);
        assertEquals(8, testGameManager.getBoard().size());
    }

    // also tests needing to place butterfly after capture!
    @Test
    void testKamikazeWalking() throws IOException {
        this.setup2();
        // true cases
        assertEquals(10, testGameManager.getBoard().size());
        assertEquals(MoveResult.OK, testGameManager.moveCreature(CRAB, 1, -2, 0,-2).moveResult()); // capture ally piece
        assertEquals(testGameManager.getBoard().get(new Point(0, -2)).getDef().name(),CRAB);
        assertEquals(9, testGameManager.getBoard().size());

        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(CRAB, 2, 0, 0,1).moveResult()); // this move breaks slidability while capturing
        assertEquals(MoveResult.OK, testGameManager.moveCreature(CRAB, 2, 0, 0,0).moveResult()); // Capture enemy butterfly
        assertEquals(testGameManager.getBoard().get(new Point(0,0)).getDef().name(),CRAB);
        assertEquals(8, testGameManager.getBoard().size());

        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(CRAB, 0, -2, -1,-1).moveResult()); // needs to place butteryfly
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.placeCreature(DOVE, 0, -3).moveResult()); // needs to place butterfly
        assertEquals(MoveResult.OK, testGameManager.placeCreature(BUTTERFLY, 0, -3).moveResult()); // needs to place butterfly
    }
    // test with a movement of 0
    @Test
    void testNoMovement() throws IOException {
        this.setup();
        // true cases
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(TURTLE, -1, -1, -1,-1).moveResult());
        assertEquals(MoveResult.MOVE_ERROR, testGameManager.moveCreature(DOVE, 0, -2, 0,-2).moveResult());
    }



}
