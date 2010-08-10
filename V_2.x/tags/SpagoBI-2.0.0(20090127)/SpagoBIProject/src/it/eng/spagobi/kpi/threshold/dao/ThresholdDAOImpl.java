package it.eng.spagobi.kpi.threshold.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.exception.ConstraintViolationException;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.metadata.SbiDomains;
import it.eng.spagobi.kpi.config.metadata.SbiKpi;
import it.eng.spagobi.kpi.model.metadata.SbiKpiModelAttrVal;
import it.eng.spagobi.kpi.model.metadata.SbiKpiModelInst;
import it.eng.spagobi.kpi.model.metadata.SbiKpiModelResources;
import it.eng.spagobi.kpi.threshold.bo.Threshold;
import it.eng.spagobi.kpi.threshold.metadata.SbiThreshold;
import it.eng.spagobi.tools.dataset.metadata.SbiDataSetConfig;
import it.eng.spagobi.wapp.metadata.SbiMenuRole;
import it.eng.spagobi.wapp.metadata.SbiMenuRoleId;

public class ThresholdDAOImpl extends AbstractHibernateDAO implements
		IThresholdDAO {

	static private Logger logger = Logger.getLogger(ThresholdDAOImpl.class);
	static private String THRESHOLD_NAME = "name";
	static private String THRESHOLD_DESCRIPTION = "description";

	private String getThreshodProperty(String property) {
		String toReturn = null;
		if (property != null && property.equals("NAME"))
			toReturn = THRESHOLD_NAME;
		if (property != null && property.equals("DESCRIPTION"))
			toReturn = THRESHOLD_DESCRIPTION;
		return toReturn;
	}

	public Threshold loadThresholdById(Integer id) throws EMFUserError {
		logger.debug("IN");
		Threshold toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiThreshold hibThreshold = (SbiThreshold) aSession.load(
					SbiThreshold.class, id);
			toReturn = new Threshold();
			toReturn.setThresholdName(hibThreshold.getName());
			toReturn.setThresholdDescription(hibThreshold.getDescription());
			toReturn.setThresholdCode(hibThreshold.getCode());
			toReturn.setId(hibThreshold.getThresholdId());
			toReturn.setThresholdTypeId(hibThreshold.getSbiDomains()
					.getValueId());

		} catch (HibernateException he) {
			logger.error("Error while loading the Threshold with id "
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

	public List loadThresholdList(String fieldOrder, String typeOrder)
			throws EMFUserError {
		logger.debug("IN");
		List toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			toReturn = new ArrayList();
			List toTransform = null;
			if (fieldOrder != null && typeOrder != null) {
				Criteria crit = aSession
				.createCriteria(SbiThreshold.class);
				if (typeOrder.toUpperCase().trim().equals("ASC"))
					crit.addOrder(Order.asc(getThreshodProperty(fieldOrder)));
				if (typeOrder.toUpperCase().trim().equals("DESC"))
					crit.addOrder(Order.desc(getThreshodProperty(fieldOrder)));
				toTransform = crit.list();
			} else {
				toTransform = aSession.createQuery("from SbiThreshold").list();
			}

			for (Iterator iterator = toTransform.iterator(); iterator.hasNext();) {
				SbiThreshold hibThreshold = (SbiThreshold) iterator.next();
				Threshold threshold = new Threshold();
				threshold.setThresholdName(hibThreshold.getName());
				threshold.setThresholdCode(hibThreshold.getCode());
				threshold
						.setThresholdDescription(hibThreshold.getDescription());
				threshold.setId(hibThreshold.getThresholdId());
				threshold.setThresholdTypeId(hibThreshold.getSbiDomains()
						.getValueId());
				toReturn.add(threshold);
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

	public void modifyThreshold(Threshold threshold) throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			String name = threshold.getThresholdName();
			String description = threshold.getThresholdDescription();
			String code = threshold.getThresholdCode();
			Integer thresholdTypeId = threshold.getThresholdTypeId();

			SbiThreshold sbiThreshold = (SbiThreshold) aSession.load(
					SbiThreshold.class, threshold.getId());

			SbiDomains thresholdType = null;
			if (thresholdTypeId != null) {
				thresholdType = (SbiDomains) aSession.load(SbiDomains.class,
						thresholdTypeId);
			}

			sbiThreshold.setName(name);
			sbiThreshold.setDescription(description);
			sbiThreshold.setCode(code);
			sbiThreshold.setSbiDomains(thresholdType);

			aSession.saveOrUpdate(sbiThreshold);

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

	public Integer insertThreshold(Threshold threshold) throws EMFUserError {
		logger.debug("IN");
		Integer idToReturn;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			String name = threshold.getThresholdName();
			String description = threshold.getThresholdDescription();
			String code = threshold.getThresholdCode();
			Integer thresholdTypeId = threshold.getThresholdTypeId();

			SbiThreshold sbiThreshold = new SbiThreshold();

			SbiDomains thresholdType = null;
			if (thresholdTypeId != null) {
				thresholdType = (SbiDomains) aSession.load(SbiDomains.class,
						thresholdTypeId);
			}

			sbiThreshold.setName(name);
			sbiThreshold.setDescription(description);
			sbiThreshold.setCode(code);
			sbiThreshold.setSbiDomains(thresholdType);

			idToReturn = (Integer) aSession.save(sbiThreshold);

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

	public boolean deleteThreshold(Integer thresholdId) throws EMFUserError {
		Session aSession = getSession();
		Transaction tx = null;
		try {
			tx = aSession.beginTransaction();
			SbiThreshold aThreshold = (SbiThreshold) aSession.load(
					SbiThreshold.class, thresholdId);
			aSession.delete(aThreshold);

			tx.commit();

		} catch (ConstraintViolationException cve) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			logger.error("Impossible to delete a Threshold", cve);
			throw new EMFUserError(EMFErrorSeverity.WARNING, 10016);
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

	public List loadThresholdList() throws EMFUserError {
		return loadThresholdList(null, null);
	}

}
