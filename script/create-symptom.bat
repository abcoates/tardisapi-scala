@echo off

rem Usage: create-symptom [user-id] [whichsymptom] [whensymptom]

setlocal

set USER_ID=%1
set WHICHSYMPTOM=%~2
set WHENSYMPTOM=%~3

call configure.bat

set HTTPHEADERS=-LH "Accept: application/json"

del %NEW_COOKIE_FILE%

curl %HTTPHEADERS% --data "whichsymptom=%WHICHSYMPTOM%&whensymptom=%WHENSYMPTOM%" --cookie %OLD_COOKIE_FILE% --cookie-jar %NEW_COOKIE_FILE% "%URLPREFIX%/mobile/v1/patients/%USER_ID%/symptoms"

if not exist %NEW_COOKIE_FILE% echo !!!! missing cookie file

copy %NEW_COOKIE_FILE% %OLD_COOKIE_FILE%

endlocal
