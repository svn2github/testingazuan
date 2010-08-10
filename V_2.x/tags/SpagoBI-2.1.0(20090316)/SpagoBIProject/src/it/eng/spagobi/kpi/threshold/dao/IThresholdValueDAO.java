package it.eng.spagobi.kpi.threshold.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.kpi.threshold.bo.ThresholdValue;

import java.util.List;

public interface IThresholdValueDAO {

	List loadThresholdValueList(Integer thresholdId,String fieldOrder, String typeOrder) throws EMFUserError;

	ThresholdValue loadThresholdValueById(Integer id) throws EMFUserError;

	void modifyThresholdValue(ThresholdValue threshold) throws EMFUserError;

	Integer insertThresholdValue(ThresholdValue toCreate) throws EMFUserError;

	boolean deleteThresholdValue(Integer thresholdId) throws EMFUserError;
}
