@echo off

rem Usage: create-event [user-id] [eventname] [eventtime]

setlocal

set USER_ID=%1
set EVENTNAME=%~2
set EVENTTIME=%~3

call configure.bat

set HTTPHEADERS=-LH "Accept: application/json"

del %NEW_COOKIE_FILE%

curl %HTTPHEADERS% --data "eventname=%EVENTNAME%&eventtime=%EVENTTIME%" --cookie %OLD_COOKIE_FILE% --cookie-jar %NEW_COOKIE_FILE% "%URLPREFIX%/mobile/v1/patients/%USER_ID%/events"

if not exist %NEW_COOKIE_FILE% echo !!!! missing cookie file

copy %NEW_COOKIE_FILE% %OLD_COOKIE_FILE%

endlocal
