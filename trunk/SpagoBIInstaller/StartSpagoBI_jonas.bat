@echo off
if exist "%OPENOFFICE_HOME%" goto openOfficeOk
echo OPENOFFICE_HOME is not defined ,  SpagoBIBooklets component for booklets creation will not work!!
echo Control that OpenOffice 2.0.x is installed and set OPENOFFICE_HOME variable to the installation folder.
pause
goto :runExo
:openOfficeOk
start OOStart.bat
:runExo
@cd .\bin\nt
start jonas start
exit
