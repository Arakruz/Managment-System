package database;

import util.PrintablePreparedStatement;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;

public class DataRetriever {
    private DatabaseConnectionHandler dbHandler;
    public Connection connection;

    public DataRetriever(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;
        this.connection = DatabaseConnectionHandler.connection;
    }

    public DefaultTableModel getModelData(String query) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        return getModelData(statement);
    }

    public DefaultTableModel getModelData(PreparedStatement statement) throws SQLException {
        ResultSet rs = statement.executeQuery();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<>();
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();


        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }


        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                row.addElement(rs.getObject(columnIndex));
            }
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames);
    }
}
