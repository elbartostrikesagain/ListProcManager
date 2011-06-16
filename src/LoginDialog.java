import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class LoginDialog {
	String password;

    LoginDialog(String emailInput) {
        getIDandPassword(emailInput);
    }

    // modal dialog to get user ID and password
    static String[] ConnectOptionNames = { "Login", "Cancel" };
    static String   ConnectTitle = "Email Login for ";
    void getIDandPassword(String emailInput) {
        JPanel      connectionPanel;

 	// Create the labels and text fields.
	JLabel     emailLabel = new JLabel("Email:   ", JLabel.RIGHT);
 	JTextField emailField = new JTextField(emailInput);
 	emailField.setEditable(false);
	JLabel     passwordLabel = new JLabel("Password:   ", JLabel.RIGHT);
	JTextField passwordField = new JPasswordField("");
	connectionPanel = new JPanel(false);
	connectionPanel.setLayout(new BoxLayout(connectionPanel,
						BoxLayout.X_AXIS));
	JPanel namePanel = new JPanel(false);
	namePanel.setLayout(new GridLayout(0, 1));
	namePanel.add(emailLabel);
	namePanel.add(passwordLabel);
	JPanel fieldPanel = new JPanel(false);
	fieldPanel.setLayout(new GridLayout(0, 1));
	fieldPanel.add(emailField);
	fieldPanel.add(passwordField);
	connectionPanel.add(namePanel);
	connectionPanel.add(fieldPanel);

        // Connect or quit
	if(JOptionPane.showOptionDialog(null, connectionPanel, 
                                        ConnectTitle+emailInput,
                                        JOptionPane.OK_CANCEL_OPTION, 
                                        JOptionPane.INFORMATION_MESSAGE,
                                        null, ConnectOptionNames, 
                                        ConnectOptionNames[0]) != 0) 
        {
	    System.exit(0);
	}
        password = passwordField.getText();
    }
}
