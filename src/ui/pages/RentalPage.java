package ui.pages;


import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;


public class RentalPage extends TablePage implements ItemListener {
    private static JButton returnButton;
    private static JButton rentButton;
    private int currentEquipmentID;
    JCheckBox availableItemsBox;
    JCheckBox tableViewBox;
    private boolean showBorrowed = false;
    private boolean showTenantTools = false;


    RentalPage () {
        super();
    }

    @Override
    protected void InitializeFieldsHelper() {
        returnButton = new JButton("Return Item");
        returnButton.setBounds(10, 20, 80, 80);
        returnButton.addActionListener(this);
        buttonPanel.add(returnButton);

        rentButton = new JButton("Borrow Item");
        rentButton.setBounds(10, 20, 80, 80);
        rentButton.addActionListener(this);
        buttonPanel.add(rentButton);

        availableItemsBox = new JCheckBox("Borrowed Items");
        availableItemsBox.setBounds(100,100, 50,50);
        availableItemsBox.addItemListener(this);
        buttonPanel.add(availableItemsBox);
    }

    @Override
    protected void staffSetUp() {
        updateTableQuery();
    }

    @Override
    protected void tenantSetUp() {
        tableViewBox = new JCheckBox("Display Tools");
        tableViewBox.setBounds(100,100, 50,50);
        tableViewBox.addItemListener(this);
        buttonPanel.add(tableViewBox);
        updateTableQuery();
    }

    private void updateTableQuery () {
        if (currentUser == UserType.Staff || showTenantTools) {
            setToolTableQuery();
        } else if (currentUser == UserType.Tenant) {
            setGameTableQuery();
        }
    }

    private void setToolTableQuery() {
        if (showBorrowed) {
            tableQuery = "SELECT tse.EquipmentID, tse.EquipmentName\n" +
                    "FROM ToolsSuppliesEquipment tse\n" +
                    "         LEFT JOIN (\n" +
                    "    SELECT EquipmentID, MAX(BorrowDate) AS LastBorrowDate\n" +
                    "    FROM StaffToolsSuppliesBorrowing\n" +
                    "    WHERE StaffID = " + userIdentifier + " " +
                    "    GROUP BY EquipmentID\n" +
                    ") LastBorrow ON tse.EquipmentID = LastBorrow.EquipmentID\n" +
                    "         LEFT JOIN (\n" +
                    "    SELECT EquipmentID, MAX(ReturnDate) AS LastReturnDate\n" +
                    "    FROM StaffToolsSuppliesReturning\n" +
                    "    GROUP BY EquipmentID\n" +
                    ") LastReturn ON tse.EquipmentID = LastReturn.EquipmentID\n" +
                    "WHERE LastBorrow.LastBorrowDate IS NOT NULL AND (LastReturn.LastReturnDate IS NULL OR LastReturn.LastReturnDate <= LastBorrow.LastBorrowDate)";

        } else {
            tableQuery = "SELECT tse.EquipmentID, tse.EquipmentName\n" +
                    "FROM ToolsSuppliesEquipment tse\n" +
                    "         LEFT JOIN (\n" +
                    "    SELECT EquipmentID, MAX(BorrowDate) AS LastBorrowDate\n" +
                    "    FROM (\n" +
                    "             SELECT EquipmentID, BorrowDate FROM StaffToolsSuppliesBorrowing\n" +
                    "             UNION ALL\n" +
                    "             SELECT EquipmentID, BorrowDate FROM TenantToolsSuppliesBorrowing\n" +
                    "         ) CombinedBorrow\n" +
                    "    GROUP BY EquipmentID\n" +
                    ") LastBorrow ON tse.EquipmentID = LastBorrow.EquipmentID\n" +
                    "         LEFT JOIN (\n" +
                    "    SELECT EquipmentID, MAX(ReturnDate) AS LastReturnDate\n" +
                    "    FROM (\n" +
                    "             SELECT EquipmentID, ReturnDate FROM StaffToolsSuppliesReturning\n" +
                    "             UNION ALL\n" +
                    "             SELECT EquipmentID, ReturnDate FROM TenantToolsSuppliesReturning\n" +
                    "         ) CombinedReturn\n" +
                    "    GROUP BY EquipmentID\n" +
                    ") LastReturn ON tse.EquipmentID = LastReturn.EquipmentID\n" +
                    "WHERE (LastBorrow.LastBorrowDate IS NULL OR LastReturn.LastReturnDate > LastBorrow.LastBorrowDate)";
        }
    }

