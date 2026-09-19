import java.awt.Dimension;
import javax.swing.*;

public class PasswordGUI {

    public static void main(String[] args) {
    	
    	
    	//CREATING THE WINDOW
        JFrame frame = new JFrame("Password Length Checker");

        //SETTING THE SIZE OF THE WINDOW 
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // CREATING THE TITILE TEXT 
        JLabel title = new JLabel("Password Length Checker");
        panel.add(title);

        JSeparator line = new JSeparator();
        line.setMaximumSize(new Dimension(350, 10));
        panel.add(line);

        // CREATING THE PASSSWORD BOX 
        JLabel passwordLabel = new JLabel("Enter Password:");
        panel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(250, 30));
        panel.add(passwordField);

        
        JCheckBox showPassword = new JCheckBox("Show Password");
        panel.add(showPassword);


        JLabel accountLabel = new JLabel("Account Type:");
        panel.add(accountLabel);

        JRadioButton personal = new JRadioButton("Personal");
        JRadioButton school = new JRadioButton("School");

        ButtonGroup group = new ButtonGroup();
        group.add(personal);
        group.add(school);

        panel.add(personal);
        panel.add(school);

        // CREATING THE RADIO BUTTON 
        JButton checkButton = new JButton("Check Password");
        panel.add(checkButton);

        // Result
        JLabel result = new JLabel("");
        panel.add(result);

      
        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('•');
            }
        });

        // Check password button
        checkButton.addActionListener(e -> {

            String password =
                    new String(passwordField.getPassword());

            Main checker = new Main();

            result.setText(checker.checkLength(password));
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}
