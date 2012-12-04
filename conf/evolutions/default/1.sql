# Users schema

# --- !Ups

CREATE SEQUENCE user_id_seq;
CREATE TABLE user (
    id integer NOT NULL DEFAULT nextval('user_id_seq'),
    username varchar(255),
    password varchar(255),
    email varchar(255)
);

CREATE SEQUENCE symptom_id_seq;
CREATE TABLE symptom (
    id integer NOT NULL DEFAULT nextval('symptom_id_seq'),
    userid integer NOT NULL,
    whichsymptom varchar(255),
    whensymptom varchar(255)
);

CREATE SEQUENCE event_id_seq;
CREATE TABLE event (
    id integer NOT NULL DEFAULT nextval('event_id_seq'),
    userid integer NOT NULL,
    eventname varchar(255),
    eventtime varchar(255)
);

# --- !Downs

DROP TABLE event;
DROP SEQUENCE event_id_seq;

DROP TABLE symptom;
DROP SEQUENCE symptom_id_seq;

DROP TABLE user;
DROP SEQUENCE user_id_seq;