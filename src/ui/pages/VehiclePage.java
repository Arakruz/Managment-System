package ui.pages;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;

public class VehiclePage extends TablePage implements ItemListener {
    JCheckBox checkBox;
    JButton returnButton;
    JButton borrowButton;
    private boolean borrowingView;
    private String currentVIN;

    VehiclePage() {
        super();
    }
    @Override
    protected void staffSetUp() {
        checkBox = new JCheckBox("View Borrowed Vehicles");
        checkBox.setBounds(100,100, 50,50);
        checkBox.addItemListener(this);
        buttonPanel.add(checkBox);


        updateTableQuery();
    }

    @Override
    protected void tenantSetUp() {
        //never gets here
    }

    @Override
    protected void InitializeFieldsHelper() {
        returnButton = new JButton("Return Vehicle");
        returnButton.setBounds(10, 20, 80, 80);
        returnButton.addActionListener(this);
        buttonPanel.add(returnButton);

        borrowButton = new JButton("Borrow Vehicle");
        borrowButton.setBounds(10, 20, 80, 80);
        borrowButton.addActionListener(this);
        buttonPanel.add(borrowButton);

    }

    private void updateTableQuery() {
        if (!borrowingView) {
            tableQuery = "SELECT v.VIN, v.Make, v.Model, v.Year, v.InUse, s.StaffName, s.StaffID " +
                    "FROM Vehicle v " +
                    "         LEFT JOIN Staff s ON v.VIN = s.VIN " +
                    "WHERE v.InUse = 0";
        } else {
            tableQuery = "SELECT v.VIN, v.Make, v.Model, v.Year, v.InUse, s.StaffName, s.StaffID " +
                    "FROM Vehicle v " +
                    "         LEFT JOIN Staff s ON v.VIN = s.VIN " +
                    "WHERE v.InUse = 1 AND s.StaffID = " + userIdentifier;
        }

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        borrowingView = ((JCheckBox) e.getSource()).isSelected();
        updateTableQuery();
        refreshTable();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1) {
            int row = table.getSelectedRow();
            currentVIN = (String) table.getValueAt(row,0);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (currentVIN == null) return;
        if (e.getSource() == returnButton) {
            dataModifier.staffReturnVehicle(userIdentifier, currentVIN);
            refreshTable();
        } else if (e.getSource() == borrowButton) {
            dataModifier.staffBorrowVehicle(userIdentifier, currentVIN);
            refreshTable();
        }
    }
}
