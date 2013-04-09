@echo off

setlocal

rem set PLAYHOME=C:\Apps\play-2.0.4
set PLAYHOME=C:\Apps\play-2.1.1

set DATABASE_URL=jdbc:postgresql://localhost/tardisapi

call %PLAYHOME%\play.bat %*

endlocal
