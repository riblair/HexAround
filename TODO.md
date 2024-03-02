# TDD Tests and refactorings

Add to the following table

| ID  | Done |                           Description                           |
|-----|:----:|:---------------------------------------------------------------:|
| T1  |  X   |            Create check for butterfly before turn 5             |
| R1  |  X   |                  Rewrote logic for legalMoves                   |
| T2  |  X   |                 Write tests for butterfly logic                 |
| R2  |  X   |           Restructure package to include Board Class            |
| R3  |  X   |        Rewrite logic for Move to include MoveHandler(s)         |
| T2  |  X   |                Write check for slidable movement                |
| R4  |  X   | Write support for different movement (walking, flying, jumping) |
| R5  |  X   |               Implement (partial)game over check                |
| R6  |  X   |             Implement Max Number of Creature stuffy             | 
| R7  |  X   |                       Implemented Running                       |   
| R8  |  X   |                 Implemented Kamikaze attribute                  |
| R9  |  X   |                      Implemented Swapping                       |

## Citations

No Citations

## Design Patterns Used

I created the Board method to handle all of the logic of hexes, getting/setting, and methods not related to the state of the game itself. 

MoveHandler has a static method "createMoveHandler" that acts as a factory method. 
MoveHandler is an abstract class that represents the core functionality of handling a move action. 
Handling a move action is different for each type of creature. This method reduces branching code in the moveCreature method in HexaroundGame. 