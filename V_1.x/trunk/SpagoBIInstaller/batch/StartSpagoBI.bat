@echo off
${OO_START_COMMAND}

rem Run database/databases
@cd .\sbidata\database
java -cp hsqldb1_8_0_2.jar;testSpagobiHsqldbAlive.jar it.eng.spagobi.testhsqldb.TestSpagobiHsqldbAlive
if %ERRORLEVEL%==0 goto runExo
start start.bat
goto wait

:loop
java -cp hsqldb1_8_0_2.jar;testSpagobiHsqldbAlive.jar it.eng.spagobi.testhsqldb.TestSpagobiHsqldbAlive
if %ERRORLEVEL%==1 goto wait

:runExo
@cd ..\..\${BIN_DIR}
start ${START_COMMAND}
exit

:wait
@ping 127.0.0.1 -n 3 -w 1000 > nul
goto loop
