package it.eng.spagobi.kpi.model.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.metadata.SbiDomains;
import it.eng.spagobi.kpi.config.bo.KpiInstance;
import it.eng.spagobi.kpi.config.metadata.SbiKpi;
import it.eng.spagobi.kpi.config.metadata.SbiKpiInstPeriod;
import it.eng.spagobi.kpi.config.metadata.SbiKpiInstance;
import it.eng.spagobi.kpi.config.metadata.SbiKpiInstanceHistory;
import it.eng.spagobi.kpi.config.metadata.SbiKpiPeriodicity;
import it.eng.spagobi.kpi.config.metadata.SbiKpiValue;
import it.eng.spagobi.kpi.model.bo.Model;
import it.eng.spagobi.kpi.model.bo.ModelInstance;
import it.eng.spagobi.kpi.model.metadata.SbiKpiModel;
import it.eng.spagobi.kpi.model.metadata.SbiKpiModelInst;
import it.eng.spagobi.kpi.threshold.metadata.SbiThreshold;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.exception.ConstraintViolationException;

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
		String label = value.getLabel();
		Date startDate = value.getStartDate();
		Date endDate = value.getEndDate();
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
			if (sbiKpiInstance.getSbiThreshold() != null) {
				aKpiInstance.setThresholdId(sbiKpiInstance.getSbiThreshold()
						.getThresholdId());
			}
			if (sbiKpiInstance.getSbiDomains() != null) {
				aKpiInstance.setChartTypeId(sbiKpiInstance.getSbiDomains()
						.getValueId());
			}
			// TODO
			if (sbiKpiInstance.getSbiKpiInstPeriods() != null && !(sbiKpiInstance.getSbiKpiInstPeriods().isEmpty())) {
				SbiKpiInstPeriod instPeriod = (SbiKpiInstPeriod) sbiKpiInstance
						.getSbiKpiInstPeriods().toArray()[0];

				aKpiInstance.setPeriodicityId(instPeriod.getSbiKpiPeriodicity()
						.getIdKpiPeriodicity());
			} //
			aKpiInstance.setWeight(sbiKpiInstance.getWeight());
			aKpiInstance.setTarget(sbiKpiInstance.getTarget());
			aKpiInstance.setD(sbiKpiInstance.getBeginDt());
			//
			toReturn.setKpiInstance(aKpiInstance);
		}

		toReturn.setId(id);
		toReturn.setName(name);
		toReturn.setDescription(description);
		toReturn.setLabel(label);
		toReturn.setStartDate(startDate);
		toReturn.setEndDate(endDate);
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
			String kpiModelInstanceLb = value.getLabel();
			Date kpiModelInstanceStartDate = value.getStartDate();
			Date kpiModelInDateEndDate = value.getEndDate();

			SbiKpiModelInst sbiKpiModelInst = (SbiKpiModelInst) aSession.load(
					SbiKpiModelInst.class, kpiModelInstanceId);
			sbiKpiModelInst.setDescription(kpiModelInstanceDesc);
			sbiKpiModelInst.setName(kpiModelInstanceNm);
			sbiKpiModelInst.setLabel(kpiModelInstanceLb);
			sbiKpiModelInst.setStartDate(kpiModelInstanceStartDate);
			sbiKpiModelInst.setEndDate(kpiModelInDateEndDate);

			SbiKpiInstance oldSbiKpiInstance = sbiKpiModelInst
					.getSbiKpiInstance();
			boolean newKpiInstanceHistory = true;
			boolean deleteOldHistory = false;

			// new kpiInstance is null
			if (value.getKpiInstance() == null) {
				newKpiInstanceHistory = false;
				deleteOldHistory = true;
			}

			// old kpiInstance is null and new kpiInstance has a value
			if (oldSbiKpiInstance == null && value.getKpiInstance() != null) {
				newKpiInstanceHistory = false;
			}

			// old kpiId is different from new kpiId
			if (newKpiInstanceHistory
					&& !(areBothNull(oldSbiKpiInstance.getSbiKpi(), value
							.getKpiInstance().getKpi()) || (oldSbiKpiInstance
							.getSbiKpi() != null && areNullOrEquals(
							oldSbiKpiInstance.getSbiKpi().getKpiId(), value
									.getKpiInstance().getKpi())))) {
				newKpiInstanceHistory = false;
				deleteOldHistory = true;
				// create new sbiKpiInstance
			}

			// check if same value is changed
			if (newKpiInstanceHistory
					&& !((areBothNull(oldSbiKpiInstance.getSbiThreshold(),
							value.getKpiInstance().getThresholdId()) || (oldSbiKpiInstance
							.getSbiThreshold() != null && areNullOrEquals(
							oldSbiKpiInstance.getSbiThreshold()
									.getThresholdId(), value.getKpiInstance()
									.getThresholdId())))
							&& (areBothNull(oldSbiKpiInstance.getSbiDomains(),
									value.getKpiInstance().getChartTypeId()) || (oldSbiKpiInstance
									.getSbiDomains() != null && areNullOrEquals(
									oldSbiKpiInstance.getSbiDomains()
											.getValueId(), value
											.getKpiInstance().getChartTypeId())))
					/*
					 * TODO && (areBothNull(oldSbiKpiInstance
					 * .getSbiKpiPeriodicity(), value
					 * .getKpiInstance().getPeriodicityId()) ||
					 * (oldSbiKpiInstance .getSbiKpiPeriodicity() != null &&
					 * areNullOrEquals( oldSbiKpiInstance.getSbiKpiPeriodicity()
					 * .getIdKpiPeriodicity(), value .getKpiInstance()
					 * .getPeriodicityId())))
					 */
					&& areNullOrEquals(oldSbiKpiInstance.getWeight(), value
							.getKpiInstance().getWeight()))) {
				// create new History
				Calendar now = Calendar.getInstance();
				SbiKpiInstanceHistory sbiKpiInstanceHistory = new SbiKpiInstanceHistory();
				sbiKpiInstanceHistory.setSbiKpiInstance(oldSbiKpiInstance);
				sbiKpiInstanceHistory.setSbiThreshold(oldSbiKpiInstance
						.getSbiThreshold());
				sbiKpiInstanceHistory.setSbiDomains(oldSbiKpiInstance
						.getSbiDomains());
				sbiKpiInstanceHistory.setWeight(oldSbiKpiInstance.getWeight());
				sbiKpiInstanceHistory
						.setBeginDt(oldSbiKpiInstance.getBeginDt());
				sbiKpiInstanceHistory.setEndDt(now.getTime());
				aSession.save(sbiKpiInstanceHistory);
			}

			SbiKpiInstance kpiInstanceToCreate = null;

			if (value.getKpiInstance() != null) {
				if (newKpiInstanceHistory) {
					kpiInstanceToCreate = setSbiKpiInstanceFromModelInstance(
							aSession, value, oldSbiKpiInstance);
				} else {
					// create new kpiInstance
					kpiInstanceToCreate = new SbiKpiInstance();
					Calendar now = Calendar.getInstance();
					kpiInstanceToCreate.setBeginDt(now.getTime());
					kpiInstanceToCreate = setSbiKpiInstanceFromModelInstance(
							aSession, value, kpiInstanceToCreate);
				}
				aSession.saveOrUpdate(kpiInstanceToCreate);
				sbiKpiModelInst.setSbiKpiInstance(kpiInstanceToCreate);
			} else {
				sbiKpiModelInst.setSbiKpiInstance(null);
			}
			aSession.update(sbiKpiModelInst);

			if (deleteOldHistory && oldSbiKpiInstance != null) {
				deleteKpiInstance(aSession, oldSbiKpiInstance
						.getIdKpiInstance());
			}

			tx.commit();

		}

		catch (org.hibernate.exception.ConstraintViolationException ce) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			logger.error("Impossible to modify a Model Instance Instance", ce);
			throw new EMFUserError(EMFErrorSeverity.WARNING, 101);

		}

		catch (HibernateException he) {
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

	private SbiKpiInstance setSbiKpiInstanceFromModelInstance(Session aSession,
			ModelInstance value, SbiKpiInstance sbiKpiInstance) {
		if (value.getKpiInstance().getKpi() != null) {
			sbiKpiInstance.setSbiKpi((SbiKpi) aSession.load(SbiKpi.class, value
					.getKpiInstance().getKpi()));
		} else {
			sbiKpiInstance.setSbiKpi(null);
		}
		if (value.getKpiInstance().getThresholdId() != null) {
			sbiKpiInstance.setSbiThreshold((SbiThreshold) aSession
					.load(SbiThreshold.class, value.getKpiInstance()
							.getThresholdId()));
		} else {
			sbiKpiInstance.setSbiThreshold(null);
		}

		if (value.getKpiInstance().getChartTypeId() != null) {
			sbiKpiInstance.setSbiDomains((SbiDomains) aSession.load(
					SbiDomains.class, value.getKpiInstance().getChartTypeId()));
		} else {
			sbiKpiInstance.setSbiDomains(null);
		}
		if (value.getKpiInstance().getPeriodicityId() != null) {
			// AGGIUNTA O AGGIORNAMENTO RIGA
			// TODO

			SbiKpiPeriodicity sbiKpiPeriodicity = (SbiKpiPeriodicity) aSession
					.load(SbiKpiPeriodicity.class, value.getKpiInstance()
							.getPeriodicityId());
			Criteria critt = aSession.createCriteria(SbiKpiInstPeriod.class);
			critt.add(Expression.eq("sbiKpiInstance", sbiKpiInstance));
			List instPeriodsList = critt.list();

			if (instPeriodsList == null || instPeriodsList.isEmpty()) {
				SbiKpiInstPeriod toInsert = new SbiKpiInstPeriod();
				toInsert.setSbiKpiInstance(sbiKpiInstance);
				toInsert.setSbiKpiPeriodicity(sbiKpiPeriodicity);
				toInsert.setDefault_(true);

				aSession.save(toInsert);

			} else {
				((SbiKpiInstPeriod)instPeriodsList.get(0)).setSbiKpiPeriodicity(sbiKpiPeriodicity);
				aSession.update(instPeriodsList.get(0));
			}

			// sbiKpiInstance.setSbiKpiPeriodicity((SbiKpiPeriodicity) aSession
			// .load(SbiKpiPeriodicity.class, value.getKpiInstance()
			// .getPeriodicityId()));
		} else {
			// RIMOZIONE DELLA RIGA DAL DB
			Set InstPeriods = sbiKpiInstance.getSbiKpiInstPeriods();
			for (Iterator iterator = InstPeriods.iterator(); iterator.hasNext();) {
				SbiKpiInstPeriod sbiKpiInstPeriod = (SbiKpiInstPeriod) iterator
						.next();
				aSession.delete(sbiKpiInstPeriod);
			}
			//
		}

		sbiKpiInstance.setWeight(value.getKpiInstance().getWeight());
		sbiKpiInstance.setTarget((value.getKpiInstance().getTarget()));
		return sbiKpiInstance;
	}

	private boolean areBothNull(Object a, Object b) {
		boolean toReturn = false;
		if (a == null && b == null)
			toReturn = true;
		return toReturn;
	}

	private boolean areNullOrEquals(Object a, Object b) {
		boolean toReturn = false;
		if (a == null && b == null)
			toReturn = true;
		else
			toReturn = false;

		if (!toReturn && a != null && b != null && a.equals(b))
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
			sbiKpiModelInst.setLabel(toCreate.getLabel());
			sbiKpiModelInst.setStartDate(toCreate.getStartDate());
			sbiKpiModelInst.setEndDate(toCreate.getEndDate());
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
			toReturn = toModelInstanceWithChildren(aSession,
					hibSbiKpiModelInst, null);
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

	private ModelInstance toModelInstanceWithChildren(Session session,
			SbiKpiModelInst value, Integer parentId) {
		logger.debug("IN");
		ModelInstance toReturn = new ModelInstance();
		String name = value.getName();
		String description = value.getDescription();
		String label = value.getLabel();
		Date startDate = value.getStartDate();
		Date endDate = value.getEndDate();
		Integer id = value.getKpiModelInst();
		SbiKpiModel sbiKpiModel = value.getSbiKpiModel();
		Model aModel = ModelDAOImpl
				.toModelWithoutChildren(sbiKpiModel, session);
		SbiKpiInstance sbiKpiInstance = value.getSbiKpiInstance();

		if (sbiKpiInstance != null) {
			// toKpiInstance
			KpiInstance aKpiInstance = new KpiInstance();
			aKpiInstance.setKpiInstanceId(sbiKpiInstance.getIdKpiInstance());
			aKpiInstance.setKpi(sbiKpiInstance.getSbiKpi().getKpiId());
			if (sbiKpiInstance.getSbiThreshold() != null) {
				aKpiInstance.setThresholdId(sbiKpiInstance.getSbiThreshold()
						.getThresholdId());
			}
			if (sbiKpiInstance.getSbiDomains() != null) {
				aKpiInstance.setChartTypeId(sbiKpiInstance.getSbiDomains()
						.getValueId());
			}
			// TODO
			if (sbiKpiInstance.getSbiKpiInstPeriods() != null && !(sbiKpiInstance.getSbiKpiInstPeriods().isEmpty())) {
				SbiKpiInstPeriod instPeriod = (SbiKpiInstPeriod) sbiKpiInstance
						.getSbiKpiInstPeriods().toArray()[0];

				aKpiInstance.setPeriodicityId(instPeriod.getSbiKpiPeriodicity()
						.getIdKpiPeriodicity());
			}
			aKpiInstance.setWeight(sbiKpiInstance.getWeight());
			aKpiInstance.setTarget(sbiKpiInstance.getTarget());
			aKpiInstance.setD(sbiKpiInstance.getBeginDt());
			//
			toReturn.setKpiInstance(aKpiInstance);
		}

		List childrenNodes = new ArrayList();

		Criteria critt = session.createCriteria(SbiKpiModelInst.class);
		critt.add(Expression.eq("sbiKpiModelInst", value));
		critt.createCriteria("sbiKpiModel").addOrder(Order.asc("kpiModelCd"));

		List children = critt.list();

		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			SbiKpiModelInst sbiKpichild = (SbiKpiModelInst) iterator.next();
			ModelInstance child = toModelInstanceWithChildren(session,
					sbiKpichild, id);
			childrenNodes.add(child);
		}

		toReturn.setId(id);
		toReturn.setName(name);
		toReturn.setDescription(description);
		toReturn.setLabel(label);
		toReturn.setStartDate(startDate);
		toReturn.setEndDate(endDate);
		toReturn.setChildrenNodes(childrenNodes);
		toReturn.setParentId(parentId);
		toReturn.setModel(aModel);


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

	private void deleteKpiInstance(Session aSession, Integer kpiInstId)
			throws EMFUserError {
		SbiKpiInstance sbiKpiInst = (SbiKpiInstance) aSession.load(
				SbiKpiInstance.class, kpiInstId);

		// deleteKpiHistory(Integer sbiKpiInstance)
		Criteria critt = aSession.createCriteria(SbiKpiInstanceHistory.class);
		critt.add(Expression.eq("sbiKpiInstance", sbiKpiInst));
		List sbiKpiInstanceHistory = critt.list();

		for (Iterator iterator = sbiKpiInstanceHistory.iterator(); iterator
				.hasNext();) {
			SbiKpiInstanceHistory sbiKpiH = (SbiKpiInstanceHistory) iterator
					.next();

			aSession.delete(sbiKpiH);
		}

		deleteKpiValue(aSession, kpiInstId);

		aSession.delete(sbiKpiInst);
	}

	private void deleteKpiValue(Session aSession, Integer kpiInstId) {
		SbiKpiInstance sbiKpiInst = (SbiKpiInstance) aSession.load(
				SbiKpiInstance.class, kpiInstId);
		Criteria critt = aSession.createCriteria(SbiKpiValue.class);
		critt.add(Expression.eq("sbiKpiInstance", sbiKpiInst));
		List sbiKpiValueList = critt.list();

		for (Iterator iterator = sbiKpiValueList.iterator(); iterator.hasNext();) {
			SbiKpiValue sbiKpiValue = (SbiKpiValue) iterator.next();

			aSession.delete(sbiKpiValue);

		}
	}

	public void deleteKpiValue(Integer kpiInstId) throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpiInstance sbiKpiInst = (SbiKpiInstance) aSession.load(
					SbiKpiInstance.class, kpiInstId);
			Criteria critt = aSession.createCriteria(SbiKpiValue.class);
			critt.add(Expression.eq("sbiKpiInstance", sbiKpiInst));
			List sbiKpiValueList = critt.list();

			for (Iterator iterator = sbiKpiValueList.iterator(); iterator
					.hasNext();) {
				SbiKpiValue sbiKpiValue = (SbiKpiValue) iterator.next();

				aSession.delete(sbiKpiValue);
			}
		} catch (HibernateException he) {
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

	}

	public boolean deleteModelInstance(Integer modelId) throws EMFUserError {
		Session aSession = getSession();
		Transaction tx = null;
		try {
			tx = aSession.beginTransaction();
			SbiKpiModelInst aModelInst = (SbiKpiModelInst) aSession.load(
					SbiKpiModelInst.class, modelId);
			recursiveStepDelete(aSession, aModelInst);
			deleteModelInstKpiInstResourceValue(aSession, aModelInst);

			tx.commit();

		} catch (ConstraintViolationException cve) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			logger.error("Impossible to delete a Model Instance", cve);
			throw new EMFUserError(EMFErrorSeverity.WARNING, 10015);
		} catch (HibernateException e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			logger.error("Error while delete a Model ", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 101);
		} finally {
			aSession.close();
		}
		return true;
	}

	private void recursiveStepDelete(Session aSession,
			SbiKpiModelInst aModelInst) throws EMFUserError {
		Set children = aModelInst.getSbiKpiModelInsts();
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			SbiKpiModelInst modelInstChild = (SbiKpiModelInst) iterator.next();
			recursiveStepDelete(aSession, modelInstChild);
			// delete Model Instance, Kpi Inst, History, Resource and Value
			deleteModelInstKpiInstResourceValue(aSession, modelInstChild);
		}
	}

	private void deleteModelInstKpiInstResourceValue(Session aSession,
			SbiKpiModelInst aModelInst) throws EMFUserError {
		// Delete associations between the model and resources
		DAOFactory.getModelResources().removeAllModelResource(
				aModelInst.getKpiModelInst());
		// delete the model Inst
		aSession.delete(aModelInst);
		// Delete Kpi Instance Kpi Instance History Value
		if (aModelInst.getSbiKpiInstance() != null) {
			deleteKpiInstance(aSession, aModelInst.getSbiKpiInstance()
					.getIdKpiInstance());
		}
	}

	public List loadModelsInstanceRoot(String fieldOrder, String typeOrder)
			throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		List toReturn = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Criteria crit = aSession.createCriteria(SbiKpiModelInst.class);
			crit.add(Expression.isNull("sbiKpiModelInst"));

			if (fieldOrder != null && typeOrder != null) {
				if (typeOrder.toUpperCase().trim().equals("ASC"))
					crit.addOrder(Order
							.asc(getModelInstanceProperty(fieldOrder)));
				if (typeOrder.toUpperCase().trim().equals("DESC"))
					crit.addOrder(Order
							.desc(getModelInstanceProperty(fieldOrder)));
			}

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

	private String getModelInstanceProperty(String property) {
		String toReturn = null;
		if (property != null && property.equals("NAME"))
			toReturn = "name";
		return toReturn;
	}
}
