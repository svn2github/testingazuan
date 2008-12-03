package it.eng.spagobi.kpi.threshold.dao;

import java.util.List;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.kpi.threshold.bo.Threshold;

public interface IThresholdDAO {
	
	/**
	 * Returns the Threshold of the referred id
	 * 
	 * @param id of the Threshold
	 * @return Threshold of the referred id
	 * @throws EMFUserError If an Exception occurred
	 */
	public Threshold loadThresholdById(Integer id) throws EMFUserError ;
	
	/**
	 * Returns the list of Thresholds
	 * @return the list of Thresholds
	 * @throws EMFUserError If an Exception occurred
	 */
	public List loadThresholdList() throws EMFUserError ;	

}
