package it.eng.qbe.utility;

import it.eng.spago.configuration.ConfigSingleton;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Andrea Zoppello
 * 
 * An implementation of IDataMartModelRetriever that retrieve datamart files
 * from File System
 *
 */
public class LocalFileSystemDataMartModelRetriever implements
		IDataMartModelRetriever {

	/**
	 * @see it.eng.qbe.utility.IDataMartModelRetriever#getJarFile(java.lang.String)
	 */
	public File getJarFile(String dataMartPath) {
		
		String qbeDataMartDir = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MART_DIR.dir");
		
		String completeFileName = qbeDataMartDir + System.getProperty("file.separator") + dataMartPath + System.getProperty("file.separator") + "datamart.jar";
		File f = new File(completeFileName);
		if (f.exists())
			return f;
		else
			return null;
	}

	/**
	 * @see it.eng.qbe.utility.IDataMartModelRetriever#getJarFile(java.lang.String, java.lang.String)
	 */
	public File getJarFile(String dataMartPath, String dialect) {
		String qbeDataMartDir = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MART_DIR.dir");
		String completeFileName = qbeDataMartDir  + System.getProperty("file.separator") + dataMartPath + System.getProperty("file.separator") + "datamart-"+dialect+".jar";
		
		File f = new File(completeFileName);
		if (f.exists())
			return f;
		else
			return getJarFile(dataMartPath);
	}
	
	
	public static List getAllDataMartPath() {
		String qbeDataMartDir = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MART_DIR.dir");
		File f = new File(qbeDataMartDir);
		
		List dataMartPaths = new ArrayList();

		String[] children = f.list();
	    
		if (children != null)
			dataMartPaths = Arrays.asList(children);
		return dataMartPaths;
	}
	

}
