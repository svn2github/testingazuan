package it.eng.spagobi.kpi.model.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.metadata.SbiDomains;
import it.eng.spagobi.kpi.config.bo.KpiInstance;
import it.eng.spagobi.kpi.config.metadata.SbiKpi;
import it.eng.spagobi.kpi.config.metadata.SbiKpiInstance;
import it.eng.spagobi.kpi.config.metadata.SbiKpiInstanceHistory;
import it.eng.spagobi.kpi.config.metadata.SbiKpiPeriodicity;
import it.eng.spagobi.kpi.model.bo.Model;
import it.eng.spagobi.kpi.model.bo.ModelInstance;
import it.eng.spagobi.kpi.model.metadata.SbiKpiModel;
import it.eng.spagobi.kpi.model.metadata.SbiKpiModelInst;
import it.eng.spagobi.kpi.model.dao.ModelDAOImpl;
import it.eng.spagobi.kpi.threshold.metadata.SbiThreshold;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;

public class ModelInstanceDAOImpl extends AbstractHibernateDAO implements
		IModelInstanceDAO {

	static private Logger logger = Logger.getLogger(ModelInstanceDAOImpl.class);

	public List loadModelsInstanceRoot() throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		List toReturn = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Criteria crit = aSession.createCriteria(SbiKpiModelInst.class);
			crit.add(Expression.isNull("sbiKpiModelInst"));
			List sbiKpiModelInstanceList = crit.list();
			for (Iterator iterator = sbiKpiModelInstanceList.iterator(); iterator
					.hasNext();) {
				SbiKpiModelInst sbiKpiModelInst = (SbiKpiModelInst) iterator
						.next();
				ModelInstance aModelInst = toModelInstanceWithoutChildren(
						sbiKpiModelInst, aSession);
				toReturn.add(aModelInst);
			}
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 101);

		} finally {
			if (aSession != null) {
				if (aSession.isOpen())
					aSession.close();
			}
		}
		logger.debug("OUT");
		return toReturn;

	}

	private ModelInstance toModelInstanceWithoutChildren(SbiKpiModelInst value,
			Session aSession) {
		logger.debug("IN");
		ModelInstance toReturn = new ModelInstance();

		String name = value.getName();
		String description = value.getDescription();
		Integer id = value.getKpiModelInst();
		SbiKpiModel sbiKpiModel = value.getSbiKpiModel();
		Model aModel = ModelDAOImpl.toModelWithoutChildren(sbiKpiModel,
				aSession);
		SbiKpiInstance sbiKpiInstance = value.getSbiKpiInstance();

		if (sbiKpiInstance != null) {
			// toKpiInstance
			KpiInstance aKpiInstance = new KpiInstance();
			aKpiInstance.setKpiInstanceId(sbiKpiInstance.getIdKpiInstance());
			aKpiInstance.setKpi(sbiKpiInstance.getSbiKpi().getKpiId());
			if(sbiKpiInstance.getSbiThreshold()!= null){
				aKpiInstance.setThresholdId(sbiKpiInstance.getSbiThreshold()
						.getThresholdId());
			}
			if(sbiKpiInstance.getSbiDomains()!= null){
				aKpiInstance.setChartTypeId(sbiKpiInstance.getSbiDomains()
						.getValueId());
			}
			if(sbiKpiInstance.getSbiKpiPeriodicity()!= null){
				aKpiInstance.setPeriodicityId(sbiKpiInstance.getSbiKpiPeriodicity()
						.getIdKpiPeriodicity());
			}
			aKpiInstance.setWeight(sbiKpiInstance.getWeight());
			aKpiInstance.setD(sbiKpiInstance.getBeginDt());
			//
			toReturn.setKpiInstance(aKpiInstance);
		}

		toReturn.setId(id);
		toReturn.setName(name);
		toReturn.setDescription(description);
		toReturn.setModel(aModel);

		logger.debug("OUT");
		return toReturn;
	}

	public ModelInstance loadModelInstanceWithoutChildrenById(Integer id)
			throws EMFUserError {
		logger.debug("IN");
		ModelInstance toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpiModelInst hibSbiKpiModelInstance = (SbiKpiModelInst) aSession
					.load(SbiKpiModelInst.class, id);
			toReturn = toModelInstanceWithoutChildren(hibSbiKpiModelInstance,
					aSession);

		} catch (HibernateException he) {
			logger.error("Error while loading the Model Instance with id "
					+ ((id == null) ? "" : id.toString()), he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 101);

		} finally {
			if (aSession != null) {
				if (aSession.isOpen())
					aSession.close();
				logger.debug("OUT");
			}
		}
		logger.debug("OUT");
		return toReturn;
	}

	public void modifyModelInstance(ModelInstance value) throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Integer kpiModelInstanceId = value.getId();
			String kpiModelInstanceDesc = value.getDescription();
			String kpiModelInstanceNm = value.getName();

			SbiKpiModelInst sbiKpiModelInst = (SbiKpiModelInst) aSession.load(
					SbiKpiModelInst.class, kpiModelInstanceId);
			sbiKpiModelInst.setDescription(kpiModelInstanceDesc);
			sbiKpiModelInst.setName(kpiModelInstanceNm);
			
			SbiKpiInstance sbiKpiInstance = sbiKpiModelInst.getSbiKpiInstance();
			boolean newKpiInstanceHistory = true;
			

			//TODO creazione di KPIINstance solo se KpiID != null
			if(sbiKpiInstance == null ){
				sbiKpiInstance = new SbiKpiInstance();
				Calendar now = Calendar.getInstance();
				sbiKpiInstance.setBeginDt(now.getTime());
				newKpiInstanceHistory = false;
			}
			
			if(sbiKpiInstance.getSbiKpi() == null) {
				//TODO eliminazione KpiInstanceHistory se KPIID cambia
				newKpiInstanceHistory = false;
			}

			if (newKpiInstanceHistory && 
					!( 
					(
					  areBothNull(sbiKpiInstance.getSbiKpi(),value.getKpiInstance().getKpi())
					|| 
					  (sbiKpiInstance.getSbiKpi()!= null && areNullOrEquals(sbiKpiInstance.getSbiKpi().getKpiId(), value.getKpiInstance().getKpi()))
					)
					&&
					(
					  areBothNull(sbiKpiInstance.getSbiThreshold(),value.getKpiInstance().getThresholdId())
					||
					 (sbiKpiInstance.getSbiThreshold()!=null && areNullOrEquals(sbiKpiInstance.getSbiThreshold().getThresholdId(), value.getKpiInstance().getThresholdId()))
					)
					&&
					(
					  areBothNull(sbiKpiInstance.getSbiDomains(),value.getKpiInstance().getChartTypeId())
					||  
					  (sbiKpiInstance.getSbiDomains()!=null && areNullOrEquals(sbiKpiInstance.getSbiDomains().getValueId(), value.getKpiInstance().getChartTypeId()))
					)
					&&
					(
					  areBothNull(sbiKpiInstance.getSbiKpiPeriodicity(),value.getKpiInstance().getPeriodicityId())
					||
					  (sbiKpiInstance.getSbiKpiPeriodicity()!=null && areNullOrEquals(sbiKpiInstance.getSbiKpiPeriodicity().getIdKpiPeriodicity(), value.getKpiInstance().getPeriodicityId()))
					)
					&& areNullOrEquals(sbiKpiInstance.getWeight(),value.getKpiInstance().getWeight()))){
				
				Calendar now = Calendar.getInstance();
				SbiKpiInstanceHistory sbiKpiInstanceHistory = new SbiKpiInstanceHistory();
				sbiKpiInstanceHistory.setSbiKpiInstance(sbiKpiInstance);
				sbiKpiInstanceHistory.setSbiThreshold(sbiKpiInstance.getSbiThreshold());
				sbiKpiInstanceHistory.setSbiDomains(sbiKpiInstance.getSbiDomains());
				sbiKpiInstanceHistory.setWeight(sbiKpiInstance.getWeight());
				sbiKpiInstanceHistory.setBeginDt(sbiKpiInstance.getBeginDt());
				sbiKpiInstanceHistory.setEndDt(now.getTime());
				aSession.save(sbiKpiInstanceHistory);
			}
			
			if (value.getKpiInstance().getKpi()!= null)
				sbiKpiInstance.setSbiKpi((SbiKpi) aSession.load(SbiKpi.class, value.getKpiInstance().getKpi()));
			// else elimnia tutto..
			if (value.getKpiInstance().getThresholdId()!= null)
			sbiKpiInstance.setSbiThreshold((SbiThreshold)aSession.load(
					SbiThreshold.class, value.getKpiInstance().getThresholdId()));
			else{
				sbiKpiInstance.setSbiThreshold(null);
			}
			if(value.getKpiInstance().getChartTypeId()!= null)
			sbiKpiInstance.setSbiDomains((SbiDomains)aSession.load(
					SbiDomains.class, value.getKpiInstance().getChartTypeId()));
			else{
				sbiKpiInstance.setSbiDomains(null);	
			}
			if (value.getKpiInstance().getPeriodicityId()!= null)
			sbiKpiInstance.setSbiKpiPeriodicity((SbiKpiPeriodicity)aSession.load(
					SbiKpiPeriodicity.class, value.getKpiInstance().getPeriodicityId()));
			else{
				sbiKpiInstance.setSbiKpiPeriodicity(null);
			}
			
			sbiKpiInstance.setWeight(value.getKpiInstance().getWeight());
			
			aSession.saveOrUpdate(sbiKpiInstance);
			sbiKpiModelInst.setSbiKpiInstance(sbiKpiInstance);
			
			aSession.update(sbiKpiModelInst);
			tx.commit();

		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 101);

		} finally {
			if (aSession != null) {
				if (aSession.isOpen())
					aSession.close();
			}
		}
		logger.debug("OUT");
	}
	

	private boolean areBothNull(Object a, Object b){
		boolean toReturn = false;
		if(a == null && b == null)
			toReturn = true;
		return toReturn;
	}
	
	private boolean areNullOrEquals(Object a, Object b){
		boolean toReturn = false;
		if(a == null && b == null)
			toReturn = true;
		else
			toReturn = false;
		
		if(!toReturn && a != null && b !=null && a.equals(b))
			toReturn = true;
		return toReturn;
	}

	public Integer insertModelInstance(ModelInstance toCreate)
			throws EMFUserError {
		logger.debug("IN");
		Integer idToReturn;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			Integer parentId = toCreate.getParentId();
			// set the sbiKpiModel
			SbiKpiModelInst sbiKpiModelInst = new SbiKpiModelInst();
			sbiKpiModelInst.setName(toCreate.getName());
			sbiKpiModelInst.setDescription(toCreate.getDescription());
			Model aModel = toCreate.getModel();
			if (aModel != null && aModel.getId() != null) {
				SbiKpiModel sbiKpiModel = (SbiKpiModel) aSession.load(
						SbiKpiModel.class, aModel.getId());
				sbiKpiModelInst.setSbiKpiModel(sbiKpiModel);

				// set the sbiKpiInstance
				SbiKpi sbiKpi = sbiKpiModel.getSbiKpi();
				if (sbiKpi != null) {
					SbiKpiInstance sbiKpiInstance = new SbiKpiInstance();
					sbiKpiInstance.setSbiKpi(sbiKpi);
					sbiKpiInstance.setSbiThreshold(sbiKpi.getSbiThreshold());
					sbiKpiInstance.setWeight(sbiKpi.getWeight());
					Calendar now = Calendar.getInstance();
					sbiKpiInstance.setBeginDt(now.getTime());
					aSession.save(sbiKpiInstance);
					sbiKpiModelInst.setSbiKpiInstance(sbiKpiInstance);
				}

			}
			if (parentId != null) {
				SbiKpiModelInst sbiKpiModelInstParent = (SbiKpiModelInst) aSession
						.load(SbiKpiModelInst.class, parentId);
				sbiKpiModelInst.setSbiKpiModelInst(sbiKpiModelInstParent);
			}

			idToReturn = (Integer) aSession.save(sbiKpiModelInst);

			tx.commit();

		} catch (HibernateException he) {
			logException(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 101);

		} finally {
			if (aSession != null) {
				if (aSession.isOpen())
					aSession.close();
			}
		}
		logger.debug("OUT");
		return idToReturn;

	}

	public ModelInstance loadModelInstanceWithChildrenById(Integer id)
			throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		ModelInstance toReturn = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpiModelInst hibSbiKpiModelInst = (SbiKpiModelInst) aSession
					.load(SbiKpiModelInst.class, id);
			toReturn = toModelInstanceWithChildren(hibSbiKpiModelInst, null);
		} catch (HibernateException he) {
			logger.error("Error while loading the ModelInstance with id "
					+ ((id == null) ? "" : id.toString()), he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 101);

		} finally {
			if (aSession != null) {
				if (aSession.isOpen())
					aSession.close();
				logger.debug("OUT");
			}
		}
		logger.debug("OUT");
		return toReturn;

	}

	private ModelInstance toModelInstanceWithChildren(SbiKpiModelInst value,
			Integer parentId) {
		logger.debug("IN");
		ModelInstance toReturn = new ModelInstance();
		String name = value.getName();
		String description = value.getDescription();
		Integer id = value.getKpiModelInst();

		List childrenNodes = new ArrayList();

		Set children = value.getSbiKpiModelInsts();
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			SbiKpiModelInst sbiKpichild = (SbiKpiModelInst) iterator.next();
			ModelInstance child = toModelInstanceWithChildren(sbiKpichild, id);
			childrenNodes.add(child);
		}

		toReturn.setId(id);
		toReturn.setName(name);
		toReturn.setDescription(description);
		toReturn.setChildrenNodes(childrenNodes);
		toReturn.setParentId(parentId);

		logger.debug("OUT");
		return toReturn;
	}

	public List getCandidateModelChildren(Integer parentId) throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		List toReturn = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpiModelInst sbiKpiModelInst = (SbiKpiModelInst) aSession.load(
					SbiKpiModelInst.class, parentId);
			SbiKpiModel aModel = (SbiKpiModel) sbiKpiModelInst.getSbiKpiModel();

			// Load all Children
			if (aModel != null) {
				Set modelChildren = aModel.getSbiKpiModels();
				// Load all ModelInstance Children
				Set modelInstanceChildren = sbiKpiModelInst
						.getSbiKpiModelInsts();
				// Remove all Children just instantiated
				for (Iterator iterator = modelInstanceChildren.iterator(); iterator
						.hasNext();) {
					SbiKpiModelInst child = (SbiKpiModelInst) iterator.next();
					modelChildren.remove(child.getSbiKpiModel());
				}
				for (Iterator iterator = modelChildren.iterator(); iterator
						.hasNext();) {
					SbiKpiModel sbiKpiModelCandidate = (SbiKpiModel) iterator
							.next();
					Model modelCandidate = new Model();
					modelCandidate.setId(sbiKpiModelCandidate.getKpiModelId());
					modelCandidate
							.setName(sbiKpiModelCandidate.getKpiModelNm());
					toReturn.add(modelCandidate);
				}
			}

		} catch (HibernateException he) {
			logger.error(
					"Error while loading the model canidate children of the parent "
							+ ((parentId == null) ? "" : parentId.toString()),
					he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 101);

		} finally {
			if (aSession != null) {
				if (aSession.isOpen())
					aSession.close();
				logger.debug("OUT");
			}
		}
		logger.debug("OUT");
		return toReturn;

	}

}
