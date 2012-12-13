-- Patients schema

# --- !Ups

CREATE SEQUENCE patient_id_seq; -- for in memory DB or Postgres
CREATE TABLE patient ( -- NOTE: 'user' is not a suitable table name for Postgres
    id integer NOT NULL DEFAULT nextval('patient_id_seq'), -- for in memory DB or Postgres
--    id integer primary key auto_increment, -- for MySQL
    patientname varchar(255),
    password varchar(255),
    email varchar(255)
);

CREATE SEQUENCE symptom_id_seq; -- for in memory DB or Postgres
CREATE TABLE symptom (
    id integer NOT NULL DEFAULT nextval('symptom_id_seq'), -- for in memory DB or Postgres
--    id integer primary key auto_increment, -- for MySQL
    patientid integer NOT NULL,
    whichsymptom varchar(255),
    whensymptom varchar(255)
);

CREATE SEQUENCE event_id_seq; -- for in memory DB or Postgres
CREATE TABLE event (
    id integer NOT NULL DEFAULT nextval('event_id_seq'), -- for in memory DB or Postgres
--    id integer primary key auto_increment, -- for MySQL
    patientid integer NOT NULL,
    eventname varchar(255),
    eventtime varchar(255)
);

# --- !Downs

DROP TABLE event;
DROP SEQUENCE event_id_seq; -- for in memory DB or Postgres

DROP TABLE symptom;
DROP SEQUENCE symptom_id_seq; -- for in memory DB or Postgres

DROP TABLE patient;
DROP SEQUENCE patient_id_seq; -- for in memory DB or Postgres
