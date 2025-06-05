CREATE TABLE MANAGER (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    firstname TEXT NOT NULL,
    lastname TEXT NOT NULL,
    CNIC TEXT UNIQUE NOT NULL,
    salary REAL NOT NULL,
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE SALESMAN (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    firstname TEXT NOT NULL,
    lastname TEXT NOT NULL,
    CNIC TEXT UNIQUE NOT NULL,
    salary REAL NOT NULL,
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE DELIVERYBOY (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    firstname TEXT NOT NULL,
    lastname TEXT NOT NULL,
    CNIC TEXT UNIQUE NOT NULL,
    salary REAL NOT NULL,
    busyTill DATETIME
);

CREATE TABLE ITEM (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    price INTEGER NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity >= 0),
    availability BOOLEAN NOT NULL DEFAULT 1,
    weight INTEGER NOT NULL,
    mfgDate DATE NOT NULL,
    expDate DATE NOT NULL,
    itemCapacity INTEGER NOT NULL CHECK (itemCapacity >= 0),
    extraDetails TEXT,
    purchasePrice INTEGER NOT NULL CHECK (purchasePrice >= 0),
    supplierName TEXT,
    extraClassType TEXT NOT NULL
);






CREATE TABLE CUSTOMER (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    firstname TEXT NOT NULL,
    lastname TEXT NOT NULL,
    CNIC TEXT UNIQUE NOT NULL,
    username TEXT UNIQUE,
    password TEXT,
    onlineStatus BOOLEAN NOT NULL DEFAULT 0

);

-- Update the BILL table to store item IDs and quantities
CREATE TABLE BILL (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    customerId INTEGER NOT NULL,
    salesmanId INTEGER NOT NULL,
    totalAmount INTEGER NOT NULL CHECK (totalAmount >= 0),
    items TEXT NOT NULL,  -- This will now store a JSON or serialized format with item IDs and quantities
    type TEXT NOT NULL CHECK (type IN ('online', 'physical')),
    FOREIGN KEY (salesmanId) REFERENCES SALESMAN(id),
    FOREIGN KEY (customerId) REFERENCES CUSTOMER(id)
);

-- Update the PURCHASE table to store item IDs and quantities
CREATE TABLE PURCHASE (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    itemsAndQuantity TEXT NOT NULL,  -- This will store item IDs and quantities
    totalAmount INTEGER NOT NULL CHECK (totalAmount >= 0)
);

