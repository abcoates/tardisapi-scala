@echo off

rem Usage: mobile-logout

setlocal

call configure.bat

set HTTPHEADERS=-LH "Accept: application/json"

del %NEW_COOKIE_FILE%

curl %HTTPHEADERS% --cookie %OLD_COOKIE_FILE% --cookie-jar %NEW_COOKIE_FILE% "%URLPREFIX%/mobile/v1/logout"

findstr "personid" %NEW_COOKIE_FILE%

endlocal
