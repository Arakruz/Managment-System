package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Citation: from git@github.students.cs.ubc.ca:CPSC304/CPSC304_Java_Project.git

/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
    // Use this version of the ORACLE_URL if you are running the code off of the server
    // private static final String ORACLE_URL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
    // Use this version of the ORACLE_URL if you are tunneling into the undergrad servers
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    public static Connection connection = null;

    public DatabaseConnectionHandler() {
        try {
            // Load the Oracle JDBC driver
            // Note that the path could change for new drivers
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

//    public void deleteRoom(int roomId) {
//        try {
//            String query = "DELETE FROM branch WHERE branch_id = ?";
//            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
//            ps.setInt(1, branchId);
//
//            int rowCount = ps.executeUpdate();
//            if (rowCount == 0) {
//                System.out.println(WARNING_TAG + " Branch " + branchId + " does not exist!");
//            }
//
//            connection.commit();
//
//            ps.close();
//        } catch (SQLException e) {
//            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//            rollbackConnection();
//        }
//    }
//
//    public void insertRoom(BranchModel model) {
//        try {
//            String query = "INSERT INTO branch VALUES (?,?,?,?,?)";
//            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
//            ps.setInt(1, model.getId());
//            ps.setString(2, model.getName());
//            ps.setString(3, model.getAddress());
//            ps.setString(4, model.getCity());
//            if (model.getPhoneNumber() == 0) {
//                ps.setNull(5, java.sql.Types.INTEGER);
//            } else {
//                ps.setInt(5, model.getPhoneNumber());
//            }
//
//            ps.executeUpdate();
//            connection.commit();
//
//            ps.close();
//        } catch (SQLException e) {
//            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//            rollbackConnection();
//        }
//    }
//
//    public TenantRentsRoomModel[] getRoomInfo() {
//        ArrayList<TenantRentsRoomModel> result = new ArrayList<TenantRentsRoomModel>();
//
//        try {
//            String query = "SELECT * FROM branch";
//            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
//            ResultSet rs = ps.executeQuery();
//
//            while(rs.next()) {
//                TenantRentsRoomModel model = new TenantRentsRoomModel(rs.getInt("SIN"),
//                        rs.getFloat("RentalRate"),
//                        rs.getDate("ContractEndDate"),
//                        rs.getInt("RoomNumber"),
//                        rs.getString("BuildingName"));
//                result.add(model);
//            }
//
//            rs.close();
//            ps.close();
//        } catch (SQLException e) {
//            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//        }
//
//        return result.toArray(new TenantRentsRoomModel[result.size()]);
//    }

//    public void updateBranch(int id, String name) {
//        try {
//            String query = "UPDATE branch SET branch_name = ? WHERE branch_id = ?";
//            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
//            ps.setString(1, name);
//            ps.setInt(2, id);
//
//            int rowCount = ps.executeUpdate();
//            if (rowCount == 0) {
//                System.out.println(WARNING_TAG + " Branch " + id + " does not exist!");
//            }
//
//            connection.commit();
//
//            ps.close();
//        } catch (SQLException e) {
//            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//            rollbackConnection();
//        }
//    }
//
    //TODO double check what else is needed to fix this, only uncommented for other stuff to be testable
    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                connection.close();
            }

            connection = DriverManager.getConnection(ORACLE_URL, username, password);
            connection.setAutoCommit(false);

            return true;
        } catch (SQLException e) {
            // TODO Change this for a better way of handling the error
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }
//
//    private void rollbackConnection() {
//        try  {
//            connection.rollback();
//        } catch (SQLException e) {
//            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//        }
//    }
//
//    public void databaseSetup.sql() {
//        dropBranchTableIfExists();
//
//        try {
//            String query = "CREATE TABLE branch (branch_id integer PRIMARY KEY, branch_name varchar2(20) not null, branch_addr varchar2(50), branch_city varchar2(20) not null, branch_phone integer)";
//            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
//            ps.executeUpdate();
//            ps.close();
//        } catch (SQLException e) {
//            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//        }
//
//        BranchModel branch1 = new BranchModel("123 Charming Ave", "Vancouver", 1, "First Branch", 1234567);
//        insertBranch(branch1);
//
//        BranchModel branch2 = new BranchModel("123 Coco Ave", "Vancouver", 2, "Second Branch", 1234568);
//        insertBranch(branch2);
//    }
//
//    private void dropBranchTableIfExists() {
//        try {
//            String query = "select table_name from user_tables";
//            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
//            ResultSet rs = ps.executeQuery();
//
//            while(rs.next()) {
//                if(rs.getString(1).toLowerCase().equals("branch")) {
//                    ps.execute("DROP TABLE branch");
//                    break;
//                }
//            }
//
//            rs.close();
//            ps.close();
//        } catch (SQLException e) {
//            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
//        }
//    }


}