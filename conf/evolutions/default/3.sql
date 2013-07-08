-- Users/patients/doctors/admins schema

# --- !Ups

ALTER TABLE person
    ALTER COLUMN name DROP NOT NULL; -- Support mobile logins where a name is not supplied.

# --- !Downs

ALTER TABLE person
    ALTER COLUMN name SET NOT NULL;
