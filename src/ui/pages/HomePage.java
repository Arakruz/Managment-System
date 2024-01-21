package ui.pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends Page implements ActionListener {
    private static JPanel upperPanel;
    private static JPanel buttonPanel;
    private static JLabel loggedInLabel;
    private static JButton logoutButton;
    private static JButton rentalButton;
    private static JButton requestButton;
    private static JButton mailButton;
    private static JButton vehicleButton;
    private static JButton roomButton;
    private static JButton buildingButton;

    HomePage() {
        super();
    }

    @Override
    protected void initializeFields() {
        upperPanel = new JPanel();
        buttonPanel = new JPanel();
        loggedInLabel = new JLabel("Logged-in as: ");


        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(upperPanel);
        mainPanel.add(buttonPanel);

        upperPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        loggedInLabel.setBounds(10, 20, 80, 25);

        upperPanel.add(loggedInLabel);

        rentalButton = new JButton("Rental Page");
        rentalButton.setBounds(10, 20, 80, 80);
        rentalButton.addActionListener(this);
        buttonPanel.add(rentalButton);

        requestButton = new JButton("Request Page");
        requestButton.setBounds(10, 20, 80, 80);
        requestButton.addActionListener(this);
        buttonPanel.add(requestButton);

        mailButton = new JButton("Mail Page");
        mailButton.setBounds(10, 20, 80, 80);
        mailButton.addActionListener(this);
        buttonPanel.add(mailButton);


        switch (currentUser) {
            case Admin:
                adminSetUp();
                break;
            case Tenant:
                tenantSetUp();
                break;
            case Staff:
                staffSetUp();
                break;
            default:
                //throw error
        }

        logoutButton = new JButton("Logout");
        logoutButton.setBounds(10, 20, 80, 80);
        logoutButton.addActionListener(this);
        upperPanel.add(logoutButton, FlowLayout.RIGHT);
    }


    private void adminSetUp() {
        upperPanel.add(new JLabel("admin"));
        roomButton = new JButton("Rooms Page");
        roomButton.setBounds(10, 20, 80, 80);
        roomButton.addActionListener(this);
        buttonPanel.add(roomButton);

        buildingButton = new JButton("Buildings Page");
        buildingButton.setBounds(10, 20, 80, 80);
        buildingButton.addActionListener(this);
        buttonPanel.add(buildingButton);
    }

    private void tenantSetUp() {
        upperPanel.add(new JLabel("tenant"));
    }

    private void staffSetUp() {
        upperPanel.add(new JLabel("staff"));
        vehicleButton = new JButton("Vehicle Page");
        vehicleButton.setBounds(10, 20, 80, 80);
        vehicleButton.addActionListener(this);
        buttonPanel.add(vehicleButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == logoutButton) {
            Page.currentUser = null;
            JOptionPane.showMessageDialog(new LoginPage(), "Logged out");
        } else if (source == rentalButton) {
            new RentalPage();
        } else if (source == requestButton) {
            new RequestsPage();
        } else if (source == mailButton) {
            new MailPage();
        } else if (source == vehicleButton) {
            new VehiclePage();
        } else if (source == buildingButton) {
            new BuildingPage();
        } else if (source == roomButton) {
            new RoomPage();
        }

        this.dispose();
    }
}