    private void setGameTableQuery() {
        if (showBorrowed) {
            tableQuery = "SELECT G.EquipmentID,\n" +
                    "       G.EquipmentName\n" +
                    "FROM GameEquipment G\n" +
                    "WHERE EXISTS (\n" +
                    "    SELECT 1\n" +
                    "    FROM TenantGameBorrowing TGB\n" +
                    "    WHERE TGB.EquipmentID = G.EquipmentID\n" +
                    "      AND TGB.SIN = " + userIdentifier + "\n" +
                    ")\n" +
                    "  AND NOT EXISTS (\n" +
                    "    SELECT 1\n" +
                    "    FROM TenantGameReturning TGR\n" +
                    "    WHERE TGR.EquipmentID = G.EquipmentID\n" +
                    "      AND TGR.SIN = " + userIdentifier + "\n" +
                    "      AND (TGR.ReturnDate IS NULL OR TGR.ReturnDate >= (SELECT MAX(BorrowDate) FROM TenantGameBorrowing WHERE EquipmentID = G.EquipmentID AND SIN = " + userIdentifier + "))\n" +
                    ")\n" +
                    "  AND (\n" +
                    "            " + checkOverdue() + " = 0\n" +
                    "        OR (" + checkOverdue() + "= 1 AND TRUNC(CURRENT_DATE) > (SELECT MAX(TGB2.BorrowDate) + G.LoanPeriod FROM TenantGameBorrowing TGB2 WHERE TGB2.EquipmentID = G.EquipmentID AND TGB2.SIN = " + userIdentifier + "))\n" +
                    "    )\n" +
                    "ORDER BY G.EquipmentID";

        } else {
            tableQuery = "SELECT G.EquipmentID, G.EquipmentName\n" +
                    "FROM GameEquipment G\n" +
                    "WHERE NOT EXISTS (\n" +
                    "    SELECT 1\n" +
                    "    FROM TenantGameBorrowing TGB\n" +
                    "    WHERE TGB.EquipmentID = G.EquipmentID\n" +
                    ")\n" +
                    "   OR EXISTS (\n" +
                    "    SELECT 1\n" +
                    "    FROM TenantGameReturning TGR\n" +
                    "    WHERE TGR.EquipmentID = G.EquipmentID\n" +
                    "      AND (TGR.ReturnDate IS NOT NULL AND TGR.ReturnDate > (SELECT MAX(BorrowDate) FROM TenantGameBorrowing WHERE EquipmentID = G.EquipmentID))\n" +
                    ")\n" +
                    "ORDER BY G.EquipmentID";
        }
    }

    private int checkOverdue() {
        return 0;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (e.getSource() == returnButton) {
            returnActionHelper();
        } else if (e.getSource() == rentButton) {
            rentActionHelper();
        } else if (e.getSource() == backButton) {
            new HomePage();
            this.dispose();
        }
    }


    private void rentActionHelper() {
        if (currentUser == UserType.Tenant) {
            if (showTenantTools) {
                dataModifier.tenantBorrowsTool(userIdentifier,currentEquipmentID);
            } else {
                dataModifier.tenantBorrowsGame(userIdentifier,currentEquipmentID);
            }

        } else if (currentUser == UserType.Staff) {
            dataModifier.staffBorrowsTool(userIdentifier,currentEquipmentID);
        }
        refreshTable();
    }

    private void returnActionHelper() {
        if (currentUser == UserType.Tenant) {
            if (showTenantTools) {
                dataModifier.tenantReturnsTool(userIdentifier,currentEquipmentID);
            } else {
                dataModifier.tenantReturnsGame(userIdentifier,currentEquipmentID);
            }
        } else if (currentUser == UserType.Staff) {
            dataModifier.staffReturnsTool(userIdentifier,currentEquipmentID);
        }

        refreshTable();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1) {
            int row = table.getSelectedRow();
            currentEquipmentID = ((BigDecimal) table.getValueAt(row,0)).intValue();
        }
    }


    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == availableItemsBox) {
            showBorrowed = ((JCheckBox) e.getSource()).isSelected();

        } else if (e.getSource() == tableViewBox) {
            showTenantTools = ((JCheckBox) e.getSource()).isSelected();
        }

        updateTableQuery();
        refreshTable();
    }
}
