SPAGOBI BUILD AND INSTALLATION ON EXO-PORTAL

PRE-REQUIREMENTS:	Maven 2
			eXo Portal Tomcat 1.1.4 or eXo Portal JBoss 1.1.2 or eXo Portal Jonas 1.1.3 
      or Tomcat 5.5 - Liferay 4.2.2
      or Tomcat 6.0.x - eXo Enterprise WebOS
			
Launch the Maven 2 command at command line from the folder containing "SpagoBIProject", "SpagoBIDriversAPI" .... folders:

		'mvn -Pspagobi-tomcat package'        for Tomcat server or
		'mvn -Pspagobi-jboss package'         for JBoss server or
		'mvn -Pspagobi-jonas package'         for JOnAS server or
    'mvn -Pspagobi-liferay package'       for Tomcat 5.5 - Liferay 4.2.2 server or	
    'mvn -Pspagobi-webos package'         for Tomcat 6.0.x - eXo Enterprise WebOS server

A folder "build" will be created; inside it you will find a folder called "<server-name>-server". 
Please read the relevant installation file and follow its instructions.

If you want to compile also the SpagoBIBookletsModule go the the "SpagoBIBookletsModule" folder and launch the Maven 2 command:
		mvn compile
This command will produce the directory "SpagoBIBookletsModule/build" containing example files and also the file "spagobi.zip". Follow the instructions you find in this file.

At last you can run the command "mvn clean": this command will delete all the "target" directories in the sub-projects, but it will not delete the folder "build" in the main directory.
After having installed the application you can remove it manually.
