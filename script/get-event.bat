@echo off

rem Usage: get-event [user-id] [event-id]

setlocal

set USER_ID=%1
set EVENT_ID=%2

call configure.bat

set HTTPHEADERS=-LH "Accept: application/json"

curl %HTTPHEADERS% --cookie %OLD_COOKIE_FILE% --cookie-jar %NEW_COOKIE_FILE% "%URLPREFIX%/mobile/v1/patients/%USER_ID%/events/%EVENT_ID%"

ren %NEW_COOKIE_FILE% %OLD_COOKIE_FILE%

endlocal
