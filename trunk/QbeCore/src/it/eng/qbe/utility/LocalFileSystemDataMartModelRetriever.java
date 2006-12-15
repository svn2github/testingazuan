package it.eng.qbe.utility;

import it.eng.qbe.model.DataMartModel;
import it.eng.spago.configuration.ConfigSingleton;

import java.io.File;
import java.io.FilenameFilter;
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

	public List getViewJarFiles(String dataMartPath, String dialect) {
		List files = new ArrayList();
		String qbeDataMartDir = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MART_DIR.dir");
		String directory = qbeDataMartDir + System.getProperty("file.separator") + dataMartPath + System.getProperty("file.separator");
		File dir = new File(directory);
//	   	 It is also possible to filter the list of returned files.
	       // This example does not return any files that start with `.'.
	       FilenameFilter filter = new FilenameFilter() {
	           public boolean accept(File dir, String name) {
	               return name.endsWith(".jar") && !name.equalsIgnoreCase("datamart.jar");
	           }
	       };
	    
	       String[] children = dir.list(filter);
           if (children == null) {
               // Either dir does not exist or is not a directory
           } else {
               for (int i=0; i<children.length; i++) {
                   // Get filename of file or directory
                   String filename = children[i];
                   files.add(new File(dir, filename));
               }
           }
          return files;
	}

}
