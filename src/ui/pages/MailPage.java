package ui.pages;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.jar.JarEntry;

public class MailPage extends TablePage{
    private int applyDateFilter = 0;
    private JComboBox<String> comboBox;
    private static final String[] dateFilters = {"All Mail","Old Mail","Recent Mail"};
    private JButton assignMailButton;
    JButton cancelButton;
    JButton createButton;
    JComboBox jcd;
    JLabel tenantLabel;
    JTextField tenantIDField;
    JLabel roomLabel;
    JTextField roomNumberField;
    JLabel buildingLabel;
    JTextField buildingNameField;
    JDialog dialog;
    int tenantID;
    int roomID;

    MailPage() {
        super();
    }


    @Override
    protected void InitializeFieldsHelper() {
    }

    @Override
    protected void staffSetUp() {
        comboBox = new JComboBox<>(dateFilters);
        comboBox.setSelectedIndex(0);
        comboBox.addActionListener(this);
        buttonPanel.add(comboBox);
        updateTableQuery();

        assignMailButton = new JButton("Assign New Mail");
        assignMailButton.setBounds(10, 20, 80, 80);
        assignMailButton.addActionListener(this);
        buttonPanel.add(assignMailButton);
    }

    private void updateTableQuery() {
        tableQuery = "SELECT m.MailID, m.TenantSIN, m.RoomNumber, m.BuildingName, m.MailType, brm.DateReceived " +
                "FROM Mail m " +
                "         JOIN BuildingReceivesMail brm ON m.MailID = brm.MailID " +
                "    AND ( " +
                "                                                      " + applyDateFilter + " = 0 " +
                "                                                  OR (" + applyDateFilter + " = 1 AND brm.DateReceived <= CURRENT_DATE - 7) " +
                "                                                  OR (" + applyDateFilter + " = 2 AND brm.DateReceived > CURRENT_DATE - 7) " +
                "                                              ) " +
                "ORDER BY brm.DateReceived DESC";
    }

    @Override
    protected void tenantSetUp() {
        tableQuery = "SELECT m.MailID, m.TenantSIN, m.RoomNumber, m.BuildingName, m.MailType, brm.DateReceived " +
                     "FROM Mail m JOIN BuildingReceivesMail brm " +
                     "ON m.MailID = brm.MailID " +
                     "WHERE m.TenantSIN = " + userIdentifier + " " +
                     "ORDER BY brm.DateReceived";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Object actionCaller = e.getSource();
        if (actionCaller == backButton) {
            new HomePage();
            this.dispose();
        } else if (actionCaller == comboBox) {
            JComboBox cb = (JComboBox) actionCaller;
            String choice = (String) cb.getSelectedItem();

            applyDateFilter = java.util.Arrays.asList(dateFilters).indexOf(choice);
            updateTableQuery();
            refreshTable();
        } else if (actionCaller == assignMailButton) {
            getOptions();
        } else if (actionCaller == cancelButton) {
            dialog.dispose();
        } else if (actionCaller == createButton) {
            try {
                tenantID = Integer.parseInt(tenantIDField.getText());
                roomID = Integer.parseInt(roomNumberField.getText());
            } catch (NumberFormatException ne) {
                JOptionPane.showMessageDialog(this, "Invalid Values");
            }
            try {
            dataModifier.staffAssignMail(tenantID,roomID,buildingNameField.getText(),(String) jcd.getSelectedItem());
            refreshTable();
            } catch (Exception error) {
                JOptionPane.showMessageDialog(this, "Invalid Values");
            }
        }
    }

    private void getOptions() {

        String[] mailType = {"Parcel","Letter"};
        jcd = new JComboBox(mailType);
        tenantLabel = new JLabel("TenantID");
        tenantIDField = new JTextField(20);
        roomLabel = new JLabel("Room");
        roomNumberField = new JTextField(20);
        buildingLabel = new JLabel("Building");
        buildingNameField = new JTextField(20);



        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        createButton = new JButton("Assign");
        createButton.addActionListener(this);

        JPanel optionPanel = new JPanel();
        optionPanel.add(new JLabel("Mail Information"));

        optionPanel.add(jcd);
        optionPanel.add(tenantLabel);
        optionPanel.add(tenantIDField);
        optionPanel.add(roomLabel);
        optionPanel.add(roomNumberField);
        optionPanel.add(buildingLabel);
        optionPanel.add(buildingNameField);
        JPanel tempButtonPanel = new JPanel();
        tempButtonPanel.add(cancelButton);
        tempButtonPanel.add(createButton);
        optionPanel.add(tempButtonPanel);

        dialog = new JDialog();
        dialog.getContentPane().add(optionPanel);
        dialog.pack();
        dialog.setVisible(true);
    }
}
