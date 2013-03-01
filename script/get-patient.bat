@echo off

rem Usage: get-patient [user-id]

setlocal

set USER_ID=%1

call configure.bat

set HTTPHEADERS=-LH "Accept: application/json"

curl %HTTPHEADERS% --cookie %COOKIE_FILE% --cookie-jar %COOKIE_FILE% "%URLPREFIX%/mobile/v1/patients/%USER_ID%"

endlocal
