package it.eng.qbe.utility;

import java.io.File;

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
		String completeFileName = dataMartPath + System.getProperty("file.separator") + "datamart.jar";
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
		String completeFileName = dataMartPath + System.getProperty("file.separator") + "datamart-"+dialect+".jar";
		File f = new File(completeFileName);
		if (f.exists())
			return f;
		else
			return getJarFile(dataMartPath);
	}

}
