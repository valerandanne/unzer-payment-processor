DROP TABLE IF EXISTS PAYMENT;

CREATE TABLE payment (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    AMOUNT DOUBLE NOT NULL,
    CURRENCY CHAR NOT NULL,
    STATUS VARCHAR(50),
    APPROVAL_ID VARCHAR(250),
    CREATION_DATE DATETIME NOT NULL);