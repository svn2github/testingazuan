if [ -n "$OPENOFFICE_HOME" ]; then
  ./OOStart.sh &
else
  echo "OPENOFFICE_HOME is not defined ,  SpagoBIBooklets component for booklets creation will not work!!"
  echo "Control that OpenOffice 2.0.x is installed and set OPENOFFICE_HOME variable to the installation folder."
sleep 3
fi

BASEDIR=./bin
exec "./bin/unix/jonas start" "$@" start

exit 1
