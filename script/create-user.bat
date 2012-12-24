@echo off

rem Usage: create-user [username] [password] [email]

setlocal

set USERNAME=%~1
set PASSWORD=%~2
set EMAIL=%~3

rem set URLPREFIX=http://localhost:9000
set URLPREFIX=http://rocky-forest-2992.herokuapp.com

curl --data "username=%USERNAME%&password=%PASSWORD%&email=%EMAIL%" "%URLPREFIX%/users"

endlocal
