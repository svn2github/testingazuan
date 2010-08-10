/**
 * 
 */
package it.eng.qbe.utility;

import java.io.File;

/**
 * @author andrea gioia
 *
 */
public class FileUtils {
	public static boolean isAbsolutePath(String path) {
		if(path == null) return false;
		return (path.startsWith("/") || path.startsWith("\\") || path.charAt(1) == ':');
	}
	
	public static String getQbeDataMartDir(File baseDir) {
		String qbeDataMartDir = null;
		qbeDataMartDir = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MART_DIR.dir");
		if( !isAbsolutePath(qbeDataMartDir) )  {
			String baseDirStr = (baseDir != null)? baseDir.toString(): it.eng.spago.configuration.ConfigSingleton.getInstance().getRootPath(); //System.getProperty("user.home");
			qbeDataMartDir = baseDirStr + System.getProperty("file.separator") + qbeDataMartDir;
		}
		return qbeDataMartDir;
	}
	
	public static String getQbeScriptDir(File baseDir) {
		String qbeDataMartDir = null;
		qbeDataMartDir = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-SCRIPT-DIR.dir");
		if( !isAbsolutePath(qbeDataMartDir) )  {
			String baseDirStr = (baseDir != null)? baseDir.toString(): it.eng.spago.configuration.ConfigSingleton.getInstance().getRootPath(); //System.getProperty("user.home");
			qbeDataMartDir = baseDirStr + System.getProperty("file.separator") + qbeDataMartDir;
		}
		return qbeDataMartDir;
	}
}
