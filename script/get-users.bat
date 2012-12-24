@echo off

rem Usage: get-users

setlocal

call configure-host.bat

rem set HTTPHEADERS=-LH "Accept: text/html"
set HTTPHEADERS=-LH "Accept: application/json"

curl %HTTPHEADERS% --get "%URLPREFIX%/users"

endlocal
