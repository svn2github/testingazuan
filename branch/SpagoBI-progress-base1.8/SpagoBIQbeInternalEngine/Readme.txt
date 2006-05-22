How to install SpagoBIQbeInternalEngine

Copy the content of the folder 'web-content' (only the content, not the folder itself) with the compiled classes into exo-home/webapps/spagobi.
Pay attention to the spago libraries: any older spago-core-xxx.jar and spago-web-xxx.jar files than spago-core-2.0.2.jar and spago-web-2.0.2.jar must be deleted.
Then insert into file exo-home/webapps/spagobi/WEB-INF/conf/master.xml the following lines:
	
	<!-- START QBE -->
	<CONFIGURATOR path="/WEB-INF/conf/components/spagobiqbeIE/qbe.xml" />
	<CONFIGURATOR path="/WEB-INF/conf/components/spagobiqbeIE/actions.xml" />
	<CONFIGURATOR path="/WEB-INF/conf/components/spagobiqbeIE/presentation.xml" />
	<CONFIGURATOR path="/WEB-INF/conf/components/spagobiqbeIE/publishers.xml" />
	<!-- END QBE -->