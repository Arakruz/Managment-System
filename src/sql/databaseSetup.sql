-- Check if the MailIDSeq sequence exists and then drop it
BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE MailIDSeq';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2289 THEN
            RAISE;
        END IF;
END;

-- Check if the RequestIDSeq sequence exists and then drop it
BEGIN
    EXECUTE IMMEDIATE 'DROP SEQUENCE RequestIDSeq';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -2289 THEN
            RAISE;
        END IF;
END;

-- Create a sequence
CREATE SEQUENCE MailIDSeq
    START WITH 10000
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- Create a sequence
CREATE SEQUENCE RequestIDSeq
    START WITH 50000
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;


BEGIN
    FOR t IN (SELECT table_name FROM user_tables) LOOP
            EXECUTE IMMEDIATE 'DROP TABLE ' || t.table_name || ' CASCADE CONSTRAINTS';
        END LOOP;
END;

CREATE TABLE PostalCode(PostalCode VARCHAR(6) PRIMARY KEY, City VARCHAR(50) NOT NULL, Province VARCHAR(50) NOT NULL);

CREATE TABLE Building(BuildingName VARCHAR(50) PRIMARY KEY, Address VARCHAR(100) NOT NULL, NumberOfStaff INTEGER NOT NULL, City VARCHAR(50) NOT NULL, Province VARCHAR(50) NOT NULL, PostalCode VARCHAR(6) NOT NULL, FOREIGN KEY (PostalCode) REFERENCES PostalCode(PostalCode) ON DELETE CASCADE);

CREATE TABLE Tenant(SIN INTEGER PRIMARY KEY, TenantName VARCHAR(50) NOT NULL, Birthdate DATE NOT NULL, Email VARCHAR(100));

CREATE TABLE Room(RoomNumber INTEGER NOT NULL, BuildingName VARCHAR(50) NOT NULL, CleanStatus NUMBER(1) NOT NULL, OccupancyLimit INTEGER NOT NULL, NumberOfOccupants INTEGER NOT NULL, PRIMARY KEY (RoomNumber, BuildingName), FOREIGN KEY (BuildingName) REFERENCES Building(BuildingName));

CREATE TABLE TenantRentsRoom(SIN INTEGER, RentalRate FLOAT NOT NULL, ContractEndDate DATE NOT NULL, RoomNumber INTEGER, BuildingName VARCHAR(50), PRIMARY KEY (RoomNumber, BuildingName, SIN), FOREIGN KEY (SIN) REFERENCES Tenant(SIN), FOREIGN KEY (RoomNumber, BuildingName) REFERENCES Room(RoomNumber, BuildingName));

CREATE TABLE Mail(MailID INTEGER PRIMARY KEY, TenantSIN INTEGER NOT NULL, RoomNumber INTEGER NOT NULL, BuildingName VARCHAR(50) NOT NULL, MailType VARCHAR(50) NOT NULL, FOREIGN KEY (TenantSIN) REFERENCES Tenant(SIN), FOREIGN KEY (RoomNumber, BuildingName) REFERENCES Room(RoomNumber, BuildingName));

-- Trigger to automatically assign MailID values from the sequence
CREATE OR REPLACE TRIGGER Mail_BI
    BEFORE INSERT ON Mail
    FOR EACH ROW
BEGIN
    SELECT MailIDSeq.NEXTVAL INTO :NEW.MailID FROM DUAL;
END;


CREATE TABLE BuildingReceivesMail(MailID INTEGER, DateReceived DATE, PRIMARY KEY (MailID), FOREIGN KEY (MailID) REFERENCES Mail(MailID));

CREATE TABLE Vehicle(VIN VARCHAR(20) PRIMARY KEY, Make VARCHAR(50) NOT NULL, Year INTEGER NOT NULL, InUse NUMBER(1) NOT NULL, Model VARCHAR(50) NOT NULL);

CREATE TABLE Staff(StaffID INTEGER PRIMARY KEY, StaffName VARCHAR(50) NOT NULL, VIN VARCHAR(20) DEFAULT NULL, FOREIGN KEY (VIN) REFERENCES Vehicle(VIN));

