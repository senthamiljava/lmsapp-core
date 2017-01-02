DROP TABLE LEAVE_TYPES;

CREATE TABLE LEAVE_TYPES
(
ID INT PRIMARY KEY AUTO_INCREMENT,
LEAVE_TYPE VARCHAR(100) NOT NULL,
ACTIVE TINYINT DEFAULT 1,
CONSTRAINT LEAVE_TYPE_UQ UNIQUE( LEAVE_TYPE)
);

INSERT INTO LEAVE_TYPES ( LEAVE_TYPE) 
VALUES 
('Casual Leave'),
('Sick Leave'),
('Paid Leave'),
('Maternity Leave'),
('Paternity Leave'),
('Privileged Leave');


SELECT * FROM LEAVE_TYPES;