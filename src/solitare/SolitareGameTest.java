package solitare;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// THESE TESTS CHECK THE GAME LOGIC.
public class SolitareGameTest {

    private SolitareGame game;

    // START WITH A NEW GAME BEFORE EACH TEST.
    @BeforeEach
    void setUp() {
        game = new SolitareGame();
    }

    // CHECK THE STARTING SIZE, TYPE, MARBLES, AND EMPTY CENTER.
    @Test
    void defaultBoardStartsCorrectly() {
        assertEquals("English", game.getBoardType());
        assertEquals(7, game.getBoardSize());
        assertEquals(32, game.countPegs());

        // THE CENTER SHOULD START EMPTY.
        assertEquals(SolitareGame.EMPTY, game.getCell(3, 3));

        // THIS CORNER SHOULD BE OUTSIDE THE BOARD SHAPE.
        assertEquals(SolitareGame.INVALID, game.getCell(0, 0));

        // THE TOP MIDDLE SHOULD HAVE A MARBLE.
        assertEquals(SolitareGame.PEG, game.getCell(0, 3));
    }

    // CHECK ALL NINE BOARD TYPE AND SIZE COMBINATIONS.
    @Test
    void allBoardTypesAndSizesStartCorrectly() {
        String[] types = {"English", "Hexagon", "Diamond"};
        int[] sizes = {7, 9, 11};

        // ROWS MATCH THE SIZES AND COLUMNS MATCH THE TYPES.
        int[][] expectedCounts = {
            {32, 36, 24},
            {44, 60, 40},
            {56, 90, 60}
        };

        for (int sizeIndex = 0; sizeIndex < sizes.length;
                sizeIndex++) {

            for (int typeIndex = 0; typeIndex < types.length;
                    typeIndex++) {

                int size = sizes[sizeIndex];
                String type = types[typeIndex];

                game.startNewGame(size, type);

                String description = type + ", size " + size;

                assertEquals(
                    size, game.getBoardSize(), description
                );

                assertEquals(
                    type, game.getBoardType(), description
                );

                assertEquals(
                    expectedCounts[sizeIndex][typeIndex],
                    game.countPegs(),
                    description
                );

                int center = size / 2;

                assertEquals(
                    SolitareGame.EMPTY,
                    game.getCell(center, center),
                    description
                );

                // EACH NEW BOARD SHOULD HAVE A VALID MOVE.
                assertFalse(game.isGameOver(), description);
            }
        }
    }

    // CHECK A HORIZONTAL JUMP INTO THE EMPTY CENTER.
    @Test
    void validHorizontalMoveUpdatesBoard() {
        assertTrue(game.isValidMove(3, 1, 3, 3));
        assertTrue(game.makeMove(3, 1, 3, 3));

        // THE START AND JUMPED POSITION SHOULD NOW BE EMPTY.
        assertEquals(SolitareGame.EMPTY, game.getCell(3, 1));
        assertEquals(SolitareGame.EMPTY, game.getCell(3, 2));

        // THE MARBLE SHOULD LAND IN THE CENTER.
        assertEquals(SolitareGame.PEG, game.getCell(3, 3));
        assertEquals(31, game.countPegs());
    }

    // CHECK A VERTICAL JUMP INTO THE EMPTY CENTER.
    @Test
    void validVerticalMoveUpdatesBoard() {
        assertTrue(game.isValidMove(1, 3, 3, 3));
        assertTrue(game.makeMove(1, 3, 3, 3));

        assertEquals(SolitareGame.EMPTY, game.getCell(1, 3));
        assertEquals(SolitareGame.EMPTY, game.getCell(2, 3));
        assertEquals(SolitareGame.PEG, game.getCell(3, 3));
        assertEquals(31, game.countPegs());
    }

    // CHECK THAT A DIAGONAL JUMP ALSO WORKS.
    @Test
    void validDiagonalMoveUpdatesBoard() {

        // MAKE AN EMPTY HOLE FOR THE DIAGONAL JUMP.
        assertTrue(game.makeMove(1, 3, 3, 3));

        // JUMP OVER THE MARBLE AT ROW 2, COLUMN 2.
        assertTrue(game.isValidMove(3, 1, 1, 3));
        assertTrue(game.makeMove(3, 1, 1, 3));

        assertEquals(SolitareGame.EMPTY, game.getCell(3, 1));
        assertEquals(SolitareGame.EMPTY, game.getCell(2, 2));
        assertEquals(SolitareGame.PEG, game.getCell(1, 3));
        assertEquals(30, game.countPegs());
    }

