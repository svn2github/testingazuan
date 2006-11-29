package it.eng.spagobi.installers.demoinstaller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

public class InstallSpagoBIDemo {

	public static void main(String[] args) {

		String pathsource = args[0] + "/spagobi_demo_contents";
		String pathdest = args[0];

			if (!installCommonLibs(pathsource, pathdest))
				return;
	        
			if(!installPatchBin(pathsource, pathdest))
				return;
			
			if (!installPatchHsqldb(pathsource, pathdest))
				return;

//			if (!installCss(pathsource, pathdest))
//				return;
	
//			if (!installWebRssNews(pathsource, pathdest))
//				return;
			
			if (!installWars(pathsource, pathdest))
				return;
	
			if (!installPortalApplication(pathsource, pathdest))
				return;
	
			if (!installSbidata(pathsource, pathdest))
				return;
			
			if (!installJndiConfiguration(pathsource, pathdest))
				return;
			
			if (!installBookletConfiguration(pathsource, pathdest))
				return;
			
//			if (!installDB(pathsource, pathdest))
//				return;
			
			if (!deleteDirectory(pathsource))
				return;
	}

	private static boolean installCommonLibs(String pathsource,	String pathdest) {
		try {
			FileUtilities.copy(pathdest + "/common/lib", pathsource 
					+ "/commonlib/concurrent-1.3.4.jar");
			FileUtilities.copy(pathdest + "/common/lib", pathsource
					+ "/commonlib/ehcache-1.1.jar");
			FileUtilities.copy(pathdest + "/common/lib", pathsource 
					+ "/commonlib/jackrabbit-core-1.0.1.jar");
			FileUtilities.copy(pathdest + "/common/lib", pathsource 
					+ "/commonlib/jcr-1.0.jar");
			FileUtilities.copy(pathdest + "/common/lib", pathsource 
					+ "/commonlib/slf4j-log4j12.jar");	
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	
	private static boolean installPatchBin(String pathsource, String pathdest) {
		try {
			File catalinaold = new File(pathdest + "/bin/catalina.sh");
			catalinaold.delete();
			File runbatold = new File(pathdest + "/bin/catalina.bat");
			runbatold.delete();
			FileUtilities.copy(pathdest + "/bin", pathsource	+ "/patchbin/catalina.sh");
			FileUtilities.copy(pathdest + "/bin", pathsource	+ "/patchbin/catalina.bat");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	private static boolean installPatchHsqldb(String pathsource, String pathdest) {
		try {
			File hsqloldjar = new File(pathdest + "/common/lib/hsqldb-1.8.0.1.jar");
			hsqloldjar.delete();
			FileUtilities.copy(pathdest + "/common/lib", pathsource	+ "/hsqlPatch/hsqldb1_8_0_2.jar");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	

//	private static boolean installCss(String pathsource, String pathdest) {
//		try {
//			//FileUtilities.explode(pathdest + "/webapps/resources", pathdest
//			//		+ "/webapps/resources.war");
//			//File community = new File(pathdest
//			//		+ "/webapps/resources/community/skin/community.css");
//			//community.delete();
//			//File defaultcss = new File(pathdest
//			//		+ "/webapps/resources/skin/portlet/default-portlet.css");
//			//defaultcss.delete();
//			//FileUtilities.copy(pathdest + "/webapps/resources/community/skin",
//			//		pathsource + "/css/community.css");
//			//FileUtilities.copy(pathdest + "/webapps/resources/community/skin",
//			//		pathsource + "/css/jsr168-portlet.css");
//			//FileUtilities.copy(pathdest + "/webapps/resources/skin/portlet",
//			//		pathsource + "/css/default-portlet.css");
//		} catch (Exception exc) {
//			return false;
//		}
//		return true;
//	}

	
//	private static boolean installWebRssNews(String pathsource, String pathdest) {
//		try {
//			FileUtilities.explode(pathdest + "/webapps/web", pathdest + "/webapps/web.war");
//			FileUtilities.copy(pathdest + "/webapps/web", pathsource + "/web/spagobinews.xml");
//		} catch (Exception e) {
//			return false;
//		}
//		return true;
//	}
	
	private static boolean installWars(String pathsource, String pathdest) {
		try {
			FileUtilities.explode(pathdest + "/webapps/SpagoBIBirtReportEngine",
					pathsource + "/wars/SpagoBIBirtReportEngine.war");
			FileUtilities.explode(pathdest + "/webapps/SpagoBIGeoEngine",
					pathsource + "/wars/SpagoBIGeoEngine.war");
			FileUtilities.explode(pathdest
					+ "/webapps/SpagoBIJasperReportEngine", pathsource
					+ "/wars/SpagoBIJasperReportEngine.war");
			FileUtilities.explode(pathdest + "/webapps/SpagoBIJPivotEngine",
					pathsource + "/wars/SpagoBIJPivotEngine.war");
			FileUtilities.explode(pathdest + "/webapps/SpagoBIWekaEngine",
					pathsource + "/wars/SpagoBIWekaEngine.war");
			FileUtilities.explode(pathdest + "/webapps/spagobi", pathsource
					+ "/wars/spagobi.war");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}

	private static boolean installPortalApplication(String pathsource, String pathdest) {
		try {
			FileUtilities.explode(pathdest + "/webapps/sbiportal", pathsource
					+ "/wars/sbiportal.war");
			String tempDirStr = pathdest + "/temp";
	 		File tempDir = new File(tempDirStr);
	 		if (tempDir.exists() && !tempDir.isDirectory()) return false;
	 		tempDir.mkdirs();
	 		String dataDirStr = tempDirStr + "/data";
	 		File dataDir = new File(dataDirStr);
	 		dataDir.mkdirs();
	 		FileUtilities.copy(dataDirStr, pathsource + "/exoMetadataDb/sbiportal.script");
	 		FileUtilities.copy(dataDirStr, pathsource + "/exoMetadataDb/sbiportal.properties");
	 		FileUtilities.copy(dataDirStr, pathsource + "/exoMetadataDb/sbiportal.log");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}

	private static boolean installSbidata(String pathsource, String pathdest) {
		try {
			FileUtilities.explode(pathdest, pathsource
					+ "/sbidata/sbidata.zip");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	private static boolean installJndiConfiguration(String pathsource, String pathdest) {
		try {
			FileUtilities.copy(pathdest + "/conf/Catalina/localhost", 
					   pathsource + "/jndi/sbiportal.xml");
			FileUtilities.copy(pathdest + "/conf/Catalina/localhost", 
					   pathsource + "/jndi/spagobi.xml");
			FileUtilities.copy(pathdest + "/conf/Catalina/localhost", 
					   pathsource + "/jndi/SpagoBIBirtReportEngine.xml");
			FileUtilities.copy(pathdest + "/conf/Catalina/localhost", 
					   pathsource + "/jndi/SpagoBIGeoEngine.xml");
			FileUtilities.copy(pathdest + "/conf/Catalina/localhost", 
					   pathsource + "/jndi/SpagoBIJasperReportEngine.xml");
			FileUtilities.copy(pathdest + "/conf/Catalina/localhost", 
					   pathsource + "/jndi/SpagoBIJPivotEngine.xml");
			FileUtilities.copy(pathdest + "/conf/Catalina/localhost", 
					   pathsource + "/jndi/SpagoBIWekaEngine.xml");
			// delete the exo-tomcat server.xml file
			File servconf = new File(pathdest + "/conf/server.xml");
			servconf.delete();
			//FileUtilities.copy(pathdest + "/conf", 
			//		   pathsource + "/jndi/server.xml");
            
			//******* FOR JACKRABBIT *************************************
			
			// read into a string buffer the installation server.xml file 
			servconf = new File(pathsource + "/jndi/server.xml");
			FileReader reader = new FileReader(servconf);
			
			//FileInputStream fis = new FileInputStream(servconf);
			StringBuffer servbuf = new StringBuffer();
			char[] buffer = new char[1024];
			int len;
			while ((len = reader.read(buffer)) >= 0) {
				servbuf.append(buffer, 0, len);
			}
			reader.close();
			// replace the path of the cms root and file config
		    int startcmshome = servbuf.indexOf("${cmshome}");
		    servbuf.replace(startcmshome, startcmshome+10, pathdest+"/sbidata/jcrRepositoryFS");
		    int startconffile = servbuf.indexOf("${confrepfile}");
		    servbuf.replace(startconffile, startconffile+14, 
		    		        pathdest+"/sbidata/repository.xml");
		    // write the string buffer to the new file server.xml into exo-tomcat conf directory
		    File newservconf = new File(pathdest + "/conf/server.xml");
			FileOutputStream fos = new FileOutputStream(newservconf);
			fos.write(servbuf.toString().getBytes());
			fos.flush();
			fos.close();
		} catch (Exception exc) {
			System.out.println(exc);
			return false;
		}
		return true;
	}
	
	private static boolean installBookletConfiguration(String pathsource, String pathdest) {
		try {
			
			// read into a string buffer the booklet.xml file 
			File bookletconf = new File(pathdest + "/webapps/spagobi/WEB-INF/conf/components/booklets/booklets.xml");
			FileReader reader = new FileReader(bookletconf);
			
			//FileInputStream fis = new FileInputStream(servconf);
			StringBuffer stringBuffer = new StringBuffer();
			char[] buffer = new char[1024];
			int len;
			while ((len = reader.read(buffer)) >= 0) {
				stringBuffer.append(buffer, 0, len);
			}
			reader.close();
			// deletes the old file
			bookletconf.delete();
			
			// replace the path of the exo root 
		    int startbooklettempdir = stringBuffer.indexOf("${booklettempdir}");
		    stringBuffer.replace(startbooklettempdir, startbooklettempdir+17, pathdest+"/temp/booklet-temp");
		    // write the string buffer to the new file booklet.xml
		    bookletconf = new File(pathdest + "/webapps/spagobi/WEB-INF/conf/components/booklets/booklets.xml");
			FileOutputStream fos = new FileOutputStream(bookletconf);
			fos.write(stringBuffer.toString().getBytes());
			fos.flush();
			fos.close();
		} catch (Exception exc) {
			System.out.println(exc);
			return false;
		}
		return true;
	}
	
//	private static boolean installDB(String pathsource, String pathdest) {
//		try {
//			FileUtilities.copy(pathdest + "/sbidata/databases", pathsource
//					+ "/databases/foodmart.properties");
//			FileUtilities.copy(pathdest + "/sbidata/databases", pathsource
//					+ "/databases/foodmart.script");
//			FileUtilities.copy(pathdest + "/sbidata/databases", pathsource
//					+ "/databases/spagobi.properties");
//			FileUtilities.copy(pathdest + "/sbidata/databases", pathsource
//					+ "/databases/spagobi.script");
//			FileUtilities.copy(pathdest + "/sbidata/databases", pathsource
//					+ "/databases/hsqldb1_8_0_2.jar");
//			FileUtilities.copy(pathdest + "/sbidata/databases", pathsource
//					+ "/databases/start.sh");
//			FileUtilities.copy(pathdest + "/sbidata/databases", pathsource
//					+ "/databases/start.bat");
//		} catch (Exception e) {
//			return false;
//		}
//		return true;
//	}
	
	public static boolean deleteDirectory(String pathdest) {
		try {
			File directory = new File(pathdest);
			if (directory.isDirectory()) {
				File[] files = directory.listFiles();
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					if (file.isFile()) {
						boolean deletion = file.delete();
						if (!deletion)
							return false;
					} else
						deleteDirectory(file.getAbsolutePath());
				}
			}
			boolean deletion = directory.delete();
			if (!deletion)
				return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
