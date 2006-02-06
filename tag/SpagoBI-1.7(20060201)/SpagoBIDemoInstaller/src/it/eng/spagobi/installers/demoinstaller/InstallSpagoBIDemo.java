package it.eng.spagobi.installers.demoinstaller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;

public class InstallSpagoBIDemo {

	public static void main(String[] args) {

		String pathsource = args[0] + "/spagobi_demo_contents";
		String pathdest = args[0];

			if (!installPatchHibernate(pathsource, pathdest))
				return;
	
			if (!installPatchHsqldb(pathsource, pathdest))
				return;
			
			if (!installPatchCmsJndi(pathsource, pathdest))
				return;
			
			if (!installJaasFile(pathsource, pathdest))
				return;
	
			if (!installCss(pathsource, pathdest))
				return;
	
			if (!installNavProp(pathsource, pathdest))
				return;
	
			if (!installWebRssNews(pathsource, pathdest))
				return;
			
			if (!installWars(pathsource, pathdest))
				return;
	
			if (!installPortal(pathsource, pathdest))
				return;
	
			if (!installCms(pathsource, pathdest))
				return;
			
			if (!installJndiConfiguration(pathsource, pathdest))
				return;
			
			if (!installDB(pathsource, pathdest))
				return;
			
			if (!deleteDirectory(pathsource))
				return;
	}

