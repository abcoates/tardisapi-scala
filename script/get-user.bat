@echo off

rem Usage: get-user [user-id]

setlocal

set USERID=%1

set URLPREFIX=http://localhost:9000

rem set HTTPHEADERS=-LH "Accept: text/html"
set HTTPHEADERS=-LH "Accept: application/json"

curl %HTTPHEADERS% --get "%URLPREFIX%/users/%USERID%"

endlocal
