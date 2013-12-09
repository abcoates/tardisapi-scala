-- Users/patients/doctors/admins schema

# --- !Ups

ALTER TABLE patient
    RENAME COLUMN age TO ageatregistration; -- ageatregistration at date of registration

# --- !Downs

ALTER TABLE patient
    RENAME COLUMN ageatregistration TO ageatregistration; -- ageatregistration at date of registration
