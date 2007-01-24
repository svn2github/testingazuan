package it.eng.qbe.utility;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;

import java.util.List;

/**
 * @author Andrea Zoppello
 * 
 * This is the interface for classes that implements 
 * logig to load and persist query on a persistent store than can be, JSR 170 repository, Database,
 * File System and so on
 * 
 */
public interface IQueryPersister {

	/**
	 * @param dm: The datamart model
	 * @param wizObject: The Query object to persist
	 */
	public void persist(DataMartModel dm, ISingleDataMartWizardObject wizObject);
	
	/**
	 * @param dm: The datamart model
	 * @return: all the query for datamart dm
	 */
	public List loadAllQueries(DataMartModel dm);
	
	/**
	 * @param dm: The datamart model
	 * @param key: The identifier of the query to retrieve
	 * @return: the query of the datamart identified by key
	 */
	public ISingleDataMartWizardObject load(DataMartModel dm, String key);
}
