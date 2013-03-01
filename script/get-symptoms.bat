@echo off

rem Usage: get-symptoms [user-id]

setlocal

set USER_ID=%1

call configure.bat

set HTTPHEADERS=-LH "Accept: application/json"

curl %HTTPHEADERS% --cookie %OLD_COOKIE_FILE% --cookie-jar %NEW_COOKIE_FILE% "%URLPREFIX%/mobile/v1/patients/%USER_ID%/symptoms"

ren %NEW_COOKIE_FILE% %OLD_COOKIE_FILE%

endlocal
