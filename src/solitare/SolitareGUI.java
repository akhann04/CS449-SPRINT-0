package solitare;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// THIS CLASS HANDLES THE WINDOW AND PLAYER CLICKS.
public class SolitareGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private final SolitareGame game;
    private final JPanel boardPanel;
    private final JLabel statusLabel;

    private final JComboBox<String> boardTypeBox;
    private final JComboBox<Integer> boardSizeBox;

    // NEGATIVE ONE MEANS NO MARBLE IS SELECTED.
    private int selectedRow = -1;
    private int selectedCol = -1;

    private boolean gameOver = false;

    public SolitareGUI() {
        game = new SolitareGame();

        // SET UP THE MAIN WINDOW.
        setTitle("Peg Solitare");
        setSize(760, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel(
            "Peg Solitare",
            SwingConstants.CENTER
        );

        title.setFont(new Font("Dialog", Font.BOLD, 24));

        // ADD THE BOARD TYPE OPTIONS.
        String[] boardTypes = {
            "English", "Hexagon", "Diamond"
        };

        boardTypeBox = new JComboBox<>(boardTypes);

        // ADD THE BOARD SIZE OPTIONS.
        Integer[] boardSizes = {7, 9, 11};
        boardSizeBox = new JComboBox<>(boardSizes);

        boardTypeBox.setSelectedItem(game.getBoardType());
        boardSizeBox.setSelectedItem(game.getBoardSize());

        // PUT THE OPTIONS AT THE TOP OF THE WINDOW.
        JPanel settingsPanel = new JPanel();

        settingsPanel.add(new JLabel("Board type:"));
        settingsPanel.add(boardTypeBox);
        settingsPanel.add(new JLabel("Board size:"));
        settingsPanel.add(boardSizeBox);

        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(settingsPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // THIS PANEL HOLDS THE BOARD BUTTONS.
        boardPanel = new JPanel();

        boardPanel.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        add(boardPanel, BorderLayout.CENTER);

        // SHOW THE INSTRUCTIONS AND MARBLE COUNT.
        statusLabel = new JLabel("", SwingConstants.CENTER);
        updateStatus("Select a marble.");

        JButton newGameButton = new JButton("New Game");

        newGameButton.addActionListener(event -> startNewGame());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newGameButton);

        JPanel controls = new JPanel(new BorderLayout(5, 5));

        controls.add(statusLabel, BorderLayout.NORTH);
        controls.add(buttonPanel, BorderLayout.SOUTH);

        add(controls, BorderLayout.SOUTH);

        displayBoard();
    }

    // START A GAME USING THE SELECTED OPTIONS.
    private void startNewGame() {
        int size = (Integer) boardSizeBox.getSelectedItem();
        String type = (String) boardTypeBox.getSelectedItem();

        game.startNewGame(size, type);

        // CLEAR THE OLD MARBLE SELECTION.
        selectedRow = -1;
        selectedCol = -1;

        gameOver = game.isGameOver();

        updateStatus("Select a marble.");
        displayBoard();

        if (gameOver) {
            updateStatus("Game over.");
            showGameOverMessage();
        }
    }

    // UPDATE THE MESSAGE BELOW THE BOARD.
    private void updateStatus(String message) {
        statusLabel.setText(
            message + "  Marbles left: " + game.countPegs()
        );
    }

    // DRAW THE BOARD USING THE GAME DATA.
    private void displayBoard() {
        boardPanel.removeAll();

        int size = game.getBoardSize();

        boardPanel.setLayout(
            new GridLayout(size, size, 5, 5)
        );

        // USE SMALLER MARBLES FOR BIGGER BOARDS.
        int fontSize = Math.max(18, 36 - size);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int cell = game.getCell(row, col);

                // LEAVE BLANK SPACES OUTSIDE THE BOARD SHAPE.
                if (cell == SolitareGame.INVALID) {
                    boardPanel.add(new JPanel());
                    continue;
                }

                // MAKE A BUTTON FOR EACH PLAYABLE HOLE.
                JButton hole = new JButton();

                hole.setFont(
                    new Font("Dialog", Font.BOLD, fontSize)
                );

                hole.setForeground(new Color(0, 120, 60));
                hole.setFocusPainted(false);
                hole.setMargin(new Insets(0, 0, 0, 0));

                // SHOW A CIRCLE WHEN A MARBLE IS PRESENT.
                if (cell == SolitareGame.PEG) {
                    hole.setText("\u25CF");
                }

                // OUTLINE THE SELECTED MARBLE IN ORANGE.
                if (row == selectedRow && col == selectedCol) {
                    hole.setBorder(
                        BorderFactory.createLineBorder(
                            Color.ORANGE, 4
                        )
                    );
                }

                // REMEMBER WHICH POSITION THIS BUTTON USES.
                final int clickedRow = row;
                final int clickedCol = col;

                hole.addActionListener(event ->
                    handleClick(clickedRow, clickedCol)
                );

                boardPanel.add(hole);
            }
        }

        // REFRESH THE BOARD AFTER THE BUTTONS ARE ADDED.
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    // HANDLE THE POSITION THE PLAYER CLICKED.
    private void handleClick(int row, int col) {

        // STOP MOVES AFTER THE GAME ENDS.
        if (gameOver) {
            return;
        }

        if (game.getCell(row, col) == SolitareGame.PEG) {

            // CLICK THE SAME MARBLE AGAIN TO CLEAR THE SELECTION.
            if (row == selectedRow && col == selectedCol) {
                selectedRow = -1;
                selectedCol = -1;

                updateStatus("Select a marble.");

            } else {

                // SELECT THE MARBLE THE PLAYER CLICKED.
                selectedRow = row;
                selectedCol = col;

                updateStatus("Now select an empty hole.");
            }

        } else if (selectedRow == -1) {

            // THE PLAYER NEEDS TO SELECT A MARBLE FIRST.
            updateStatus("Select a marble first.");

        } else if (game.makeMove(
                selectedRow, selectedCol, row, col)) {

            // CLEAR THE SELECTION AFTER A VALID MOVE.
            selectedRow = -1;
            selectedCol = -1;

            // CHECK WHETHER ANY VALID MOVES REMAIN.
            gameOver = game.isGameOver();

            if (gameOver) {
                updateStatus("Game over.");
            } else {
                updateStatus("Move completed. Select a marble.");
            }

            displayBoard();

            if (gameOver) {
                showGameOverMessage();
            }

            return;

        } else {

            // LET THE PLAYER KNOW THE MOVE WAS INVALID.
            updateStatus(
                "Invalid move. Choose another empty hole."
            );
        }

        displayBoard();
    }

    // SHOW THE FINAL RESULT.
    private void showGameOverMessage() {
        int remaining = game.countPegs();
        String message;

        if (remaining == 1) {
            message = "You won! Only one marble remains.";
        } else {
            message = "No more valid moves!\n"
                + "Marbles remaining: " + remaining;
        }

        JOptionPane.showMessageDialog(
            this,
            message,
            "Game Over",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}