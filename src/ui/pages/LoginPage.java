package ui.pages;

import javax.swing.*;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;

public class LoginPage extends Page implements ActionListener{

    // Increase the scope so we can introduce action listener
    private static JLabel userLabel;
    private static JLabel passwordLabel;
    private static JButton button;
    private static JTextField userInput;
    private static JTextField passwordInput;
    private static final int MAX_LOGIN_ATTEMPTS = 3;

    private int loginAttempts;

    public LoginPage() {
        super();
    }

    @Override
    protected void initializeFields() {
        userLabel = new JLabel("Username", JLabel.CENTER);
        passwordLabel = new JLabel("Password", JLabel.CENTER);
        button = new JButton("Login");
        userInput = new JTextField(40);
        passwordInput = new JPasswordField(40);

        button.addActionListener(this);

        // Initialize page components
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        mainPanel.setLayout(gb);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // place the username label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(userLabel, c);
        mainPanel.add(userLabel);

        // place the text field for the username
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(userInput, c);
        mainPanel.add(userInput);

        // place password label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(passwordLabel, c);
        mainPanel.add(passwordLabel);

        // place the password field
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(passwordInput, c);
        mainPanel.add(passwordInput);

        // place the login button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(button, c);
        mainPanel.add(button);
    }

    public void handleLoginFailed() {
        loginAttempts++;
        passwordInput.setText(""); // clear password field
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String user = userInput.getText();
        String password = passwordInput.getText();

        if (user.equals("admin") && password.equals("admin")) {
            Page.currentUser = UserType.Admin;

        } else if(user.equals("tenant") && password.equals("tenant")) {
            Page.currentUser = UserType.Tenant;
            Page.userIdentifier = 1001;

        } else if(user.equals("staff") && password.equals("staff")) {
            Page.currentUser = UserType.Staff;
            Page.userIdentifier = 2001;

        } else {
            JOptionPane.showMessageDialog(this,"Invalid credentials");
            handleLoginFailed();
            if(loginAttempts >= MAX_LOGIN_ATTEMPTS) {
                JOptionPane.showMessageDialog(new JPanel(), "Too many invalid logins, closing application :(");
                System.exit(-1);
            }
            return;
        }

        new HomePage();
        this.dispose();
    }
}



