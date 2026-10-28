package solitare;

// THIS CLASS HANDLES THE BOARD AND THE GAME RULES.
public class SolitareGame {

    // THESE NUMBERS TELL US WHAT EACH BOARD POSITION CONTAINS.
    public static final int INVALID = -1;
    public static final int EMPTY = 0;
    public static final int PEG = 1;

    private int[][] board;

    // START WITH A SIZE 7 ENGLISH BOARD.
    private int boardSize = 7;
    private String boardType = "English";

    public SolitareGame() {
        startNewGame();
    }

    // RESET THE GAME USING THE CURRENT BOARD SIZE AND TYPE.
    public void startNewGame() {
        startNewGame(boardSize, boardType);
    }

    // CREATE A NEW BOARD USING THE PLAYER'S CHOICES.
    public void startNewGame(int size, String type) {

        // ONLY ALLOW THE SIZES USED IN THIS GAME.
        if (size != 7 && size != 9 && size != 11) {
            throw new IllegalArgumentException(
                "Board size must be 7, 9, or 11."
            );
        }

        // MAKE SURE THE BOARD TYPE IS ONE OF THE THREE OPTIONS.
        if (!"English".equals(type)
                && !"Hexagon".equals(type)
                && !"Diamond".equals(type)) {
            throw new IllegalArgumentException(
                "Choose English, Hexagon, or Diamond."
            );
        }

        boardSize = size;
        boardType = type;
        board = new int[size][size];

        int center = size / 2;

        // CHECK EACH POSITION TO BUILD THE CHOSEN BOARD SHAPE.
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {

                boolean playable;

                if (type.equals("English")) {

                    // THE MIDDLE THREE ROWS AND COLUMNS MAKE A CROSS.
                    playable =
                        Math.abs(row - center) <= 1
                        || Math.abs(col - center) <= 1;

                } else if (type.equals("Diamond")) {

                    // KEEP POSITIONS CLOSE ENOUGH TO THE CENTER.
                    playable =
                        Math.abs(row - center)
                        + Math.abs(col - center) <= center;

                } else {

                    // USE DISTANCES FROM THE CENTER TO MAKE A HEXAGON.
                    int q = col - center;
                    int r = row - center;

                    playable =
                        Math.abs(q) <= center
                        && Math.abs(r) <= center
                        && Math.abs(q + r) <= center;
                }

                // FILL PLAYABLE POSITIONS WITH MARBLES.
                board[row][col] = playable ? PEG : INVALID;
            }
        }

        // THE CENTER HOLE ALWAYS STARTS EMPTY.
        board[center][center] = EMPTY;
    }

    // RETURN WHAT IS STORED AT THIS POSITION.
    public int getCell(int row, int col) {
        return board[row][col];
    }

    public int getBoardSize() {
        return boardSize;
    }

    public String getBoardType() {
        return boardType;
    }

    // CHECK THAT THE POSITION IS INSIDE THE ARRAY.
    private boolean isInsideBoard(int row, int col) {
        return row >= 0 && row < boardSize
            && col >= 0 && col < boardSize;
    }

    // CHECK THE MOVE BEFORE CHANGING THE BOARD.
    public boolean isValidMove(int fromRow, int fromCol,
                               int toRow, int toCol) {

        // BOTH POSITIONS MUST BE INSIDE THE ARRAY.
        if (!isInsideBoard(fromRow, fromCol)
                || !isInsideBoard(toRow, toCol)) {
            return false;
        }

        // START ON A MARBLE AND LAND IN AN EMPTY HOLE.
        if (board[fromRow][fromCol] != PEG
                || board[toRow][toCol] != EMPTY) {
            return false;
        }

        int rowDistance = Math.abs(toRow - fromRow);
        int colDistance = Math.abs(toCol - fromCol);

        // A JUMP MUST GO TWO POSITIONS IN AN ALLOWED DIRECTION.
        boolean horizontal =
            rowDistance == 0 && colDistance == 2;

        boolean vertical =
            rowDistance == 2 && colDistance == 0;

        boolean diagonal =
            rowDistance == 2 && colDistance == 2;

        if (!(horizontal || vertical || diagonal)) {
            return false;
        }

        // FIND THE POSITION BETWEEN THE START AND END.
        int middleRow = (fromRow + toRow) / 2;
        int middleCol = (fromCol + toCol) / 2;

        // THERE MUST BE A MARBLE TO JUMP OVER.
        return board[middleRow][middleCol] == PEG;
    }

    // COMPLETE THE MOVE IF IT IS VALID.
    public boolean makeMove(int fromRow, int fromCol,
                            int toRow, int toCol) {

        if (!isValidMove(fromRow, fromCol, toRow, toCol)) {
            return false;
        }

        int middleRow = (fromRow + toRow) / 2;
        int middleCol = (fromCol + toCol) / 2;

        // EMPTY THE START AND REMOVE THE JUMPED MARBLE.
        board[fromRow][fromCol] = EMPTY;
        board[middleRow][middleCol] = EMPTY;

        // PUT THE MOVING MARBLE IN THE DESTINATION.
        board[toRow][toCol] = PEG;

        return true;
    }

    // COUNT HOW MANY MARBLES ARE STILL ON THE BOARD.
    public int countPegs() {
        int count = 0;

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (board[row][col] == PEG) {
                    count++;
                }
            }
        }

        return count;
    }

    // THE GAME IS OVER WHEN NO VALID MOVES ARE LEFT.
    public boolean isGameOver() {

        // CHECK HORIZONTAL, VERTICAL, AND DIAGONAL JUMPS.
        int[][] directions = {
            {-2, 0},
            {2, 0},
            {0, -2},
            {0, 2},
            {-2, -2},
            {-2, 2},
            {2, -2},
            {2, 2}
        };

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {

                // ONLY CHECK POSITIONS THAT HAVE A MARBLE.
                if (board[row][col] != PEG) {
                    continue;
                }

                for (int[] direction : directions) {
                    int toRow = row + direction[0];
                    int toCol = col + direction[1];

                    // ONE VALID MOVE MEANS THE GAME CAN CONTINUE.
                    if (isValidMove(row, col, toRow, toCol)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}