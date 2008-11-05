package it.eng.spagobi.kpi.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.kpi.bo.Kpi;
import it.eng.spagobi.kpi.bo.KpiInstance;
import it.eng.spagobi.kpi.bo.KpiValue;
import it.eng.spagobi.kpi.metadata.SbiKpiInstance;
import it.eng.spagobi.kpi.metadata.SbiKpiValue;
import it.eng.spagobi.tools.dataset.bo.DataSetConfig;
import it.eng.spagobi.tools.dataset.bo.FileDataSet;
import it.eng.spagobi.tools.dataset.bo.JClassDataSet;
import it.eng.spagobi.tools.dataset.bo.QueryDataSet;
import it.eng.spagobi.tools.dataset.bo.ScriptDataSet;
import it.eng.spagobi.tools.dataset.bo.WSDataSet;
import it.eng.spagobi.tools.dataset.dao.DataSetDAOHibImpl;
import it.eng.spagobi.tools.dataset.metadata.SbiDataSetConfig;
import it.eng.spagobi.tools.dataset.metadata.SbiFileDataSet;
import it.eng.spagobi.tools.dataset.metadata.SbiJClassDataSet;
import it.eng.spagobi.tools.dataset.metadata.SbiQueryDataSet;
import it.eng.spagobi.tools.dataset.metadata.SbiScriptDataSet;
import it.eng.spagobi.tools.dataset.metadata.SbiWSDataSet;
import it.eng.spagobi.tools.datasource.metadata.SbiDataSource;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class KpiDAOImpl extends AbstractHibernateDAO implements IKpiDAO {
	
	static private Logger logger = Logger.getLogger(KpiDAOImpl.class);

	public void deleteKpiValue(KpiValue value) throws EMFUserError {
		// TODO Auto-generated method stub
		
	}

	public KpiValue getKpiActualValue(KpiInstance kpi) throws EMFUserError {
		// TODO Auto-generated method stub
		return null;
	}

	public KpiValue getKpiValue(KpiInstance kpi, Date d) throws EMFUserError {
		
		logger.debug("IN");
		 
		Integer kpiInstID = kpi.getKpiId();
		KpiValue toReturn = null;	
		Session aSession = null;
		Transaction tx = null;
		SbiKpiInstance hibKpiInstance = null;
		SbiKpiValue hibKpiValue = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			hibKpiInstance = (SbiKpiInstance)aSession.load(SbiKpiInstance.class, kpiInstID);
			Set kpiValues = hibKpiInstance.getSbiKpiValues();
			
			
			toReturn = toKpiValue(hibKpiValue);
			tx.commit();

			} catch (HibernateException he) {
				logger.error("Error while loading the data Set with id " , he);			

				if (tx != null)
					tx.rollback();

				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

			} finally {
				if (aSession!=null){
					if (aSession.isOpen()) aSession.close();
					logger.debug("OUT");
				}
			}
			logger.debug("OUT");
		return null;
	}

	public void insertKpiValue(KpiValue value) throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpiValue hibKpiValue = toSbiKpiValue(value);

			aSession.save(hibKpiValue);
			tx.commit();
			
		} catch (HibernateException he) {
			logger.error("Error while inserting the KpiValue", he);			

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
				logger.debug("OUT");
			}
		}
		logger.debug("OUT");
		
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
	
	private SbiKpiValue toSbiKpiValue(KpiValue value){
		
		SbiKpiValue hibKpiValue = new SbiKpiValue();
		Date beginDt = value.getBeginDate();
		Date endDt = value.getEndDate();
		String kpiValue = value.getValue().toString();
		Integer kpiInstanceId = value.getKpiInstanceId();
		
		hibKpiValue.setBeginDt(beginDt);
		hibKpiValue.setEndDt(endDt);
		hibKpiValue.setKpiValue(kpiValue);
		hibKpiValue.setIdKpiInstanceValue(kpiInstanceId);
		
		return hibKpiValue;
	}
	
	private KpiValue toKpiValue(SbiKpiValue value){
		
		KpiValue toReturn = new KpiValue();
		
		Date beginDate = value.getBeginDt();
		Date endDate = value.getEndDt();
		String val = value.getKpiValue();
		Integer kpiInstanceID = value.getIdKpiInstanceValue();
		SbiKpiInstance kpiInst = value.getSbiKpiInstance();
		Double weight = kpiInst.getWeight();
		
		
		toReturn.setBeginDate(beginDate);
		toReturn.setEndDate(endDate);
		toReturn.setValue(val);
		toReturn.setKpiInstanceId(kpiInstanceID);
		toReturn.setWeight(weight);
		
		
		return toReturn;
	}

}
