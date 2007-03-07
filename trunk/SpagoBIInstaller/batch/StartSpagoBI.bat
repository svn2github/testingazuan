@echo off
if exist "%OPENOFFICE_HOME%" goto openOfficeOk
echo OPENOFFICE_HOME is not defined ,  SpagoBIBooklets component for booklets creation will not work!!
echo Control that OpenOffice 2.0.x is installed and set OPENOFFICE_HOME variable to the installation folder.
pause
goto :runExo
:openOfficeOk
start OOStart.bat

rem Run database/databases

@cd .\sbidata\database
java -cp hsqldb1_8_0_2.jar;testSpagobiHsqldbAlive.jar it.eng.spagobi.testhsqldb.TestSpagobiHsqldbAlive
if %ERRORLEVEL%==0 goto runExo
if %ERRORLEVEL%==1 start start.bat
goto wait

:loop
java -cp hsqldb1_8_0_2.jar;testSpagobiHsqldbAlive.jar it.eng.spagobi.testhsqldb.TestSpagobiHsqldbAlive
if %ERRORLEVEL%==1 goto wait

:runExo
@cd ..\..\${BIN_DIR}
start ${START_COMMAND}
@ping 127.0.0.1 -n 5 -w 10000 > nul
cd C:\WINDOWS
start explorer.exe "${URL}"
exit

:wait
@ping 127.0.0.1 -n 3 -w 1000 > nul
goto loop