    // INVALID MOVES SHOULD LEAVE THE WHOLE BOARD UNCHANGED.
    @Test
    void invalidMovesDoNotChangeBoard() {
        int[][] originalBoard = copyBoard();

        int[][] invalidMoves = {
            {3, 0, 3, 2},   // THE DESTINATION IS OCCUPIED.
            {3, 3, 3, 1},   // THE STARTING HOLE IS EMPTY.
            {2, 3, 3, 3},   // THE MOVE IS ONLY ONE POSITION.
            {1, 2, 3, 3},   // THE JUMP DIRECTION IS NOT ALLOWED.
            {0, 0, 3, 3},   // THE START IS OUTSIDE THE BOARD SHAPE.
            {2, 2, 0, 0},   // THE END IS OUTSIDE THE BOARD SHAPE.
            {-1, 3, 3, 3},  // THE START HAS A NEGATIVE ROW.
            {1, 3, 7, 3}    // THE END IS OUTSIDE THE ARRAY.
        };

        for (int[] move : invalidMoves) {
            assertFalse(
                game.isValidMove(
                    move[0], move[1], move[2], move[3]
                )
            );

            assertFalse(
                game.makeMove(
                    move[0], move[1], move[2], move[3]
                )
            );

            assertBoardMatches(originalBoard);
        }
    }

    // A MARBLE CANNOT JUMP OVER AN EMPTY HOLE.
    @Test
    void cannotJumpOverAnEmptyHole() {
        assertTrue(game.makeMove(3, 1, 3, 3));

        // THE FIRST MOVE LEFT ROW 3, COLUMN 2 EMPTY.
        int[][] originalBoard = copyBoard();

        assertFalse(game.isValidMove(3, 3, 3, 1));
        assertFalse(game.makeMove(3, 3, 3, 1));

        assertBoardMatches(originalBoard);
        assertEquals(31, game.countPegs());
    }

    // RESET SHOULD RESTORE THE SAME BOARD SIZE AND TYPE.
    @Test
    void resetRestoresSelectedBoard() {
        game.startNewGame(9, "Diamond");

        int[][] originalBoard = copyBoard();

        assertEquals(40, game.countPegs());
        assertTrue(game.makeMove(2, 4, 4, 4));
        assertEquals(39, game.countPegs());

        game.startNewGame();

        assertEquals(9, game.getBoardSize());
        assertEquals("Diamond", game.getBoardType());
        assertEquals(40, game.countPegs());

        assertBoardMatches(originalBoard);
    }

    // A NEW GAME SHOULD NOT ALREADY BE OVER.
    @Test
    void newGameIsNotOver() {
        assertTrue(game.isValidMove(3, 1, 3, 3));
        assertFalse(game.isGameOver());
    }

    // CHECK THAT THE GAME ENDS WHEN NO LEGAL JUMPS ARE LEFT.
    @Test
    void gameEndsWhenNoValidMovesRemain() {

        // THESE MOVES END WITH SIX MARBLES AND NO VALID JUMPS.
        // ROW AND COLUMN NUMBERS START AT ZERO.
        int[][] moves = {
            {1, 3, 3, 3},
            {2, 1, 2, 3},
            {0, 2, 2, 2},
            {0, 4, 0, 2},
            {2, 3, 2, 1},
            {2, 0, 2, 2},
            {2, 4, 0, 4},
            {2, 6, 2, 4},
            {3, 1, 1, 3},
            {0, 4, 2, 2},
            {3, 2, 1, 2},
            {0, 2, 2, 2},
            {3, 4, 1, 4},
            {3, 6, 3, 4},
            {3, 3, 3, 5},
            {4, 0, 2, 0},
            {4, 2, 4, 0},
            {4, 4, 4, 2},
            {4, 5, 2, 5},
            {1, 4, 3, 6},
            {4, 6, 2, 6},
            {5, 2, 3, 2},
            {2, 2, 4, 2},
            {5, 3, 3, 1},
            {2, 0, 4, 2},
            {6, 3, 4, 5}
        };

        for (int index = 0; index < moves.length; index++) {

            // THE GAME SHOULD STILL ALLOW THE NEXT MOVE.
            assertFalse(
                game.isGameOver(),
                "Game ended before move " + (index + 1)
            );

            int[] move = moves[index];

            // CHECK THAT EVERY MOVE IN THE LIST SUCCEEDS.
            assertTrue(
                game.makeMove(
                    move[0], move[1], move[2], move[3]
                ),
                "Move " + (index + 1) + " should be legal"
            );
        }

        assertEquals(6, game.countPegs());

        // CHECK THE POSITIONS OF THE SIX REMAINING MARBLES.
        int[][] remainingPegs = {
            {2, 6}, {4, 0}, {4, 2},
            {4, 5}, {6, 2}, {6, 4}
        };

        for (int[] position : remainingPegs) {
            assertEquals(
                SolitareGame.PEG,
                game.getCell(position[0], position[1])
            );
        }

        assertTrue(game.isGameOver());
    }

    // SAVE A COPY OF THE BOARD SO WE CAN COMPARE IT LATER.
    private int[][] copyBoard() {
        int size = game.getBoardSize();
        int[][] copy = new int[size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                copy[row][col] = game.getCell(row, col);
            }
        }

        return copy;
    }

    // COMPARE EVERY POSITION WITH THE SAVED BOARD.
    private void assertBoardMatches(int[][] expected) {
        assertEquals(expected.length, game.getBoardSize());

        for (int row = 0; row < expected.length; row++) {
            for (int col = 0; col < expected[row].length; col++) {
                assertEquals(
                    expected[row][col],
                    game.getCell(row, col),
                    "Unexpected change at (" + row + ", " + col + ")"
                );
            }
        }
    }
}