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
set USER_ID=1

echo.
echo Get patient details ...
call get-patient.bat %USER_ID%
echo.

echo.
echo Get patient symptoms ...
call get-symptoms.bat %USER_ID%
echo.

rem !! Need to set up symptom IDs manually.
for /l %%i in (1,1,2) do (
echo.
echo Get patient symptom #%%i ...
call get-symptom.bat %USER_ID% %%i
echo.
)

echo.
echo Get patient events ...
call get-events.bat %USER_ID%
echo.

rem !! Need to set up event IDs manually.
for /l %%i in (1,1,2) do (
echo.
echo Get patient event #%%i ...
call get-event %USER_ID% %%i
echo.
)

echo.
echo Create patient symptom ...
set PLUS=%%%%2B
rem This was previously "Symptom #4" at "2013-04-04 23:15:00".
rem call create-symptom %USER_ID% "Regurgitation" "2013-05-05 15:28:13 +0000"
rem call create-symptom %USER_ID% "Regurgitation" "2013-04-04 23:15:00"
rem call create-symptom %USER_ID% "Regurgitation" "2013-04-04 15:28:13"
rem call create-symptom %USER_ID% "Regurgitation" "2013-05-05 15:28:13"
rem call create-symptom %USER_ID% "Regurgitation" "2013-05-05 15:28:13 +0000"
call create-symptom %USER_ID% "Regurgitation" "2013-05-05 15:28:13 %PLUS%0000"
echo.

echo.
echo Create patient event ...
call create-event %USER_ID% "Event #4" 2013-02-28
echo.

echo.
echo Logging out ...
call mobile-logout.bat
echo.

echo.
echo Creating new patient by logging in with age ...
call mobile-login.bat mini@mouse minimouse 2.5
echo.

rem !! Need to set up new patient ID manually.
set USER_ID=15

echo.
echo Get patient details ...
call get-patient.bat %USER_ID%
echo.

echo.
echo Logging out ...
call mobile-logout.bat
echo.

echo.
echo Done.
echo.

endlocal
