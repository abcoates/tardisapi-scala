@echo off

rem Usage: mobile-logout

setlocal

call configure.bat

set HTTPHEADERS=-LH "Accept: application/json"

curl %HTTPHEADERS% --cookie %COOKIE_FILE% "%URLPREFIX%/mobile/v1/logout"

endlocal