CREATE TABLE Request(RequestID INTEGER PRIMARY KEY, BuildingName VARCHAR(50) NOT NULL, RoomNumber INT NOT NULL, RequestType VARCHAR(50) NOT NULL, Complete NUMBER(1) DEFAULT 0, SubmissionDate DATE NOT NULL, StaffID INTEGER NOT NULL, SIN INTEGER NOT NULL, FOREIGN KEY (RoomNumber, BuildingName) REFERENCES Room(RoomNumber, BuildingName), FOREIGN KEY (StaffID) REFERENCES Staff(StaffID), FOREIGN KEY (SIN) REFERENCES Tenant(SIN));

-- Trigger to automatically assign MailID values from the sequence
CREATE OR REPLACE TRIGGER Request_BI
    BEFORE INSERT ON Request
    FOR EACH ROW
BEGIN
    SELECT RequestIDSeq.NEXTVAL INTO :NEW.RequestID FROM DUAL;
END;

CREATE TABLE ToolsSuppliesEquipment(EquipmentID INTEGER PRIMARY KEY, Cost FLOAT NOT NULL, LoanPeriod INTEGER NOT NULL, LateFee FLOAT NOT NULL, EquipmentName VARCHAR(50) NOT NULL, DateLastTested DATE NOT NULL);

CREATE TABLE GameEquipment(EquipmentID INTEGER PRIMARY KEY, Cost FLOAT NOT NULL, LoanPeriod INTEGER NOT NULL, LateFee FLOAT NOT NULL, EquipmentName VARCHAR(50) NOT NULL, NumberOfParts INTEGER NOT NULL);

CREATE TABLE StaffToolsSuppliesBorrowing(EquipmentID INTEGER,BorrowDate DATE, StaffID INTEGER, PRIMARY KEY (StaffID, EquipmentID, BorrowDate), FOREIGN KEY (StaffID) REFERENCES Staff(StaffID), FOREIGN KEY (EquipmentID) REFERENCES ToolsSuppliesEquipment(EquipmentID));

CREATE TABLE StaffGameBorrowing(EquipmentID INTEGER, BorrowDate DATE, StaffID INTEGER, PRIMARY KEY (StaffID, EquipmentID, BorrowDate), FOREIGN KEY (StaffID) REFERENCES Staff(StaffID), FOREIGN KEY (EquipmentID) REFERENCES GameEquipment(EquipmentID));

CREATE TABLE TenantToolsSuppliesBorrowing(EquipmentID INTEGER, BorrowDate DATE, SIN INTEGER, PRIMARY KEY (SIN, EquipmentID, BorrowDate), FOREIGN KEY (SIN) REFERENCES Tenant(SIN), FOREIGN KEY (EquipmentID) REFERENCES ToolsSuppliesEquipment(EquipmentID));

CREATE TABLE TenantGameBorrowing(EquipmentID INTEGER, BorrowDate DATE, SIN INTEGER, PRIMARY KEY (SIN, EquipmentID, BorrowDate), FOREIGN KEY (SIN) REFERENCES Tenant(SIN), FOREIGN KEY (EquipmentID) REFERENCES GameEquipment(EquipmentID));

CREATE TABLE StaffToolsSuppliesReturning(EquipmentID INTEGER, ReturnDate DATE, StaffID INTEGER, PRIMARY KEY (StaffID, EquipmentID, ReturnDate), FOREIGN KEY (StaffID) REFERENCES Staff(StaffID), FOREIGN KEY (EquipmentID) REFERENCES ToolsSuppliesEquipment(EquipmentID));

CREATE TABLE StaffGameReturning(EquipmentID INTEGER, ReturnDate DATE, StaffID INTEGER, PRIMARY KEY (StaffID, EquipmentID, ReturnDate), FOREIGN KEY (StaffID) REFERENCES Staff(StaffID), FOREIGN KEY (EquipmentID) REFERENCES GameEquipment(EquipmentID));

CREATE TABLE TenantToolsSuppliesReturning(EquipmentID INTEGER, ReturnDate DATE, SIN INTEGER, PRIMARY KEY (SIN, EquipmentID, ReturnDate), FOREIGN KEY (SIN) REFERENCES Tenant(SIN), FOREIGN KEY (EquipmentID) REFERENCES ToolsSuppliesEquipment(EquipmentID));

