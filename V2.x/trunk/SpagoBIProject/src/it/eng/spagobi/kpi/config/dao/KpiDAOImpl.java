package it.eng.spagobi.kpi.config.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.metadata.SbiDomains;
import it.eng.spagobi.commons.metadata.SbiExtRoles;
import it.eng.spagobi.kpi.config.bo.Kpi;
import it.eng.spagobi.kpi.config.bo.KpiInstance;
import it.eng.spagobi.kpi.config.bo.KpiValue;
import it.eng.spagobi.kpi.config.metadata.SbiKpi;
import it.eng.spagobi.kpi.config.metadata.SbiKpiInstance;
import it.eng.spagobi.kpi.config.metadata.SbiKpiInstanceHistory;
import it.eng.spagobi.kpi.config.metadata.SbiKpiPeriodicity;
import it.eng.spagobi.kpi.config.metadata.SbiKpiRole;
import it.eng.spagobi.kpi.config.metadata.SbiKpiValue;
import it.eng.spagobi.kpi.model.bo.ModelInstance;
import it.eng.spagobi.kpi.model.bo.ModelInstanceNode;
import it.eng.spagobi.kpi.model.bo.Resource;
import it.eng.spagobi.kpi.model.metadata.SbiKpiModelInst;
import it.eng.spagobi.kpi.model.metadata.SbiKpiModelResources;
import it.eng.spagobi.kpi.model.metadata.SbiResources;
import it.eng.spagobi.kpi.threshold.bo.Threshold;
import it.eng.spagobi.kpi.threshold.metadata.SbiThreshold;
import it.eng.spagobi.kpi.threshold.metadata.SbiThresholdValue;
import it.eng.spagobi.tools.dataset.bo.DataSetConfig;
import it.eng.spagobi.tools.dataset.metadata.SbiDataSetConfig;
import it.eng.spagobi.tools.distributionlist.bo.DistributionList;
import it.eng.spagobi.tools.distributionlist.bo.Email;
import it.eng.spagobi.tools.distributionlist.metadata.SbiDistributionList;
import it.eng.spagobi.tools.distributionlist.metadata.SbiDistributionListUser;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;

public class KpiDAOImpl extends AbstractHibernateDAO implements IKpiDAO {
	
	static private Logger logger = Logger.getLogger(KpiDAOImpl.class);

	public void deleteKpiValue(KpiValue value) throws EMFUserError {
		// TODO Auto-generated method stub
		
	}

