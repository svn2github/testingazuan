package it.eng.spagobi.tools.distributionlist.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.tools.datasource.bo.DataSource;
import it.eng.spagobi.tools.distributionlist.bo.DistributionList;

import java.util.List;

/**
 * Defines the interfaces for all methods needed to insert, modify and delete a Distribution List
 */
public interface IDistributionListDAO {
	
	
	/**
	 * Loads all detail information for a distribution list identified by its id. 
	 * All these informations,  achived by a query to the DB, are stored 
	 * into a distributionlist object, which is returned.
	 * 
	 * @param Id The id for the distributionlist to load
	 * @return	A <code>distributionlist</code> object containing all loaded information
	 * @throws EMFUserError If an Exception occurred
	 */
	public DistributionList loadDistributionListById(Integer Id) throws EMFUserError;
	
	/**
	 * Loads all distribution lists. 
	 * All these informations,  achived by a query to the DB, are stored 
	 * into a List object, which is returned.
	 * 
	 * @return	A <code>List</code> object containing all distribution Lists
	 * @throws EMFUserError If an Exception occurred
	 */
	public List loadAllDistributionLists() throws EMFUserError;

	/**
	 * Implements the query to modify a distributionlist. All information needed is stored 
	 * into the input <code>distributionlist</code> object.
	 * 
	 * @param aDistributionList The object containing all modified informations
	 * @throws EMFUserError If an Exception occurred
	 */
	
	//public void modifyDistributionList(DistributionList aDistributionList) throws EMFUserError;
	
	/**
	 * Implements the query to insert a distribution list. All information needed is stored 
	 * into the input <code>distributionlist</code> object.
	 * 
	 * @param aDistributionList The object containing all insert information
	 * @throws EMFUserError If an Exception occurred
	 */
	public void insertDistributionList(DistributionList aDistributionList) throws EMFUserError;
	
	/**
	 * Implements the query to erase a distribution list. All information needed is stored 
	 * into the input <code>distributionlist</code> object.
	 * 
	 * @param aDistributionList The object containing all informations to be deleted
	 * @throws EMFUserError If an Exception occurred
	 */	
	public void eraseDistributionList(DistributionList aDistributionList) throws EMFUserError;
}
