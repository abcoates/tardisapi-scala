@echo off

rem Usage: get-symptom [user-id] [symptom-id]

setlocal

set USER_ID=%1
set SYMPTOM_ID=%2

call configure.bat

set HTTPHEADERS=-LH "Accept: application/json"

del %NEW_COOKIE_FILE%

curl %HTTPHEADERS% --cookie %OLD_COOKIE_FILE% --cookie-jar %NEW_COOKIE_FILE% "%URLPREFIX%/mobile/v1/patients/%USER_ID%/symptoms/%SYMPTOM_ID%"

if not exist %NEW_COOKIE_FILE% echo !!!! missing cookie file

copy %NEW_COOKIE_FILE% %OLD_COOKIE_FILE%

endlocal
