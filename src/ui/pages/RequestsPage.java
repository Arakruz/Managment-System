package ui.pages;

import javax.swing.*;
import java.awt.event.*;
import java.math.BigDecimal;

public class RequestsPage extends TablePage implements ItemListener{
    int complete = 0;
    JCheckBox checkBox;
    JButton createCleaningRequestButton;
    JButton createMaintenanceRequestButton;
    private int applyDateFilter = 0;
    private static final String[] dateFilters = {"All Requests","Old Requests","Recent Requests"};
    private JComboBox<String> comboBox;
    private JButton completeButton;

    RequestsPage () {
        super();
    }

    @Override
    protected void staffSetUp() {
        completeButton = new JButton("Complete Request");
        completeButton.setBounds(10, 20, 80, 80);
        completeButton.addActionListener(this);
        buttonPanel.add(completeButton);

        comboBox = new JComboBox<>(dateFilters);
        comboBox.setSelectedIndex(0);
        comboBox.addActionListener(this);
        buttonPanel.add(comboBox);

        updateTableQuery();
    }

    @Override
    protected void tenantSetUp() {
        createCleaningRequestButton = new JButton("Create Cleaning Request");
        createCleaningRequestButton.setBounds(10, 20, 80, 80);
        createCleaningRequestButton.addActionListener(this);
        buttonPanel.add(createCleaningRequestButton);

        createMaintenanceRequestButton = new JButton("Create Maintenance Request");
        createMaintenanceRequestButton.setBounds(10, 20, 80, 80);
        createMaintenanceRequestButton.addActionListener(this);
        buttonPanel.add(createMaintenanceRequestButton);


        updateTableQuery();
    }

    @Override
    protected void InitializeFieldsHelper() {
        checkBox = new JCheckBox("Completed Requests");
        checkBox.setBounds(100,100, 50,50);
        checkBox.addItemListener(this);
        buttonPanel.add(checkBox);
    }

    private void updateTableQuery() {
        if (currentUser == UserType.Tenant) {
            tableQuery = "SELECT * FROM Request WHERE SIN = " + userIdentifier + " AND Complete = " + complete;
        } else if (currentUser == UserType.Staff) {
            tableQuery = "SELECT * " +
                    "FROM Request " +
                    "WHERE StaffID = 2001 " +
                    "  AND Complete = " + complete + " " +
                    "  AND ( " +
                    "            " + applyDateFilter + " = 0 " +
                    "        OR (" + applyDateFilter + " = 1 AND SubmissionDate <= CURRENT_DATE - 7) " +
                    "        OR (" + applyDateFilter + " = 2 AND SubmissionDate > CURRENT_DATE - 7) " +
                    "    ) ";
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
            complete = ((JCheckBox) e.getSource()).isSelected()? 1:0;
            updateTableQuery();
            refreshTable();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (e.getSource() == createCleaningRequestButton) {
            dataModifier.tenantRequests(userIdentifier, "Cleaning");
            refreshTable();
        } else if (e.getSource() == createMaintenanceRequestButton) {
            dataModifier.tenantRequests(userIdentifier, "Maintenance");
            refreshTable();
        } else if (e.getSource() == comboBox) {
            JComboBox cb = (JComboBox) e.getSource();
            String choice = (String) cb.getSelectedItem();

            applyDateFilter = java.util.Arrays.asList(dateFilters).indexOf(choice);
            updateTableQuery();
            refreshTable();
        } else if (e.getSource() == completeButton) {
            dataModifier.staffCompleteRequest(((BigDecimal) table.getValueAt(table.getSelectedRow(),0)).intValue(),userIdentifier);
            refreshTable();
        }
    }
}
