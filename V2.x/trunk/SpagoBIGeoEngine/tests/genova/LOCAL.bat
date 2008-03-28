@echo off



set GEO_ENGINE_HOME=../..
set CMD_LINE_ARGS= -t "%cd%\templates\local.xml" -d "%cd%" -o "local" -f JPEG

cd %GEO_ENGINE_HOME%/bin
./run.bat %CMD_LINE_ARGS%


