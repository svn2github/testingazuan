package it.eng.spagobi.analiticalmodel.document.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;

public interface IBIObjectRating {
	
	
	/**
	 * Implements the query to insert a rating for a BI Object. 
	 * 
	 * @param  obj The BIobject containing all modify information
	 * @param  userid The userid String
	 * @param  rating The rating String
	 * @throws EMFUserError If an Exception occurred
	 */
	
	public void voteBIObject(BIObject obj,String userid, String rating) throws EMFUserError;
	
	/**
	 * Implements the query to calculate the medium rating for a BI Object. 
	 * 
	 * @param  obj The BIobject containing all modify information
	 * @return The BI object medium rating 
	 * @throws EMFUserError If an Exception occurred
	 */
	public Double calculateBIObjectRating(BIObject obj) throws EMFUserError;

}
