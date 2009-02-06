package it.eng.spagobi.kpi.config.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.kpi.config.bo.Periodicity;
import it.eng.spagobi.kpi.config.metadata.SbiKpiPeriodicity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PeriodicityDAOImpl extends AbstractHibernateDAO implements
		IPeriodicityDAO {

	static private Logger logger = Logger.getLogger(PeriodicityDAOImpl.class);

	public Periodicity loadPeriodicityById(Integer id) throws EMFUserError {
		logger.debug("IN");
		Periodicity toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiKpiPeriodicity hibKpiPeriodicity = (SbiKpiPeriodicity)aSession.load(SbiKpiPeriodicity.class,id);
			toReturn = new Periodicity(hibKpiPeriodicity
					.getIdKpiPeriodicity(), hibKpiPeriodicity.getName(),
					hibKpiPeriodicity.getMonths(), hibKpiPeriodicity
							.getDays(), hibKpiPeriodicity.getHours(),
					hibKpiPeriodicity.getMinutes(), hibKpiPeriodicity.getChronString());
		} catch (HibernateException he) {
			logger.error("Error while loading the Periodicity with id " + ((id == null)?"":id.toString()), he);			

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 10101);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
				logger.debug("OUT");
			}
		}
		return toReturn;
	}

	public List loadPeriodicityList() throws EMFUserError {
		logger.debug("IN");
		List toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			toReturn = new ArrayList();
			List toTransform = null;
			toTransform = aSession.createQuery("from SbiKpiPeriodicity").list();

			for (Iterator iterator = toTransform.iterator(); iterator.hasNext();) {
				SbiKpiPeriodicity hibKpiPeriodicity = (SbiKpiPeriodicity) iterator
						.next();
				Periodicity periodicity = new Periodicity(hibKpiPeriodicity
						.getIdKpiPeriodicity(), hibKpiPeriodicity.getName(),
						hibKpiPeriodicity.getMonths(), hibKpiPeriodicity
								.getDays(), hibKpiPeriodicity.getHours(),
						hibKpiPeriodicity.getMinutes(), hibKpiPeriodicity
								.getChronString());
				toReturn.add(periodicity);
			}

		} catch (HibernateException he) {
			logger.error("Error while loading the list of Periodicity", he);

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

}
