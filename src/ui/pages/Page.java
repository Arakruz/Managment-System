package ui.pages;

import Controller.ManagementSystem;
import database.DataModifier;
import database.DataRetriever;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

// Represents the main frame of the program. Also generates all other GUI elements
public abstract class Page extends JFrame implements ActionListener {
    public enum UserType {
        Tenant,
        Staff,
        Admin
    }

    // Preferred and minimum sizes of the JFrame
    public static int FRAME_WIDTH = 2000;
    public static int FRAME_HEIGHT = 1500;
    public static int MIN_FRAME_WIDTH = 0;
    public static int MIN_FRAME_HEIGHT = 0;

    // How the background and foreground of the whole program looks and the fonts for the buttons and the lists
    public static final Color BACKGROUND = new Color(0x2F3137);
    public static final Color FOREGROUND = new Color(0x72757D);
    public static final Font BUTTON_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 30);
    public static final Font LIST_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 16);

    public static UserType currentUser;

    protected static JPanel mainPanel;
    protected static DataRetriever dataRetriever;
    protected static DataModifier dataModifier;

    protected static int userIdentifier;

    public Page() {
        super("Management System");
        mainPanel = new JPanel();
        this.add(mainPanel);
        dataRetriever = new DataRetriever(ManagementSystem.dbHandler);
        dataModifier = new DataModifier(ManagementSystem.dbHandler);

        initializeFields();
        initializePageFrame();
    }

    // Base method for frame's initialization, should cover everything that all frames need
    protected void initializePageFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setMinimumSize(new Dimension(MIN_FRAME_WIDTH, MIN_FRAME_HEIGHT));
        setLocationRelativeTo(mainPanel);
        this.pack();
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );
        setVisible(true);
    }

    // Initializes all other fields, things like panels, buttons, etc. different for all pages
    protected abstract void initializeFields();

    protected void setUpTable(JTable table, MouseListener listenerParent) {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(listenerParent);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setAutoCreateColumnsFromModel(true);
        table.getTableHeader().setResizingAllowed(true);
        table.setFillsViewportHeight(true);
    }
}