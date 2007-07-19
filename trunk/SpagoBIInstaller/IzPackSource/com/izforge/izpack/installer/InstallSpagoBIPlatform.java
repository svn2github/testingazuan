package com.izforge.izpack.installer;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class InstallSpagoBIPlatform {

	private static String SPAGOBI_ZIP_FILE = "SpagoBI-bin-1.9.3-07062007.zip";
	private static String BIRT_ZIP_FILE = "SpagoBIBirtReportEngine-bin-1.9.3-07062007.zip";
	private static String GEO_ZIP_FILE = "SpagoBIGeoEngine-bin-1.9.3-07062007.zip";
	private static String JASPER_ZIP_FILE = "SpagoBIJasperReportEngine-bin-1.9.3-07062007.zip";
	private static String JPIVOT_ZIP_FILE = "SpagoBIJPivotEngine-bin-1.9.3-07062007.zip";
	private static String QBE_ZIP_FILE = "SpagoBIQbeEngine-bin-1.9.3-07062007.zip";
	private static String WEKA_ZIP_FILE = "SpagoBIWekaEngine-bin-1.9.3-07062007.zip";
	private static String TALEND_ZIP_FILE = "SpagoBITalendEngine-bin-1.9.3-07062007.zip";
	private static String EXOPROFILEATTRMANAGER_ZIP_FILE = "ExoProfileAttributesManagerModule-bin-1.9.3-07062007.zip";
	private static String BOOKLETS_ZIP_FILE = "SpagoBIBookletsComponent-bin-1.9.3_07062007.zip";
	
	private static String _pathdest;
	private static String _spagobi_plaftorm_source_dir;
	private static String _spagobi_examples_source_dir;
	private static String _server_name;
	private static String _spagobi_deploy_dir;
	private static String _engines_deploy_dir;
	// _common_lib_dir is not the actual server common library folder, 
	// it is just the folder where we have to put all external jars (jacrabbit, jcr, ehcache ...)
	private static String _common_lib_dir;
	private static String _drivers_lib_dir;
	private static String _path_hsql_lib_dir;
	private static String _spagobi_metadata_db_dir;
	private static String _exo_metadata_db_dir;
	private static boolean _install_birt;
	private static boolean _install_geo;
	private static boolean _install_jasper;
	private static boolean _install_jpivot;
	private static boolean _install_qbe;
	private static boolean _install_weka;
	private static boolean _install_talend;
	private static boolean _install_exoprofileattrmanager;
	private static boolean _install_booklets;
	private static boolean _install_examples;
	private static boolean _install_docs;
	private static String _connection_url;
	private static String _driver;
	private static String _username;
	private static String _password;
	private static final char fs = File.separatorChar;
	// war extension
	private static String _ext;
	private static String jndiName;
	private static String spagobigeoJndiName;
	
	
	public static void installSpagoBIPlatorm (String pathdest, String server_name, String install_birt,
			 String install_geo, String install_jasper, String install_jpivot, String install_qbe, 
			 String install_weka, String install_talend, String install_exoprofileattrmanager, 
			 String install_booklets, String install_examples, String install_docs, String driver, 
			 String connection_url, String username, String password) {
		
		// initializes variables
		_pathdest 						= pathdest;
		_spagobi_plaftorm_source_dir 	= _pathdest + fs + "spagobi" + fs + "spagobi_platform";
		_spagobi_examples_source_dir 	= _pathdest + fs + "spagobi" + fs + "spagobi_examples";
		_server_name 					= server_name;
		_install_birt 					= install_birt.equalsIgnoreCase("yes");
		_install_geo 					= install_geo.equalsIgnoreCase("yes");
		_install_jasper					= install_jasper.equalsIgnoreCase("yes");
		_install_jpivot 				= install_jpivot.equalsIgnoreCase("yes");
		_install_qbe 					= install_qbe.equalsIgnoreCase("yes");
		_install_weka 					= install_weka.equalsIgnoreCase("yes");
		_install_talend 				= install_talend.equalsIgnoreCase("yes");
		_install_exoprofileattrmanager 	= install_exoprofileattrmanager.equalsIgnoreCase("yes");
		_install_booklets				= install_booklets.equalsIgnoreCase("yes");
		_install_examples 				= install_examples.equalsIgnoreCase("yes");
		_install_docs 					= install_docs.equalsIgnoreCase("yes");
		
		_driver 						= driver;
		_connection_url 				= connection_url;
		_connection_url = _connection_url.replace('\\', '/');
		_username 						= username;
		_password 						= password;
		if (_password == null) password = "";
		
		_spagobi_metadata_db_dir = _pathdest + fs + "sbidata" + fs + "database";
		_exo_metadata_db_dir = _pathdest + fs + "temp" + fs + "data";
		
		if ("tomcat".equalsIgnoreCase(_server_name)) {
			_spagobi_deploy_dir = _pathdest + fs + "webapps";
			_engines_deploy_dir = _spagobi_deploy_dir;
			_common_lib_dir = _pathdest + fs + "common" + fs + "lib";
			_path_hsql_lib_dir = _common_lib_dir;
			_drivers_lib_dir = _common_lib_dir;
		} else if ("jboss".equalsIgnoreCase(_server_name)) {
			_spagobi_deploy_dir = _pathdest + fs + "server" + fs + "default" + fs + "deploy" + fs + "exoplatform.sar";
			_engines_deploy_dir = _pathdest + fs + "server" + fs + "default" + fs + "deploy";
			_common_lib_dir = _spagobi_deploy_dir;
			_path_hsql_lib_dir = _pathdest + fs + "server" + fs + "default" + fs + "lib";
			_drivers_lib_dir = _path_hsql_lib_dir;
		} else if ("jonas".equalsIgnoreCase(_server_name)) {
			_spagobi_deploy_dir = _pathdest + fs + "apps" + fs + "autoload" + fs + "exoplatform.ear";
			_engines_deploy_dir = _pathdest + fs + "webapps" + fs + "autoload";
			_common_lib_dir = _pathdest + fs + "lib" + fs + "apps";
			_path_hsql_lib_dir = _pathdest + fs + "lib" + fs + "commons" + fs + "jonas";
			_drivers_lib_dir = _path_hsql_lib_dir;
		}
		
		_ext = ".war";
		if ("tomcat".equalsIgnoreCase(_server_name)) _ext = "";
		
		// install spagobi platform
		if (!installCommonLibs()) return;
		if (!installDrivers()) return;
		if (!installPatchHsqldb()) return;
		if (!installSpagoBIWar()) return;
		if (!installEngines()) return;
		if (!installSpagoBIMetadataDb()) return;
		// configure jndi resources only for a clean installation;
		// if the user selected installation with examples, all jndi files are copied from spagobi_examples folder
		if (!_install_examples) {
			if (!installJndiConfiguration()) return;
		}
		if ("jboss".equalsIgnoreCase(_server_name)) {
			installJBossApplicationXml();
			installJBossXmdescPatch();
		} else if ("jonas".equalsIgnoreCase(_server_name)) {
			installJOnASApplicationXml();
		}
		if (!installBatchFiles()) return;
		
		if (_install_examples) {
			jndiName = "sbifoodmart";
			spagobigeoJndiName = "spagobigeo";
		} else {
			jndiName = "dwh";
			spagobigeoJndiName = "dwh";
		}
		
		// install examples if required
		if (_install_examples) {
			if (!installSpagoBIExamplesDwh()) return;
			if (!installSpagoBICms()) return;
			if (!overwriteExistingFiles()) return;
			if (!installSbiportalDb()) return;
		}
		
		if (_install_exoprofileattrmanager) {
			// the following method simply copies required file into spagobi; 
			// configuration files are arranged by arrangeSpagoBIConfFiles method
			if (!installExoprofileattrmanager()) return;
		}
		
		if (_install_booklets) {
			// the following method simply copies required file into spagobi; 
			// configuration files are arranged by arrangeSpagoBIConfFiles method
			if (!installBookletsComponent()) return;
		}
		
		if (!arrangeSpagoBIConfFiles()) return;
		if (!arrangeEnginesConfFiles()) return;
		
		if (_install_docs) {
			if (!arrangeDocs(_pathdest + fs + "spagobi-docs")) return;
		}
		
		// if the os is not Windows (i.e. it is Unix) set the file permissions
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("windows") == -1) {
			try {
				Runtime.getRuntime().exec("chmod -R 777 .", null, new File(_pathdest));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (!arrangeLinkFile()) return;
		
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
	
	private static boolean installDrivers() {
		try {
			String pathsource = _spagobi_plaftorm_source_dir + fs + "drivers";
			FileUtilities.copy(_drivers_lib_dir, pathsource + fs + "mysql-connector-java-3.1.13-bin.jar");
			FileUtilities.copy(_drivers_lib_dir, pathsource + fs + "ojdbc14.jar");
			FileUtilities.copy(_drivers_lib_dir, pathsource + fs + "postgresql-8.0-311.jdbc2.jar");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	private static boolean installPatchHsqldb() {
		try {
			String pathsource = _spagobi_plaftorm_source_dir + fs + "hsqlPatch";
			File hsqloldjar = new File(_path_hsql_lib_dir + fs + "hsqldb-1.8.0.1.jar");
			if (hsqloldjar.exists()) hsqloldjar.delete();
			hsqloldjar = new File(_path_hsql_lib_dir + fs + "hsqldb.jar");
			if (hsqloldjar.exists()) hsqloldjar.delete();
			FileUtilities.copy(_path_hsql_lib_dir, pathsource + fs + "hsqldb1_8_0_2.jar");
			if ("jonas".equalsIgnoreCase(_server_name)) {
				hsqloldjar = new File(_common_lib_dir + fs + "hsqldb-1.8.0.1.jar");
				if (hsqloldjar.exists()) hsqloldjar.delete();
				FileUtilities.copy(_common_lib_dir, pathsource + fs + "hsqldb1_8_0_2.jar");
			}
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
				//installJBossXmdescPatch();
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
			FileUtilities.extractArchiveFile(_pathdest + fs + SPAGOBI_ZIP_FILE, pathsource, "war");
			FileUtilities.deleteFile(SPAGOBI_ZIP_FILE, _pathdest);
			FileUtilities.explode(_spagobi_deploy_dir + fs + "spagobi" + _ext,
					pathsource + fs + "spagobi.war");
			if ("jboss".equalsIgnoreCase(_server_name)) { 
				FileUtilities.deleteFile("commons-logging-1.0.jar", _spagobi_deploy_dir + fs 
						+ "spagobi.war" + fs + "WEB-INF" + fs + "lib");
				// in case of jboss the actual common lib is server/default/lib, the same of hsqldb library 
				FileUtilities.deleteFile("commons-collections.jar", _path_hsql_lib_dir);
				FileUtilities.copy(_path_hsql_lib_dir, _spagobi_deploy_dir + fs + "spagobi" + _ext 
						+ fs + "WEB-INF" + fs + "lib" + fs + "commons-collections-3.1.jar");
			}
			if ("jonas".equalsIgnoreCase(_server_name)) {
				FileUtilities.deleteFile("commons-logging-1.0.jar", _spagobi_deploy_dir + fs + "spagobi.war" 
						+ fs + "WEB-INF" + fs + "lib");
				File oldHibCfg = new File(_spagobi_deploy_dir + fs + "spagobi.war" + fs + "WEB-INF" + fs +
						"classes" + fs + "hibernate.cfg.hsql.xml");
				if (oldHibCfg.exists()) oldHibCfg.delete();
				File newHibCfg = new File(_spagobi_deploy_dir + fs + "spagobi.war" + fs + "WEB-INF" + fs + 
						"classes" + fs + "hibernate_jonas.cfg.hsql.xml");
				newHibCfg.renameTo(oldHibCfg);
				File oldJbpmCfg = new File(_spagobi_deploy_dir + fs + "spagobi.war" + fs + "WEB-INF" + fs + 
						"classes" + fs + "jbpm.hibernate.cfg.jndi.xml");
				if (oldJbpmCfg.exists()) oldJbpmCfg.delete();
				File newJbpmCfg = new File(_spagobi_deploy_dir + fs + "spagobi.war" + fs + "WEB-INF" + fs + 
						"classes" + fs + "jbpm.hibernate.cfg.jndi_jonas.xml");
				newJbpmCfg.renameTo(oldJbpmCfg);
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
				FileUtilities.extractArchiveFile(_pathdest + fs + BIRT_ZIP_FILE, pathsource, "war");
				FileUtilities.deleteFile(BIRT_ZIP_FILE, _pathdest);
				FileUtilities.explode(_engines_deploy_dir + fs + "SpagoBIBirtReportEngine" + _ext,
					pathsource + fs + "SpagoBIBirtReportEngine.war");
				if ("jboss".equalsIgnoreCase(_server_name)) {
					FileUtilities.deleteFile("log4j-1.2.8.jar", _engines_deploy_dir + fs + 
							"SpagoBIBirtReportEngine.war" + fs + "WEB-INF" + fs + "lib");
				}
				if ("jonas".equalsIgnoreCase(_server_name)) {
					// TODO zip classes folder into a jar to be put into lib dir
				}
			}
			if (_install_geo) {
				FileUtilities.extractArchiveFile(_pathdest + fs + GEO_ZIP_FILE, pathsource, "war");
				FileUtilities.deleteFile(GEO_ZIP_FILE, _pathdest);
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
//					// TODO this part must be moved on engine configuration
//					File data_access = new File(_engines_deploy_dir + fs + 
//							"SpagoBIGeoEngine.war" + fs + "WEB-INF" + fs + "conf" + fs + "data_access.xml");
//					data_access.delete();
//					File data_access_jonas = new File(_engines_deploy_dir + fs + 
//							"SpagoBIGeoEngine.war" + fs + "WEB-INF" + fs + "conf" + fs + "data_access_jonas.xml");
//					data_access_jonas.renameTo(data_access);
				}
			}
			if (_install_jasper) {
				FileUtilities.extractArchiveFile(_pathdest + fs + JASPER_ZIP_FILE, pathsource, "war");
				FileUtilities.deleteFile(JASPER_ZIP_FILE, _pathdest);
				FileUtilities.explode(_engines_deploy_dir + fs + "SpagoBIJasperReportEngine" + _ext,
					pathsource + fs + "SpagoBIJasperReportEngine.war");
				if ("jboss".equalsIgnoreCase(_server_name)) {
					FileUtilities.deleteFile("log4j-1.2.8.jar", _engines_deploy_dir + fs + 
							"SpagoBIJasperReportEngine.war" + fs + "WEB-INF" + fs + "lib");
				}
			}
			if (_install_jpivot) {
				FileUtilities.extractArchiveFile(_pathdest + fs + JPIVOT_ZIP_FILE, pathsource, "war");
				FileUtilities.deleteFile(JPIVOT_ZIP_FILE, _pathdest);
				FileUtilities.explode(_engines_deploy_dir + fs + "SpagoBIJPivotEngine" + _ext,
					pathsource + fs + "SpagoBIJPivotEngine.war");
				if ("jboss".equalsIgnoreCase(_server_name)) {
					FileUtilities.deleteFile("log4j-1.2.8.jar", _engines_deploy_dir + fs + 
							"SpagoBIJPivotEngine.war" + fs + "WEB-INF" + fs + "lib");
				}
			}
			if (_install_qbe) {
				FileUtilities.extractArchiveFile(_pathdest + fs + QBE_ZIP_FILE, pathsource, "war");
				FileUtilities.deleteFile(QBE_ZIP_FILE, _pathdest);
				FileUtilities.explode(_engines_deploy_dir + fs + "SpagoBIQbeEngine" + _ext,
					pathsource + fs + "SpagoBIQbeEngine.war");
				if ("jonas".equalsIgnoreCase(_server_name)) {
					// TODO this part must be moved on engine configuration
					File data_access = new File(_engines_deploy_dir + fs + "SpagoBIQbeEngine.war" + fs + 
							"WEB-INF" + fs + "conf" + fs + "data_access");
					if (data_access.exists()) data_access.delete();
					File data_access_jonas = new File(_engines_deploy_dir + fs + "SpagoBIQbeEngine.war" 
							+ fs + "WEB-INF" + fs + "conf" + fs + "data_access_jonas");
					data_access_jonas.renameTo(data_access);
				}
			}
			if (_install_weka) {
				FileUtilities.extractArchiveFile(_pathdest + fs + WEKA_ZIP_FILE, pathsource, "war");
				FileUtilities.deleteFile(WEKA_ZIP_FILE, _pathdest);
				FileUtilities.explode(_engines_deploy_dir + fs + "SpagoBIWekaEngine" + _ext,
					pathsource + fs + "SpagoBIWekaEngine.war");
				if ("jboss".equalsIgnoreCase(_server_name)) {
					FileUtilities.deleteFile("log4j-1.2.8.jar", _engines_deploy_dir + fs + 
							"SpagoBIWekaEngine.war" + fs + "WEB-INF" + fs + "lib");
				}
				if (_driver.toLowerCase().indexOf("hsqldb") != -1) {
					// TODO manage database.properties
				} else if (_driver.toLowerCase().indexOf("oracle") != -1) {
					// TODO manage database.properties
				} else if (_driver.toLowerCase().indexOf("postgresql") != -1) {
					// TODO manage database.properties
				} else if (_driver.toLowerCase().indexOf("mysql") != -1) {
					// TODO manage database.properties
				}
			}
			if (_install_talend) {
				FileUtilities.extractArchiveFile(_pathdest + fs + TALEND_ZIP_FILE, pathsource, "war");
				FileUtilities.deleteFile(TALEND_ZIP_FILE, _pathdest);
				FileUtilities.explode(_engines_deploy_dir + fs + "SpagoBITalendEngine" + _ext,
					pathsource + fs + "SpagoBITalendEngine.war");
				if ("jboss".equalsIgnoreCase(_server_name)) {
					FileUtilities.deleteFile("log4j-1.2.8.jar", _engines_deploy_dir + fs + 
							"SpagoBITalendEngine.war" + fs + "WEB-INF" + fs + "lib");
				}
				String spagobiTalendEngineHome = _engines_deploy_dir + fs + "SpagoBITalendEngine" + _ext;
				spagobiTalendEngineHome = spagobiTalendEngineHome.replace('\\', '/');
				String talendPropertiesFile = _engines_deploy_dir + fs + "SpagoBITalendEngine" + _ext + fs + "WEB-INF" + fs + "classes" + fs + "talend.properties";
				Properties props = new Properties();
				props.setProperty("${SPAGOBI_TALEND_ENGINE_HOME}", spagobiTalendEngineHome);
				FileUtilities.replaceParametersInFile(talendPropertiesFile, talendPropertiesFile, props, true);
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
			FileUtilities.copy(_spagobi_metadata_db_dir, pathsource	+ fs + "start.bat");
			FileUtilities.copy(_spagobi_metadata_db_dir, pathsource	+ fs + "start.sh");
			FileUtilities.copy(_spagobi_metadata_db_dir, pathsource	+ fs + "testSpagobiHsqldbAlive.jar");
			FileUtilities.copy(_spagobi_metadata_db_dir, pathsource	+ fs + "server.properties");
			String hsqldb_lib_pathsource = _spagobi_plaftorm_source_dir + fs + "hsqlPatch";
			FileUtilities.copy(_spagobi_metadata_db_dir, hsqldb_lib_pathsource	+ fs + "hsqldb1_8_0_2.jar");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	private static boolean installTomcatServerXml() {
		try {
			File servconf = new File(_pathdest + fs + "conf" + fs + "server.xml");
			servconf.delete();
			replaceDwhConnectionParameters(_spagobi_plaftorm_source_dir + fs + "tomcat" + fs + 
					"jndi" + fs + "server.xml", _pathdest + fs + "conf" + fs + "server.xml");
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
			replaceDwhConnectionParameters(_spagobi_plaftorm_source_dir + fs + "jboss" + fs + "jndi" + fs +  
					"dwh-ds.xml", _pathdest + fs + "server" + fs + "default" + fs + "deploy" + fs + "dwh-ds.xml");
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
			replaceDwhConnectionParameters(_spagobi_plaftorm_source_dir + fs + "jonas" + fs + "jndi" + fs 
					+ "dwh.properties", _pathdest + fs + "conf" + fs + "dwh.properties");
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
	
	private static boolean installBatchFiles() {
		try {
			replaceStartCommandParameters();
			replaceStopCommandParameters();
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
	
	private static boolean replaceDwhConnectionParameters(String sourceFilePath, String destFilePath) {
		try {

			Properties props = new Properties();
			props.setProperty("${DRIVER}", _driver);
			props.setProperty("${CONNECTION_URL}", _connection_url);
			props.setProperty("${USERNAME}", _username);
			props.setProperty("${PASSWORD}", _password);
			if ("jboss".equalsIgnoreCase(_server_name)) {
				String mapper = null;
				if (_driver.toLowerCase().indexOf("hsqldb") != -1) mapper = "Hypersonic SQL";
				else if (_driver.toLowerCase().indexOf("oracle") != -1) mapper = "Oracle9i";
				else if (_driver.toLowerCase().indexOf("postgresql") != -1) mapper = "PostgreSQL";
				else if (_driver.toLowerCase().indexOf("mysql") != -1) mapper = "mySQL";
				props.setProperty("${TYPE_MAPPING}", mapper);
			} else if ("jonas".equalsIgnoreCase(_server_name)) {
				String mapper = null;
				if (_driver.toLowerCase().indexOf("hsqldb") != -1) mapper = "rdb.hsql";
				else if (_driver.toLowerCase().indexOf("oracle") != -1) mapper = "rdb.oracle";
				else if (_driver.toLowerCase().indexOf("postgresql") != -1) mapper = "rdb.postgres";
				else if (_driver.toLowerCase().indexOf("mysql") != -1) mapper = "rdb.mysql";
				props.setProperty("${TYPE_MAPPING}", mapper);
			}
			FileUtilities.replaceParametersInFile(sourceFilePath, destFilePath, props, false);
			
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	private static boolean replaceStartCommandParameters() {
		try {
			// arrange StartSpagoBI.bat files for different servers
			String binDir = "bin";
			String startCommand = null;
			if ("tomcat".equalsIgnoreCase(_server_name)) {
				startCommand = "exo-run.bat";
			} else if ("jboss".equalsIgnoreCase(_server_name)) {
				startCommand = "run.bat";
			} else if ("jonas".equalsIgnoreCase(_server_name)) {
				startCommand = "jonas start";
				binDir = "bin" + fs + "nt";
			}
			String openOfficeStartCommand = "";
			if (_install_booklets) {
				openOfficeStartCommand = "start \"Starting OpenOffice\" OOStart.bat";
			}
			Properties batProps = new Properties();
			batProps.setProperty("${OO_START_COMMAND}", openOfficeStartCommand);
			batProps.setProperty("${BIN_DIR}", binDir);
			batProps.setProperty("${START_COMMAND}", startCommand);
			String startBat = _pathdest + fs + "StartSpagoBI.bat";
			FileUtilities.replaceParametersInFile(startBat, startBat, batProps, true);
			
			// arrange StartSpagoBI.sh files for different servers
			if ("tomcat".equalsIgnoreCase(_server_name)) {
				startCommand = "exo-run.sh \"$@\" start";
			} else if ("jboss".equalsIgnoreCase(_server_name)) {
				startCommand = "run.sh \"$@\" start";
			} else if ("jonas".equalsIgnoreCase(_server_name)) {
				binDir = "bin" + fs + "unix";
				startCommand = "jonas start";
			}
			openOfficeStartCommand = "";
			if (_install_booklets) {
				openOfficeStartCommand = "./OOStart.sh &";
			}
			Properties shProps = new Properties();
			shProps.setProperty("${OO_START_COMMAND}", openOfficeStartCommand);
			shProps.setProperty("${BIN_DIR}", binDir);
			shProps.setProperty("${START_COMMAND}", startCommand);
			String startSh = _pathdest + fs + "StartSpagoBI.sh";
			FileUtilities.replaceParametersInFile(startSh, startSh, shProps, true);
			
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	private static boolean replaceStopCommandParameters() {
		try {
			
			// arrange StopSpagoBI.bat files for different servers
			String binDir = "bin";
			String stopCommand = "shutdown.bat";
			String backDir = "..";
			if ("jboss".equalsIgnoreCase(_server_name)) {
				stopCommand = "shutdown.bat -S";
			}
			if ("jonas".equalsIgnoreCase(_server_name)) {
				binDir = "bin" + fs + "nt";
				backDir = ".." + fs + "..";
				stopCommand = "jonas stop";
			}
			Properties batProps = new Properties();
			batProps.setProperty("${BIN_DIR}", binDir);
			batProps.setProperty("${STOP_COMMAND}", stopCommand);
			batProps.setProperty("${BACK_DIR}", backDir);
			String stopBat = _pathdest + fs + "StopSpagoBI.bat";
			FileUtilities.replaceParametersInFile(stopBat, stopBat, batProps, true);
			
			// arrange StopSpagoBI.sh files for different servers
			stopCommand = "shutdown.sh";
			if ("jboss".equalsIgnoreCase(_server_name)) {
				stopCommand = "shutdown.sh -S";
			}
			if ("jonas".equalsIgnoreCase(_server_name)) {
				binDir = "bin" + fs + "unix";
				backDir = ".." + fs + "..";
				stopCommand = "jonas stop";
			}
			Properties shProps = new Properties();
			shProps.setProperty("${BIN_DIR}", binDir);
			shProps.setProperty("${STOP_COMMAND}", stopCommand);
			shProps.setProperty("${BACK_DIR}", backDir);
			String stopSh = _pathdest + fs + "StopSpagoBI.sh";
			FileUtilities.replaceParametersInFile(stopSh, stopSh, shProps, true);
			
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	private static boolean arrangeDocs(String docsFilePath) {
		File installationManual = new File(docsFilePath + fs + "SpagoBI-Installation-Manual.pdf");
		if (installationManual.exists() && installationManual.isFile()) installationManual.delete();
		File tomcatInstallationManual = new File(docsFilePath + fs + "SpagoBI_eXoTomcat_Installation_Manual_1.4.3.pdf");
		File jbossInstallationManual = new File(docsFilePath + fs + "SpagoBI_JBoss_Installation_Manual-1.4.3.pdf");
		File jonasInstallationManual = new File(docsFilePath + fs + "SpagoBI_JOnAS_Installation_Manual-1.4.3.pdf");
		if ("tomcat".equalsIgnoreCase(_server_name)) {
			if (!tomcatInstallationManual.renameTo(installationManual)) return false;
			if (!jbossInstallationManual.delete()) return false;
			if (!jonasInstallationManual.delete()) return false;
		} else if ("jboss".equalsIgnoreCase(_server_name)) {
			if (!tomcatInstallationManual.delete()) return false;
			if (!jbossInstallationManual.renameTo(installationManual)) return false;
			if (!jonasInstallationManual.delete()) return false;
		} else if ("jonas".equalsIgnoreCase(_server_name)) {
			if (!tomcatInstallationManual.delete()) return false;
			if (!jbossInstallationManual.delete()) return false;
			if (!jonasInstallationManual.renameTo(installationManual)) return false;
		}
		return true;		
	}
	
	private static boolean arrangeLinkFile() {
		Properties props = new Properties();
		String port = "8080";
		String portal = "portal";
		if ("jonas".equalsIgnoreCase(_server_name))	port = "9000";
		if (_install_examples) portal = "sbiportal";
		props.setProperty("${PORT}", port);
		props.setProperty("${PORTAL}", portal);
		String connectToSpagoBIFile = _pathdest + fs + "ConnectToSpagoBI.URL";
		try {
			FileUtilities.replaceParametersInFile(connectToSpagoBIFile, connectToSpagoBIFile, props, false);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private static boolean installExoprofileattrmanager() {
		String pathsource = _spagobi_plaftorm_source_dir + fs + "exo_profile_attr_manager";
		try {
			FileUtilities.extractArchiveFile(_pathdest + fs + EXOPROFILEATTRMANAGER_ZIP_FILE, pathsource, "zip");
			FileUtilities.deleteFile(EXOPROFILEATTRMANAGER_ZIP_FILE, _pathdest);
			FileUtilities.explode(pathsource, pathsource + fs + "spagobi.zip");
			FileUtilities.copyDirectory( _spagobi_deploy_dir + fs + "spagobi" + _ext, pathsource + fs + "spagobi", true);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private static boolean installBookletsComponent() {
		String pathsource = _spagobi_plaftorm_source_dir + fs + "booklets_component";
		try {
			FileUtilities.extractArchiveFile(_pathdest + fs + BOOKLETS_ZIP_FILE, pathsource, "zip");
			FileUtilities.deleteFile(BOOKLETS_ZIP_FILE, _pathdest);
			FileUtilities.explode(pathsource, pathsource + fs + "spagobi.zip");
			FileUtilities.copyDirectory( _spagobi_deploy_dir + fs + "spagobi" + _ext, pathsource + fs + "spagobi", true);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private static boolean arrangeSpagoBIConfFiles() {
		try {
			/* manages master.xml */
			Properties props = new Properties();
			String exoProfileManagerModuleConfFiles = "";
			// deletes old master.xml file
			FileUtilities.deleteFile("master.xml", _spagobi_deploy_dir + fs + "spagobi" + _ext + fs + "WEB-INF" + fs + "conf");
			String masterSourceFile = _spagobi_plaftorm_source_dir + fs + "spagobi-conf-files" + fs + "spagobi" + fs + "master.xml";
			String masterDestFile = _spagobi_deploy_dir + fs + "spagobi" + _ext + fs + "WEB-INF" + fs + "conf" + fs + "master.xml";
			if (_install_exoprofileattrmanager) {
				exoProfileManagerModuleConfFiles =
					"	<!-- start exo profile manager module -->\n" + 
					"	<CONFIGURATOR path=\"/WEB-INF/conf/components/exoprofileattributemanager/pages.xml\" />\n" +
					"	<CONFIGURATOR path=\"/WEB-INF/conf/components/exoprofileattributemanager/modules.xml\" />\n" + 
					"	<CONFIGURATOR path=\"/WEB-INF/conf/components/exoprofileattributemanager/presentation.xml\" />\n" +
					"	<CONFIGURATOR path=\"/WEB-INF/conf/components/exoprofileattributemanager/publishers.xml\" />\n" + 
					"	<!-- end exo profile manager module -->\n";
			}
			String bookletsModuleConfFiles = "";
			if (_install_booklets) {
				bookletsModuleConfFiles =
					"	<!-- start booklets module -->\n" + 
					"	<CONFIGURATOR path=\"/WEB-INF/conf/components/booklets/booklets.xml\" />\n" +
					"	<CONFIGURATOR path=\"/WEB-INF/conf/components/booklets/pages.xml\" />\n" +
					"	<CONFIGURATOR path=\"/WEB-INF/conf/components/booklets/modules.xml\" />\n" +
					"	<CONFIGURATOR path=\"/WEB-INF/conf/components/booklets/presentation.xml\" />\n" +
					"	<CONFIGURATOR path=\"/WEB-INF/conf/components/booklets/publishers.xml\" />\n" +
					"	<CONFIGURATOR path=\"/WEB-INF/conf/components/booklets/initializers.xml\" />\n" +
					"	<CONFIGURATOR path=\"/WEB-INF/conf/components/booklets/actions.xml\" />\n" +
					"	<CONFIGURATOR path=\"/WEB-INF/conf/components/booklets/workflow.xml\" />\n" +
					"	<CONFIGURATOR path=\"/WEB-INF/conf/components/booklets/forms.xml\" />\n" +
					"	<CONFIGURATOR path=\"/WEB-INF/conf/components/booklets/validation.xml\" />\n" +
					"	<!-- end booklets module -->\n";
			}
			props.setProperty("${EXO_PROFILE_ATTRIBUTE_MANAGER_CONF_FILES}", exoProfileManagerModuleConfFiles);
			props.setProperty("${BOOKLETS_COMPONENT_CONF_FILES}", bookletsModuleConfFiles);
			FileUtilities.replaceParametersInFile(masterSourceFile, masterDestFile, props, false);
	
			
			/* manages portlet.xml */
			if (_install_exoprofileattrmanager) {
				FileUtilities.copy(_spagobi_deploy_dir + fs + "spagobi" + _ext + fs + "WEB-INF", 
					_spagobi_plaftorm_source_dir + fs + "spagobi-conf-files" + fs + "spagobi" + fs + "portlet.xml");
			}
			
			/* manages web.xml */
			props = new Properties();
			String bookletServlet = "";
			String bookletMapping = "";
			// deletes old web.xml file
			FileUtilities.deleteFile("web.xml", _spagobi_deploy_dir + fs + "spagobi" + _ext + fs + "WEB-INF");
			String webSourceFile = _spagobi_plaftorm_source_dir + fs + "spagobi-conf-files" + fs + "spagobi" + fs + "web.xml";
			String webDestFile = _spagobi_deploy_dir + fs + "spagobi" + _ext + fs + "WEB-INF" + fs + "web.xml";
			if (_install_booklets) {
				bookletServlet = 
					"	<servlet>\n" +
					"		<servlet-name>BookletServlet</servlet-name>\n" +
					"		<servlet-class>it.eng.spagobi.booklets.BookletsServlet</servlet-class>\n" +
					"	</servlet>\n";
				bookletMapping =
					"	<servlet-mapping>\n" +
					"		<servlet-name>BookletServlet</servlet-name>\n" +
					"		<url-pattern>/BookletService</url-pattern>\n" +
					"	</servlet-mapping>\n";
			}
			props.setProperty("${BOOKLET_SERVLET}", bookletServlet);
			props.setProperty("${BOOKLET_SERVLET_MAPPING}", bookletMapping);
			props.setProperty("${JNDI_NAME}", jndiName);
			FileUtilities.replaceParametersInFile(webSourceFile, webDestFile, props, false);
	
			
			if (_install_booklets) {
				/* manages spagobi_components.xml */
				FileUtilities.copy(_spagobi_deploy_dir + fs + "spagobi" + _ext + fs + "WEB-INF" + fs + "conf" + fs + "spagobi", 
					_spagobi_plaftorm_source_dir + fs + "spagobi-conf-files" + fs + "spagobi" + fs + "spagobi_components.xml");
				/* manages initializers.xml */
				FileUtilities.copy(_spagobi_deploy_dir + fs + "spagobi" + _ext + fs + "WEB-INF" + fs + "conf", 
						_spagobi_plaftorm_source_dir + fs + "spagobi-conf-files" + fs + "spagobi" + fs + "initializers.xml");
				/* manages jbpm.hibernate.cfg.hsql.xml */
				FileUtilities.copy(_spagobi_deploy_dir + fs + "spagobi" + _ext + fs + "WEB-INF" + fs + "classes", 
						_spagobi_plaftorm_source_dir + fs + "spagobi-conf-files" + fs + "spagobi" + fs + "jbpm.hibernate.cfg.hsql.xml");
				/* manages booklets.xml */
				FileUtilities.copy(_spagobi_deploy_dir + fs + "spagobi" + _ext + fs + "WEB-INF" + fs + "conf" + fs + "components" + fs + "booklets", 
						_spagobi_plaftorm_source_dir + fs + "spagobi-conf-files" + fs + "spagobi" + fs + "booklets.xml");
	
			}
			
			arrangeContextXmlJbossWebXmlJonasWebXmlAndWebXmlConfFiles("spagobi", false, jndiName);
			
			/* manages data-access.xml */
			arrangeDataAccessFile("spagobi", "${JNDI_NAME}", jndiName);
			
			/* manages cms.xml */
			// TODO cambiare il cms.xml a cms_jndi.xml, cancellare il cms_jboss_jonas.xml e poi copiare il file
			FileUtilities.copy(_spagobi_deploy_dir + fs + "spagobi" + _ext + fs + "WEB-INF" + fs + "conf", 
					_spagobi_plaftorm_source_dir + fs + "spagobi-conf-files" + fs + "spagobi" + fs + "cms.xml");
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private static boolean arrangeEnginesConfFiles() {
		try {
			/* SpagoBIBirtReportEngine */
			if (_install_birt) {
				arrangeContextXmlJbossWebXmlJonasWebXmlAndWebXmlConfFiles("SpagoBIBirtReportEngine", true, jndiName);
				arrangeEngineConfigFile("SpagoBIBirtReportEngine");
			}
			
			/* SpagoBIGeoEngine */
			if (_install_geo) {
				arrangeContextXmlJbossWebXmlJonasWebXmlAndWebXmlConfFiles("SpagoBIGeoEngine", true, spagobigeoJndiName);
				String jndiRef = null;
				if ("jonas".equalsIgnoreCase(_server_name)) {
					jndiRef = "";
				} else {
					jndiRef = "java:comp/env/";
				}
				if (_install_examples) {
					jndiRef += "jdbc/spagobigeo";
				} else {
					jndiRef += "jdbc/dwh";
				}
				arrangeDataAccessFile("SpagoBIGeoEngine", "${JNDI_REF}", jndiRef);
			}
			
			/* SpagoBIJasperReportEngine */
			if (_install_jasper) {
				arrangeContextXmlJbossWebXmlJonasWebXmlAndWebXmlConfFiles("SpagoBIJasperReportEngine", true, jndiName);
				arrangeEngineConfigFile("SpagoBIJasperReportEngine");
			}
			
			/* SpagoBIJPivotEngine */
			if (_install_jpivot) {
				arrangeContextXmlJbossWebXmlJonasWebXmlAndWebXmlConfFiles("SpagoBIJPivotEngine", true, jndiName);
				Properties props = new Properties();
				// deletes old engine-config.xml file
				FileUtilities.deleteFile("engine-config.xml", _spagobi_deploy_dir + fs + "SpagoBIJPivotEngine" + _ext + fs + "WEB-INF" + fs + "classes");
				String engineConfigSourceFile = _spagobi_plaftorm_source_dir + fs + "spagobi-conf-files" + fs + "SpagoBIJPivotEngine" + fs + "engine-config.xml";
				String engineConfigDestFile = _spagobi_deploy_dir + fs + "SpagoBIJPivotEngine" + _ext + fs + "WEB-INF" + fs + "classes" + fs + "engine-config.xml";
				props.setProperty("${JNDI_NAME}", jndiName);
				String foodmartRef = null;
				if (_install_examples) {
					foodmartRef = "<SCHEMA catalogUri=\"/WEB-INF/queries/FoodMart.xml\" name=\"FoodMart\" />";
				} else {
					foodmartRef = "<!--SCHEMA catalogUri=\"/WEB-INF/queries/FoodMart.xml\" name=\"FoodMart\" /-->";
				}
				props.setProperty("${FOODMART_REF}", foodmartRef);
				FileUtilities.replaceParametersInFile(engineConfigSourceFile, engineConfigDestFile, props, false);
			}
			
			/* SpagoBIQbeEngine */
			if (_install_qbe) {
				arrangeContextXmlJbossWebXmlJonasWebXmlAndWebXmlConfFiles("SpagoBIQbeEngine", true, jndiName);
				String connectionName = null;
				String jndiContext = null;
				if ("jonas".equalsIgnoreCase(_server_name)) {
					jndiContext = "";
				} else {
					jndiContext = "java:comp/env";
				}
				if (_install_examples) {
					connectionName = "hsqlFoodmart";
				} else {
					connectionName = "defaultDwh";
				}
				Properties props = new Properties();
				// deletes old data-access.xml file
				FileUtilities.deleteFile("data-access.xml", _spagobi_deploy_dir + fs + "SpagoBIQbeEngine" + _ext + fs + "WEB-INF" + fs + "conf");
				// deletes data_access_jonas.xml file
				FileUtilities.deleteFile("data_access_jonas.xml", _spagobi_deploy_dir + fs + "SpagoBIQbeEngine" + _ext + fs + "WEB-INF" + fs + "conf");
				String dataAccessSourceFile = _spagobi_plaftorm_source_dir + fs + "spagobi-conf-files" + fs + "SpagoBIQbeEngine" + fs + "data-access.xml";
				String dataAccessDestFile = _spagobi_deploy_dir + fs + "SpagoBIQbeEngine" + _ext + fs + "WEB-INF" + fs + "conf" + fs + "data-access.xml";
				props.setProperty("${CONN_NAME}", connectionName);
				props.setProperty("${JNDI_CONTEXT}", jndiContext);
				props.setProperty("${JNDI_NAME}", jndiName);
				FileUtilities.replaceParametersInFile(dataAccessSourceFile, dataAccessDestFile, props, false);
			}
			
			/* SpagoBIWekaEngine */
			if (_install_weka) {
				arrangeContextXmlJbossWebXmlJonasWebXmlAndWebXmlConfFiles("SpagoBIWekaEngine", true, jndiName);
				arrangeEngineConfigFile("SpagoBIWekaEngine");
			}
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private static void arrangeContextXmlJbossWebXmlJonasWebXmlAndWebXmlConfFiles(String webappName, boolean manageWebXml, String resourceName) throws Exception {
		Properties props = new Properties();
		props.setProperty("${JNDI_NAME}", resourceName);
		if ("tomcat".equalsIgnoreCase(_server_name)) {
			/* manages context file */
			// TODO controllare che non ci sia gi� il file di contesto altrimenti bisogna cancellarlo
			String contextSourceFile = _spagobi_plaftorm_source_dir + fs + "spagobi-conf-files" + fs + webappName + fs + webappName + ".xml";
			String contextDestFile = _pathdest + fs + "conf" + fs + "Catalina" + fs + "localhost" + fs + webappName + ".xml";
			FileUtilities.replaceParametersInFile(contextSourceFile, contextDestFile, props, false);
		} else if ("jboss".equalsIgnoreCase(_server_name)) {
			/* manages jboss-web.xml */
			// deletes old jboss-web.xml file
			FileUtilities.deleteFile("jboss-web.xml", _spagobi_deploy_dir + fs + webappName + _ext + fs + "WEB-INF");
			String jbosswebSourceFile = _spagobi_plaftorm_source_dir + fs + "spagobi-conf-files" + fs + webappName + fs + "jboss-web.xml";
			String jbosswebDestFile = _spagobi_deploy_dir + fs + webappName + _ext + fs + "WEB-INF" + fs + "jboss-web.xml";
			FileUtilities.replaceParametersInFile(jbosswebSourceFile, jbosswebDestFile, props, false);
		} else if ("jonas".equalsIgnoreCase(_server_name)) {
			/* manages jonas-web.xml */
			// deletes old jonas-web.xml file
			FileUtilities.deleteFile("jonas-web.xml", _spagobi_deploy_dir + fs + webappName + _ext + fs + "WEB-INF");
			String jonaswebSourceFile = _spagobi_plaftorm_source_dir + fs + "spagobi-conf-files" + fs + webappName + fs + "jonas-web.xml";
			String jonaswebDestFile = _spagobi_deploy_dir + fs + webappName + _ext + fs + "WEB-INF" + fs + "jonas-web.xml";
			FileUtilities.replaceParametersInFile(jonaswebSourceFile, jonaswebDestFile, props, false);
		}
		if (manageWebXml) {
			// deletes old web.xml file
			FileUtilities.deleteFile("web.xml", _spagobi_deploy_dir + fs + webappName + _ext + fs + "WEB-INF");
			String webSourceFile = _spagobi_plaftorm_source_dir + fs + "spagobi-conf-files" + fs + webappName + fs + "web.xml";
			String webDestFile = _spagobi_deploy_dir + fs + webappName + _ext + fs + "WEB-INF" + fs + "web.xml";
			FileUtilities.replaceParametersInFile(webSourceFile, webDestFile, props, false);
		}
	}

	private static void arrangeEngineConfigFile(String webappName) throws Exception {
		Properties props = new Properties();
		// deletes old engine-config.xml file
		FileUtilities.deleteFile("engine-config.xml", _spagobi_deploy_dir + fs + webappName + _ext + fs + "WEB-INF" + fs + "classes");
		String engineConfigSourceFile = _spagobi_plaftorm_source_dir + fs + "spagobi-conf-files" + fs + webappName + fs + "engine-config.xml";
		String engineConfigDestFile = _spagobi_deploy_dir + fs + webappName + _ext + fs + "WEB-INF" + fs + "classes" + fs + "engine-config.xml";
		props.setProperty("${JNDI_NAME}", jndiName);
		FileUtilities.replaceParametersInFile(engineConfigSourceFile, engineConfigDestFile, props, false);
	}
	
	private static void arrangeDataAccessFile(String webappName, String placeHolder, String resourceName) throws Exception {
		Properties props = new Properties();
		// deletes old data-access.xml file
		FileUtilities.deleteFile("data-access.xml", _spagobi_deploy_dir + fs + webappName + _ext + fs + "WEB-INF" + fs + "conf");
		String dataAccessSourceFile = _spagobi_plaftorm_source_dir + fs + "spagobi-conf-files" + fs + webappName + fs + "data-access.xml";
		String dataAccessDestFile = _spagobi_deploy_dir + fs + webappName + _ext + fs + "WEB-INF" + fs + "conf" + fs + "data-access.xml";
		props.setProperty(placeHolder, resourceName);
		FileUtilities.replaceParametersInFile(dataAccessSourceFile, dataAccessDestFile, props, false);
	}
}
