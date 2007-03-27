export PATH=$PATH:$OPENOFFICE_HOME"/program
exec soffice -quickstart -invisible -accept="socket,host=0,port=9005;urp;StarOffice.ServiceManager"
RESULT=$?
if [ $RESULT != 0 ]; then 
	echo "OPENOFFICE_HOME is not defined and the path does not include the OpenOffice soffice command!"
	echo "SpagoBIBooklets component for booklets creation will not work!!"
	echo "Control that OpenOffice 2.0.x is installed and set OPENOFFICE_HOME variable to the installation folder."
	sleep 3
fi
exit
