@echo off

rem Usage: mobile-login [email] [password]

setlocal

set EMAIL=%1
set PASSWORD=%2

call configure.bat

set HTTPHEADERS=-LH "Accept: application/json"

if exist %NEW_COOKIE_FILE% del %NEW_COOKIE_FILE%

curl %HTTPHEADERS% --data "email=%EMAIL%&password=%PASSWORD%" --cookie-jar %NEW_COOKIE_FILE% "%URLPREFIX%/mobile/v1/login"

if not exist %NEW_COOKIE_FILE% echo !!!! missing cookie file

copy %NEW_COOKIE_FILE% %OLD_COOKIE_FILE%

endlocal
