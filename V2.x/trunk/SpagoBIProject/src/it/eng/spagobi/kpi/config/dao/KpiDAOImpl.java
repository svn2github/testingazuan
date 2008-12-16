package it.eng.spagobi.kpi.config.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.metadata.SbiDomains;
import it.eng.spagobi.commons.metadata.SbiExtRoles;
import it.eng.spagobi.kpi.alarm.metadata.SbiAlarm;
import it.eng.spagobi.kpi.alarm.metadata.SbiAlarmEvent;
import it.eng.spagobi.kpi.config.bo.Kpi;
import it.eng.spagobi.kpi.config.bo.KpiInstance;
import it.eng.spagobi.kpi.config.bo.KpiValue;
import it.eng.spagobi.kpi.config.metadata.SbiKpi;
import it.eng.spagobi.kpi.config.metadata.SbiKpiInstance;
import it.eng.spagobi.kpi.config.metadata.SbiKpiInstanceHistory;
import it.eng.spagobi.kpi.config.metadata.SbiKpiPeriodicity;
import it.eng.spagobi.kpi.config.metadata.SbiKpiRole;
import it.eng.spagobi.kpi.config.metadata.SbiKpiValue;
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

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;

public class KpiDAOImpl extends AbstractHibernateDAO implements IKpiDAO {

	static private Logger logger = Logger.getLogger(KpiDAOImpl.class);

