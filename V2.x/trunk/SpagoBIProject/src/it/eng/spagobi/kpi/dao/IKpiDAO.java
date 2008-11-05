package it.eng.spagobi.kpi.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.kpi.bo.Kpi;
import it.eng.spagobi.kpi.bo.KpiInstance;
import it.eng.spagobi.kpi.bo.KpiValue;

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
	 * Gets a Kpi Value of the past
	 * 
	 * @param Kpi , Date of when the KpiValue has to valid
	 * 
	 * @return Returns the correct KpiValue for that Date
	 * 
	 * @throws EMFUserError If an Exception occurred
	 */	
	public KpiValue getKpiValue(KpiInstance kpi, Date d) throws EMFUserError;
	
	/**
	 * Lists all Kpi Values of a specific KPI
	 * 
	 * @param Kpi 
	 * 
	 * @return Returns a list of all the Kpi Value of the specific KPI
	 * 
	 * @throws EMFUserError If an Exception occurred
	 */	
	public KpiValue getKpiActualValue(KpiInstance kpi) throws EMFUserError;
	
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
