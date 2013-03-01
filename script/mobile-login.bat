@echo on

rem Usage: mobile-login [email] [password]

setlocal

set EMAIL=%1
set PASSWORD=%2

call configure.bat

set HTTPHEADERS=-LH "Accept: application/json"

curl %HTTPHEADERS% --data "email=%EMAIL%&password=%PASSWORD%" --dump-header header.txt --cookie-jar %NEW_COOKIE_FILE% "%URLPREFIX%/mobile/v1/login"

dir %NEW_COOKIE_FILE%

ren %NEW_COOKIE_FILE% %OLD_COOKIE_FILE%

dir %OLD_COOKIE_FILE%

endlocal
