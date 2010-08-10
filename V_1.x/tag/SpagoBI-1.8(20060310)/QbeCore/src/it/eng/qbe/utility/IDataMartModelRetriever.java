package it.eng.qbe.utility;

import java.io.File;

/**
 * @author Andrea Zoppello
 * 
 * This is the interface for classes that implements 
 * logig to retrieve the datamart model file given a path
 * 
 */
public interface IDataMartModelRetriever {

	/**
	 * @param dataMartPath
	 * @return the default datamart file
	 */
	public File getJarFile(String dataMartPath);
	
	/**
	 * @param dataMartPath
	 * @param dialect
	 * @return the specific file of datamart given Hibernate Dialect
	 */
	public File getJarFile(String dataMartPath, String dialect);
}
