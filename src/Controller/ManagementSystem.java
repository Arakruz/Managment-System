package Controller;

import Delegates.DBLoginWindowDelegate;
import database.DatabaseConnectionHandler;
import ui.DBLoginWindow;
import ui.pages.LoginPage;

import javax.swing.*;

public class ManagementSystem implements DBLoginWindowDelegate {
    public static DatabaseConnectionHandler dbHandler = null;
    private DBLoginWindow dbLoginWindow = null;

    public ManagementSystem() {
        dbHandler = new DatabaseConnectionHandler();
    }

    private void start() {
        dbLoginWindow = new DBLoginWindow();
        dbLoginWindow.showFrame(this);
    }
    public static void main(String[] args) {
        //TODO uncomment, this is only out to test ui
        ManagementSystem managementSystem = new ManagementSystem();
        managementSystem.start();
        //new LoginPage();
    }

    @Override
    public void login(String username, String password) {
        boolean didConnect = dbHandler.login(username, password);

        if (didConnect) {
            // Once connected, remove login window and system login
            dbLoginWindow.dispose();
            JOptionPane.showMessageDialog(new LoginPage(), "Connected To Oracle");

        } else {
            dbLoginWindow.handleLoginFailed();

            if (dbLoginWindow.hasReachedMaxLoginAttempts()) {
                JOptionPane.showMessageDialog(new JPanel(), ("Too many failed logins, closing application :("));
                dbLoginWindow.dispose();
                System.exit(-1);
            }
        }
    }

    public void databaseSetup() {
        //dbHandler.databaseSetup();;
    }
}