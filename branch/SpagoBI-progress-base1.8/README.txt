SPAGOBI BUILD AND INSTALLATION ON EXO-TOMCAT

PRE-REQUIREMENTS:	Maven 2
			eXo Portal 1.1 Final
			
Launch the Maven 2 command at command line from the folder containing "SpagoBIProject", "SpagoBIDriversAPI" .... folders:

		mvn -Pspagobi-tomcat package	for Tomcat server or
		mvn -Pspagobi-jboss package	for JBoss server or
		mvn -Pspagobi-jonas package	for JOnAS server

A folder "build" will be created; inside it you will find a folder called "<server-name>-server". 
Please read the relevant installation file and follow its instructions.

If you want to compile also the SpagoBIPamphletsModule go the the "SpagoBIPamphletsModule" folder and launch the Maven 2 command:
		mvn compile
This command will produce the directory "SpagoBIPamphletsModule/build" containing example files and also the file "spagobi.zip". Follow the instructions you find in this file.

At last you can run the command "mvn clean": this command will delete all the "target" directories in the sub-projects, but it will not delete the folder "build" in the main directory.
After having installed the application you can remove it manually.
