if [ -n "$OPENOFFICE_HOME" ]; then
  ./OOStart.sh &
else
  echo "OPENOFFICE_HOME is not defined ,  SpagoBIBooklets component for booklets creation will not work!!"
  echo "Control that OpenOffice 2.0.x is installed and set OPENOFFICE_HOME variable to the installation folder."
sleep 3
fi

# start database/databases
# controls if database is alive
cd ./sbidata/database
java -cp hsqldb1_8_0_2.jar:testSpagobiHsqldbAlive.jar it.eng.spagobi.testhsqldb.TestSpagobiHsqldbAlive

RESULT=$?
# if database is not alive starts it
if [ $RESULT = 1 ]; then 
	./start.sh &
	sleep 3
fi

while [ $RESULT = 1 ]
do
	java -cp hsqldb1_8_0_2.jar:testSpagobiHsqldbAlive.jar it.eng.spagobi.testhsqldb.TestSpagobiHsqldbAlive
	RESULT=$?
done

cd ../../bin
exec ./exo-run.sh "$@" start

exit 1
