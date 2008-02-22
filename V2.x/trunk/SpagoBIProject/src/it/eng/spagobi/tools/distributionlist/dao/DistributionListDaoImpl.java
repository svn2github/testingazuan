package it.eng.spagobi.tools.distributionlist.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.metadata.SbiDomains;
import it.eng.spagobi.tools.datasource.bo.DataSource;
import it.eng.spagobi.tools.datasource.metadata.SbiDataSource;
import it.eng.spagobi.tools.distributionlist.bo.DistributionList;
import it.eng.spagobi.tools.distributionlist.bo.Email;
import it.eng.spagobi.tools.distributionlist.metadata.SbiDistributionList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
/**
* @author Chiarelli Chiara (chiara.chiarelli@eng.it)
*/

public class DistributionListDaoImpl extends AbstractHibernateDAO implements IDistributionListDAO {

	static private Logger logger = Logger.getLogger(DistributionListDaoImpl.class);
	
	public void eraseDistributionList(DistributionList aDistributionList)
			throws EMFUserError {

		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiDistributionList hibDistributionList = (SbiDistributionList) aSession.load(SbiDistributionList.class,
					new Integer(aDistributionList.getId()));

			aSession.delete(hibDistributionList);
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while erasing the distribution list with id " + ((aDistributionList == null)?"":String.valueOf(aDistributionList.getId())), he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
				logger.debug("OUT");
			}
		}


	}

	public void insertDistributionList(DistributionList aDistributionList)
			throws EMFUserError {
		
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			SbiDistributionList hibDistributionList = new SbiDistributionList();
			
			hibDistributionList.setName(aDistributionList.getName());
			hibDistributionList.setDescr(aDistributionList.getDescr());
			
			aSession.save(hibDistributionList);
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while inserting the distribution list with name " + ((aDistributionList == null)?"":String.valueOf(aDistributionList.getName())), he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
				logger.debug("OUT");
			}
		}

	}

	public List loadAllDistributionLists() throws EMFUserError {
		
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			Query hibQuery = aSession.createQuery(" from SbiDistributionList");
			
			List hibList = hibQuery.list();
			Iterator it = hibList.iterator();

			while (it.hasNext()) {
				realResult.add(toDistributionList((SbiDistributionList) it.next()));
			}
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while loading all Distribution Lists ", he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();				
			}
		}
		logger.debug("OUT");
		return realResult;
		
	}

	public DistributionList loadDistributionListById(Integer Id)
			throws EMFUserError {

		logger.debug("IN");
		DistributionList toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiDistributionList hibDistributionList = (SbiDistributionList)aSession.load(SbiDistributionList.class,Id);
			toReturn = toDistributionList(hibDistributionList);
			tx.commit();
			
		} catch (HibernateException he) {
			logger.error("Error while loading the distribution list with id " + Id.toString(), he);			

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
	
	public DistributionList loadDistributionListByName(String name) throws EMFUserError {
		logger.debug("IN");
		DistributionList biDL = null;
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			Criterion nameCriterrion = Expression.eq("name", name);
			Criteria criteria = tmpSession.createCriteria(SbiDistributionList.class);
			criteria.add(nameCriterrion);	
			SbiDistributionList hibDL = (SbiDistributionList) criteria.uniqueResult();
			if (hibDL == null) return null;
			biDL = toDistributionList(hibDL);				
			
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while loading the distribution list with name " + name, he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}
		}
		logger.debug("OUT");
		return biDL;		
	}


	
	public void modifyDistributionList(DistributionList aDistributionList)
			throws EMFUserError {
		
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			SbiDistributionList hibDistributionList = (SbiDistributionList) aSession.load(SbiDistributionList.class,
					new Integer(aDistributionList.getId()));			
			hibDistributionList.setName(aDistributionList.getName());
			hibDistributionList.setDescr(aDistributionList.getDescr());
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while modifing the distribution list with id " + ((aDistributionList == null)?"":String.valueOf(aDistributionList.getId())), he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
				logger.debug("OUT");
			}
		}

	}
	
	public DistributionList toDistributionList(SbiDistributionList hibDistributionList){
		DistributionList dl = new DistributionList();
		
		dl.setId((hibDistributionList.getDlId()).intValue());
		dl.setName(hibDistributionList.getName());
		dl.setDescr(hibDistributionList.getDescr());		
		return dl;
	}
	
	public boolean hasBIObjAssociated (String dlId) throws EMFUserError{
		logger.debug("IN");		
		boolean bool = false; 
		
		
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Integer dlIdInt = Integer.valueOf(dlId);
			
			String hql = " from SbiObjects s where s.distributionList.dlId = "+ dlIdInt;
			Query aQuery = aSession.createQuery(hql);
			
			List biObjectsAssocitedWithDl = aQuery.list();
			if (biObjectsAssocitedWithDl.size() > 0)
				bool = true;
			else
				bool = false;
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while getting the objects associated with the distribution list with id " + dlId, he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		logger.debug("OUT");
		return bool;
		
	}

}
