-- Users/patients/doctors/admins schema

# --- !Ups

ALTER TABLE person
    ADD COLUMN dateofregistration date; -- date of registration
ALTER TABLE patient
    ADD COLUMN age double precision; -- age

# --- !Downs

ALTER TABLE person
    DROP COLUMN dateofregistration; -- date of registration
ALTER TABLE patient
    DROP COLUMN age; -- age
