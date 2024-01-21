--------------------START OF TENANT-------------------------

-- (Tenant) Games that can be Borrowed
SELECT G.EquipmentID, G.EquipmentName
FROM GameEquipment G
WHERE NOT EXISTS (
    SELECT 1
    FROM TenantGameBorrowing TGB
    WHERE TGB.EquipmentID = G.EquipmentID
)
   OR EXISTS (
    SELECT 1
    FROM TenantGameReturning TGR
    WHERE TGR.EquipmentID = G.EquipmentID
      AND (TGR.ReturnDate IS NOT NULL AND TGR.ReturnDate > (SELECT MAX(BorrowDate) FROM TenantGameBorrowing WHERE EquipmentID = G.EquipmentID))
)
ORDER BY G.EquipmentID;

--INSERT INTO TenantGameBorrowing (BorrowDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-21', 'YYYY-MM-DD'), 1005, 4001);
--INSERT INTO TenantGameBorrowing (BorrowDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-21', 'YYYY-MM-DD'), 1005, 4001);
--INSERT INTO TenantGameBorrowing (BorrowDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-21', 'YYYY-MM-DD'), 1005, 4004);


-- (Tenant) Games that can be returned -- replace :OverdueFlag (0 or 1) and :SIN (test value for :SIN 1005)
SELECT G.EquipmentID,
       G.EquipmentName
FROM GameEquipment G
WHERE EXISTS (
    SELECT 1
    FROM TenantGameBorrowing TGB
    WHERE TGB.EquipmentID = G.EquipmentID
      AND TGB.SIN = :SIN -- Replace with given SIN
)
  AND NOT EXISTS (
    SELECT 1
    FROM TenantGameReturning TGR
    WHERE TGR.EquipmentID = G.EquipmentID
      AND TGR.SIN = :SIN
      AND (TGR.ReturnDate IS NULL OR TGR.ReturnDate >= (SELECT MAX(BorrowDate) FROM TenantGameBorrowing WHERE EquipmentID = G.EquipmentID AND SIN = :SIN))
)
  AND (
            :OverdueFlag = 0
        OR (:OverdueFlag = 1 AND TRUNC(CURRENT_DATE) > (SELECT MAX(TGB2.BorrowDate) + G.LoanPeriod FROM TenantGameBorrowing TGB2 WHERE TGB2.EquipmentID = G.EquipmentID AND TGB2.SIN = :SIN))
    )
ORDER BY G.EquipmentID;

--INSERT INTO StaffToolsSuppliesBorrowing (BorrowDate, StaffID, EquipmentID) VALUES (TO_DATE('2023-11-21', 'YYYY-MM-DD'), 2001, 3001);
--INSERT INTO TenantToolsSuppliesBorrowing (BorrowDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-21', 'YYYY-MM-DD'), 1001, 3004);

-- Tools that can be borrowed (both staff and tenants)
SELECT tse.EquipmentID, tse.EquipmentName
FROM ToolsSuppliesEquipment tse
         LEFT JOIN (
    SELECT EquipmentID, MAX(BorrowDate) AS LastBorrowDate
    FROM (
             SELECT EquipmentID, BorrowDate FROM StaffToolsSuppliesBorrowing
             UNION ALL
             SELECT EquipmentID, BorrowDate FROM TenantToolsSuppliesBorrowing
         ) CombinedBorrow
    GROUP BY EquipmentID
) LastBorrow ON tse.EquipmentID = LastBorrow.EquipmentID
         LEFT JOIN (
    SELECT EquipmentID, MAX(ReturnDate) AS LastReturnDate
    FROM (
             SELECT EquipmentID, ReturnDate FROM StaffToolsSuppliesReturning
             UNION ALL
             SELECT EquipmentID, ReturnDate FROM TenantToolsSuppliesReturning
         ) CombinedReturn
    GROUP BY EquipmentID
) LastReturn ON tse.EquipmentID = LastReturn.EquipmentID
WHERE (LastBorrow.LastBorrowDate IS NULL OR LastReturn.LastReturnDate > LastBorrow.LastBorrowDate);

-- (Tenant) Tools that can be returned (by given SIN)
SELECT tse.EquipmentID, tse.EquipmentName
FROM ToolsSuppliesEquipment tse
         LEFT JOIN (
    SELECT EquipmentID, MAX(BorrowDate) AS LastBorrowDate
    FROM TenantToolsSuppliesBorrowing
    WHERE SIN = :SIN -- replace with given SIN
    GROUP BY EquipmentID
) LastBorrow ON tse.EquipmentID = LastBorrow.EquipmentID
         LEFT JOIN (
    SELECT EquipmentID, MAX(ReturnDate) AS LastReturnDate
    FROM TenantToolsSuppliesReturning
    GROUP BY EquipmentID
) LastReturn ON tse.EquipmentID = LastReturn.EquipmentID
WHERE LastBorrow.LastBorrowDate IS NOT NULL AND (LastReturn.LastReturnDate IS NULL OR LastReturn.LastReturnDate <= LastBorrow.LastBorrowDate);

-- (Tenant) View my requests -- replace :Complete with Complete (test value:1 or 0) and replace :SIN with SIN (test value: 1005)
SELECT *
FROM Request
WHERE SIN = :SIN AND
        Complete = :Complete;

-- (Tenant) Submit Request -- replace :RequestType (test value: 'Cleaning'/ 'Maintenance') and SIN with SIN (test value: 1005) (staff assigned set to StaffID: 2001)
INSERT INTO Request (BuildingName, RoomNumber, RequestType, Complete, SubmissionDate, StaffID, SIN)
SELECT t.BuildingName, t.RoomNumber, :RequestType, 0, TRUNC(CURRENT_DATE), 2001, :SIN
FROM TenantRentsRoom t
WHERE t.SIN = :SIN;

-- (Tenant) View my mail -- replace :TenantSIN with TenantSIN (test value: 1005)
SELECT m.MailID, m.TenantSIN, m.RoomNumber, m.BuildingName, m.MailType, brm.DateReceived
FROM Mail m
         JOIN BuildingReceivesMail brm ON m.MailID = brm.MailID
WHERE m.TenantSIN = :TenantSIN
<<<<<<< HEAD
ORDER BY brm.DateReceived DESC;
=======
ORDER BY brm.DateReceived;
>>>>>>> ab959f8b154518de26332728c8a452f549ce3d1b

--------------------END OF TENANT-------------------------

--------------------START OF STAFF-------------------------

-- Tools that can be borrowed (both staff and tenants)
SELECT tse.EquipmentID, tse.EquipmentName
FROM ToolsSuppliesEquipment tse
         LEFT JOIN (
    SELECT EquipmentID, MAX(BorrowDate) AS LastBorrowDate
    FROM (
             SELECT EquipmentID, BorrowDate FROM StaffToolsSuppliesBorrowing
             UNION ALL
             SELECT EquipmentID, BorrowDate FROM TenantToolsSuppliesBorrowing
         ) CombinedBorrow
    GROUP BY EquipmentID
) LastBorrow ON tse.EquipmentID = LastBorrow.EquipmentID
         LEFT JOIN (
    SELECT EquipmentID, MAX(ReturnDate) AS LastReturnDate
    FROM (
             SELECT EquipmentID, ReturnDate FROM StaffToolsSuppliesReturning
             UNION ALL
             SELECT EquipmentID, ReturnDate FROM TenantToolsSuppliesReturning
         ) CombinedReturn
    GROUP BY EquipmentID
) LastReturn ON tse.EquipmentID = LastReturn.EquipmentID
WHERE (LastBorrow.LastBorrowDate IS NULL OR LastReturn.LastReturnDate > LastBorrow.LastBorrowDate);

-- (Staff) Tools that can be returned (by given StaffID)
SELECT tse.EquipmentID, tse.EquipmentName
FROM ToolsSuppliesEquipment tse
         LEFT JOIN (
    SELECT EquipmentID, MAX(BorrowDate) AS LastBorrowDate
    FROM StaffToolsSuppliesBorrowing
    WHERE StaffID = :StaffID
    GROUP BY EquipmentID
) LastBorrow ON tse.EquipmentID = LastBorrow.EquipmentID
         LEFT JOIN (
    SELECT EquipmentID, MAX(ReturnDate) AS LastReturnDate
    FROM StaffToolsSuppliesReturning
    GROUP BY EquipmentID
) LastReturn ON tse.EquipmentID = LastReturn.EquipmentID
WHERE LastBorrow.LastBorrowDate IS NOT NULL AND (LastReturn.LastReturnDate IS NULL OR LastReturn.LastReturnDate <= LastBorrow.LastBorrowDate);

-- (Staff) View requests -- replace :CompleteStatus with complete variable (0 or 1)
-- When the flag is 0, the date filter is not applied.
-- When the flag is 1, the query filters for submissions that are older than a week.
-- When the flag is 2, the query filters for submissions within the last week.

SELECT *
FROM Request
WHERE StaffID = 2001
  AND Complete = :CompleteStatus
  AND (
            :ApplyDateFilter = 0
        OR (:ApplyDateFilter = 1 AND SubmissionDate <= CURRENT_DATE - 7)
        OR (:ApplyDateFilter = 2 AND SubmissionDate > CURRENT_DATE - 7)
    );

-- (Staff) Mark request as complete -- replace :RequestID with RequestID variable and :StaffID from values in selected row
UPDATE Request
SET Complete = 1
WHERE RequestID = :RequestID
  AND StaffID = :StaffID;

-- (Staff) View Available Vehicles

SELECT v.VIN, v.Make, v.Model, v.Year, v.InUse, s.StaffName, s.StaffID
FROM Vehicle v
         LEFT JOIN Staff s ON v.VIN = s.VIN
WHERE v.InUse = 0;

-- (Staff) Borrow Vehicle -- replace :VIN from selected row
-- Vehicle and Staff Table need to be updated
UPDATE Vehicle
SET InUse = 1
WHERE VIN = :VIN;

UPDATE Staff
SET VIN = :VIN
WHERE StaffID = :StaffID;

-- (Staff) View Vehicle that They can return

SELECT v.VIN, v.Make, v.Model, v.Year, v.InUse, s.StaffName, s.StaffID
FROM Vehicle v
         LEFT JOIN Staff s ON v.VIN = s.VIN
WHERE v.InUse = 1 AND s.StaffID = :StaffID;


-- (Staff) Return Vehicle -- replace :VIN from selected row
-- Vehicle and Staff Table need to be updated
UPDATE Vehicle
SET InUse = 1
WHERE VIN = :VIN;

UPDATE Staff
SET VIN = NULL
WHERE StaffID = :StaffID;


-- (Staff) View All Mail -- replace :ApplyDateFilter (Date Filter same as Request Page)
-- When the flag is 0, the date filter is not applied.
-- When the flag is 1, the query filters for mail received that are older than a week.
-- When the flag is 2, the query filters for mail received within the last week.
SELECT m.MailID, m.TenantSIN, m.RoomNumber, m.BuildingName, m.MailType, brm.DateReceived
FROM Mail m
         JOIN BuildingReceivesMail brm ON m.MailID = brm.MailID
    AND (
                                                      :ApplyDateFilter = 0
                                                  OR (:ApplyDateFilter = 1 AND brm.DateReceived <= CURRENT_DATE - 7)
                                                  OR (:ApplyDateFilter = 2 AND brm.DateReceived > CURRENT_DATE - 7)
                                              )
ORDER BY brm.DateReceived DESC;

-- (Staff) Assign Mail -- replace :TenantSIN, :RoomNumber, :BuildingName, :MailType
INSERT INTO Mail (TenantSIN, RoomNumber, BuildingName, MailType)
VALUES (:TenantSIN, :RoomNumber, :BuildingName, :MailType);

INSERT INTO BuildingReceivesMail (MailID, DateReceived)
VALUES (:MailID, TRUNC(CURRENT_DATE));

--------------------END OF STAFF-------------------------

--------------------START OF ADMIN-------------------------

-- (Admin) Games that can be Borrowed
SELECT G.EquipmentID, G.EquipmentName
FROM GameEquipment G
WHERE NOT EXISTS (
    SELECT 1
    FROM TenantGameBorrowing TGB
    WHERE TGB.EquipmentID = G.EquipmentID
)
   OR EXISTS (
    SELECT 1
    FROM TenantGameReturning TGR
    WHERE TGR.EquipmentID = G.EquipmentID
      AND (TGR.ReturnDate IS NOT NULL AND TGR.ReturnDate > (SELECT MAX(BorrowDate) FROM TenantGameBorrowing WHERE EquipmentID = G.EquipmentID))
)
ORDER BY G.EquipmentID;

--INSERT INTO TenantGameBorrowing (BorrowDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-21', 'YYYY-MM-DD'), 1005, 4001);
--INSERT INTO TenantGameBorrowing (BorrowDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-21', 'YYYY-MM-DD'), 1005, 4001);
--INSERT INTO TenantGameBorrowing (BorrowDate, SIN, EquipmentID) VALUES (TO_DATE('2023-11-21', 'YYYY-MM-DD'), 1005, 4004);


-- (Admin) Games that can be returned -- (test value for :SIN 1005)
SELECT G.EquipmentID,
       G.EquipmentName
FROM GameEquipment G
WHERE EXISTS (
    SELECT 1
    FROM TenantGameBorrowing TGB
    WHERE TGB.EquipmentID = G.EquipmentID
      AND TGB.SIN = :SIN -- Replace with given SIN
)
  AND NOT EXISTS (
    SELECT 1
    FROM TenantGameReturning TGR
    WHERE TGR.EquipmentID = G.EquipmentID
      AND TGR.SIN = :SIN
      AND (TGR.ReturnDate IS NULL OR TGR.ReturnDate >= (SELECT MAX(BorrowDate) FROM TenantGameBorrowing WHERE EquipmentID = G.EquipmentID AND SIN = :SIN))
)
  AND (
            :OverdueFlag = 0
        OR (:OverdueFlag = 1 AND TRUNC(CURRENT_DATE) > (SELECT MAX(BorrowDate) + G.LoanPeriod FROM TenantGameBorrowing WHERE EquipmentID = G.EquipmentID AND SIN = :SIN))
    )
ORDER BY G.EquipmentID;


-- Tools that can be borrowed (both staff and tenants)
SELECT tse.EquipmentID, tse.EquipmentName
FROM ToolsSuppliesEquipment tse
         LEFT JOIN (
    SELECT EquipmentID, MAX(BorrowDate) AS LastBorrowDate
    FROM (
             SELECT EquipmentID, BorrowDate FROM StaffToolsSuppliesBorrowing
             UNION ALL
             SELECT EquipmentID, BorrowDate FROM TenantToolsSuppliesBorrowing
         ) CombinedBorrow
    GROUP BY EquipmentID
) LastBorrow ON tse.EquipmentID = LastBorrow.EquipmentID
         LEFT JOIN (
    SELECT EquipmentID, MAX(ReturnDate) AS LastReturnDate
    FROM (
             SELECT EquipmentID, ReturnDate FROM StaffToolsSuppliesReturning
             UNION ALL
             SELECT EquipmentID, ReturnDate FROM TenantToolsSuppliesReturning
         ) CombinedReturn
    GROUP BY EquipmentID
) LastReturn ON tse.EquipmentID = LastReturn.EquipmentID
WHERE (LastBorrow.LastBorrowDate IS NULL OR LastReturn.LastReturnDate > LastBorrow.LastBorrowDate);

-- Tools that can be returned (both staff and tenant)
SELECT tse.EquipmentID, tse.EquipmentName
FROM ToolsSuppliesEquipment tse
         LEFT JOIN (
    SELECT EquipmentID, MAX(BorrowDate) AS LastBorrowDate
    FROM (
             SELECT EquipmentID, BorrowDate FROM StaffToolsSuppliesBorrowing
             UNION ALL
             SELECT EquipmentID, BorrowDate FROM TenantToolsSuppliesBorrowing
         ) CombinedBorrow
    GROUP BY EquipmentID
) LastBorrow ON tse.EquipmentID = LastBorrow.EquipmentID
         LEFT JOIN (
    SELECT EquipmentID, MAX(ReturnDate) AS LastReturnDate
    FROM (
             SELECT EquipmentID, ReturnDate FROM StaffToolsSuppliesReturning
             UNION ALL
             SELECT EquipmentID, ReturnDate FROM TenantToolsSuppliesReturning
         ) CombinedReturn
    GROUP BY EquipmentID
) LastReturn ON tse.EquipmentID = LastReturn.EquipmentID
WHERE LastBorrow.LastBorrowDate IS NOT NULL AND (LastReturn.LastReturnDate IS NULL OR LastReturn.LastReturnDate <= LastBorrow.LastBorrowDate);

-- (Admin) View All Requests -- replace :Complete with complete variable (0 or 1)
SELECT * from Request
WHERE StaffID = 2001 AND
        Complete = :Complete;

-- (Admin) View Vehicles and who is using -- replace :inUse (1 or 0)

SELECT v.VIN, v.Make, v.Model, v.Year, v.InUse, s.StaffName, s.StaffID
FROM Vehicle v
         LEFT JOIN Staff s ON v.VIN = s.VIN
WHERE v.InUse = :inUse;

-- (Admin) View All Mail -- replace :ApplyDateFilter (Date Filter same as Request Page)
-- When the flag is 0, the date filter is not applied.
-- When the flag is 1, the query filters for mail received that are older than a week.
-- When the flag is 2, the query filters for mail received within the last week.
SELECT m.MailID, m.TenantSIN, m.RoomNumber, m.BuildingName, m.MailType, brm.DateReceived
FROM Mail m
         JOIN BuildingReceivesMail brm ON m.MailID = brm.MailID
    AND (
                                                      :ApplyDateFilter = 0
                                                  OR (:ApplyDateFilter = 1 AND brm.DateReceived <= CURRENT_DATE - 7)
                                                  OR (:ApplyDateFilter = 2 AND brm.DateReceived > CURRENT_DATE - 7)
                                              )
ORDER BY brm.DateReceived DESC;


-- (Admin) Assign Mail -- replace :TenantSIN, :RoomNumber, :BuildingName, :MailType
DECLARE
v_TenantSIN INTEGER;
    v_RoomNumber INTEGER;
    v_BuildingName VARCHAR2(50);
    v_MailType VARCHAR2(50);
    v_MailID INTEGER;
BEGIN
    -- Assign values to variables
    v_TenantSIN := :TenantSIN;
    v_RoomNumber := :RoomNumber;
    v_BuildingName := :BuildingName;
    v_MailType := :MailType;

INSERT INTO Mail (TenantSIN, RoomNumber, BuildingName, MailType)
VALUES (v_TenantSIN, v_RoomNumber, v_BuildingName, v_MailType)
    RETURNING MailID INTO v_MailID;

INSERT INTO BuildingReceivesMail (MailID, DateReceived)
VALUES (v_MailID, TRUNC(CURRENT_DATE));
END;


-- (Admin) View all Tenants
SELECT * FROM Tenant;

-- (Admin) View all Tenants without Rooms assigned
SELECT T.*
FROM Tenant T
         LEFT JOIN TenantRentsRoom TRR ON T.SIN = TRR.SIN
WHERE TRR.SIN IS NULL;


-- (Admin) Add Tenant
INSERT INTO Tenant (SIN, TenantName, Birthdate, Email)
VALUES (:SIN, :TenantName, TO_DATE(:Birthdate, 'YYYY-MM-DD'), :Email);

-- (Admin) Assign Tenant A Room
INSERT INTO TenantRentsRoom (SIN, RentalRate, ContractEndDate, RoomNumber, BuildingName)
VALUES (:SIN, :RentalRate, TO_DATE(:ContractEndDate, 'YYYY-MM-DD'), :RoomNumber, :BuildingName);

-- (Admin) See All PostalCodes
SELECT * FROM PostalCode;

-- (Admin) See All Buildings
SELECT * FROM Building;

-- (Admin) Add Building


-- Attempt to insert the new postal code if it doesn't exist
INSERT INTO PostalCode (PostalCode, City, Province)
SELECT :PostalCode, :City, :Province
    WHERE NOT EXISTS (
    SELECT 1 FROM PostalCode WHERE PostalCode = :PostalCode
);

-- Insert the new building
INSERT INTO Building (BuildingName, Address, NumberOfStaff, City, Province, PostalCode)
VALUES (:BuildingName, :Address, :NumberOfStaff, :City, :Province, :PostalCode);


-- (Admin) See All Rooms
SELECT * FROM Room;


-- (Admin) Add Room
INSERT INTO Room (RoomNumber, BuildingName, CleanStatus, OccupancyLimit, NumberOfOccupants)
VALUES (:RoomNumber, :BuildingName, :CleanStatus, :OccupancyLimit, :NumberOfOccupants);
