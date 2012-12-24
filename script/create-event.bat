@echo off

rem Usage: create-event [userid] [eventname] [eventtime]

setlocal

set USERID=%1
set EVENTNAME=%~2
set EVENTTIME=%~3

call configure-host.bat

curl --data "eventname=%EVENTNAME%&eventtime=%EVENTTIME%" "%URLPREFIX%/users/%USERID%/events"

endlocal
