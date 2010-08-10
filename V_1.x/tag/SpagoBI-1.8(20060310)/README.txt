SPAGOBI BUILD AND INSTALLATION ON EXO-ECM

PRE-REQUIREMENTS:	Maven 2
			eXo ECM 1.0

According to the application server on which you are going to install SpagoBI and to the CMS system you prefer you can choose one of the following Maven 2 commands:

Application server	|	CMS system	|	command to run
----------------------------------------------------------------------------------------------------
			|			|	
eXo-Tomcat		|	eXo-CMS		|	mvn -Ptomcat-server,exo-cms package
			|			|	
eXo-Tomcat		|	JackRabbit	|	mvn -Ptomcat-server,jackrabbit package
			|			|	
eXo-JBoss		|	eXo-CMS		|	mvn -Pjboss-server,exo-cms package
			|			|	
eXo-JBoss		|	JackRabbit	|	mvn -Pjboss-server,jackrabbit package
			|			|	
eXo-JOnAS		|	eXo-CMS		|	mvn -Pjonas-server,exo-cms package
			|			|	
eXo-JOnAS		|	JackRabbit	|	mvn -Pjonas-server,jackrabbit package
			|			|	

The command must be launched at command line from the folder containing "SpagoBIProject", "SpagoBIDriversAPI" .... folders.

A folder "build" will be created; inside it you will find a folder called "tomcat-server" or "jboss-server" or "jonas-server" according to the application server you chose. 
Please read the relevant installation file and follow its instructions.

Instead, if you want to produce spagobi.war, SpagoBIJasperResportEngine.war and SpagoBIJPivotEngine.war for exo-bi, run the command:

	mvn -Pbi package

You will find the 3 wars into the folder build/bi. Copy them into the "webapps" folder of your exo-tomcat installation.

At last you can run the command "mvn clean": this command will delete all the "target" directories in the sub-projects, but it will not delete the folder "build" in the main directory.
After having installed the application you can remove it manually.
