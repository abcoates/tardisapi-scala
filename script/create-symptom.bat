@echo off

rem Usage: create-symptom [userid] [whichsymptom] [whensymptom]

setlocal

set USERID=%1
set WHICHSYMPTOM=%~2
set WHENSYMPTOM=%~3

call configure-host.bat

curl --data "whichsymptom=%WHICHSYMPTOM%&whensymptom=%WHENSYMPTOM%" "%URLPREFIX%/users/%USERID%/symptoms"

endlocal
