How to install SpagoBIDashboardInternalEngine

Copy the content of the folder 'web-content' (only the content, not the folder itself) with the compiled classes into exo-home/webapps/spagobi.
Pay attention to the spago libraries: any older spago-core-xxx.jar and spago-web-xxx.jar files than spago-core-2.0.2.jar and spago-web-2.0.2.jar must be deleted.
Then insert into file exo-home/webapps/spagobi/WEB-INF/conf/master.xml the following lines:

	<!-- START DASHBOARD -->
	<CONFIGURATOR path="/WEB-INF/conf/components/spagobidashboardIE/publishers.xml" />
	<!-- END DASHBOARD -->