package solitare;

import javax.swing.SwingUtilities;

// THIS CLASS STARTS THE SOLITARE GAME.
public class Main {

    public static void main(String[] args) {

        // RUN THE WINDOW CODE ON SWING'S EVENT THREAD.
        SwingUtilities.invokeLater(() -> {

            // CREATE THE GAME WINDOW AND SHOW IT.
            SolitareGUI window = new SolitareGUI();
            window.setVisible(true);
        });
    }
}