CREATE TABLE TenantGameReturning(EquipmentID INTEGER, ReturnDate DATE, SIN INTEGER, PRIMARY KEY (SIN, EquipmentID, ReturnDate), FOREIGN KEY (SIN) REFERENCES Tenant(SIN), FOREIGN KEY (EquipmentID) REFERENCES GameEquipment(EquipmentID));


INSERT INTO PostalCode (PostalCode, City, Province) VALUES ('V6T1Z2', 'Vancouver', 'BC');
INSERT INTO PostalCode (PostalCode, City, Province) VALUES ('V6T1K2', 'Vancouver', 'BC');
INSERT INTO PostalCode (PostalCode, City, Province) VALUES ('V6T1W9', 'Vancouver', 'BC');
INSERT INTO PostalCode (PostalCode, City, Province) VALUES ('V6T1Z4', 'Vancouver', 'BC');
INSERT INTO PostalCode (PostalCode, City, Province) VALUES ('V6T2G9', 'Vancouver', 'BC');

INSERT INTO Building (BuildingName, Address, NumberOfStaff, City, Province, PostalCode) VALUES ('Ponderosa', '2075 West Mall', 10, 'Vancouver', 'BC', 'V6T1Z2');
INSERT INTO Building (BuildingName, Address, NumberOfStaff, City, Province, PostalCode) VALUES ('Walter Gage', '5959 Student Union Blvd', 20, 'Vancouver', 'BC', 'V6T1K2');
INSERT INTO Building (BuildingName, Address, NumberOfStaff, City, Province, PostalCode) VALUES ('Totem Park', '2525 West Mall', 30, 'Vancouver', 'BC', 'V6T1W9');
INSERT INTO Building (BuildingName, Address, NumberOfStaff, City, Province, PostalCode) VALUES ('Marine Drive', '2205 Lower Mall', 40, 'Vancouver', 'BC', 'V6T1Z4');
INSERT INTO Building (BuildingName, Address, NumberOfStaff, City, Province, PostalCode) VALUES ('Thunderbird', '6335 Thunderbird Crescent', 50, 'Vancouver', 'BC', 'V6T2G9');

INSERT INTO Tenant (SIN, TenantName, Birthdate, Email) VALUES (1001, 'John Doe', TO_DATE('1990-01-01', 'YYYY-MM-DD'), 'john.doe@example.com');
INSERT INTO Tenant (SIN, TenantName, Birthdate, Email) VALUES (1002, 'Jane Smith', TO_DATE('1992-02-02', 'YYYY-MM-DD'), 'jane.smith@example.com');
INSERT INTO Tenant (SIN, TenantName, Birthdate, Email) VALUES (1003, 'Alice Johnson', TO_DATE('1994-03-03', 'YYYY-MM-DD'), 'alice.johnson@example.com');
INSERT INTO Tenant (SIN, TenantName, Birthdate, Email) VALUES (1004, 'Bob Brown', TO_DATE('1996-04-04', 'YYYY-MM-DD'), 'bob.brown@example.com');
INSERT INTO Tenant (SIN, TenantName, Birthdate, Email) VALUES (1005, 'Charlie Davis', TO_DATE('1998-05-05', 'YYYY-MM-DD'), 'charlie.davis@example.com');

INSERT INTO Room (RoomNumber, BuildingName, CleanStatus, OccupancyLimit, NumberOfOccupants) VALUES (101, 'Ponderosa', 1, 4, 3);
INSERT INTO Room (RoomNumber, BuildingName, CleanStatus, OccupancyLimit, NumberOfOccupants) VALUES (102, 'Ponderosa', 0, 1, 1);
INSERT INTO Room (RoomNumber, BuildingName, CleanStatus, OccupancyLimit, NumberOfOccupants) VALUES (101, 'Marine Drive', 0, 2, 2);
INSERT INTO Room (RoomNumber, BuildingName, CleanStatus, OccupancyLimit, NumberOfOccupants) VALUES (102, 'Marine Drive', 0, 4, 4);
INSERT INTO Room (RoomNumber, BuildingName, CleanStatus, OccupancyLimit, NumberOfOccupants) VALUES (101, 'Thunderbird', 1, 4, 4);
INSERT INTO Room (RoomNumber, BuildingName, CleanStatus, OccupancyLimit, NumberOfOccupants) VALUES (102, 'Walter Gage', 1, 4, 4);
INSERT INTO Room (RoomNumber, BuildingName, CleanStatus, OccupancyLimit, NumberOfOccupants) VALUES (101, 'Totem Park', 1, 2, 2);


