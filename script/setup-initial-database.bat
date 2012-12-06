@echo off

call create-user.bat eviemac wookie eviemac@wookie
call create-user.bat tony bony tony@bony

call create-symptom 1 "eviemac symptom 1" today
call create-symptom 1 "eviemac symptom 2" yesterday

call create-symptom 2 "tony symptom 1" today
call create-symptom 2 "tony symptom 2" yesterday

call create-event 1 "eviemac event 1" today
call create-event 1 "eviemac event 2" yesterday

call create-event 2 "tony event 1" today
call create-event 2 "tony event 2" yesterday
