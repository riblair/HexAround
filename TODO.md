# TDD Tests and refactorings

Add to the following table

| ID  | Done |                           Description                           |
|-----|:----:|:---------------------------------------------------------------:|
| T1  |  X   |            Create check for butterfly before turn 5             |
| R1  |  X   |                  Rewrote logic for legalMoves                   |
| T2  |  X   |                 Write tests for butterfly logic                 |
| R3  |  X   |           Restructure package to include Board Class            |
| R4  |  X   |        Rewrite logic for Move to include MoveHandler(s)         |
| T2  |  X   |                Write check for slidable movement                |
| R3  |      | Write support for different movement (walking, flying, jumping) |
| T3  |      |                  Implement Kamikaze attribute                   |
| T4  |      |                    Implement game over check                    |
| R4  |      |             Implement Max Number of Creature stuffy             |                                             |

## Citations

Put citations to any code snippets you used in your implementation and
where you found it.

## Design Patterns Used

MoveHandler has a static method "createMoveHandler" that acts as a factory method. 
MoveHandler is an abstract class that represents the core functionality of handling a move action. 
Handling a move action is different for each type of creature. This method reduces branching code in the moveCreature method in HexaroundGame. 