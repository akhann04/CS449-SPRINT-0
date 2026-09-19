package solitare2;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class SolitareGUI {

    public static void main(String[] args) {

        // Creates the main window
        JFrame frame = new JFrame("Solitaire");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creates the main panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Creates the title text
        JLabel title = new JLabel("Solitaire Game");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);

        // Creates a line under the title
        JSeparator line = new JSeparator();
        line.setMaximumSize(new Dimension(650, 10));
        panel.add(line);

        // Creates instruction text
        JLabel instructions =
                new JLabel("Choose your game settings before starting.");
        instructions.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(instructions);

        // Adds space
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Creates checkbox
        JCheckBox hints = new JCheckBox("Show Hints");
        hints.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(hints);

        // Creates text for radio buttons
        JLabel drawLabel = new JLabel("Choose Draw Mode:");
        drawLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(drawLabel);

        // Creates radio buttons
        JRadioButton drawOne = new JRadioButton("Draw 1");
        JRadioButton drawThree = new JRadioButton("Draw 3");

        drawOne.setAlignmentX(Component.CENTER_ALIGNMENT);
        drawThree.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Groups radio buttons
        ButtonGroup drawGroup = new ButtonGroup();
        drawGroup.add(drawOne);
        drawGroup.add(drawThree);

        // Default option
        drawOne.setSelected(true);

        panel.add(drawOne);
        panel.add(drawThree);

        // Creates another line
        JSeparator secondLine = new JSeparator();
        secondLine.setMaximumSize(new Dimension(650, 10));
        panel.add(secondLine);

        // Creates tableau label
        JLabel tableauLabel = new JLabel("Solitaire Table");
        tableauLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(tableauLabel);

        // Creates panel for card piles
        JPanel cardPanel = new JPanel();

        // Creates seven simple card piles
        for (int i = 1; i <= 7; i++) {

            JLabel pile = new JLabel(
                    "Pile " + i,
                    SwingConstants.CENTER
            );

            pile.setPreferredSize(new Dimension(70, 100));

            pile.setBorder(
                    BorderFactory.createLineBorder(java.awt.Color.BLACK)
            );

            cardPanel.add(pile);
        }

        panel.add(cardPanel);

        // Creates Start Game button
        JButton startButton = new JButton("Start Game");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(startButton);

        // Creates status text
        JLabel status =
                new JLabel("Choose settings and start the game.");

        status.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(status);

        // Runs when Start Game is clicked
        startButton.addActionListener(e -> {

            SolitareSettings settings =
                    new SolitareSettings();

            String drawMode =
                    settings.getDrawMode(
                            drawThree.isSelected()
                    );

            String hintSetting =
                    settings.getHintSetting(
                            hints.isSelected()
                    );

            status.setText(
                    "Game started: "
                    + drawMode
                    + " | "
                    + hintSetting
            );
        });

        // Adds panel to window
        frame.add(panel);

        // Centers the window
        frame.setLocationRelativeTo(null);

        // Shows the window
        frame.setVisible(true);
    }
}