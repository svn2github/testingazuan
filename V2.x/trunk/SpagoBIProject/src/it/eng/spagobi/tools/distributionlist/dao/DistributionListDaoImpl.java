/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.tools.distributionlist.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.dao.BIObjectDAOHibImpl;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.metadata.SbiDomains;
import it.eng.spagobi.tools.distributionlist.bo.DistributionList;
import it.eng.spagobi.tools.distributionlist.bo.Email;
import it.eng.spagobi.tools.distributionlist.metadata.SbiDistributionList;
import it.eng.spagobi.tools.distributionlist.metadata.SbiDistributionListUser;
import it.eng.spagobi.tools.distributionlist.metadata.SbiDistributionListsObjects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
		
		//Gets all userids and respective emails and puts them into a list of Emails
		List emails = new ArrayList();
		Set s = hibDistributionList.getSbiDistributionListUsers();
		Iterator i = s.iterator();
		while(i.hasNext()){
			SbiDistributionListUser dls =(SbiDistributionListUser) i.next();
			String userId = dls.getUserId();
			String e_mail = dls.getEMail();
			Email email = new Email();
			email.setUserId(userId);
			email.setEmail(e_mail);
			emails.add(email);			
		}

		dl.setEmails(emails);
		
		//Gets all documents related to the distribution list and puts them into a list of documents
		List documents = new ArrayList();
		Set d = hibDistributionList.getSbiDistributionListsObjectses();
		Iterator it = d.iterator();
		while(it.hasNext()){
			SbiDistributionListsObjects dlo =(SbiDistributionListsObjects) it.next();
			SbiObjects so = dlo.getSbiObjects();
			BIObjectDAOHibImpl objDAO = new BIObjectDAOHibImpl();
			BIObject obj = objDAO.toBIObject(so);
			documents.add(obj);
		}

		dl.setDocuments(documents);
	
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
	
	public void subscribeToDistributionList(DistributionList aDistributionList, Email user) throws EMFUserError{
		
		logger.debug("IN");		
	
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			SbiDistributionList hibDistributionList = (SbiDistributionList) aSession.load(SbiDistributionList.class,
					new Integer(aDistributionList.getId()));	
			
			SbiDistributionListUser hibDistributionListUser = new SbiDistributionListUser();
			hibDistributionListUser.setUserId(user.getUserId());
			hibDistributionListUser.setEMail(user.getEmail());
			hibDistributionListUser.setSbiDistributionList(hibDistributionList);
			
			aSession.save(hibDistributionListUser);
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while subscribing to the distribution list with id " + ((aDistributionList == null)?"":String.valueOf(aDistributionList.getId())), he);

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
	
	
	public void unsubscribeFromDistributionList(DistributionList aDistributionList,String user) throws EMFUserError{
		
		logger.debug("IN");		
		
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			SbiDistributionList hibDistributionList = (SbiDistributionList) aSession.load(SbiDistributionList.class,
					new Integer(aDistributionList.getId()));	

			Set s = hibDistributionList.getSbiDistributionListUsers();
			Iterator i = s.iterator();
			while(i.hasNext()){
				SbiDistributionListUser dls =(SbiDistributionListUser) i.next();
				String userId = dls.getUserId();
				if (userId.equals(user)){
					aSession.delete(dls);
				}			
			}

			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while unsubscribing to the distribution list with id " + ((aDistributionList == null)?"":String.valueOf(aDistributionList.getId())), he);

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
	
	public void insertDLforDocument(DistributionList dl, int objId, String xml) throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			SbiDistributionListsObjects hibDistributionListsObjects = new SbiDistributionListsObjects();
			SbiDistributionList hibDistributionList = (SbiDistributionList) aSession.load(SbiDistributionList.class,
					new Integer(dl.getId()));
			SbiObjects hibObj = (SbiObjects) aSession.load(SbiObjects.class,new Integer(objId));
			
			hibDistributionListsObjects.setSbiDistributionList(hibDistributionList);
			hibDistributionListsObjects.setSbiObjects(hibObj);
			hibDistributionListsObjects.setXml(xml);
			
			aSession.save(hibDistributionListsObjects);
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while inserting the document to the distribution list with name " + ((dl == null)?"":String.valueOf(dl.getName())), he);

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
	
}