	public ModelInstanceNode loadModelInstanceById(Integer id) throws EMFUserError {
		logger.debug("IN");
		ModelInstanceNode toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpiModelInst hibSbiKpiModelInst = (SbiKpiModelInst)aSession.load(SbiKpiModelInst.class,id);
			toReturn = toModelInstanceNode(hibSbiKpiModelInst);
			
		} catch (HibernateException he) {
			logger.error("Error while loading the Model Instance with id " + ((id == null)?"":id.toString()), he);			

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 9104);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
				logger.debug("OUT");
			}
		}
		logger.debug("OUT");
		return toReturn;
	}
	
	public SbiKpiInstance loadSbiKpiInstanceById(Integer id) throws EMFUserError {
		logger.debug("IN");
		SbiKpiInstance toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpiInstance hibSbiKpiInstance = (SbiKpiInstance)aSession.load(SbiKpiInstance.class,id);
			toReturn = hibSbiKpiInstance;
			
		} catch (HibernateException he) {
			logger.error("Error while loading the Model Instance with id " + ((id == null)?"":id.toString()), he);			

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 9104);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
				logger.debug("OUT");
			}
		}
		logger.debug("OUT");
		return toReturn;
	}
	
	public KpiInstance loadKpiInstanceById(Integer id) throws EMFUserError {
		logger.debug("IN");
		KpiInstance toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpiInstance hibSbiKpiInstance = (SbiKpiInstance)aSession.load(SbiKpiInstance.class,id);
			toReturn = toKpiInstance(hibSbiKpiInstance);
			
		} catch (HibernateException he) {
			logger.error("Error while loading the Model Instance with id " + ((id == null)?"":id.toString()), he);			

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 9104);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
				logger.debug("OUT");
			}
		}
		logger.debug("OUT");
		return toReturn;
	}
	
	public SbiKpi loadSbiKpiById(Integer id) throws EMFUserError {
		logger.debug("IN");
		SbiKpi toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpi hibSbiKpiInstance = (SbiKpi)aSession.load(SbiKpi.class,id);
			toReturn = hibSbiKpiInstance;
			
		} catch (HibernateException he) {
			logger.error("Error while loading the Model Instance with id " + ((id == null)?"":id.toString()), he);			

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 9104);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
				logger.debug("OUT");
			}
		}
		logger.debug("OUT");
		return toReturn;
	}
	
	public Kpi loadKpiById(Integer id) throws EMFUserError {
		logger.debug("IN");
		Kpi toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpi hibSbiKpiInstance = (SbiKpi)aSession.load(SbiKpi.class,id);
			toReturn = toKpi(hibSbiKpiInstance);
			
		} catch (HibernateException he) {
			logger.error("Error while loading the Model Instance with id " + ((id == null)?"":id.toString()), he);			

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 9104);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
				logger.debug("OUT");
			}
		}
		logger.debug("OUT");
		return toReturn;
	}
	
	public List getKpiActualValue(KpiInstance kpi) throws EMFUserError {
		// TODO Auto-generated method stub
		return null;
	}

	public List getKpiValue(KpiInstance kpi, Date d) throws EMFUserError {
		
		logger.debug("IN");
		 
		Integer kpiInstID = kpi.getKpiInstanceId();
		Session aSession = null;
		Transaction tx = null;
		SbiKpiInstance hibKpiInstance = null;
		List values = new ArrayList();

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			hibKpiInstance = (SbiKpiInstance)aSession.load(SbiKpiInstance.class, kpiInstID);
			Set kpiValues = hibKpiInstance.getSbiKpiValues();
			
			 Iterator iVa = kpiValues.iterator();
				while(iVa.hasNext()){
					SbiKpiValue value =(SbiKpiValue) iVa.next();
					Date kpiValueBegDt = value.getBeginDt();
					Date kpiValueEndDt = value.getEndDt();
					if (d.after(kpiValueBegDt) && d.before(kpiValueEndDt)){
					KpiValue val = toKpiValue(value,d);					
						values.add(val);
					}				
				}
				
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
		return values;
	}
	
	public List getKpiValue(SbiKpiInstance kpi, Date d) throws EMFUserError {
		
		logger.debug("IN");
		 
		Integer kpiInstID = kpi.getIdKpiInstance();
		Session aSession = null;
		Transaction tx = null;
		SbiKpiInstance hibKpiInstance = null;
		List values = new ArrayList();

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			hibKpiInstance = (SbiKpiInstance)aSession.load(SbiKpiInstance.class, kpiInstID);
			Set kpiValues = hibKpiInstance.getSbiKpiValues();
			
			 Iterator iVa = kpiValues.iterator();
				while(iVa.hasNext()){
					SbiKpiValue value =(SbiKpiValue) iVa.next();
					Date kpiValueBegDt = value.getBeginDt();
					Date kpiValueEndDt = value.getEndDt();
					if (d.after(kpiValueBegDt) && d.before(kpiValueEndDt)){
					KpiValue val = toKpiValue(value,d);					
						values.add(val);
					}				
				}
				
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
		return values;
	}


	public void insertKpiValue(KpiValue value) throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpiValue hibKpiValue = new SbiKpiValue();
			Date beginDt = value.getBeginDate();
			Date endDt = value.getEndDate();
			String kpiValue = value.getValue().toString();
			Integer kpiInstanceId = value.getKpiInstanceId();
			SbiKpiInstance sbiKpiInstance = (SbiKpiInstance)aSession.load(SbiKpiInstance.class,kpiInstanceId);
			Resource r = value.getR();
			SbiResources sbiResources = toSbiResource(r);
			
			hibKpiValue.setBeginDt(beginDt);
			hibKpiValue.setEndDt(endDt);
			hibKpiValue.setValue(kpiValue);
			hibKpiValue.setSbiKpiInstance(sbiKpiInstance);
			hibKpiValue.setSbiResources(sbiResources);
			
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
	
	public List getThresholds(KpiInstance k)throws EMFUserError{
		
		logger.debug("IN");
		List thresholds = new ArrayList();
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpiInstance hibSbiKpiInstance = (SbiKpiInstance)aSession.load(SbiKpiInstance.class,k.getKpiInstanceId());
			SbiThreshold t = hibSbiKpiInstance.getSbiThreshold();
			
			Set thresholdValues = t.getSbiThresholdValues();
			Iterator it = thresholdValues.iterator();
			while (it.hasNext()){
				SbiThresholdValue val = (SbiThresholdValue) it.next();
				Threshold tr = toThreshold(val);
				thresholds.add(tr);
			}
			
		} catch (HibernateException he) {
			logger.error("Error while loading the Model Instance with id " , he);			

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 9104);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
				logger.debug("OUT");
			}
		}
		logger.debug("OUT");
		return thresholds;
	}
	
	public Date calculateEndDate(KpiValue value, Date beginDate) throws EMFUserError {
		logger.debug("IN");
		Date toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Integer kpiInstanceID = value.getKpiInstanceId();
			SbiKpiInstance instK = loadSbiKpiInstanceById(kpiInstanceID);
			Set periodicity = instK.getSbiKpiPeriodicities();
			//TODO continuare
			
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
		return toReturn;
	}

	public boolean isActualValue(KpiValue value) throws EMFUserError {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean hasActualValues(KpiInstance inst, Date d) throws EMFUserError {

		boolean toReturn = false ;
		//I verify if effectively the required date is in the 
		Date instBegDt = inst.getD();
		if (d.after(instBegDt)){
			toReturn = true ;
		}	
		List values = inst.getValues();
		if (values.isEmpty()){
			toReturn = false ;
		}
		return toReturn;
	}

	public boolean isAlarmingValue(KpiValue value) throws EMFUserError {
		// TODO Auto-generated method stub
		return false;
	}

	public List listKpiValues(KpiInstance kpi) throws EMFUserError {
		// TODO Auto-generated method stub
		return null;
	}
	
	private SbiKpiValue toSbiKpiValue(KpiValue value) throws EMFUserError{
		
		SbiKpiValue hibKpiValue = new SbiKpiValue();
		Date beginDt = value.getBeginDate();
		Date endDt = value.getEndDate();
		String kpiValue = value.getValue().toString();
		Integer kpiInstanceId = value.getKpiInstanceId();
		SbiKpiInstance sbiKpiInstance = loadSbiKpiInstanceById(kpiInstanceId);
		Resource r = value.getR();
		SbiResources sbiResources = toSbiResource(r);
		
		hibKpiValue.setBeginDt(beginDt);
		hibKpiValue.setEndDt(endDt);
		hibKpiValue.setValue(kpiValue);
		hibKpiValue.setIdKpiInstanceValue(kpiInstanceId);
		hibKpiValue.setSbiKpiInstance(sbiKpiInstance);
		hibKpiValue.setSbiResources(sbiResources);
		
		return hibKpiValue;
	}
	
	private KpiValue toKpiValue(SbiKpiValue value,Date d){
		
		KpiValue toReturn = new KpiValue();
		
		Date beginDate = value.getBeginDt();
		Date endDate = value.getEndDt();
		String val = value.getValue();
		Integer kpiInstanceID = value.getIdKpiInstanceValue();
		SbiKpiInstance kpiInst = value.getSbiKpiInstance();
		Double weight = kpiInst.getWeight();
		SbiResources res = value.getSbiResources();
		Resource r = toResource(res);
		List thresholds = new ArrayList();
		SbiKpiInstance ski = value.getSbiKpiInstance();
		Date kpiInstBegDt = ski.getBeginDt();
		//in case the current threshold is correct
		if (d.before(endDate)&& d.after(beginDate)){
			
			SbiThreshold t = ski.getSbiThreshold();
			Set ts = t.getSbiThresholdValues();
			 Iterator i = ts.iterator();
				while(i.hasNext()){
					SbiThresholdValue tls =(SbiThresholdValue) i.next();
					Threshold tr = toThreshold(tls);
					thresholds.add(tr);
				}
						
		}else{//in case older thresholds have to be retrieved
			
			Set kpiInstHist = ski.getSbiKpiInstanceHistories();
			Iterator i = kpiInstHist.iterator();
			while(i.hasNext()){
				SbiKpiInstanceHistory ih =(SbiKpiInstanceHistory) i.next();
				Date ihBegDt = ih.getBeginDt();
				Date ihEndDt = ih.getEndDt();
				if (d.after(ihBegDt) && d.before(ihEndDt)){
					
					SbiThreshold t = ih.getSbiThreshold();
					Set ts = t.getSbiThresholdValues();
					 Iterator it = ts.iterator();
						while(it.hasNext()){
							SbiThresholdValue tls =(SbiThresholdValue) it.next();
							Threshold tr = toThreshold(tls);
							thresholds.add(tr);
						}
				}				
			}
		}		
		
		toReturn.setBeginDate(beginDate);
		toReturn.setEndDate(endDate);
		toReturn.setValue(val);
		toReturn.setKpiInstanceId(kpiInstanceID);
		toReturn.setWeight(weight);	
		toReturn.setR(r);
		//toReturn.setScaleCode(scaleCode);
		//toReturn.setScaleName(scaleName);
		toReturn.setThresholds(thresholds);
		
		return toReturn;
	}
	
	
	private ModelInstanceNode toModelInstanceNode(SbiKpiModelInst hibSbiKpiModelInst) throws EMFUserError{
		
		ModelInstanceNode toReturn = new ModelInstanceNode();
				
		 String descr = hibSbiKpiModelInst.getDescription();
		 String name = hibSbiKpiModelInst.getName();
		 SbiKpiInstance kpiInst = hibSbiKpiModelInst.getSbiKpiInstance();
		 KpiInstance kpiInstanceAssociated = toKpiInstance(kpiInst);
		 Set resources = hibSbiKpiModelInst.getSbiKpiModelResourceses();
		 List res = new ArrayList();
		 Iterator i = resources.iterator();
			while(i.hasNext()){
				SbiKpiModelResources dls =(SbiKpiModelResources) i.next();
				Resource r = toResource(dls);
				res.add(r);
			}
		//gets father id
		 SbiKpiModelInst father = hibSbiKpiModelInst.getSbiKpiModelInst();
		 Integer fatherId = null;
		 Boolean isRoot = false;
		 if (father!=null){
			 
			 fatherId = father.getKpiModelInst();
		 }else{
			 isRoot = true;
		 }
		 
		 //gets list of children id
		 Set children = hibSbiKpiModelInst.getSbiKpiModelInsts();
		 List childrenIds = new ArrayList();
		 Iterator iCI = childrenIds.iterator();
			while(iCI.hasNext()){
				SbiKpiModelInst skml =(SbiKpiModelInst) iCI.next();
				Integer childId = skml.getKpiModelInst();
				childrenIds.add(childId);
			}
		
		
		
		toReturn.setDescr(descr);
		toReturn.setName(name);
		toReturn.setKpiInstanceAssociated(kpiInstanceAssociated);
		toReturn.setResources(res);	
		toReturn.setFatherId(fatherId);
		//toReturn.setModelReference(reference);TODO aspettare che ci sia il legame nel db
		toReturn.setIsRoot(isRoot);		
		toReturn.setChildren(childrenIds);	
		
		return toReturn;
	}
	
	private KpiInstance toKpiInstance(SbiKpiInstance kpiInst) throws EMFUserError{
		
		KpiInstance toReturn = new KpiInstance();
		Integer kpiId = kpiInst.getIdKpiInstance();
		SbiKpi kpi = kpiInst.getSbiKpi();
		Integer k = kpi.getKpiId();
		Date d = new Date();
		d = kpiInst.getBeginDt();
		List values = getKpiValue(kpiInst, d);	
		//TODO da cambiare quando ci sarà la FK corretta
		Set periodicities = kpiInst.getSbiKpiPeriodicities();
		Iterator periodIt = periodicities.iterator();
		SbiKpiPeriodicity period =(SbiKpiPeriodicity)periodIt.next();
		Integer seconds = period.getValue();
		
		toReturn.setKpiInstanceId(kpiId);
		toReturn.setKpi(k);	
		toReturn.setValues(values);
		toReturn.setD(d);
		toReturn.setPeriodicity(seconds);
		
		return toReturn;
	}
	
	private Resource toResource(SbiResources r){
		
		Resource toReturn = new Resource();
		
		String coumn_name = r.getColumnName();
		String name = r.getResourceName();
		String table_name = r.getTableName();
		SbiDomains d = r.getSbiDomains();
		String type = d.getValueCd();	
		Integer resourceId = r.getResourceId();
		
		toReturn.setColumn_name(coumn_name);
		toReturn.setName(name);
		toReturn.setTable_name(table_name);
		toReturn.setType(type);
		toReturn.setId(resourceId);
		
		return toReturn;
	}
	
	private Resource toResource(SbiKpiModelResources re){
		
		Resource toReturn = new Resource();
		
		SbiResources r = re.getSbiResources();
		toReturn = toResource(r);
		
		return toReturn;
	}
	
	private SbiResources toSbiResource(Resource r) throws EMFUserError{
		
		SbiResources toReturn = new SbiResources();
		String columnName = r.getColumn_name();
		String resourceName = r.getName();
		String tableName = r.getTable_name();
		Integer resourceId = r.getId();
		String type = r.getType();
		Domain domain = DAOFactory.getDomainDAO().loadDomainByCodeAndValue("RESOURCE",type);
		SbiDomains sbiDomains = new SbiDomains();
		sbiDomains.setDomainCd(domain.getDomainCode());
		sbiDomains.setDomainNm(domain.getDomainName());
		sbiDomains.setValueCd(domain.getValueCd());
		sbiDomains.setValueDs(domain.getValueDescription());
		sbiDomains.setValueId(domain.getValueId());
		sbiDomains.setValueNm(domain.getValueName());
		
		toReturn.setColumnName(columnName);
		toReturn.setResourceId(resourceId);
		toReturn.setResourceName(resourceName);
		toReturn.setSbiDomains(sbiDomains);
		toReturn.setTableName(tableName);
		
		return toReturn;
	}
	
	public Kpi toKpi(SbiKpi kpi) throws EMFUserError{
		
		Kpi toReturn = new Kpi();
		
		String description = kpi.getDescription();
		String documentLabel = kpi.getDocumentLabel();
		Boolean isParent = false;
		if (kpi.getFlgIsFather().equals(new Character('T'))){
			isParent = true;
		}
		Integer kpiId = kpi.getKpiId();		
		String kpiName = kpi.getName();
		SbiDataSetConfig dsC = kpi.getSbiDataSet();
		DataSetConfig ds = DAOFactory.getDataSetDAO().loadDataSetByID(dsC.getDsId());
		Set kpiRoles = kpi.getSbiKpiRoles();
		List roles = new ArrayList();
		 Iterator i = kpiRoles.iterator();
			while(i.hasNext()){
				SbiKpiRole dls =(SbiKpiRole) i.next();
				SbiExtRoles extR = dls.getSbiExtRoles();
				roles.add(extR);
			}
		
		SbiThreshold thresh = kpi.getSbiThreshold();
		Set threshValues = thresh.getSbiThresholdValues();
		List thresholds = new ArrayList();
		 Iterator iTre = threshValues.iterator();
			while(iTre.hasNext()){
				SbiThresholdValue trs =(SbiThresholdValue) iTre.next();
				Threshold t = toThreshold(trs);
				thresholds.add(t);
			}
			
		Double standardWeight = kpi.getWeight();
		Set kInstances = kpi.getSbiKpiInstances();
		List KpiInstances = new ArrayList();
		Iterator iKI = kInstances.iterator();
			while(iKI.hasNext()){
				SbiKpiInstance kpiI =(SbiKpiInstance) iKI.next();
				KpiInstance kI = toKpiInstance(kpiI);
				KpiInstances.add(kI);
			}
			
		//Gets the father 
		SbiKpi dad = kpi.getSbiKpi();
		Boolean isRoot = false;
		Integer father = null;
		if (dad!=null){
			father = dad.getKpiId();
		}else {
			isRoot = true;
		}
		
		//Gets the children 
		Set children = kpi.getSbiKpis();
		List kpiChildren = new ArrayList();
		Iterator iKC = children.iterator();
			while(iKC.hasNext()){
				SbiKpi kCh =(SbiKpi) iKC.next();
				Integer kI = kCh.getKpiId();
				kpiChildren.add(kI);
			}
		 
		//String metric = kpi.getMetric();	
		//kpi.getSbiMeasureUnit();
		
		
		toReturn.setDescription(description);
		toReturn.setDocumentLabel(documentLabel);
		toReturn.setFather(father);
		toReturn.setIsParent(isParent);
		toReturn.setIsRoot(isRoot);
		toReturn.setKpiChildren(kpiChildren);
		toReturn.setKpiDs(ds);
		toReturn.setKpiId(kpiId);
		toReturn.setKpiInstances(KpiInstances);
		toReturn.setKpiName(kpiName);
		toReturn.setRoles(roles);
		toReturn.setStandardWeight(standardWeight);
		toReturn.setThresholds(thresholds);
		//toReturn.setMetric(metric);
		//toReturn.setScaleCode(scaleCode);
		//toReturn.setScaleName(scaleName);
		
		return toReturn;
	}
	
	public Threshold toThreshold(SbiThresholdValue t){
		
		Threshold toReturn = new Threshold();

		String label = t.getLabel();
		Integer id = t.getIdThresholdValue();
		Double maxValue = t.getMaxValue();
		Double minValue = t.getMinValue();
		Integer position = t.getPosition();
		SbiDomains d = t.getSbiDomains();
		String severity = d.getValueCd();
		SbiThreshold sbit = t.getSbiThreshold();
		String thresholdName = sbit.getName();
		String thresholdDescription = sbit.getDescription();
		String type = sbit.getSbiDomains().getValueCd();
		//TODO usare il colore vero
		String col = t.getColour();		
		Color color = new Color(255, 153, 0);
		
		toReturn.setColor(color);
		toReturn.setId(id);
		toReturn.setLabel(label);
		toReturn.setMaxValue(maxValue);
		toReturn.setMinValue(minValue);
		toReturn.setPosition(position);
		toReturn.setSeverity(severity);
		toReturn.setThresholdDescription(thresholdDescription);
		toReturn.setThresholdName(thresholdName);
		toReturn.setType(type);
		
		return toReturn;
	}

}
