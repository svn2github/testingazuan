package it.eng.spagobi.kpi.config.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.kpi.config.bo.KpiInstance;
import it.eng.spagobi.kpi.config.bo.KpiValue;
import it.eng.spagobi.kpi.model.bo.ModelInstance;
import it.eng.spagobi.kpi.model.bo.ModelInstanceNode;

import java.util.Date;
import java.util.List;

public interface IKpiDAO {
	
	/**
	 * Inserts a new KPI Value with its date, value period and thresholds
	 * 
	 * @param KpiValue to insert for the specific Kpi
	 * @throws EMFUserError If an Exception occurred
	 */	
	public void insertKpiValue(KpiValue value) throws EMFUserError;

	/**
	 * Returns the ModelInstance of the referred id
	 * 
	 * @param id of the ModelInstance
	 * @return ModelInstance of the referred id
	 * @throws EMFUserError If an Exception occurred
	 */	
	public ModelInstanceNode loadModelInstanceById(Integer id) throws EMFUserError ;
	
	/**
	 * Returns true if the values are the actual ones, false they have to be recalculated
	 * 
	 * @param KpiInstance
	 * @return  Returns true if the values are the actual ones, false they have to be recalculated
	 * @throws EMFUserError If an Exception occurred
	 */	
	public boolean hasActualValues(KpiInstance inst) throws EMFUserError ;
	
	/**
	 * Returns true if the value is not in the correct threshold, false if it's fine
	 * 
	 * @param KpiValue 
	 * 
	 * @return Returns true if the value is not in the correct threshold, false if it's fine
	 * 
	 * @throws EMFUserError if an Exception occurs
	 */
	public boolean isAlarmingValue(KpiValue value)	throws EMFUserError;
	
	/**
	 * Returns true if the value is the actual one, false if it has to be recalculated
	 * 
	 * @param KpiValue 
	 * 
	 * @return Returns true if the value is the actual one, false if it has to be recalculated
	 * 
	 * @throws EMFUserError if an Exception occurs
	 */
	public  boolean isActualValue(KpiValue value)  throws EMFUserError;
	
	
	/**
	 * Deletes the KPI Value 
	 * 
	 * @param KpiValue to delete
	 * @throws EMFUserError If an Exception occurred
	 */	
	public void deleteKpiValue(KpiValue value) throws EMFUserError;
	
	/**
	 * Gets the Kpi Values of the past for each resource
	 * 
	 * @param Kpi , Date of when the KpiValue has to valid
	 * 
	 * @return Returns the correct KpiValue for that Date
	 * 
	 * @throws EMFUserError If an Exception occurred
	 */	
	public List getKpiValue(KpiInstance kpi, Date d) throws EMFUserError;
	
	/**
	 * Gets all actual Kpi Values for each resource
	 * 
	 * @param Kpi Instance
	 * 
	 * @return Returns a list of all actual Kpi Values for each resource
	 * 
	 * @throws EMFUserError If an Exception occurred
	 */	
	public List getKpiActualValue(KpiInstance kpi) throws EMFUserError;
	
	/**
	 * Lists all Kpi Values of a specific KPIInstance
	 * 
	 * @param Kpi 
	 * 
	 * @return Returns a list of all the Kpi Value of the specific KPI Instance
	 * 
	 * @throws EMFUserError If an Exception occurred
	 */	
	public List listKpiValues(KpiInstance kpi) throws EMFUserError;

	
	

}
