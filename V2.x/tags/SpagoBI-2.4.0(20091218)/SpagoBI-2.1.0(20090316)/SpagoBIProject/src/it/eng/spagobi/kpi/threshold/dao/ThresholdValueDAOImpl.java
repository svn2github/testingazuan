package it.eng.spagobi.kpi.threshold.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.metadata.SbiDomains;
import it.eng.spagobi.kpi.threshold.bo.ThresholdValue;
import it.eng.spagobi.kpi.threshold.metadata.SbiThreshold;
import it.eng.spagobi.kpi.threshold.metadata.SbiThresholdValue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.exception.ConstraintViolationException;

public class ThresholdValueDAOImpl extends AbstractHibernateDAO implements
		IThresholdValueDAO {

	static private Logger logger = Logger
			.getLogger(ThresholdValueDAOImpl.class);

	static private String THRESHOLD_VALUE_POSITION = "position";
	static private String THRESHOLD_VALUE_LABEL = "label";
	static private String THRESHOLD_VALUE_MIN_VALUE = "minValue";
	static private String THRESHOLD_VALUE_MAX_VALUE = "maxValue";
	
	private String getThresholdValueProperty(String property){
		String toReturn = null;
		if(property != null && property.toUpperCase().equals("POSITION"))
			toReturn = THRESHOLD_VALUE_POSITION;
		if(property != null && property.toUpperCase().equals("LABEL"))
			toReturn = THRESHOLD_VALUE_LABEL;
		if(property != null && property.toUpperCase().equals("MIN_VALUE"))
			toReturn = THRESHOLD_VALUE_MIN_VALUE;
		if(property != null && property.toUpperCase().equals("MAX_VALUE"))
			toReturn = THRESHOLD_VALUE_MAX_VALUE;
		
		return toReturn;
	}
	

	public List loadThresholdValueList(Integer thresholdId, String fieldOrder,
			String typeOrder) throws EMFUserError {
		logger.debug("IN");
		List toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			toReturn = new ArrayList();
			List toTransform = null;

			SbiThreshold sbiThreshold = (SbiThreshold) aSession.load(
					SbiThreshold.class, thresholdId);

			Criteria crit = aSession.createCriteria(SbiThresholdValue.class);
			crit.add(Expression.eq("sbiThreshold", sbiThreshold));
			
			if (fieldOrder != null && typeOrder != null) {
				if (typeOrder.toUpperCase().trim().equals("ASC"))
					crit.addOrder(Order.asc(getThresholdValueProperty(fieldOrder)));
				if (typeOrder.toUpperCase().trim().equals("DESC"))
					crit.addOrder(Order.desc(getThresholdValueProperty(fieldOrder)));
			}
			toTransform = crit.list();

			for (Iterator iterator = toTransform.iterator(); iterator.hasNext();) {
				SbiThresholdValue hibThresholdValue = (SbiThresholdValue) iterator
						.next();
				ThresholdValue thresholdValue = new ThresholdValue();

				thresholdValue.setId(hibThresholdValue.getIdThresholdValue());
				thresholdValue.setLabel(hibThresholdValue.getLabel());
				thresholdValue.setPosition(hibThresholdValue.getPosition());
				thresholdValue.setMaxValue(hibThresholdValue.getMaxValue());
				thresholdValue.setMinValue(hibThresholdValue.getMinValue());

				thresholdValue.setThresholdId(hibThresholdValue
						.getSbiThreshold().getThresholdId());
				toReturn.add(thresholdValue);
			}

		} catch (HibernateException he) {
			logger.error("Error while loading the list of Threshold", he);

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

	public ThresholdValue loadThresholdValueById(Integer id)
			throws EMFUserError {
		logger.debug("IN");
		ThresholdValue toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiThresholdValue hibThresholdValue = (SbiThresholdValue) aSession
					.load(SbiThresholdValue.class, id);
			toReturn = new ThresholdValue();
			toReturn.setId(hibThresholdValue.getIdThresholdValue());
			toReturn.setPosition(hibThresholdValue.getPosition());
			toReturn.setLabel(hibThresholdValue.getLabel());
			toReturn.setMinValue(hibThresholdValue.getMinValue());
			toReturn.setMaxValue(hibThresholdValue.getMaxValue());
			toReturn.setColourString(hibThresholdValue.getColour());
			toReturn.setSeverityId(hibThresholdValue.getSbiDomains()
					.getValueId());
			toReturn.setThresholdId(hibThresholdValue.getSbiThreshold()
					.getThresholdId());
			toReturn.setType(hibThresholdValue.getSbiThreshold()
					.getSbiDomains().getValueCd());

			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while loading the ThresholdValue with id "
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
		return toReturn;
	}

	public void modifyThresholdValue(ThresholdValue thresholdValue)
			throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			Integer position = thresholdValue.getPosition();
			String label = thresholdValue.getLabel();
			Double minValue = thresholdValue.getMinValue();
			Double maxValue = thresholdValue.getMaxValue();
			String colour = thresholdValue.getColourString();
			Integer thresholdId = thresholdValue.getThresholdId();
			Integer severityId = thresholdValue.getSeverityId();

			SbiThresholdValue sbiThresholdValue = (SbiThresholdValue) aSession
					.load(SbiThresholdValue.class, thresholdValue.getId());

			SbiDomains severity = null;
			if (severityId != null) {
				severity = (SbiDomains) aSession.load(SbiDomains.class,
						severityId);
			}

			SbiThreshold threshold = null;
			if (thresholdId != null) {
				threshold = (SbiThreshold) aSession.load(SbiThreshold.class,
						thresholdId);
			}

			sbiThresholdValue.setPosition(position);
			sbiThresholdValue.setLabel(label);
			sbiThresholdValue.setMinValue(minValue);
			sbiThresholdValue.setMaxValue(maxValue);
			sbiThresholdValue.setColour(colour);
			sbiThresholdValue.setSbiThreshold(threshold);
			sbiThresholdValue.setSbiDomains(severity);

			aSession.saveOrUpdate(sbiThresholdValue);

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

	public Integer insertThresholdValue(ThresholdValue toCreate)
			throws EMFUserError {
		logger.debug("IN");
		Integer idToReturn;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			Integer position = toCreate.getPosition();
			String label = toCreate.getLabel();
			Double minValue = toCreate.getMinValue();
			Double maxValue = toCreate.getMaxValue();
			String colour = toCreate.getColourString();
			Integer thresholdId = toCreate.getThresholdId();
			Integer severityId = toCreate.getSeverityId();

			SbiThresholdValue sbiThresholdValue = new SbiThresholdValue();

			SbiDomains severity = null;
			if (severityId != null) {
				severity = (SbiDomains) aSession.load(SbiDomains.class,
						severityId);
			}

			SbiThreshold threshold = null;
			if (thresholdId != null) {
				threshold = (SbiThreshold) aSession.load(SbiThreshold.class,
						thresholdId);
			}

			sbiThresholdValue.setPosition(position);
			sbiThresholdValue.setLabel(label);
			sbiThresholdValue.setMinValue(minValue);
			sbiThresholdValue.setMaxValue(maxValue);
			sbiThresholdValue.setColour(colour);
			sbiThresholdValue.setSbiThreshold(threshold);
			sbiThresholdValue.setSbiDomains(severity);

			idToReturn = (Integer) aSession.save(sbiThresholdValue);

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

	public boolean deleteThresholdValue(Integer thresholdValueId)
			throws EMFUserError {
		Session aSession = getSession();
		Transaction tx = null;
		try {
			tx = aSession.beginTransaction();
			SbiThresholdValue aThresholdValue = (SbiThresholdValue) aSession
					.load(SbiThresholdValue.class, thresholdValueId);
			aSession.delete(aThresholdValue);
			tx.commit();

		} catch (ConstraintViolationException cve) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			logger.error("Impossible to delete a Threshold", cve);
			throw new EMFUserError(EMFErrorSeverity.WARNING, 10017);
		} catch (HibernateException e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			logger.error("Error while delete a Threshold ", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 101);
		} finally {
			aSession.close();
		}
		return true;
	}
}
