@echo off
set PATH=%PATH%;%OPENOFFICE_HOME%\program\
soffice -quickstart -accept="socket,host=localhost,port=9005;urp;StarOffice.ServiceManager"
if %ERRORLEVEL% NEQ 0 goto warning
exit
:warning
echo OPENOFFICE_HOME is not defined and the path does not include the OpenOffice soffice command!
echo SpagoBIBooklets component for booklets creation will not work!!
echo Control that OpenOffice 2.0.x is installed and set OPENOFFICE_HOME variable to the installation folder.
pause
exit
