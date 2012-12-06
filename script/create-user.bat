@echo off

rem Usage: create-user [username] [password] [email]

setlocal

set USERNAME=%~1
set PASSWORD=%~2
set EMAIL=%~3

set URLPREFIX=http://localhost:9000

curl --data "username=%USERNAME%&password=%PASSWORD%&email=%EMAIL%" "%URLPREFIX%/users"

endlocal
