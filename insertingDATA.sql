-- Insert random data into MANAGER
INSERT INTO MANAGER (firstname, lastname, CNIC, salary, username, password)
VALUES
('SAAD', 'UMAR', '12345-6789012-3', 120000, 'manager', '123'),


-- Insert random data into SALESMAN
INSERT INTO SALESMAN (firstname, lastname, CNIC, salary, username, password)
VALUES
('Charlie', 'Wilson', '45678-9012345-6', 60000, 'user1', '123'),
('Emily', 'Davis', '56789-0123456-7', 65000, 'user2', '123'),
('Michael', 'Taylor', '67890-1234567-8', 58000, 'user3', '123');

-- Insert random data into DELIVERYBOY
INSERT INTO DELIVERYBOY (firstname, lastname, CNIC, salary, busyTill)
VALUES
('Liam', 'Johnson', '78901-2345678-9', 45000, '2024-12-01 17:00:00'),
('Noah', 'Martinez', '89012-3456789-0', 43000, '2024-12-01 14:30:00'),
('Ethan', 'Garcia', '90123-4567890-1', 48000, '2024-12-01 16:00:00');

-- Insert random data into ITEM
INSERT INTO ITEM (name, price, quantity, availability, mfgDate, expDate, itemCapacity, extraDetails, purchasePrice, supplierName)
VALUES
('Laptop', 500, 5, 1, '2023-06-01', '2025-06-01', 10, 'Electronics item', 450, 'TechSupply Co.'),
('Smartphone', 300, 08, 1, '2023-08-15', '2024-08-15', 5, 'Latest model', 270, 'MobileHub'),
('Chair', 2000, 25, 1, '2023-01-01', '2025-01-01', 30, 'Office furniture', 1800, 'FurniWorld');



-- Insert random data into PURCHASE
INSERT INTO PURCHASE (itemsAndQuantity, totalAmount)
VALUES
('1:5,3:10', 20250),  -- Item 1 with quantity 5, Item 3 with quantity 10
('2:8', 2160),      -- Item 2 with quantity 8
('3:15', 27000);      -- Item 3 with quantity 15

-- Insert random data into CUSTOMER
INSERT INTO CUSTOMER (firstname, lastname, CNIC, username, password, onlineStatus, bills)
VALUES
('Sophia', 'Miller', '11223-4455667-8', 'sophiam', 'sophiapass', 1, '1,2'),
('James', 'Clark', '22334-5566778-9', 'jamesc', 'jamespass', 0, ''),
('Olivia', 'Adams', '33445-6677889-0', 'oliviaa', 'oliviapass', 1, '3'),
('William', 'Scott', '44556-7788990-1', 'williams', 'willpass', 0, '2,3'),
('Emma', 'Turner', '55667-8899001-2', 'emmat', 'emmapass', 1, '1');
