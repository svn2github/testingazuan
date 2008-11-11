package it.eng.spagobi.kpi.config.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.kpi.config.bo.Kpi;
import it.eng.spagobi.kpi.config.bo.KpiInstance;
import it.eng.spagobi.kpi.config.bo.KpiValue;
import it.eng.spagobi.kpi.config.metadata.SbiKpi;
import it.eng.spagobi.kpi.config.metadata.SbiKpiInstance;
import it.eng.spagobi.kpi.model.bo.ModelInstance;
import it.eng.spagobi.kpi.model.bo.ModelInstanceNode;
import it.eng.spagobi.kpi.threshold.bo.Threshold;
import it.eng.spagobi.kpi.threshold.metadata.SbiThresholdValue;

import java.util.Date;
import java.util.List;

public interface IKpiDAO {
	
	/**
	 * Inserts a new KPI Value with its date, value period and thresholds
	 * 
	 * @param KpiValue to insert 
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
	public ModelInstanceNode loadModelInstanceById(Integer id,Date requestedDate) throws EMFUserError ;
	
	/**
	 * Returns the KpiInstance of the referred id
	 * 
	 * @param id of the KpiInstance
	 * @return KpiInstance of the referred id
	 * @throws EMFUserError If an Exception occurred
	 */
	public KpiInstance loadKpiInstanceById(Integer id) throws EMFUserError ;
	
	/**
	 * Returns a List of all the the Threshols of the KpiInstance
	 * 
	 * @param KpiInstance k
	 * @return List of all the the Threshols of the KpiInstance
	 * @throws EMFUserError If an Exception occurred
	 */
	public List getThresholds(KpiInstance k)throws EMFUserError;

	/**
	 * Returns the Kpi of the referred id
	 * 
	 * @param id of the Kpi
	 * @return Kpi of the referred id
	 * @throws EMFUserError If an Exception occurred
	 */	
	public Kpi loadKpiById(Integer id) throws EMFUserError ;
	
	/**
	 * Returns true if the values are the actual ones, false if they have to be recalculated
	 * 
	 * @param KpiInstance and date for which the values have to be actual
	 * @return  Returns true if the values are the actual ones, false if they have to be recalculated
	 * @throws EMFUserError If an Exception occurred
	 */	
	public boolean hasActualValues(KpiInstance inst, Date d) throws EMFUserError ;
	
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

}
