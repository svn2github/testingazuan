@echo off

set CMD_LINE_ARGS= -t ".\templates\ammla.xml" -d "." -o "ammla" -f JPEG

set TMP_CLASSPATH=%CLASSPATH%

set CLASSPATH=%CLASSPATH%;.\classes\;
for %%i in (".\lib\*.jar") do call "./bin/cpappend.bat" %%i
for %%i in (".\lib\*.zip") do call "./bin/cpappend.bat" %%i
set GEO_CLASSPATH=%CLASSPATH%
set CLASSPATH=%TMP_CLASSPATH%

java -cp "%GEO_CLASSPATH%" it.eng.spagobi.engines.geo.application.GeoEngineCLI %CMD_LINE_ARGS%

pause


