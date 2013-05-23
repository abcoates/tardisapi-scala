-- Users/patients/doctors/admins schema

# --- !Ups

ALTER TABLE symptom
    ALTER COLUMN whensymptom TYPE timestamp with time zone -- date/time to the nearest quarter hour

# --- !Downs

ALTER TABLE symptom
    ALTER COLUMN whensymptom TYPE timestamp -- date/time to the nearest quarter hour
