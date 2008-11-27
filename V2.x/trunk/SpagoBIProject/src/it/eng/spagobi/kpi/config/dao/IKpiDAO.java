package it.eng.spagobi.kpi.config.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.kpi.config.bo.Kpi;
import it.eng.spagobi.kpi.config.bo.KpiInstance;
import it.eng.spagobi.kpi.config.bo.KpiValue;
import it.eng.spagobi.kpi.model.bo.ModelInstanceNode;
import it.eng.spagobi.kpi.model.bo.Resource;
import it.eng.spagobi.tools.dataset.bo.DataSetConfig;

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
	 * Returns the DatasetConfig for the KPI with id kpiId
	 * 
	 * @param kpiId of the KPI 
	 * @return DataSetConfig used to calculate the KPI with ID kpiId 
	 * @throws EMFUserError if an Exception occurs
	 */
	public DataSetConfig getDsFromKpiId(Integer kpiId) throws EMFUserError;
	
	/**
	 * KpiValue valid for the the KpiInstance selected, for the resource selected, in the date selected 
	 * 
	 * @param KpiValue 
	 * @return KpiValue valid for the the KpiInstance selected, for the resource selected, in the date selected 
	 * @throws EMFUserError if an Exception occurs
	 */
	public KpiValue getKpiValue(Integer kpiInstanceId, Date d, Resource r) throws EMFUserError;
	
	/**
	 * Returns True if the KPIInstance with id kpiInstID is under AlarmControl, false if it is not 
	 * 
	 * @param kpiInstID of the KPIInstance that we want to monitor
	 * @return Boolean that shows if the KPIInstance with id kpiInstID is under AlarmControl  
	 * @throws EMFUserError if an Exception occurs
	 */
	public Boolean isKpiInstUnderAlramControl(Integer kpiInstID) throws EMFUserError;
	
	//public boolean hasActualValues(KpiInstance inst, Date d) throws EMFUserError ;
	
	/**
	 * For the specific KpiValue verifies if it is ok with every threshold and if not writes an alarm event in the AlarmEvent table so that later on an alarm will be sent
	 * 
	 * @param KpiValue 
	 *  
	 * @throws EMFUserError if an Exception occurs
	 */
	public void isAlarmingValue(KpiValue value)	throws EMFUserError;
	
	/**
	 * Returns the ChartType of the specific KpiInstance (it could also be null)
	 * 
	 * @param kpiInstanceID 
	 * @return Returns the ChartType of the specific KpiInstance (it could also be null)
	 * @throws EMFUserError if an Exception occurs
	 */
	public String getChartType(Integer kpiInstanceID) throws EMFUserError;

}
