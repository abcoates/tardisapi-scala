-- Users/patients/doctors/admins schema

# --- !Ups

CREATE SEQUENCE person_id_seq; -- for in memory DB or Postgres
CREATE TABLE person ( -- NOTE: 'user' is not a suitable table name for Postgres
    id integer NOT NULL DEFAULT nextval('person_id_seq'), -- for in memory DB or Postgres
--    id integer primary key auto_increment, -- for MySQL
    name varchar(255) NOT NULL,
    email varchar(255) NOT NULL,
    password varchar(255) NOT NULL, -- this is a password has, not an actual password
    salt varchar(255) -- optional salt for use with the password hash
);

CREATE SEQUENCE admin_id_seq; -- for in memory DB or Postgres
CREATE TABLE admin (
    id integer NOT NULL DEFAULT nextval('admin_id_seq'), -- for in memory DB or Postgres
--    id integer primary key auto_increment, -- for MySQL
    personid integer NOT NULL
);

CREATE SEQUENCE doctor_id_seq; -- for in memory DB or Postgres
CREATE TABLE doctor (
    id integer NOT NULL DEFAULT nextval('doctor_id_seq'), -- for in memory DB or Postgres
--    id integer primary key auto_increment, -- for MySQL
    personid integer NOT NULL
);

CREATE SEQUENCE patient_id_seq; -- for in memory DB or Postgres
CREATE TABLE patient (
    id integer NOT NULL DEFAULT nextval('patient_id_seq'), -- for in memory DB or Postgres
--    id integer primary key auto_increment, -- for MySQL
    personid integer NOT NULL,
    uniquestudyid bigint NOT NULL,
    consent1 boolean,
    consent2 boolean,
    consent3 boolean,
    consent4 boolean,
    consent5 boolean,
    consenttimestamp timestamp
);

CREATE TABLE doctor_to_patient (
    doctorid integer NOT NULL,
    patientid integer NOT NULL
);

CREATE SEQUENCE symptom_id_seq; -- for in memory DB or Postgres
CREATE TABLE symptom (
    id integer NOT NULL DEFAULT nextval('symptom_id_seq'), -- for in memory DB or Postgres
--    id integer primary key auto_increment, -- for MySQL
    patientid integer NOT NULL,
    whichsymptom varchar(255),
    whensymptom timestamp -- date/time to the nearest quarter hour
);

CREATE SEQUENCE event_id_seq; -- for in memory DB or Postgres
CREATE TABLE event (
    id integer NOT NULL DEFAULT nextval('event_id_seq'), -- for in memory DB or Postgres
--    id integer primary key auto_increment, -- for MySQL
    patientid integer NOT NULL,
    eventname varchar(255),
    eventtime date
);

# --- !Downs

DROP TABLE event;
DROP TABLE symptom;
DROP TABLE doctor_to_patient;
DROP TABLE patient;
DROP TABLE doctor;
DROP TABLE admin;
DROP TABLE person;

DROP SEQUENCE event_id_seq; -- for in memory DB or Postgres
DROP SEQUENCE symptom_id_seq; -- for in memory DB or Postgres
DROP SEQUENCE patient_id_seq; -- for in memory DB or Postgres
DROP SEQUENCE doctor_id_seq; -- for in memory DB or Postgres
DROP SEQUENCE admin_id_seq; -- for in memory DB or Postgres
DROP SEQUENCE person_id_seq; -- for in memory DB or Postgres
