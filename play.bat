@echo off

setlocal

set PLAYHOME=C:\Apps\play-2.0.4

set DATABASE_URL=jdbc:postgresql://localhost/tardisapi

call %PLAYHOME%\play.bat

endlocal
