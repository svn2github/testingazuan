package it.eng.spagobi.installer;

import java.io.File;

public class InstallSpagoBIPlatform {

	private static String _pathdest;
	private static String _spagobi_plaftorm_source_dir;
	private static String _spagobi_examples_source_dir;
	private static String _server_name;
	private static String _spagobi_deploy_dir;
	private static String _engines_deploy_dir;
	private static String _common_lib_dir;
	private static String _path_hsql_lib_dir;
	private static String _spagobi_metadata_db_dir;
	private static String _exo_metadata_db_dir;
	private static boolean _install_birt;
	private static boolean _install_geo;
	private static boolean _install_jasper;
	private static boolean _install_jpivot;
	private static boolean _install_qbe;
	private static boolean _install_weka;
	private static boolean _install_examples;
	private static final char fs = File.separatorChar;
	// war extension
	private static String _ext;
	
	public static void main(String[] args) {
		
		
		// initializes variables
		_pathdest 						= args[0];
		_spagobi_plaftorm_source_dir 	= args[0] + fs + "spagobi" + fs + "spagobi_platform";
		_spagobi_examples_source_dir 	= args[0] + fs + "spagobi" + fs + "spagobi_examples";
		_server_name 					= args[1];
		_install_birt 					= args[2].equalsIgnoreCase("yes");
		_install_geo 					= args[3].equalsIgnoreCase("yes");
		_install_jasper					= args[4].equalsIgnoreCase("yes");
		_install_jpivot 				= args[5].equalsIgnoreCase("yes");
		_install_qbe 					= args[6].equalsIgnoreCase("yes");
		_install_weka 					= args[7].equalsIgnoreCase("yes");
		_install_examples 				= args[8].equalsIgnoreCase("yes");
		
		_spagobi_metadata_db_dir = _pathdest + fs + "sbidata" + fs + "database";
		_exo_metadata_db_dir = _pathdest + fs + "temp" + fs + "data";
		
		if ("tomcat".equalsIgnoreCase(_server_name)) {
			_spagobi_deploy_dir = _pathdest + fs + "webapps";
			_engines_deploy_dir = _spagobi_deploy_dir;
			_common_lib_dir = _pathdest + fs + "common" + fs + "lib";
			_path_hsql_lib_dir = _common_lib_dir;
		} else if ("jboss".equalsIgnoreCase(_server_name)) {
			_spagobi_deploy_dir = _pathdest + fs + "server" + fs + "default" + fs + "deploy" + fs + "exoplatform.sar";
			_engines_deploy_dir = _pathdest + fs + "server" + fs + "default" + fs + "deploy";
			_common_lib_dir = _spagobi_deploy_dir;
			_path_hsql_lib_dir = _pathdest + fs + "server" + fs + "default" + fs + "lib";
		} else if ("jonas".equalsIgnoreCase(_server_name)) {
			_spagobi_deploy_dir = _pathdest + fs + "apps" + fs + "autoload" + fs + "exoplatform.ear";
			_engines_deploy_dir = _pathdest + fs + "webapps" + fs + "autoload";
			_common_lib_dir = _pathdest + fs + "lib" + fs + "apps";
			_path_hsql_lib_dir = _common_lib_dir;
		}
		
		_ext = ".war";
		if ("tomcat".equalsIgnoreCase(_server_name)) _ext = "";
		
		// install spagobi platform
		if (!installCommonLibs()) return;
		if (!installPatchHsqldb()) return;
		if (!installSpagoBIWar()) return;
		if (!installEngines()) return;
		if (!installSpagoBIMetadataDb()) return;
		if (!installJndiConfiguration()) return;
		if ("jboss".equalsIgnoreCase(_server_name)) {
			installJBossApplicationXml();
		} else if ("jonas".equalsIgnoreCase(_server_name)) {
			installJOnASApplicationXml();
		}
		if (!installStartBatFile()) return;
		
		// install examples if required
		if (_install_examples) {
			if (!installSpagoBIExamplesDwh()) return;
			if (!installSpagoBICms()) return;
			if (!overwriteExistingFiles()) return;
			if (!installSbiportalDb()) return;
		}
		
		// delete source directory
		if (!FileUtilities.deleteDirectory(_pathdest + fs + "spagobi")) return;
	}
	
