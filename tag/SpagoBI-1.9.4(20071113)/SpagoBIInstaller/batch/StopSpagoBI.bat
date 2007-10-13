@echo off
rem stop exo
@cd .\${BIN_DIR}
call ${STOP_COMMAND}
@ping 127.0.0.1 -n 5 -w 1000 > nul
rem stop databases
@cd ${BACK_DIR}\sbidata\database
java -cp hsqldb1_8_0_2.jar org.hsqldb.util.ShutdownServer -url jdbc:hsqldb:hsql://localhost:9002/spagobi -user sa
java -cp hsqldb1_8_0_2.jar org.hsqldb.util.ShutdownServer -url jdbc:hsqldb:hsql://localhost:9002/spagobigeo -user sa
java -cp hsqldb1_8_0_2.jar org.hsqldb.util.ShutdownServer -url jdbc:hsqldb:hsql://localhost:9002/foodmart -user sa
exit
