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

${OO_START_COMMAND}
sleep 3

# start database/databases
# controls if database is alive
cd ./sbidata/database
java -cp ./hsqldb1_8_0_2.jar:./testSpagobiHsqldbAlive.jar it.eng.spagobi.testhsqldb.TestSpagobiHsqldbAlive

RESULT=$?
# if database is not alive starts it
if [ $RESULT = 1 ]; then
	./start.sh &
	sleep 3
fi

while [ $RESULT = 1 ]
do
java -cp ./hsqldb1_8_0_2.jar:./testSpagobiHsqldbAlive.jar it.eng.spagobi.testhsqldb.TestSpagobiHsqldbAlive
RESULT=$?
done

cd ../../${BIN_DIR}
exec ./${START_COMMAND}

exit
