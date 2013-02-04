@echo off

rem Usage: mobile-login [email] [password]

setlocal

set EMAIL=%1
set PASSWORD=%2

call configure-host.bat

set HTTPHEADERS=-LH "Accept: application/json"

curl %HTTPHEADERS% --data "email=%EMAIL%&password=%PASSWORD%" "%URLPREFIX%/mobile/v1/login"

endlocal