	public ModelInstanceNode loadModelInstanceById(Integer id,
			Date requestedDate) throws EMFUserError {
		logger.debug("IN");
		ModelInstanceNode toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpiModelInst hibSbiKpiModelInst = (SbiKpiModelInst) aSession
					.load(SbiKpiModelInst.class, id);
			toReturn = toModelInstanceNode(hibSbiKpiModelInst, requestedDate);

		} catch (HibernateException he) {
			logger.error("Error while loading the Model Instance with id "
					+ ((id == null) ? "" : id.toString()), he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 10101);

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

	public KpiInstance loadKpiInstanceById(Integer id) throws EMFUserError {
		logger.debug("IN");
		KpiInstance toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpiInstance hibSbiKpiInstance = (SbiKpiInstance) aSession.load(
					SbiKpiInstance.class, id);
			toReturn = toKpiInstance(hibSbiKpiInstance, hibSbiKpiInstance
					.getBeginDt());

		} catch (HibernateException he) {
			logger.error("Error while loading the Model Instance with id "
					+ ((id == null) ? "" : id.toString()), he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 9104);

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

	public Kpi loadKpiById(Integer id) throws EMFUserError {
		logger.debug("IN");
		Kpi toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpi hibSbiKpiInstance = (SbiKpi) aSession.load(SbiKpi.class, id);
			toReturn = toKpi(hibSbiKpiInstance);

		} catch (HibernateException he) {
			logger.error("Error while loading the Model Instance with id "
					+ ((id == null) ? "" : id.toString()), he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 9104);

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

	public List loadResourcesList() throws EMFUserError {
		logger.debug("IN");
		List toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			toReturn = new ArrayList();
			List toTransform = null;
			toTransform = aSession.createQuery("from SbiResources").list();

			for (Iterator iterator = toTransform.iterator(); iterator.hasNext();) {
				SbiResources hibResource = (SbiResources) iterator.next();
				Resource resource = toResource(hibResource);
				toReturn.add(resource);
			}

		} catch (HibernateException he) {
			logger.error("Error while loading the list of Resources", he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 9104);

		} finally {
			if (aSession != null) {
				if (aSession.isOpen())
					aSession.close();
				logger.debug("OUT");
			}
		}
		return toReturn;
	}

	public Resource loadResourceById(Integer id) throws EMFUserError {
		logger.debug("IN");
		Resource toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiResources hibSbiResource = (SbiResources) aSession.load(
					SbiResources.class, id);
			toReturn = toResource(hibSbiResource);

		} catch (HibernateException he) {
			logger.error("Error while loading the Model Instance with id "
					+ ((id == null) ? "" : id.toString()), he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 9104);

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
			hibKpiInstance = (SbiKpiInstance) aSession.load(
					SbiKpiInstance.class, kpiInstID);
			Set kpiValues = hibKpiInstance.getSbiKpiValues();
			SbiDomains dom = hibKpiInstance.getSbiDomains();
			String chartType = null;
			if (dom != null)
				chartType = dom.getValueCd();

			Iterator iVa = kpiValues.iterator();
			while (iVa.hasNext()) {
				SbiKpiValue value = (SbiKpiValue) iVa.next();
				Date kpiValueBegDt = value.getBeginDt();
				Date kpiValueEndDt = value.getEndDt();

				if (d.after(kpiValueBegDt) && d.before(kpiValueEndDt)) {
					KpiValue val = toKpiValue(value, d);
					if (chartType != null) {
						val.setChartType(chartType);
					}
					values.add(val);
				}
			}

		} catch (HibernateException he) {
			logger
					.error(
							"Error while getting the List of KpiValues related to the SbiKpiInstance with id "
									+ ((kpiInstID == null) ? "" : kpiInstID
											.toString()) + "at the Date " + d,
							he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 10102);

		} finally {
			if (aSession != null) {
				if (aSession.isOpen())
					aSession.close();
				logger.debug("OUT");
			}
		}
		logger.debug("OUT");
		return values;
	}

	public Integer getPeriodicitySeconds(Integer periodicityId)
			throws EMFUserError {

		logger.debug("IN");
		Integer toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		int seconds = 0;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpiPeriodicity hibSbiKpiPeriodicity = (SbiKpiPeriodicity) aSession
					.load(SbiKpiPeriodicity.class, periodicityId);
			if (hibSbiKpiPeriodicity.getDays() != null) {
				// 86400 seconds in a day
				seconds += hibSbiKpiPeriodicity.getDays().intValue() * 86400;
			}
			if (hibSbiKpiPeriodicity.getHours() != null) {
				// 3600 seconds in an hour
				seconds += hibSbiKpiPeriodicity.getHours().intValue() * 3600;
			}
			if (hibSbiKpiPeriodicity.getMinutes() != null) {
				// 60 seconds in a minute
				seconds += hibSbiKpiPeriodicity.getMinutes().intValue() * 60;
			}
			if (hibSbiKpiPeriodicity.getMonths() != null) {
				// 2592000 seconds in a month of 30 days
				seconds += hibSbiKpiPeriodicity.getMonths().intValue() * 2592000;
			}
			toReturn = new Integer(seconds);

		} catch (HibernateException he) {
			logger.error("Error while loading the Periodicity with id "
					+ periodicityId, he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 9104);

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
			String kpiValue = value.getValue();
			Integer kpiInstanceId = value.getKpiInstanceId();
			SbiKpiInstance sbiKpiInstance = (SbiKpiInstance) aSession.load(
					SbiKpiInstance.class, kpiInstanceId);
			Resource r = value.getR();
			if (r != null) {
				SbiResources sbiResources = toSbiResource(r);
				hibKpiValue.setSbiResources(sbiResources);
			}

			hibKpiValue.setBeginDt(beginDt);
			hibKpiValue.setEndDt(endDt);
			hibKpiValue.setValue(kpiValue);
			hibKpiValue.setSbiKpiInstance(sbiKpiInstance);

			aSession.save(hibKpiValue);
			tx.commit();

		} catch (HibernateException he) {
			logger.error(
					"Error while inserting the KpiValue related to the KpiInstance with id "
							+ ((value.getKpiInstanceId() == null) ? "" : value
									.getKpiInstanceId().toString()), he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 10103);

		} finally {
			if (aSession != null) {
				if (aSession.isOpen())
					aSession.close();
				logger.debug("OUT");
			}
		}
		logger.debug("OUT");

	}

