@echo off

rem Usage: mobile-logout

setlocal

call configure-host.bat

set HTTPHEADERS=-LH "Accept: application/json"

curl %HTTPHEADERS% "%URLPREFIX%/mobile/v1/logout"

endlocal
