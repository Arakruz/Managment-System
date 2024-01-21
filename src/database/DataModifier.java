package database;

import java.sql.*;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataModifier {
    Connection connection;
    DatabaseConnectionHandler dbHandler;

    public DataModifier(DatabaseConnectionHandler dbHandler) {
        this.connection = DatabaseConnectionHandler.connection;
        this.dbHandler = dbHandler;

    }

    ////    TENANTS
    public boolean tenantBorrowsGame(int sin, int equipmentId) {
        String query = "INSERT INTO TenantGameBorrowing (BorrowDate, SIN, EquipmentID) VALUES (?, ?, ?)";
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            pstmt = connection.prepareStatement(query);
            Date currentSqlDate = Date.valueOf(LocalDate.now());
            pstmt.setDate(1, currentSqlDate);
            pstmt.setInt(2, sin);
            pstmt.setInt(3, equipmentId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                connection.commit();  // Commit the transaction if the operation is successful
                success = true;
            } else {
                connection.rollback();  // Rollback the transaction if no rows were affected
            }
        } catch (SQLException e) {
            // Handle exceptions, rollback the transaction, and log errors
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // Close resources and restore auto-commit state
            try {
                if (pstmt != null) pstmt.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return success;
    }

    public boolean tenantReturnsGame(int sin, int equipmentId) {
        String query = "INSERT INTO TenantGameReturning (ReturnDate, SIN, EquipmentID) VALUES (?, ?, ?)";
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            pstmt = connection.prepareStatement(query);
            Date currentSqlDate = Date.valueOf(LocalDate.now());
            pstmt.setDate(1, currentSqlDate);  // Set the current date as the return date
            pstmt.setInt(2, sin);              // Set the SIN of the tenant
            pstmt.setInt(3, equipmentId);      // Set the EquipmentID of the game being returned

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                connection.commit();  // Commit the transaction if the operation is successful
                success = true;
            } else {
                connection.rollback();  // Rollback the transaction if no rows were affected
            }
        } catch (SQLException e) {
            // Handle exceptions, rollback the transaction, and log errors
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // Close resources and restore auto-commit state
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return success;
    }

    public boolean tenantBorrowsTool(int sin, int equipmentId) {
        String query = "INSERT INTO TenantToolsSuppliesBorrowing (BorrowDate, SIN, EquipmentID) VALUES (?, ?, ?)";
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            pstmt = connection.prepareStatement(query);
            Date currentSqlDate = Date.valueOf(LocalDate.now()); // Gets the current date
            pstmt.setDate(1, currentSqlDate); // Sets the BorrowDate
            pstmt.setInt(2, sin); // Sets the SIN of the tenant
            pstmt.setInt(3, equipmentId); // Sets the EquipmentID of the tool being borrowed

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                connection.commit(); // Commit the transaction if the operation is successful
                success = true;
            } else {
                connection.rollback(); // Rollback the transaction if no rows were affected
            }
        } catch (SQLException e) {
            // Handle exceptions, rollback the transaction, and log errors
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // Close resources and restore auto-commit state
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return success;
    }

    public boolean tenantReturnsTool(int sin, int equipmentId) {
        String query = "INSERT INTO TenantToolsSuppliesReturning (ReturnDate, SIN, EquipmentID) VALUES (?, ?, ?)";
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            pstmt = connection.prepareStatement(query);
            Date currentSqlDate = Date.valueOf(LocalDate.now()); // Gets the current date
            pstmt.setDate(1, currentSqlDate); // Sets the ReturnDate
            pstmt.setInt(2, sin); // Sets the SIN of the tenant
            pstmt.setInt(3, equipmentId); // Sets the EquipmentID of the tool being returned

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                connection.commit(); // Commit the transaction if the operation is successful
                success = true;
            } else {
                connection.rollback(); // Rollback the transaction if no rows were affected
            }
        } catch (SQLException e) {
            // Handle exceptions, rollback the transaction, and log errors
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // Close resources and restore auto-commit state
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return success;
    }

    public boolean tenantRequests(int sin, String requestType) {
        String query = "INSERT INTO Request (BuildingName, RoomNumber, RequestType, Complete, SubmissionDate, StaffID, SIN) " +
                "SELECT t.BuildingName, t.RoomNumber, ?, 0, ?, 2001, ? " +
                "FROM TenantRentsRoom t " +
                "WHERE t.SIN = ?";
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setString(1, requestType); // Set RequestType
            Date currentSqlDate = Date.valueOf(LocalDate.now()); // Gets the current date
            pstmt.setDate(2, currentSqlDate); // Sets the ReturnDate
            pstmt.setInt(3, sin); // Set SIN for INSERT
            pstmt.setInt(4, sin); // Set SIN for WHERE condition

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                connection.commit();  // Commit the transaction if the operation is successful
                success = true;
            } else {
                connection.rollback();  // Rollback the transaction if no rows were affected
            }
        } catch (SQLException e) {
            // Handle exceptions, rollback the transaction, and log errors
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // Close resources and restore auto-commit state
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return success;
    }


    /////   STAFF

    public boolean staffBorrowsTool(int staffID, int equipmentId) {
        String query = "INSERT INTO StaffToolsSuppliesBorrowing (BorrowDate, StaffID, EquipmentID) VALUES (?, ?, ?)";
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            pstmt = connection.prepareStatement(query);
            Date currentSqlDate = Date.valueOf(LocalDate.now()); // Gets the current date
            pstmt.setDate(1, currentSqlDate); // Sets the BorrowDate
            pstmt.setInt(2, staffID); // Sets the StaffID
            pstmt.setInt(3, equipmentId); // Sets the EquipmentID of the tool being borrowed

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                connection.commit(); // Commit the transaction if the operation is successful
                success = true;
            } else {
                connection.rollback(); // Rollback the transaction if no rows were affected
            }
        } catch (SQLException e) {
            // Handle exceptions, rollback the transaction, and log errors
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // Close resources and restore auto-commit state
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return success;
    }


    public boolean staffReturnsTool(int staffID, int equipmentId) {
        String query = "INSERT INTO StaffToolsSuppliesReturning (ReturnDate, StaffID, EquipmentID) VALUES (?, ?, ?)";
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            pstmt = connection.prepareStatement(query);
            Date currentSqlDate = Date.valueOf(LocalDate.now()); // Gets the current date
            pstmt.setDate(1, currentSqlDate); // Sets the ReturnDate
            pstmt.setInt(2, staffID); // Sets the StaffID
            pstmt.setInt(3, equipmentId); // Sets the EquipmentID of the tool being returned

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                connection.commit(); // Commit the transaction if the operation is successful
                success = true;
            } else {
                connection.rollback(); // Rollback the transaction if no rows were affected
            }
        } catch (SQLException e) {
            // Handle exceptions, rollback the transaction, and log errors
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // Close resources and restore auto-commit state
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return success;
    }

    public boolean staffCompleteRequest(int requestID, int staffID) {
        String query = "UPDATE Request SET Complete = 1 WHERE RequestID = ? AND StaffID = ?";
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, requestID); // Sets the RequestID
            pstmt.setInt(2, staffID);   // Sets the StaffID

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                connection.commit(); // Commit the transaction if the operation is successful
                success = true;
            } else {
                connection.rollback(); // Rollback the transaction if no rows were affected
            }
        } catch (SQLException e) {
            // Handle exceptions, rollback the transaction, and log errors
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // Close resources and restore auto-commit state
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return success;
    }


    public boolean staffBorrowVehicle(int staffID, String vin) {
        String updateVehicleQuery = "UPDATE Vehicle SET InUse = 1 WHERE VIN = ?";
        String updateStaffQuery = "UPDATE Staff SET VIN = ? WHERE StaffID = ?";
        PreparedStatement pstmtVehicle = null;
        PreparedStatement pstmtStaff = null;
        boolean success = false;

        try {
            connection.setAutoCommit(false); // Start transaction

            // Update Vehicle table
            pstmtVehicle = connection.prepareStatement(updateVehicleQuery);
            pstmtVehicle.setString(1, vin);
            pstmtVehicle.executeUpdate();

            // Update Staff table
            pstmtStaff = connection.prepareStatement(updateStaffQuery);
            pstmtStaff.setString(1, vin);
            pstmtStaff.setInt(2, staffID);
            pstmtStaff.executeUpdate();

            connection.commit(); // Commit the transaction if both operations are successful
            success = true;
        } catch (SQLException e) {
            // Handle exceptions and rollback the transaction
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // Close resources and restore auto-commit state
            try {
                if (pstmtVehicle != null) pstmtVehicle.close();
                if (pstmtStaff != null) pstmtStaff.close();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return success;
    }

    public boolean staffReturnVehicle(int staffID, String vin) {
        String updateVehicleQuery = "UPDATE Vehicle SET InUse = 0 WHERE VIN = ?";
        String updateStaffQuery = "UPDATE Staff SET VIN = NULL WHERE StaffID = ?";
        PreparedStatement pstmtVehicle = null;
        PreparedStatement pstmtStaff = null;
        boolean success = false;

        try {
            connection.setAutoCommit(false); // Start transaction

            // Update Vehicle table
            pstmtVehicle = connection.prepareStatement(updateVehicleQuery);
            pstmtVehicle.setString(1, vin);
            pstmtVehicle.executeUpdate();

            // Update Staff table
            pstmtStaff = connection.prepareStatement(updateStaffQuery);
            pstmtStaff.setInt(1, staffID);
            pstmtStaff.executeUpdate();

            connection.commit(); // Commit the transaction if both operations are successful
            success = true;
        } catch (SQLException e) {
            // Handle exceptions and rollback the transaction
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // Close resources and restore auto-commit state
            try {
                if (pstmtVehicle != null) pstmtVehicle.close();
                if (pstmtStaff != null) pstmtStaff.close();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return success;
    }

    // can also be used for admin
    public boolean staffAssignMail(int tenantSIN, int roomNumber, String buildingName, String mailType) {
        // SQL query to insert into Mail table
        String insertMailQuery = "INSERT INTO Mail (TenantSIN, RoomNumber, BuildingName, MailType) VALUES (?, ?, ?, ?)";

        // SQL query to insert into BuildingReceivesMail table
        String insertBuildingReceivesMailQuery = "INSERT INTO BuildingReceivesMail (MailID, DateReceived) VALUES (?, TRUNC(CURRENT_DATE))";

        PreparedStatement pstmtMail = null;
        PreparedStatement pstmtBuildingReceivesMail = null;
        boolean success = false;
        ResultSet generatedKeys = null;

        try {
            connection.setAutoCommit(false); // Start transaction

            // Insert into Mail table
            pstmtMail = connection.prepareStatement(insertMailQuery, Statement.RETURN_GENERATED_KEYS);
            pstmtMail.setInt(1, tenantSIN);
            pstmtMail.setInt(2, roomNumber);
            pstmtMail.setString(3, buildingName);
            pstmtMail.setString(4, mailType);
            int affectedRowsMail = pstmtMail.executeUpdate();

            // Retrieve the generated MailID
            int mailID = 0;
            if (affectedRowsMail > 0) {
                generatedKeys = pstmtMail.getGeneratedKeys();
                if (generatedKeys.next()) {
                    mailID = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating mail failed, no ID obtained.");
                }
            }

            // Insert into BuildingReceivesMail table
            pstmtBuildingReceivesMail = connection.prepareStatement(insertBuildingReceivesMailQuery);
            pstmtBuildingReceivesMail.setInt(1, mailID);
            int affectedRowsBuilding = pstmtBuildingReceivesMail.executeUpdate();

            // Check if both insert operations were successful
            if (affectedRowsMail > 0 && affectedRowsBuilding > 0) {
                connection.commit(); // Commit the transaction
                success = true;
            } else {
                connection.rollback(); // Rollback the transaction if no rows were affected
            }
        } catch (SQLException e) {
            // Handle exceptions and rollback the transaction
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // Close resources and restore auto-commit state
            try {
                if (pstmtMail != null) pstmtMail.close();
                if (pstmtBuildingReceivesMail != null) pstmtBuildingReceivesMail.close();
                if (generatedKeys != null) generatedKeys.close();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return success;
    }


    public boolean adminAddTenant(int sin, String tenantName, LocalDate birthdate, String email) {
        String query = "INSERT INTO Tenant (SIN, TenantName, Birthdate, Email) VALUES (?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?)";
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, sin);
            pstmt.setString(2, tenantName);
            pstmt.setString(3, birthdate.toString()); // Convert LocalDate to String
            pstmt.setString(4, email);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                success = true;
                connection.commit(); // Commit the transaction
            } else {
                connection.rollback(); // Rollback in case of failure
            }
        } catch (SQLException e) {
            // Handle SQL exception
            try {
                if (connection != null) {
                    connection.rollback(); // Rollback transaction
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            // Clean up and close resources
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return success;
    }

    public boolean adminAssignTenantRoom(int sin, double rentalRate, LocalDate contractEndDate, int roomNumber, String buildingName) {
        String query = "INSERT INTO TenantRentsRoom (SIN, RentalRate, ContractEndDate, RoomNumber, BuildingName) VALUES (?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?)";
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, sin);
            pstmt.setDouble(2, rentalRate);
            pstmt.setString(3, contractEndDate.toString()); // Convert LocalDate to String
            pstmt.setInt(4, roomNumber);
            pstmt.setString(5, buildingName);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                success = true;
                connection.commit(); // Commit the transaction
            } else {
                connection.rollback(); // Rollback in case of failure
            }
        } catch (SQLException e) {
            // Handle SQL exception
            try {
                if (connection != null) {
                    connection.rollback(); // Rollback transaction
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            // Clean up and close resources
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return success;
    }

    public boolean adminAddBuilding(String postalCode, String city, String province, String buildingName, String address, int numberOfStaff) {
        String insertPostalCodeQuery = "INSERT INTO PostalCode (PostalCode, City, Province) SELECT ?, ?, ? FROM dual WHERE NOT EXISTS (SELECT 1 FROM PostalCode WHERE PostalCode = ?)";
        String insertBuildingQuery = "INSERT INTO Building (BuildingName, Address, NumberOfStaff, City, Province, PostalCode) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement pstmtPostalCode = null;
        PreparedStatement pstmtBuilding = null;
        boolean success = false;

        try {
            // Insert postal code if it doesn't exist
            pstmtPostalCode = connection.prepareStatement(insertPostalCodeQuery);
            pstmtPostalCode.setString(1, postalCode);
            pstmtPostalCode.setString(2, city);
            pstmtPostalCode.setString(3, province);
            pstmtPostalCode.setString(4, postalCode);
            pstmtPostalCode.executeUpdate();

            // Insert building
            pstmtBuilding = connection.prepareStatement(insertBuildingQuery);
            pstmtBuilding.setString(1, buildingName);
            pstmtBuilding.setString(2, address);
            pstmtBuilding.setInt(3, numberOfStaff);
            pstmtBuilding.setString(4, city);
            pstmtBuilding.setString(5, province);
            pstmtBuilding.setString(6, postalCode);

            int affectedRows = pstmtBuilding.executeUpdate();
            if (affectedRows > 0) {
                success = true;
                connection.commit();
            } else {
                connection.rollback();
            }
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (pstmtPostalCode != null) pstmtPostalCode.close();
                if (pstmtBuilding != null) pstmtBuilding.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return success;
    }



    public boolean adminAddRoom(int roomNumber, String buildingName, int cleanStatus, int occupancyLimit, int numberOfOccupants) {
        String query = "INSERT INTO Room (RoomNumber, BuildingName, CleanStatus, OccupancyLimit, NumberOfOccupants) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, roomNumber);
            pstmt.setString(2, buildingName);
            pstmt.setInt(3, cleanStatus);
            pstmt.setInt(4, occupancyLimit);
            pstmt.setInt(5, numberOfOccupants);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                success = true;
                connection.commit();
            } else {
                connection.rollback();
            }
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return success;
    }


}