INSERT INTO Vehicle (VIN, Make, Year, InUse, Model) VALUES ('VIN001', 'Toyota', 2010, 1, 'Prius');
INSERT INTO Vehicle (VIN, Make, Year, InUse, Model) VALUES ('VIN002', 'Toyota', 2011, 0, 'Corolla');
INSERT INTO Vehicle (VIN, Make, Year, InUse, Model) VALUES ('VIN003', 'Tesla', 2020, 1, 'S');
INSERT INTO Vehicle (VIN, Make, Year, InUse, Model) VALUES ('VIN004', 'Tesla', 2021, 0, '3');
INSERT INTO Vehicle (VIN, Make, Year, InUse, Model) VALUES ('VIN005', 'Tesla', 2020, 1, 'X');

INSERT INTO Staff (StaffID, StaffName, VIN) VALUES (2001, 'Elon Musk', 'VIN003');
INSERT INTO Staff (StaffID, StaffName, VIN) VALUES (2002, 'Elon Ma', 'VIN004');
INSERT INTO Staff (StaffID, StaffName, VIN) VALUES (2003, 'Jeff Bay', 'VIN002');
INSERT INTO Staff (StaffID, StaffName, VIN) VALUES (2004, 'Gregor Karl', 'VIN001');
INSERT INTO Staff (StaffID, StaffName, VIN) VALUES (2005, 'John Doe', 'VIN005');

INSERT INTO ToolsSuppliesEquipment (EquipmentID, Cost, LoanPeriod, LateFee, EquipmentName, DateLastTested) VALUES (3001, 100.0, 2, 5.0, 'Power Drill', TO_DATE('2023-11-21', 'YYYY-MM-DD'));
INSERT INTO ToolsSuppliesEquipment (EquipmentID, Cost, LoanPeriod, LateFee, EquipmentName, DateLastTested) VALUES (3002, 100.0, 1, 10.0, 'Basic Vacuum', TO_DATE('2023-09-21', 'YYYY-MM-DD'));
INSERT INTO ToolsSuppliesEquipment (EquipmentID, Cost, LoanPeriod, LateFee, EquipmentName, DateLastTested) VALUES (3003, 200.0, 1, 15.0, 'Dyson Vacuum', TO_DATE('2023-10-21', 'YYYY-MM-DD'));
INSERT INTO ToolsSuppliesEquipment (EquipmentID, Cost, LoanPeriod, LateFee, EquipmentName, DateLastTested) VALUES (3004, 50.0, 3, 20.0, 'Flat-head Screwdriver', TO_DATE('2023-11-21', 'YYYY-MM-DD'));
INSERT INTO ToolsSuppliesEquipment (EquipmentID, Cost, LoanPeriod, LateFee, EquipmentName, DateLastTested) VALUES (3005, 65.0, 3, 25.0, 'Star-head Screwdriver', TO_DATE('2021-09-01', 'YYYY-MM-DD'));

INSERT INTO GameEquipment (EquipmentID, Cost, LoanPeriod, LateFee, EquipmentName, NumberOfParts) VALUES (4001, 250.0, 5, 10.0, 'Chess Set', 32);
INSERT INTO GameEquipment (EquipmentID, Cost, LoanPeriod, LateFee, EquipmentName, NumberOfParts) VALUES (4002, 500.0, 1, 20.0, 'Pool Table Set', 16);
INSERT INTO GameEquipment (EquipmentID, Cost, LoanPeriod, LateFee, EquipmentName, NumberOfParts) VALUES (4003, 150.0, 1, 5.0, 'Soccer Ball', 1);
INSERT INTO GameEquipment (EquipmentID, Cost, LoanPeriod, LateFee, EquipmentName, NumberOfParts) VALUES (4004, 300.0, 2, 15.0, 'Tennis Racket', 2);
INSERT INTO GameEquipment (EquipmentID, Cost, LoanPeriod, LateFee, EquipmentName, NumberOfParts) VALUES (4005, 350.0, 2, 12.0, 'Basketball', 1);

