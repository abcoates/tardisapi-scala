@echo off

rem Usage: create-symptom [userid] [whichsymptom] [whensymptom]

setlocal

set USERID=%1
set WHICHSYMPTOM=%~2
set WHENSYMPTOM=%~3

set URLPREFIX=http://localhost:9000

curl --data "whichsymptom=%WHICHSYMPTOM%&whensymptom=%WHENSYMPTOM%" "%URLPREFIX%/users/%USERID%/symptoms"

endlocal
