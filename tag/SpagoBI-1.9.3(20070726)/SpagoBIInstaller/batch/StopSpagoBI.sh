# resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '.*/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done
 
PRGDIR=`dirname "$PRG"`

cd $PRGDIR

# stop exo
cd ./${BIN_DIR}
exec ./${STOP_COMMAND} &
sleep 5
# stop databases
cd ${BACK_DIR}/sbidata/database
java -cp ./hsqldb1_8_0_2.jar org.hsqldb.util.ShutdownServer -url jdbc:hsqldb:hsql://localhost:9002/spagobi -user sa
java -cp ./hsqldb1_8_0_2.jar org.hsqldb.util.ShutdownServer -url jdbc:hsqldb:hsql://localhost:9002/spagobigeo -user sa
java -cp ./hsqldb1_8_0_2.jar org.hsqldb.util.ShutdownServer -url jdbc:hsqldb:hsql://localhost:9002/foodmart -user sa
exit
