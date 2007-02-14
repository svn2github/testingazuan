@echo off
if "%OS%" == "Windows_NT" setlocal
rem ---------------------------------------------------------------------------
rem Start script for the CATALINA Server
rem
rem $Id: startup.bat,v 1.6 2004/05/27 18:25:11 yoavs Exp $
rem ---------------------------------------------------------------------------

rem #################################################################################
rem #                           EXO CONFIGURATION                                   #
rem #################################################################################
set LOG_OPTS="-Dorg.apache.commons.logging.Log=org.apache.commons.logging.impl.SimpleLog"
set SECURITY_OPTS="-Djava.security.auth.login.config=../conf/jaas.conf"
set JAVA_OPTS= %LOG_OPTS% %SECURITY_OPTS% %JAVA_OPTS% -Djava.awt.headless=true
rem #################################################################################
rem #                          END EXO CONFIGURATION                                #
rem #################################################################################

if not exist "%JAVA_HOME%" goto requireJVM

rem Guess CATALINA_HOME if not defined
set CURRENT_DIR=%cd%
if not "%CATALINA_HOME%" == "" goto gotHome
set CATALINA_HOME=%CURRENT_DIR%
if exist "%CATALINA_HOME%\bin\catalina.bat" goto okHome
cd ..
set CATALINA_HOME=%cd%
cd %CURRENT_DIR%
:gotHome
if exist "%CATALINA_HOME%\bin\catalina.bat" goto okHome
echo The CATALINA_HOME environment variable is not defined correctly
echo This environment variable is needed to run this program
goto end
:okHome

set EXECUTABLE=%CATALINA_HOME%\bin\catalina.bat

rem Check that target executable exists
if exist "%EXECUTABLE%" goto okExec
echo Cannot find %EXECUTABLE%
echo This file is needed to run this program
goto end
:okExec

rem Get remaining unshifted command line arguments and save them in the
set CMD_LINE_ARGS=
:setArgs
if ""%1""=="""" goto doneSetArgs
set CMD_LINE_ARGS=%CMD_LINE_ARGS% %1
shift
goto setArgs
:doneSetArgs

if exist "%OPENOFFICE_HOME%" goto openOfficeOk
echo OPENOFFICE_HOME is not defined ,  SpagoBIBooklets component for booklets creation will not work!!
echo Control that OpenOffice 2.0.x is installed and set OPENOFFICE_HOME variable to the installation folder.
pause
goto :runExo
:openOfficeOk
start OOStart.bat
:runExo
call "%EXECUTABLE%" run %CMD_LINE_ARGS%
goto end
:requireJVM
echo JAVA_HOME is not defined ,  Tomcat can't be launched
pause

:end