	private static boolean installPatchHibernate(String pathsource,	String pathdest) {
		try {
			File asm = new File(pathdest + "/common/lib/asm-1.4.1.jar");
			File asmutil = new File(pathdest + "/common/lib/asm-util-1.4.1.jar");
			File cglib = new File(pathdest + "/common/lib/cglib-full-2.0.1.jar");
			asm.delete();
			asmutil.delete();
			cglib.delete();
			FileUtilities.copy(pathdest + "/common/lib", pathsource
					+ "/hibernate3Patch/asm-attrs.jar");
			FileUtilities.copy(pathdest + "/common/lib", pathsource
					+ "/hibernate3Patch/asm.jar");
			FileUtilities.copy(pathdest + "/common/lib", pathsource
					+ "/hibernate3Patch/cglib-2.1.jar");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	
	private static boolean installPatchHsqldb(String pathsource, String pathdest) {
		try {
			File hsqloldjar = new File(pathdest + "/common/lib/hsqldb.jar");
			hsqloldjar.delete();
			FileUtilities.copy(pathdest + "/common/lib", pathsource	+ "/hsqlPatch/hsqldb1_8_0_2.jar");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	
	private static boolean installPatchCmsJndi(String pathsource, String pathdest) {
		try {
			File comcolljar = new File(pathdest + "/common/lib/commons-collections-2.1.1.jar");
			comcolljar.delete();
			FileUtilities.copy(pathdest + "/common/lib", pathsource	+ "/jndi/patchCmsJndi/commons-collections-3.1.jar");
			FileUtilities.copy(pathdest + "/common/lib", pathsource	+ "/jndi/patchCmsJndi/log4j-1.2.8.jar");
			FileUtilities.copy(pathdest + "/common/lib", pathsource	+ "/jndi/patchCmsJndi/eng.jcr.1.0.jar");
			FileUtilities.copy(pathdest + "/common/lib", pathsource	+ "/jndi/patchCmsJndi/eng.jackrabbit-0.16.4.1-dev.jar");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	

	

	private static boolean installJaasFile(String pathsource, String pathdest) {
		try {
			File jaasfile = new File(pathdest + "/conf/jaas.conf");
			jaasfile.delete();
			FileOutputStream outstream = new FileOutputStream(pathdest
					+ "/conf/jaas.conf");
			String jaasfileStr = "exo-domain { \n"
					+ "org.exoplatform.services.security.jaas.BasicLoginModule required;"
					+ "}; "
					+ "Jackrabbit { "
					+ "org.apache.jackrabbit.core.security.SimpleLoginModule required anonymousId=\"anonymous\";"
					+ "};";
			byte[] jaasfileBytes = jaasfileStr.getBytes();
			outstream.write(jaasfileBytes);
			outstream.flush();
			outstream.close();
		} catch (Exception exc) {
			return false;
		}
		return true;
	}

	private static boolean installCss(String pathsource, String pathdest) {
		try {
			FileUtilities.explode(pathdest + "/webapps/skin", pathdest
					+ "/webapps/skin.war");
			File css = new File(pathdest
					+ "/webapps/skin/portlet/styles/default-portlet.css");
			css.delete();
			FileUtilities.copy(pathdest + "/webapps/skin/portlet/styles",
					pathsource + "/css/default-portlet.css");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}

	private static boolean installNavProp(String pathsource, String pathdest) {
		try {
			FileUtilities.explode(pathdest + "/webapps/nav", pathdest
					+ "/webapps/nav.war");
			File fr = new File(
					pathdest
							+ "/webapps/nav/WEB-INF/classes/locale/portlet/navigation/nav_fr.properties");
			File it = new File(
					pathdest
							+ "/webapps/nav/WEB-INF/classes/locale/portlet/navigation/nav_it.properties");
			File def = new File(
					pathdest
							+ "/webapps/nav/WEB-INF/classes/locale/portlet/navigation/nav.properties");
			File zh = new File(
					pathdest
							+ "/webapps/nav/WEB-INF/classes/locale/portlet/navigation/nav_zh.properties");
			fr.delete();
			it.delete();
			def.delete();
			zh.delete();
			FileUtilities
					.copy(
							pathdest
									+ "/webapps/nav/WEB-INF/classes/locale/portlet/navigation/",
							pathsource
									+ "/nav/WEB-INF/classes/locale/portlet/navigation/nav_fr.properties");
			FileUtilities
					.copy(
							pathdest
									+ "/webapps/nav/WEB-INF/classes/locale/portlet/navigation/",
							pathsource
									+ "/nav/WEB-INF/classes/locale/portlet/navigation/nav_it.properties");
			FileUtilities
					.copy(
							pathdest
									+ "/webapps/nav/WEB-INF/classes/locale/portlet/navigation/",
							pathsource
									+ "/nav/WEB-INF/classes/locale/portlet/navigation/nav.properties");
			FileUtilities
					.copy(
							pathdest
									+ "/webapps/nav/WEB-INF/classes/locale/portlet/navigation/",
							pathsource
									+ "/nav/WEB-INF/classes/locale/portlet/navigation/nav_zh.properties");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}

	
	private static boolean installWebRssNews(String pathsource, String pathdest) {
		try {
			FileUtilities.explode(pathdest + "/webapps/web", pathdest + "/webapps/web.war");
			FileUtilities.copy(pathdest + "/webapps/web", pathsource + "/web/spagobinews.xml");
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private static boolean installWars(String pathsource, String pathdest) {
		try {
			FileUtilities.explode(pathdest + "/webapps/SpagoBIJPivotEngine",
					pathsource + "/wars/SpagoBIJPivotEngine.war");
			FileUtilities.explode(pathdest
					+ "/webapps/SpagoBIJasperReportEngine", pathsource
					+ "/wars/SpagoBIJasperReportEngine.war");
			FileUtilities.explode(pathdest + "/webapps/spagobi", pathsource
					+ "/wars/spagobi.war");

		} catch (Exception exc) {
			return false;
		}
		return true;
	}

	private static boolean installPortal(String pathsource, String pathdest) {
		try {
			File portalwar = new File(pathdest + "/webapps/portal.war");
			portalwar.delete();
			FileUtilities.explode(pathdest + "/webapps/portal", pathsource
					+ "/wars/portal.war");
		} catch (Exception exc) {
			return false;
		}
		return true;
	}

	private static boolean installCms(String pathsource, String pathdest) {
		try {
			FileUtilities.explode(pathdest + "/temp/data", pathsource
					+ "/cms/spagobi_demo.war");
			/*
			File cmsProp = new File(
					pathdest
							+ "/webapps/spagobi/WEB-INF/classes/jackrabbitSessionFactory.properties");
			cmsProp.delete();
			FileOutputStream outstream = new FileOutputStream(
					pathdest
							+ "/webapps/spagobi/WEB-INF/classes/jackrabbitSessionFactory.properties");
			String propsStr = "repository_path=" + pathdest
					+ "/temp/data/spagobi_demo\n"
					+ "name_configuration_file=repository.xml";
			byte[] propsBytes = propsStr.getBytes();
			outstream.write(propsBytes);
			outstream.flush();
			outstream.close();
			*/
		} catch (Exception exc) {
			return false;
		}
		return true;
	}
	
	private static boolean installJndiConfiguration(String pathsource, String pathdest) {
		try {
			FileUtilities.copy(pathdest + "/conf/Catalina/localhost", 
							   pathsource + "/jndi/spagobi.xml");
			FileUtilities.copy(pathdest + "/conf/Catalina/localhost", 
					   pathsource + "/jndi/SpagoBIJPivotEngine.xml");
			FileUtilities.copy(pathdest + "/conf/Catalina/localhost", 
					   pathsource + "/jndi/SpagoBIJasperReportEngine.xml");
			// delete the exo-tomcat server.xml file
			File servconf = new File(pathdest + "/conf/server.xml");
			servconf.delete();
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
		    servbuf.replace(startcmshome, startcmshome+10, pathdest+"/temp/data/spagobi_demo");
		    int startconffile = servbuf.indexOf("${confrepfile}");
		    servbuf.replace(startconffile, startconffile+14, 
		    		        pathdest+"/webapps/spagobi/WEB-INF/classes/repository.xml");
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
	
	
	private static boolean installDB(String pathsource, String pathdest) {
		try {
			FileUtilities.copy(pathdest + "/temp/data/databases", pathsource
					+ "/databases/foodmart.properties");
			FileUtilities.copy(pathdest + "/temp/data/databases", pathsource
					+ "/databases/foodmart.script");
			FileUtilities.copy(pathdest + "/temp/data/databases", pathsource
					+ "/databases/spagobi.properties");
			FileUtilities.copy(pathdest + "/temp/data/databases", pathsource
					+ "/databases/spagobi.script");
			FileUtilities.copy(pathdest + "/temp/data/databases", pathsource
					+ "/databases/hsqldb1_8_0_2.jar");
			FileUtilities.copy(pathdest + "/temp/data/databases", pathsource
					+ "/databases/start.sh");
			FileUtilities.copy(pathdest + "/temp/data/databases", pathsource
					+ "/databases/start.bat");
			/*
			String OS_NAME = System.getProperty("os.name");
			if(!OS_NAME.startsWith("WINDOWS") && !OS_NAME.startsWith("OS/2")){
				 String command = "chmod -R 777 "+pathdest+"/temp/data/databases/start.sh";
				 Runtime rt = Runtime.getRuntime();
				 rt.exec(command);
			 }*/
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
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
