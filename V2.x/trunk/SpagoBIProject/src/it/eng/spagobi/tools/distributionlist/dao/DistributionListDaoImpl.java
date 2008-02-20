package it.eng.spagobi.tools.distributionlist.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.metadata.SbiDomains;
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
			/*
			Criterion aCriterion = Expression.eq("valueId",	aDistributionList.getId());
			Criteria criteria = aSession.createCriteria(SbiDomains.class);
			criteria.add(aCriterion);

			SbiDomains dlId = (SbiDomains) criteria.uniqueResult();

			if (dlId == null){
				logger.error("The Distribution list with value_id = "+aDistributionList.getId()+" does not exist.");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 1035);
			}*/
			SbiDistributionList hibDistributionList = new SbiDistributionList();
			//hibDistributionList.setDlId(dlId);
			hibDistributionList.setName(aDistributionList.getName());
			hibDistributionList.setDescr(aDistributionList.getDescr());
			hibDistributionList.setSbiDistributionListsObjectses(null);
			aSession.save(hibDistributionList);
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while inserting the distribution list with id " + ((aDistributionList == null)?"":String.valueOf(aDistributionList.getId())), he);

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

	/*
	public void modifyDistributionList(DistributionList aDistributionList)
			throws EMFUserError {
		
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			Criterion aCriterion = Expression.eq("valueId",	aDistributionList.getId());
			Criteria criteria = aSession.createCriteria(SbiDomains.class);
			criteria.add(aCriterion);

			SbiDomains dialect = (SbiDomains) criteria.uniqueResult();

			if (dialect == null){
				logger.error("The Domain with value_id= "+aDistributionList.getId()+" does not exist.");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 1035);
			}


			SbiDistributionList hibDistributionList = (SbiDistributionList) aSession.load(SbiDistributionList.class,
					new Integer(aDistributionList.getId()));			
			hibDistributionList.setDlId(dlId);
			hibDistributionList.setName(aDistributionList.getName());
			hibDistributionList.setDescr(aDistributionList.getDescr());
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while modifing the data source with id " + ((aDistributionList == null)?"":String.valueOf(aDistributionList.getDsId())), he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
				logger.debug("OUT");
			}
		}

	}*/
	
	public DistributionList toDistributionList(SbiDistributionList hibDistributionList){
		DistributionList ds = new DistributionList();
		
		ds.setId((hibDistributionList.getDlId()).intValue());
		ds.setName(hibDistributionList.getName());
		ds.setDescr(hibDistributionList.getDescr());
		//ds.setEmails(hibDistributionList.getSbiDistributionListsObjectses());
		
		return ds;
	}

}
