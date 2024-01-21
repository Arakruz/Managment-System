package ui.pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;

public abstract class TablePage extends Page implements ActionListener, MouseListener {
    protected static JTable table;
    protected static JButton backButton;

    protected static JPanel buttonPanel;
    protected static JPanel upperPanel;
    protected static JPanel tablePanel;
    protected String tableQuery;
    protected GridBagConstraints gbc;

    TablePage () {
        super();
    }

    @Override
    protected void initializeFields() {
        buttonPanel = new JPanel();
        upperPanel = new JPanel();
        tablePanel = new JPanel();

        gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;

        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        upperPanel.setLayout(new GridBagLayout());
        tablePanel.setLayout(new GridBagLayout());

        upperPanel.add(tablePanel, gbc);
        mainPanel.add(upperPanel);
        mainPanel.add(buttonPanel);

        backButton = new JButton("Back");
        backButton.setBounds(10, 20, 80, 80);
        backButton.addActionListener(this);
        buttonPanel.add(backButton);

        InitializeFieldsHelper();
        if (currentUser == UserType.Staff) {
            staffSetUp();
        } else {
            tenantSetUp();
        }

        initializeTable();

    }

    protected abstract void staffSetUp();

    protected abstract void tenantSetUp();

    protected abstract void InitializeFieldsHelper();


    protected void initializeTable() {
        try {
            table = new JTable(dataRetriever.getModelData(tableQuery));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        upperPanel.setBackground(BACKGROUND);

        table.setDefaultEditor(Object.class,null);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(table);
        //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        setUpTable(table,this);
        tablePanel.add(scrollPane, gbc);
    }

    protected void refreshTable() {
        try {
            table.setModel(dataRetriever.getModelData(tableQuery));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            new HomePage();
            this.dispose();
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {


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


}