	public List getThresholds(KpiInstance k) throws EMFUserError {

		logger.debug("IN");
		List thresholds = new ArrayList();
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpiInstance hibSbiKpiInstance = (SbiKpiInstance) aSession.load(
					SbiKpiInstance.class, k.getKpiInstanceId());
			SbiThreshold t = hibSbiKpiInstance.getSbiThreshold();

			Set thresholdValues = t.getSbiThresholdValues();
			Iterator it = thresholdValues.iterator();
			while (it.hasNext()) {
				SbiThresholdValue val = (SbiThresholdValue) it.next();
				Threshold tr = toThreshold(val);
				thresholds.add(tr);
			}

		} catch (HibernateException he) {
			logger
					.error(
							"Error while loading the current list of Thresholds for the KpiInstance with id "
									+ ((k.getKpiInstanceId() == null) ? "" : k
											.getKpiInstanceId().toString()), he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 10104);

		} finally {
			if (aSession != null) {
				if (aSession.isOpen())
					aSession.close();
				logger.debug("OUT");
			}
		}
		logger.debug("OUT");
		return thresholds;
	}

	/*
	 * public boolean hasActualValues(KpiInstance inst, Date d) throws
	 * EMFUserError {
	 * 
	 * logger.debug("IN"); boolean toReturn = false ; //I verify if effectively
	 * the required date is after the begin date of the KpiInstance Date
	 * instBegDt = inst.getD(); if (d.after(instBegDt)){ toReturn = true ; }
	 * List values = inst.getValues(); //Even if the required date is after the
	 * begin date of the KpiInstance it could be that there are no actual
	 * KpiValues and that they have to be recalculated if (values.isEmpty()){
	 * toReturn = false ; } logger.debug("OUT"); return toReturn; }
	 */

	public Boolean isKpiInstUnderAlramControl(Integer kpiInstID)
			throws EMFUserError {
		logger.debug("IN");
		Boolean toReturn = false;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String hql = "from SbiAlarm sa where sa.sbiKpiInstance.idKpiInstance = ? ";

			Query query = aSession.createQuery(hql);
			query.setInteger(0, kpiInstID);

			List l = query.list();
			if (!l.isEmpty()) {
				toReturn = true;
			}

		} catch (HibernateException he) {

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 9104);

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

	public void isAlarmingValue(KpiValue value) throws EMFUserError {
		logger.debug("IN");

		Integer kpiInstID = value.getKpiInstanceId();
		String val = value.getValue();
		Double kpiVal = new Double(val);
		KpiInstance kInst = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpiInstance hibSbiKpiInstance = (SbiKpiInstance) aSession.load(
					SbiKpiInstance.class, kpiInstID);
			Set alarms = hibSbiKpiInstance.getSbiAlarms();
			if (!alarms.isEmpty()) {

				Iterator itAl = alarms.iterator();
				while (itAl.hasNext()) {
					boolean isAlarming = false;
					SbiAlarm alarm = (SbiAlarm) itAl.next();
					String a = "";
					SbiThresholdValue threshold = alarm.getSbiThresholdValue();
					String type = threshold.getSbiThreshold().getSbiDomains()
							.getValueCd();
					double min;
					double max;
					String thresholdValue = "";

					if (type.equals("RANGE")) {

						min = threshold.getMinValue();
						max = threshold.getMaxValue();

						// if the value is in the interval, then there should be
						// an alarm
						if (kpiVal.doubleValue() >= min
								&& kpiVal.doubleValue() <= max) {
							isAlarming = true;
							thresholdValue = "Min:" + min + "-Max:" + max;
							logger.debug("The value " + kpiVal.doubleValue()
									+ " is in the RANGE " + thresholdValue
									+ " and so an Alarm will be scheduled");
						}

					} else if (type.equals("MINIMUM")) {

						min = threshold.getMinValue();
						// if the value is smaller than the min value
						if (kpiVal.doubleValue() <= min) {
							isAlarming = true;
							thresholdValue = "Min:" + min;
							logger.debug("The value " + kpiVal.doubleValue()
									+ " is lower than " + thresholdValue
									+ " and so an Alarm will be scheduled");
						}

					} else if (type.equals("MAXIMUM")) {

						max = threshold.getMaxValue();
						// if the value is higher than the max value
						if (kpiVal.doubleValue() >= max) {
							isAlarming = true;
							thresholdValue = "Max:" + max;
							logger.debug("The value " + kpiVal.doubleValue()
									+ " is higher than " + thresholdValue
									+ " and so an Alarm will be scheduled");
						}
					}

					if (isAlarming) {
						SbiAlarmEvent alarmEv = new SbiAlarmEvent();
						Integer alarmID = alarm.getId();
						String kpiName = hibSbiKpiInstance.getSbiKpi()
								.getName();
						String resources = null;
						if (value.getR() != null) {
							resources = value.getR().getName();
						}

						alarmEv.setKpiName(kpiName);
						alarmEv.setKpiValue(val);
						alarmEv.setActive(true);
						alarmEv.setEventTs(new Date());
						alarmEv.setResources(resources);
						alarmEv.setSbiAlarms(alarm);
						alarmEv.setThresholdValue(thresholdValue);

						DAOFactory.getAlarmEventDAO().insert(alarmEv);
						logger
								.debug("A new alarm has been inserted in the Alarm Event Table");
					}

				}
			}

		} catch (HibernateException he) {
			logger
					.error(
							"Error while verifying if the KpiValue is alarming for its thresholds ",
							he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 10105);

		} finally {
			if (aSession != null) {
				if (aSession.isOpen())
					aSession.close();
				logger.debug("OUT");
			}
		}
		logger.debug("OUT");
	}

	public DataSetConfig getDsFromKpiId(Integer kpiId) throws EMFUserError {
		logger.debug("IN");
		DataSetConfig toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpi k = (SbiKpi) aSession.load(SbiKpi.class, kpiId);
			SbiDataSetConfig ds = k.getSbiDataSet();
			toReturn = DAOFactory.getDataSetDAO().loadDataSetByID(ds.getDsId());

		} catch (HibernateException he) {

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 9104);

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

	public KpiValue getKpiValue(Integer kpiInstanceId, Date d, Resource r)
			throws EMFUserError {

		logger.debug("IN");
		KpiValue toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Criteria finder = aSession.createCriteria(SbiKpiValue.class);
			finder.add(Expression.eq("sbiKpiInstance.idKpiInstance",
					kpiInstanceId));
			finder.add(Expression.lt("beginDt", d));
			finder.add(Expression.gt("endDt", d));

			if (r != null) {
				finder.add(Expression.eq("sbiResources.resourceId", r.getId()));
			}

			List l = finder.list();
			if (!l.isEmpty()) {
				Iterator it = l.iterator();
				while (it.hasNext()) {
					SbiKpiValue temp = (SbiKpiValue) it.next();
					toReturn = toKpiValue(temp, d);
					break;
				}
			}

		} catch (HibernateException he) {

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 9104);

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

	private KpiValue toKpiValue(SbiKpiValue value, Date d) {

		logger.debug("IN");
		KpiValue toReturn = new KpiValue();

		Date beginDate = value.getBeginDt();
		Date endDate = value.getEndDt();
		String val = value.getValue();
		Integer kpiInstanceID = value.getIdKpiInstanceValue();
		SbiKpiInstance kpiInst = value.getSbiKpiInstance();
		Double weight = kpiInst.getWeight();
		Double target = kpiInst.getTarget();
		SbiResources res = value.getSbiResources();
		Resource r = null;
		if (res != null)
			r = toResource(res);
		List thresholds = new ArrayList();
		SbiKpiInstance ski = value.getSbiKpiInstance();
		Date kpiInstBegDt = ski.getBeginDt();
		// in case the current threshold is correct
		if (d.before(endDate) && d.after(beginDate)) {

			SbiThreshold t = ski.getSbiThreshold();
			Set ts = t.getSbiThresholdValues();
			Iterator i = ts.iterator();
			while (i.hasNext()) {
				SbiThresholdValue tls = (SbiThresholdValue) i.next();
				Threshold tr = toThreshold(tls);
				thresholds.add(tr);
			}

		} else {// in case older thresholds have to be retrieved

			Set kpiInstHist = ski.getSbiKpiInstanceHistories();
			Iterator i = kpiInstHist.iterator();
			while (i.hasNext()) {
				SbiKpiInstanceHistory ih = (SbiKpiInstanceHistory) i.next();
				Date ihBegDt = ih.getBeginDt();
				Date ihEndDt = ih.getEndDt();
				if (d.after(ihBegDt) && d.before(ihEndDt)) {

					SbiThreshold t = ih.getSbiThreshold();
					Set ts = t.getSbiThresholdValues();
					Iterator it = ts.iterator();
					while (it.hasNext()) {
						SbiThresholdValue tls = (SbiThresholdValue) it.next();
						Threshold tr = toThreshold(tls);
						thresholds.add(tr);
					}
				}
			}
		}
		toReturn.setTarget(target);
		toReturn.setBeginDate(beginDate);
		toReturn.setEndDate(endDate);
		toReturn.setValue(val);
		toReturn.setKpiInstanceId(kpiInstanceID);
		toReturn.setWeight(weight);
		toReturn.setR(r);
		// toReturn.setScaleCode(scaleCode);
		// toReturn.setScaleName(scaleName);
		toReturn.setThresholds(thresholds);
		logger.debug("OUT");
		return toReturn;
	}

	private ModelInstanceNode toModelInstanceNode(
			SbiKpiModelInst hibSbiKpiModelInst, Date requestedDate)
			throws EMFUserError {

		logger.debug("IN");
		ModelInstanceNode toReturn = new ModelInstanceNode();

		String descr = hibSbiKpiModelInst.getDescription();
		String name = hibSbiKpiModelInst.getName();
		SbiKpiInstance kpiInst = hibSbiKpiModelInst.getSbiKpiInstance();
		KpiInstance kpiInstanceAssociated = toKpiInstance(kpiInst,
				requestedDate);
		Set resources = hibSbiKpiModelInst.getSbiKpiModelResourceses();
		List res = new ArrayList();
		if (!resources.isEmpty()) {
			Iterator i = resources.iterator();
			while (i.hasNext()) {
				SbiKpiModelResources dls = (SbiKpiModelResources) i.next();
				Resource r = toResource(dls);
				res.add(r);
			}
		}
		// gets father id
		SbiKpiModelInst father = hibSbiKpiModelInst.getSbiKpiModelInst();
		Integer fatherId = null;
		Boolean isRoot = false;
		if (father != null) {
			fatherId = father.getKpiModelInst();
		} else {
			isRoot = true;
		}

		// gets list of children id
		Set children = hibSbiKpiModelInst.getSbiKpiModelInsts();
		List childrenIds = new ArrayList();
		Iterator iCI = children.iterator();
		while (iCI.hasNext()) {
			SbiKpiModelInst skml = (SbiKpiModelInst) iCI.next();
			Integer childId = skml.getKpiModelInst();
			childrenIds.add(childId);
		}

		toReturn.setDescr(descr);
		toReturn.setName(name);
		toReturn.setKpiInstanceAssociated(kpiInstanceAssociated);
		toReturn.setResources(res);
		toReturn.setFatherId(fatherId);
		// toReturn.setModelReference(reference);TODO aspettare che ci sia il
		// legame nel db
		toReturn.setIsRoot(isRoot);
		toReturn.setChildrenIds(childrenIds);
		logger.debug("OUT");
		return toReturn;
	}

	private KpiInstance toKpiInstance(SbiKpiInstance kpiInst, Date requestedDate)
			throws EMFUserError {

		logger.debug("IN");
		KpiInstance toReturn = new KpiInstance();
		Integer kpiId = kpiInst.getIdKpiInstance();
		SbiKpi kpi = kpiInst.getSbiKpi();
		Integer k = kpi.getKpiId();
		Date d = new Date();
		d = kpiInst.getBeginDt();
		Double weight = kpiInst.getWeight();
		Double target = kpiInst.getTarget();
		// List values = getKpiValue(kpiInst, requestedDate);
		SbiKpiPeriodicity periodicity = kpiInst.getSbiKpiPeriodicity();
		Integer idPeriodicity = periodicity.getIdKpiPeriodicity();

		toReturn.setWeight(weight);
		toReturn.setTarget(target);
		toReturn.setKpiInstanceId(kpiId);
		toReturn.setKpi(k);
		// toReturn.setValues(values);
		toReturn.setD(d);
		toReturn.setPeriodicity(idPeriodicity);
		logger.debug("OUT");
		return toReturn;
	}

	private Resource toResource(SbiResources r) {

		logger.debug("IN");
		Resource toReturn = new Resource();

		String coumn_name = r.getColumnName();
		String name = r.getResourceName();
		String table_name = r.getTableName();
		String descr = r.getResourceDescr();
		SbiDomains d = r.getSbiDomains();
		String type = d.getValueCd();
		Integer resourceId = r.getResourceId();
		Integer typeId = d.getValueId();

		toReturn.setColumn_name(coumn_name);
		toReturn.setName(name);
		toReturn.setDescr(descr);
		toReturn.setTable_name(table_name);
		toReturn.setType(type);
		toReturn.setTypeId(typeId);
		toReturn.setId(resourceId);
		logger.debug("OUT");
		return toReturn;
	}

	private Resource toResource(SbiKpiModelResources re) {

		logger.debug("IN");
		Resource toReturn = new Resource();

		SbiResources r = re.getSbiResources();
		toReturn = toResource(r);
		logger.debug("OUT");
		return toReturn;
	}

	private SbiResources toSbiResource(Resource r) throws EMFUserError {

		logger.debug("IN");
		SbiResources toReturn = new SbiResources();
		String columnName = r.getColumn_name();
		String resourceName = r.getName();
		String resourceDescr = r.getDescr();
		String tableName = r.getTable_name();
		Integer resourceId = r.getId();
		String type = r.getType();
		Domain domain = DAOFactory.getDomainDAO().loadDomainByCodeAndValue(
				"RESOURCE", type);
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
		toReturn.setResourceDescr(resourceDescr);
		toReturn.setSbiDomains(sbiDomains);
		toReturn.setTableName(tableName);
		logger.debug("OUT");
		return toReturn;
	}

	public Kpi toKpi(SbiKpi kpi) throws EMFUserError {

		logger.debug("IN");
		Kpi toReturn = new Kpi();

		String description = kpi.getDescription();
		String documentLabel = kpi.getDocumentLabel();
		Boolean isParent = false;
		if (kpi.getFlgIsFather()!=null &&  kpi.getFlgIsFather().equals(new Character('T'))) {
			isParent = true;
		}
		Integer kpiId = kpi.getKpiId();
		String kpiName = kpi.getName();
		SbiDataSetConfig dsC = kpi.getSbiDataSet();
		DataSetConfig ds = DAOFactory.getDataSetDAO().loadDataSetByID(
				dsC.getDsId());
		Set kpiRoles = kpi.getSbiKpiRoles();
		List roles = new ArrayList();
		Iterator i = kpiRoles.iterator();
		while (i.hasNext()) {
			SbiKpiRole dls = (SbiKpiRole) i.next();
			SbiExtRoles extR = dls.getSbiExtRoles();
			roles.add(extR);
		}

		SbiThreshold thresh = kpi.getSbiThreshold();
		Set threshValues = thresh.getSbiThresholdValues();
		List thresholds = new ArrayList();
		Iterator iTre = threshValues.iterator();
		while (iTre.hasNext()) {
			SbiThresholdValue trs = (SbiThresholdValue) iTre.next();
			Threshold t = toThreshold(trs);
			thresholds.add(t);
		}


		Double standardWeight = kpi.getWeight();
		Set kInstances = kpi.getSbiKpiInstances();
		List KpiInstances = new ArrayList();
		Iterator iKI = kInstances.iterator();
		while (iKI.hasNext()) {
			SbiKpiInstance kpiI = (SbiKpiInstance) iKI.next();
			KpiInstance kI = toKpiInstance(kpiI, kpiI.getBeginDt());
			KpiInstances.add(kI);
		}

		// Gets the father
		SbiKpi dad = kpi.getSbiKpi();
		Boolean isRoot = false;
		Integer father = null;
		if (dad != null) {
			father = dad.getKpiId();
		} else {
			isRoot = true;
		}

		// Gets the children
		Set children = kpi.getSbiKpis();
		List kpiChildren = new ArrayList();
		Iterator iKC = children.iterator();
		while (iKC.hasNext()) {
			SbiKpi kCh = (SbiKpi) iKC.next();
			Integer kI = kCh.getKpiId();
			kpiChildren.add(kI);
		}

		// String metric = kpi.getMetric();
		// kpi.getSbiMeasureUnit();

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
		// toReturn.setMetric(metric);
		// toReturn.setScaleCode(scaleCode);
		// toReturn.setScaleName(scaleName);
		logger.debug("OUT");
		return toReturn;
	}

	public Threshold toThreshold(SbiThresholdValue t) {

		logger.debug("IN");
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
		Color color = new Color(255, 255, 0);
		String col = t.getColour();
		if (col != null) {
			color = color.decode(col);
		}

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
		logger.debug("OUT");
		return toReturn;
	}

	public String getChartType(Integer kpiInstanceID) throws EMFUserError {
		String chartType = null;
		logger.debug("IN");

		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpiInstance hibSbiKpiInstance = (SbiKpiInstance) aSession.load(
					SbiKpiInstance.class, kpiInstanceID);
			SbiDomains d = hibSbiKpiInstance.getSbiDomains();
			if (d != null) {
				chartType = d.getValueCd();
			}

		} catch (HibernateException he) {
			logger.error(
					"Error while loading the KpiInstance with id "
							+ ((kpiInstanceID == null) ? "" : kpiInstanceID
									.toString()), he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 9104);

		} finally {
			if (aSession != null) {
				if (aSession.isOpen())
					aSession.close();
				logger.debug("OUT");
			}
		}
		logger.debug("OUT");

		return chartType;
	}

	public List loadKpiList() throws EMFUserError {
		logger.debug("IN");
		List toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			toReturn = new ArrayList();
			List toTransform = null;
			toTransform = aSession.createQuery("from SbiKpi").list();

			for (Iterator iterator = toTransform.iterator(); iterator.hasNext();) {
				SbiKpi hibKpi = (SbiKpi) iterator.next();
				Kpi kpi = new Kpi();
				kpi.setDescription(hibKpi.getDescription());
				kpi.setKpiName(hibKpi.getName());
				kpi.setKpiId(hibKpi.getKpiId());
				toReturn.add(kpi);
			}

		} catch (HibernateException he) {
			logger.error("Error while loading the list of Kpi", he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 9104);

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

	public void modifyResource(Resource resource) throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Integer resourceId = resource.getId();
			String tableName = resource.getTable_name();
			String columnName = resource.getColumn_name();
			String resourceName = resource.getName();
			String resourceDescription = resource.getDescr();

			SbiDomains sbiDomain = (SbiDomains) aSession.load(SbiDomains.class,
					resource.getTypeId());
			SbiResources sbiResource = (SbiResources) aSession.load(
					SbiResources.class, resource.getId());

			sbiResource.setTableName(tableName);
			sbiResource.setColumnName(columnName);
			sbiResource.setResourceName(resourceName);
			sbiResource.setResourceDescr(resourceDescription);
			sbiResource.setSbiDomains(sbiDomain);

			aSession.update(sbiResource);
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

	public Integer insertResource(Resource toCreate) throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		Integer idToReturn;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiResources hibResource = new SbiResources();
			hibResource.setResourceName(toCreate.getName());
			hibResource.setResourceDescr(toCreate.getDescr());
			hibResource.setTableName(toCreate.getTable_name());
			hibResource.setColumnName(toCreate.getColumn_name());
			SbiDomains sbiDomains = (SbiDomains) aSession.load(
					SbiDomains.class, toCreate.getTypeId());
			hibResource.setSbiDomains(sbiDomains);

			idToReturn = (Integer) aSession.save(hibResource);
			tx.commit();

		} catch (HibernateException he) {
			logger.error("Error while inserting the KpiResource ", he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 10103);

		} finally {
			if (aSession != null) {
				if (aSession.isOpen())
					aSession.close();
				logger.debug("OUT");
			}
		}
		return idToReturn;
	}

	public void setKpiInstanceFromKPI(KpiInstance kpiInstance, Integer kpiId)
			throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpi sbiKpi = (SbiKpi) aSession.load(SbiKpi.class, kpiId);

			kpiInstance.setKpi(sbiKpi.getKpiId());
			if (sbiKpi.getSbiThreshold() != null) {
				kpiInstance.setThresholdId(sbiKpi.getSbiThreshold()
						.getThresholdId());
			}

			kpiInstance.setWeight(sbiKpi.getWeight());

		} catch (HibernateException he) {
			logger.error("Error while Loading a Kpi ", he);

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

	public void deleteResource(Integer resouceId) throws EMFUserError  {
		Session aSession = getSession();
		Transaction tx = null;
		try {
			tx = aSession.beginTransaction();
			SbiResources sbiResource = (SbiResources) aSession.load(
					SbiResources.class, resouceId);
			aSession.delete(sbiResource);

			tx.commit();
			

		}catch (ConstraintViolationException cve){
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			logger.error("Impossible to delete a Resource",cve);
			throw new EMFUserError(EMFErrorSeverity.WARNING, 10014);
			
		} 		
		catch (HibernateException e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			logger.error("Error while delete a Resource ", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 101);

		} finally {
			aSession.close();
		}
	}

}
