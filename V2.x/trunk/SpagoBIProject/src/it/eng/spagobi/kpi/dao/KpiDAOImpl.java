package it.eng.spagobi.kpi.dao;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.kpi.bo.Kpi;
import it.eng.spagobi.kpi.bo.KpiInstance;
import it.eng.spagobi.kpi.bo.KpiValue;

import java.util.Date;
import java.util.List;

public class KpiDAOImpl implements IKpiDAO {

	public void deleteKpiValue(KpiValue value) throws EMFUserError {
		// TODO Auto-generated method stub
		
	}

	public KpiValue getKpiActualValue(Kpi kpi) throws EMFUserError {
		// TODO Auto-generated method stub
		return null;
	}

	public KpiInstance getKpiInstance(Kpi kpi) throws EMFUserError {
		// TODO Auto-generated method stub
		return null;
	}

	public KpiValue getKpiValue(Kpi kpi, Date d) throws EMFUserError {
		// TODO Auto-generated method stub
		return null;
	}

	public void insertKpiValue(KpiValue value, Kpi k) throws EMFUserError {
		// TODO Auto-generated method stub
		
	}

	public boolean isActualValue(KpiValue value) throws EMFUserError {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isAlarmingValue(KpiValue value) throws EMFUserError {
		// TODO Auto-generated method stub
		return false;
	}

	public List listKpiValues(KpiInstance kpi) throws EMFUserError {
		// TODO Auto-generated method stub
		return null;
	}

}
