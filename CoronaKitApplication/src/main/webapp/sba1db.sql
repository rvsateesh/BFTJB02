DROP DATABASE sba1DB;

CREATE DATABASE sba1DB;

USE sba1DB;

CREATE TABLE products(
	pid int AUTO_INCREMENT primary key,
	pname varchar(20) not null,
	pcost decimal not null,
	pdesc varchar(60)
);

INSERT INTO products VALUES
(1, "Face Mask", "30.0", "Reusable Cotton Face Mask"),
(2, "Sanitizer", "100.0", "Alchohol based sanitizer"),
(3, "Remidesivir", "5000.0", "Immunity boosting medicine");

CREATE TABLE userdetails( 
	uid int AUTO_INCREMENT primary key, 
    uname varchar(20) not null, 
    uemail varchar(20) not null, 
    ucontact varchar(10) not null, 
    uaddress varchar(40) not null 
    );

INSERT INTO userdetails VALUES
(1, "visitor1", "visitor1@gmail.com", "9000000000", "visitorAddress");

CREATE TABLE orderdetails(
	pid int not null,
    pname varchar(20) not null,
    pcost decimal not null,
    pquantity int not null
    );
    
INSERT INTO orderdetails VALUES
(1, "Face Mask", "30.0", 5);

DELIMITER #
CREATE TRIGGER prodinstrigger after INSERT ON products 
for Each ROW 
begin 
INSERT INTO orderdetails (pid,pname,pcost,pquantity) VALUES (new.pid, new.pname, new.pcost, '0'); 
end;
#