INSERT INTO StaffToolsSuppliesBorrowing (BorrowDate, StaffID, EquipmentID) VALUES (TO_DATE('2023-11-01', 'YYYY-MM-DD'), 2001, 3001);
INSERT INTO StaffToolsSuppliesBorrowing (BorrowDate, StaffID, EquipmentID) VALUES (TO_DATE('2023-11-02', 'YYYY-MM-DD'), 2002, 3002);
INSERT INTO StaffToolsSuppliesBorrowing (BorrowDate, StaffID, EquipmentID) VALUES (TO_DATE('2023-11-03', 'YYYY-MM-DD'), 2003, 3003);
INSERT INTO StaffToolsSuppliesBorrowing (BorrowDate, StaffID, EquipmentID) VALUES (TO_DATE('2022-11-04', 'YYYY-MM-DD'), 2004, 3001);
INSERT INTO StaffToolsSuppliesBorrowing (BorrowDate, StaffID, EquipmentID) VALUES (TO_DATE('2023-11-05', 'YYYY-MM-DD'), 2005, 3001);

INSERT INTO TenantGameBorrowing (BorrowDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-01', 'YYYY-MM-DD'), 1001, 4004);
INSERT INTO TenantGameBorrowing (BorrowDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-02', 'YYYY-MM-DD'), 1002, 4004);
INSERT INTO TenantGameBorrowing (BorrowDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-03', 'YYYY-MM-DD'), 1003, 4004);
INSERT INTO TenantGameBorrowing (BorrowDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-01', 'YYYY-MM-DD'), 1004, 4005);
INSERT INTO TenantGameBorrowing (BorrowDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-02', 'YYYY-MM-DD'), 1005, 4001);

INSERT INTO StaffToolsSuppliesReturning (ReturnDate, StaffID, EquipmentID) VALUES (TO_DATE('2023-11-03', 'YYYY-MM-DD'), 2001, 3001);
INSERT INTO StaffToolsSuppliesReturning (ReturnDate, StaffID, EquipmentID) VALUES (TO_DATE('2023-11-03', 'YYYY-MM-DD'), 2002, 3002);
INSERT INTO StaffToolsSuppliesReturning (ReturnDate, StaffID, EquipmentID) VALUES (TO_DATE('2023-11-04', 'YYYY-MM-DD'), 2003, 3003);
INSERT INTO StaffToolsSuppliesReturning (ReturnDate, StaffID, EquipmentID) VALUES (TO_DATE('2023-11-05', 'YYYY-MM-DD'), 2004, 3001);
INSERT INTO StaffToolsSuppliesReturning (ReturnDate, StaffID, EquipmentID) VALUES (TO_DATE('2023-11-06', 'YYYY-MM-DD'), 2005, 3001);

INSERT INTO TenantToolsSuppliesReturning (ReturnDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-02', 'YYYY-MM-DD'), 1001, 3004);
INSERT INTO TenantToolsSuppliesReturning (ReturnDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-03', 'YYYY-MM-DD'), 1002, 3004);
INSERT INTO TenantToolsSuppliesReturning (ReturnDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-04', 'YYYY-MM-DD'), 1003, 3004);
INSERT INTO TenantToolsSuppliesReturning (ReturnDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-02', 'YYYY-MM-DD'), 1004, 3005);
INSERT INTO TenantToolsSuppliesReturning (ReturnDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-06', 'YYYY-MM-DD'), 1005, 3001);

INSERT INTO TenantGameReturning (ReturnDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-02', 'YYYY-MM-DD'), 1001, 4004);
INSERT INTO TenantGameReturning (ReturnDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-03', 'YYYY-MM-DD'), 1002, 4004);
INSERT INTO TenantGameReturning (ReturnDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-04', 'YYYY-MM-DD'), 1003, 4004);
INSERT INTO TenantGameReturning (ReturnDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-02', 'YYYY-MM-DD'), 1004, 4005);
INSERT INTO TenantGameReturning (ReturnDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-06', 'YYYY-MM-DD'), 1005, 4001);

INSERT INTO TenantRentsRoom (SIN, RentalRate, ContractEndDate, RoomNumber, BuildingName) VALUES (1001, 1200.0, TO_DATE('2024-01-01', 'YYYY-MM-DD'), 101, 'Ponderosa');
INSERT INTO TenantRentsRoom (SIN, RentalRate, ContractEndDate, RoomNumber, BuildingName) VALUES (1002, 1000.0, TO_DATE('2024-01-01', 'YYYY-MM-DD'), 102, 'Ponderosa');
INSERT INTO TenantRentsRoom (SIN, RentalRate, ContractEndDate, RoomNumber, BuildingName) VALUES (1003, 1100.0, TO_DATE('2023-12-31', 'YYYY-MM-DD'), 101, 'Marine Drive');
INSERT INTO TenantRentsRoom (SIN, RentalRate, ContractEndDate, RoomNumber, BuildingName) VALUES (1004, 950.0, TO_DATE('2023-12-31', 'YYYY-MM-DD'), 102, 'Marine Drive');
INSERT INTO TenantRentsRoom (SIN, RentalRate, ContractEndDate, RoomNumber, BuildingName) VALUES (1005, 1300.0, TO_DATE('2024-01-02', 'YYYY-MM-DD'), 101, 'Thunderbird');

INSERT INTO Mail (MailID, TenantSIN, RoomNumber, BuildingName, MailType) VALUES (NULL, 1001, 101, 'Ponderosa', 'Parcel');
INSERT INTO Mail (MailID, TenantSIN, RoomNumber, BuildingName, MailType) VALUES (NULL, 1002, 102, 'Ponderosa', 'Letter');
INSERT INTO Mail (MailID, TenantSIN, RoomNumber, BuildingName, MailType) VALUES (NULL, 1003, 101, 'Marine Drive', 'Letter');
INSERT INTO Mail (MailID, TenantSIN, RoomNumber, BuildingName, MailType) VALUES (NULL, 1004, 102, 'Marine Drive', 'Parcel');
INSERT INTO Mail (MailID, TenantSIN, RoomNumber, BuildingName, MailType) VALUES (NULL, 1005, 101, 'Thunderbird', 'Letter');

INSERT INTO BuildingReceivesMail (MailID, DateReceived) VALUES (10000, TO_DATE('2023-11-22', 'YYYY-MM-DD'));
INSERT INTO BuildingReceivesMail (MailID, DateReceived) VALUES (10001, TO_DATE('2023-11-23', 'YYYY-MM-DD'));
INSERT INTO BuildingReceivesMail (MailID, DateReceived) VALUES (10002, TO_DATE('2023-11-24', 'YYYY-MM-DD'));
INSERT INTO BuildingReceivesMail (MailID, DateReceived) VALUES (10003, TO_DATE('2023-11-25', 'YYYY-MM-DD'));
INSERT INTO BuildingReceivesMail (MailID, DateReceived) VALUES (10004, TO_DATE('2023-11-26', 'YYYY-MM-DD'));

INSERT INTO Request (RequestID, BuildingName, RoomNumber, RequestType, Complete, SubmissionDate, StaffID, SIN) VALUES (NULL, 'Ponderosa', 101, 'Maintenance', 0, TO_DATE('2023-11-20', 'YYYY-MM-DD'), 2001, 1001);
INSERT INTO Request (RequestID, BuildingName, RoomNumber, RequestType, Complete, SubmissionDate, StaffID, SIN) VALUES (NULL, 'Walter Gage', 102, 'Cleaning', 1, TO_DATE('2023-11-21', 'YYYY-MM-DD'), 2002, 1002);
INSERT INTO Request (RequestID, BuildingName, RoomNumber, RequestType, Complete, SubmissionDate, StaffID, SIN) VALUES (NULL, 'Totem Park', 101, 'Maintenance', 0, TO_DATE('2023-11-22', 'YYYY-MM-DD'), 2003, 1003);
INSERT INTO Request (RequestID, BuildingName, RoomNumber, RequestType, Complete, SubmissionDate, StaffID, SIN) VALUES (NULL, 'Marine Drive', 102, 'Maintenance', 1, TO_DATE('2023-11-23', 'YYYY-MM-DD'), 2004, 1004);
INSERT INTO Request (RequestID, BuildingName, RoomNumber, RequestType, Complete, SubmissionDate, StaffID, SIN) VALUES (NULL, 'Thunderbird', 101, 'Cleaning', 0, TO_DATE('2023-11-24', 'YYYY-MM-DD'), 2005, 1005);

