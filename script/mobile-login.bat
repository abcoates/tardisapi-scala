@echo off

rem Usage: mobile-login [email] [password] [age]
rem [age] is only used when creating a new user at the first login

setlocal

set EMAIL=%1
set PASSWORD=%2
set AGE=%3

call configure.bat

set HTTPHEADERS=-LH "Accept: application/json"

if exist %NEW_COOKIE_FILE% del %NEW_COOKIE_FILE%

curl %HTTPHEADERS% --data "email=%EMAIL%&password=%PASSWORD%&age=%AGE%" --cookie-jar %NEW_COOKIE_FILE% "%URLPREFIX%/mobile/v1/login"

if not exist %NEW_COOKIE_FILE% echo !!!! missing cookie file

copy %NEW_COOKIE_FILE% %OLD_COOKIE_FILE%

endlocal
