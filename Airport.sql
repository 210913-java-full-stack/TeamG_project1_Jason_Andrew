###########################################################
################## CREATE FRESH DATABASE ##################
###########################################################
DROP DATABASE IF EXISTS AirportKiosk; #Project1
CREATE DATABASE AirportKiosk; #Project1

USE AirportKiosk; #Project1

DROP TABLE IF EXISTS people;
DROP TABLE IF EXISTS flights;
DROP TABLE IF EXISTS ticket_people_flights;

CREATE TABLE people
(
    people_id         INT AUTO_INCREMENT,
    username         VARCHAR (12),
    access_level    INT NOT NULL, # ?Needs to be a list of selection CHECKING, SAVINGS, OTHER
    password        VARCHAR(20),
    CONSTRAINT people_id_pk PRIMARY KEY (people_id)
);

CREATE TABLE flights
(
    flight_id         INT AUTO_INCREMENT,
    departure_city         VARCHAR(20),
    arrival_city        VARCHAR(20),
    flight_date            DATE,
    flight_time            TIME,
    CONSTRAINT flight_id_pk PRIMARY KEY (flight_id)
);

  CREATE TABLE tickets_people_flights #ticket table to join all tables
(
    ticket_id         INT AUTO_INCREMENT,
    people_id         INT NOT NULL,
    flight_id         INT NOT NULL,
    CONSTRAINT ticket_people_flight_fk PRIMARY KEY (ticket_id),
    CONSTRAINT people_tickets FOREIGN KEY (people_id) REFERENCES people(people_id),
    CONSTRAINT flight_tickets FOREIGN KEY (flight_id) REFERENCES flights(flight_id)
    );

###########################################################
################# POPULATE FRESH DATABASE #################
###########################################################



Select * FROM  people ;
Select * FROM  flights ;
Select * FROM  tickets_people_flights 