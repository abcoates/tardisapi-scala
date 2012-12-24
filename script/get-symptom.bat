@echo off

rem Usage: get-symptom [user-id] [symptom-id]

setlocal

set USERID=%1
set SYMPTOMID=%2

call configure-host.bat

rem set HTTPHEADERS=-LH "Accept: text/html"
set HTTPHEADERS=-LH "Accept: application/json"

curl %HTTPHEADERS% --get "%URLPREFIX%/users/%USERID%/symptoms/%SYMPTOMID%"

endlocal