	private static boolean installCommonLibs() {
		try {
			String pathsource = _spagobi_plaftorm_source_dir + fs + "commonlib";
			FileUtilities.copy(_common_lib_dir, pathsource + fs + "concurrent-1.3.4.jar");
			FileUtilities.copy(_common_lib_dir, pathsource + fs + "ehcache-1.1.jar");
			FileUtilities.copy(_common_lib_dir, pathsource + fs + "jackrabbit-core-1.0.1.jar");
			FileUtilities.copy(_common_lib_dir, pathsource + fs + "jcr-1.0.jar");
			FileUtilities.copy(_common_lib_dir, pathsource + fs + "slf4j-log4j12.jar");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	private static boolean installPatchHsqldb() {
		try {
			String pathsource = _spagobi_plaftorm_source_dir + fs + "hsqlPatch";
			// for Tomcat and JOnAS
			File hsqloldjar = new File(_path_hsql_lib_dir + fs + "hsqldb-1.8.0.1.jar");
			if (hsqloldjar.exists()) hsqloldjar.delete();
			// for JBoss
			hsqloldjar = new File(_path_hsql_lib_dir + fs + "hsqldb.jar");
			if (hsqloldjar.exists()) hsqloldjar.delete();
			FileUtilities.copy(_path_hsql_lib_dir, pathsource	+ fs + "hsqldb1_8_0_2.jar");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}

	private static boolean installJndiConfiguration() {
		try {
			if ("tomcat".equalsIgnoreCase(_server_name)) {
				installTomcatServerXml();
				installContextFiles();
			} else if ("jboss".equalsIgnoreCase(_server_name)) {
				installJBossDSFile();
				installJBossXmdescPatch();
			} else if ("jonas".equalsIgnoreCase(_server_name)) {
				installJOnASJndiConfiguration();
			}
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	private static boolean installJBossXmdescPatch() {
		try {
			String xmdescpath = _pathdest + fs + "server" + fs + "default" + fs + "conf" + fs + "xmdesc";
			File old = new File(xmdescpath + fs + "org.jboss.deployment.MainDeployer-xmbean.xml");
			if (old.exists()) old.delete();
			FileUtilities.copy(xmdescpath, _spagobi_plaftorm_source_dir + fs + "jboss" + fs + "xmdescPatch" 
					+ fs + "org.jboss.deployment.MainDeployer-xmbean.xml");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	private static boolean installSpagoBIWar() {
		try {
			String pathsource = _spagobi_plaftorm_source_dir + fs + "wars"; 
			FileUtilities.explode(_spagobi_deploy_dir + fs + "spagobi" + _ext,
					pathsource + fs + "spagobi.war");
			if ("jboss".equalsIgnoreCase(_server_name)) { 
				FileUtilities.deleteFile("commons-logging-1.0.jar", _spagobi_deploy_dir + fs 
						+ "spagobi.war" + fs + "WEB-INF" + fs + "lib");
				// in case of jboss the actual common lib is server/default/lib, the same of hsqldb library 
				FileUtilities.deleteFile("commons-collections.jar", _path_hsql_lib_dir);
			}
			if ("jonas".equalsIgnoreCase(_server_name)) {
				FileUtilities.deleteFile("commons-logging-1.0.jar", _spagobi_deploy_dir + fs + "spagobi.war" 
						+ fs + "WEB-INF" + fs + "lib");
				File old = new File(_spagobi_deploy_dir + fs + "spagobi.war" + fs + "WEB-INF" + fs 
						+ "classes" + fs + "hibernate.cfg.hsql.xml");
				if (old.exists()) old.delete();
				File newFile = new File(_spagobi_deploy_dir + fs + "spagobi.war" + fs + "WEB-INF" + fs + 
						"classes" + fs + "hibernate_jonas.cfg.hsql.xml");
				newFile.renameTo(old);
			}
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	private static boolean installJBossApplicationXml() {
		try {
			FileUtilities.copy(_spagobi_deploy_dir + fs + "META-INF", 
					_spagobi_plaftorm_source_dir + fs + "jboss" + fs + "application.xml" + fs + "application.xml");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	private static boolean installJOnASApplicationXml() {
		try {
			FileUtilities.copy(_spagobi_deploy_dir + fs + "META-INF", 
					_spagobi_plaftorm_source_dir + fs + "jonas" + fs + "application.xml" + fs + "application.xml");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	private static boolean installEngines() {
		try {
			String pathsource = _spagobi_plaftorm_source_dir + fs + "wars";
			if (_install_birt) {
				FileUtilities.explode(_engines_deploy_dir + fs + "SpagoBIBirtReportEngine" + _ext,
					pathsource + fs + "SpagoBIBirtReportEngine.war");
				if ("jboss".equalsIgnoreCase(_server_name)) {
					FileUtilities.deleteFile("log4j-1.2.8.jar", _engines_deploy_dir + fs + 
							"SpagoBIBirtReportEngine.war" + fs + "WEB-INF" + fs + "lib");
				}
			}
			if (_install_geo) {
				FileUtilities.explode(_engines_deploy_dir + fs + "SpagoBIGeoEngine" + _ext,
					pathsource + fs + "SpagoBIGeoEngine.war");
				if ("jboss".equalsIgnoreCase(_server_name)) {
					FileUtilities.deleteFile("log4j-1.2.8.jar", _engines_deploy_dir + fs + 
							"SpagoBIGeoEngine.war" + fs + "WEB-INF" + fs + "lib");
					FileUtilities.deleteFile("xalan-2.4.0.jar", _engines_deploy_dir + fs + 
							"SpagoBIGeoEngine.war" + fs + "WEB-INF" + fs + "lib");
					FileUtilities.deleteFile("xercesImpl.jar", _engines_deploy_dir + fs + 
							"SpagoBIGeoEngine.war" + fs + "WEB-INF" + fs + "lib");
				} else if ("jonas".equalsIgnoreCase(_server_name)) {
					FileUtilities.deleteFile("xalan-2.4.0.jar", _engines_deploy_dir + fs + 
							"SpagoBIGeoEngine.war" + fs + "WEB-INF" + fs + "lib");
					FileUtilities.deleteFile("xercesImpl.jar", _engines_deploy_dir + fs + 
							"SpagoBIGeoEngine.war" + fs + "WEB-INF" + fs + "lib");
				}
			}
			if (_install_jasper) {
				FileUtilities.explode(_engines_deploy_dir + fs + "SpagoBIJasperReportEngine" + _ext,
					pathsource + fs + "SpagoBIJasperReportEngine.war");
				if ("jboss".equalsIgnoreCase(_server_name)) {
					FileUtilities.deleteFile("log4j-1.2.8.jar", _engines_deploy_dir + fs + 
							"SpagoBIJasperReportEngine.war" + fs + "WEB-INF" + fs + "lib");
				}
			}
			if (_install_jpivot) {
				FileUtilities.explode(_engines_deploy_dir + fs + "SpagoBIJPivotEngine" + _ext,
					pathsource + fs + "SpagoBIJPivotEngine.war");
				if ("jboss".equalsIgnoreCase(_server_name)) {
					FileUtilities.deleteFile("log4j-1.2.8.jar", _engines_deploy_dir + fs + 
							"SpagoBIJPivotEngine.war" + fs + "WEB-INF" + fs + "lib");
				}
			}
			if (_install_qbe) {
				FileUtilities.explode(_engines_deploy_dir + fs + "SpagoBIQbeEngine" + _ext,
					pathsource + fs + "SpagoBIQbeEngine.war");
				if ("jonas".equalsIgnoreCase(_server_name)) {
					File old = new File(_engines_deploy_dir + fs + "SpagoBIQbeEngine.war" + fs + 
							"WEB-INF" + fs + "conf" + fs + "data_access");
					if (old.exists()) old.delete();
					File newFile = new File(_engines_deploy_dir + fs + "SpagoBIQbeEngine.war" 
							+ fs + "WEB-INF" + fs + "conf" + fs + "data_access_jonas");
					newFile.renameTo(old);
				}
			}
			if (_install_weka) {
				FileUtilities.explode(_engines_deploy_dir + fs + "SpagoBIWekaEngine" + _ext,
					pathsource + fs + "SpagoBIWekaEngine.war");
				if ("jboss".equalsIgnoreCase(_server_name)) {
					FileUtilities.deleteFile("log4j-1.2.8.jar", _engines_deploy_dir + fs + 
							"SpagoBIWekaEngine.war" + fs + "WEB-INF" + fs + "lib");
				}
			}
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	private static boolean installSpagoBIMetadataDb() {
		try {
			String pathsource = _spagobi_plaftorm_source_dir + fs + "spagobiMetadataDb";
			FileUtilities.copy(_spagobi_metadata_db_dir, pathsource	+ fs + "spagobi.properties");
			FileUtilities.copy(_spagobi_metadata_db_dir, pathsource	+ fs + "spagobi.script");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	private static boolean installTomcatServerXml() {
		try {
			File servconf = new File(_pathdest + fs + "conf" + fs + "server.xml");
			servconf.delete();
			FileUtilities.copy(_pathdest + fs + "conf", 
					_spagobi_plaftorm_source_dir + fs + "tomcat" + fs + "jndi" + fs + "server.xml");
		} catch (Exception exc) {
			System.out.println(exc);
			return false;
		}
		return true;
	}
	
	private static boolean installJBossDSFile() {
		try {
			FileUtilities.copy(_pathdest + fs + "server" + fs + "default" + fs + "deploy", 
					_spagobi_plaftorm_source_dir + fs + "jboss" + fs + "jndi" + fs + "spagobi-ds.xml");
		} catch (Exception exc) {
			System.out.println(exc);
			return false;
		}
		return true;
	}
	
	private static boolean installJOnASJndiConfiguration() {
		try {
			FileUtilities.copy(_pathdest + fs + "conf", 
					_spagobi_plaftorm_source_dir + fs + "jonas" + fs + "jndi" + fs + "spagobi.properties");
			FileUtilities.copy(_pathdest + fs + "conf", 
					_spagobi_plaftorm_source_dir + fs + "jonas" + fs + "jndi" + fs + "jonas.properties");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	private static boolean installContextFiles() {
		try {
			String pathdest = _pathdest + fs + "conf" + fs + "Catalina" + fs + "localhost";
			String pathsource = _spagobi_plaftorm_source_dir + fs + "tomcat" + fs + "jndi";
			FileUtilities.copy(pathdest, pathsource + fs + "spagobi.xml");
			if (_install_birt) FileUtilities.copy(pathdest, pathsource + fs + "SpagoBIBirtReportEngine.xml");
			if (_install_geo) FileUtilities.copy(pathdest, pathsource + fs + "SpagoBIGeoEngine.xml");
			if (_install_jasper) FileUtilities.copy(pathdest, pathsource + fs + "SpagoBIJasperReportEngine.xml");
			if (_install_jpivot) FileUtilities.copy(pathdest, pathsource + fs + "SpagoBIJPivotEngine.xml");
			if (_install_qbe) FileUtilities.copy(pathdest, pathsource + fs + "SpagoBIQbeEngine.xml");
			if (_install_weka) FileUtilities.copy(pathdest, pathsource + fs + "SpagoBIWekaEngine.xml");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	private static boolean installStartBatFile() {
		try {
			// arrange StartSpagoBI.bat files for different servers
			File tomcatStartBat = new File(_pathdest + fs + "StartSpagoBI.bat");
			File jbossStartBat = new File(_pathdest + fs + "StartSpagoBI_jboss.bat");
			File jonasStartBat = new File(_pathdest + fs + "StartSpagoBI_jonas.bat");
			if ("tomcat".equalsIgnoreCase(_server_name)) {
				jbossStartBat.delete();
				jonasStartBat.delete();
			} else if ("jboss".equalsIgnoreCase(_server_name)) {
				tomcatStartBat.delete();
				jonasStartBat.delete();
				jbossStartBat.renameTo(tomcatStartBat);
			} else if ("jonas".equalsIgnoreCase(_server_name)) {
				tomcatStartBat.delete();
				jbossStartBat.delete();
				jonasStartBat.renameTo(tomcatStartBat);
			}
		} catch (Exception exc) {
			return false;
		}
		return true;
	}

	private static boolean installSpagoBIExamplesDwh() {
		try {
			String dwhsource = _spagobi_examples_source_dir + fs + "dwh";
			String dwhdest = _pathdest + fs + "sbidata" + fs + "database";
			FileUtilities.copy(dwhdest, dwhsource + fs + "foodmart.script");
			FileUtilities.copy(dwhdest, dwhsource + fs + "foodmart.properties");
			FileUtilities.copy(dwhdest, dwhsource + fs + "spagobigeo.script");
			FileUtilities.copy(dwhdest, dwhsource + fs + "spagobigeo.properties");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	private static boolean installSpagoBICms() {
		try {
			String spagobi_cms_dir = _spagobi_deploy_dir + fs + "spagobi" + _ext + fs + "jcrRepositoryFS";
			FileUtilities.deleteDirectory(spagobi_cms_dir);
			FileUtilities.explode(_spagobi_deploy_dir + fs + "spagobi" + _ext, 
					_spagobi_examples_source_dir + fs + "cms" + fs + "jcrRepositoryFS.zip");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	private static boolean overwriteExistingFiles() {
		try {
			FileUtilities.copyDirectory(_pathdest, _spagobi_examples_source_dir + fs + _server_name, false);
			if ("tomcat".equalsIgnoreCase(_server_name)) {
				// if engines are not installed, deletes their context files
				if (!_install_birt) FileUtilities.deleteFile("SpagoBIBirtReportEngine.xml", 
						_pathdest + fs + "conf" + fs + "Catalina" + fs + "localhost");
				if (!_install_geo) FileUtilities.deleteFile("SpagoBIGeoEngine.xml", 
						_pathdest + fs + "conf" + fs + "Catalina" + fs + "localhost");
				if (!_install_jasper) FileUtilities.deleteFile("SpagoBIJasperReportEngine.xml", 
						_pathdest + fs + "conf" + fs + "Catalina" + fs + "localhost");
				if (!_install_jpivot) FileUtilities.deleteFile("SpagoBIJPivotEngine.xml", 
						_pathdest + fs + "conf" + fs + "Catalina" + fs + "localhost");
				if (!_install_qbe) FileUtilities.deleteFile("SpagoBIQbeEngine.xml", 
						_pathdest + fs + "conf" + fs + "Catalina" + fs + "localhost");
				if (!_install_weka) FileUtilities.deleteFile("SpagoBIWekaEngine.xml", 
						_pathdest + fs + "conf" + fs + "Catalina" + fs + "localhost");
			}
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	private static boolean installSbiportalDb() {
		try {
			FileUtilities.copyDirectory(_exo_metadata_db_dir, 
					_spagobi_examples_source_dir + fs + "sbiportalDB", true);
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
}
