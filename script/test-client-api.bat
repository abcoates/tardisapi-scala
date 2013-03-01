@echo off

rem Usage: test-client-api [email] [password]

setlocal

set EMAIL=%1
set PASSWORD=%2

echo.
echo Logging in ...
call mobile-login.bat %1 %2
echo.

rem !! Need to set up user ID manually.
set USER_ID=4

echo.
echo Get patient details ...
call get-patient.bat %USER_ID%
echo.

echo.
echo Get patient symptoms ...
call get-symptoms.bat %USER_ID%
echo.

rem !! Need to set up symptom IDs manually.
set SYMPTOM_IDS=1

for /f %%s in ( "%SYMPTOM_IDS%" ) do (
echo.
echo Get patient symptom #%%s ...
call get-symptom.bat %USER_ID% %%s
echo.
)

echo.
echo Get patient events ...
call get-events.bat %USER_ID%
echo.

echo.
echo Logging out ...
call mobile-logout.bat
echo.

echo.
echo Done.
echo.

endlocal
