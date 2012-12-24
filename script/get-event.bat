@echo off

rem Usage: get-event [user-id] [event-id]

setlocal

set USERID=%1
set EVENTID=%2

rem set URLPREFIX=http://localhost:9000
set URLPREFIX=http://rocky-forest-2992.herokuapp.com

rem set HTTPHEADERS=-LH "Accept: text/html"
set HTTPHEADERS=-LH "Accept: application/json"

curl %HTTPHEADERS% --get "%URLPREFIX%/users/%USERID%/events/%EVENTID%"

endlocal
