package it.eng.spagobi.profiling.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.dao.BIObjectDAOHibImpl;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.profiling.bean.SbiUser;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SbiUserDAOHibImpl extends AbstractHibernateDAO implements ISbiUserDAO{
	static private Logger logger = Logger.getLogger(BIObjectDAOHibImpl.class);
	/**
	 * Load SbiUser by userId.
	 * 
	 * @param userId the user id	/**
	 * Load SbiUser by id.
	 * 
	 * @param id the bi object id
	 * 
	 * @return the BI object
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 */
	public SbiUser loadSbiUserByUserId(Integer userId) throws EMFUserError {
		logger.debug("IN");
		SbiUser toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			toReturn = (SbiUser)aSession.load(SbiUser.class,  userId);
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		logger.debug("OUT");
		return toReturn;
	} 
	
	/**
	 * Load SbiUser by id.
	 * 
	 * @param id the identifier	/**
	 * Load SbiUser by id.
	 * 
	 * @param id the bi object id
	 * 
	 * @return the BI object
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 */
	public SbiUser loadSbiUserById(Integer id) throws EMFUserError {
		logger.debug("IN");
		SbiUser toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			toReturn = (SbiUser)aSession.load(SbiUser.class,  id);
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		logger.debug("OUT");
		return toReturn;
	} 

	/**Insert SbiUser
	 * @param user
	 * @throws EMFUserError
	 */
	public void saveSbiUser(SbiUser user) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			aSession.save(SbiUser.class);
			
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
	}
}
