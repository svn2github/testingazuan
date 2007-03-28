if [ -n "$OPENOFFICE_HOME" ]; then
  exec "$OPENOFFICE_HOME"/program/soffice -quickstart -invisible -accept="socket,host=0,port=9005;urp;StarOffice.ServiceManager" &
else
  echo "OPENOFFICE_HOME is not defined ,  SpagoBIBooklets component for booklets creation will not work!!"
  echo "Control that OpenOffice 2.0.x is installed and set OPENOFFICE_HOME variable to the installation folder."
sleep 3
fi
exit
