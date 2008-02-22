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
package it.eng.spagobi.hotlink.rememberme.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.metadata.SbiDomains;
import it.eng.spagobi.engines.config.metadata.SbiEngines;
import it.eng.spagobi.hotlink.rememberme.bo.RememberMe;
import it.eng.spagobi.hotlink.rememberme.metadata.SbiRememberMe;

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
import org.hibernate.criterion.Distinct;
import org.hibernate.criterion.Expression;

/**
 * 
 * @author Zerbetto (davide.zerbetto@eng.it)
 *
 */
public class RememberMeDAOHibImpl extends AbstractHibernateDAO implements IRememberMeDAO {

	private static transient Logger logger = Logger.getLogger(RememberMeDAOHibImpl.class);
	
	public void delete(Integer rememberMeId) throws EMFInternalError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String hql = "from SbiRememberMe srm where srm.id=" + rememberMeId;
			Query query = aSession.createQuery(hql);
			SbiRememberMe hibObj = (SbiRememberMe) query.uniqueResult();
			if (hibObj == null) {
				logger.warn("SbiRememberMe with id = " + rememberMeId + " not found!");
				return;
			}
			aSession.delete(hibObj);
			tx.commit();
		} catch (HibernateException he) {
			logException(he);
			if (tx != null) tx.rollback();	
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "100");  
		} finally {
			if (aSession != null) {
				if (aSession.isOpen()) aSession.close();
			}
			logger.debug("OUT");
		}
	}

	public List getMyRememberMe(String userId) throws EMFInternalError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		List toReturn = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Criterion userIdCriterion = Expression.eq("userName", userId);
			Criteria criteria = aSession.createCriteria(SbiRememberMe.class);
			criteria.add(userIdCriterion);
			//criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List list = criteria.list();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				SbiRememberMe hibObj = (SbiRememberMe) it.next();
				toReturn.add(toRememberMe(hibObj));
			}
			return toReturn;
		} catch (HibernateException he) {
			logException(he);
			if (tx != null) tx.rollback();	
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "100");  
		} finally {
			if (aSession != null) {
				if (aSession.isOpen()) aSession.close();
			}
			logger.debug("OUT");
		}
	}

	private Object toRememberMe(SbiRememberMe hibObj) {
		RememberMe toReturn = new RememberMe();
		toReturn.setId(hibObj.getId());
		toReturn.setUserName(hibObj.getUserName());
		SbiObjects obj = hibObj.getSbiObject();
		toReturn.setObjId(obj.getBiobjId());
		toReturn.setDocumentLabel(obj.getLabel());
		toReturn.setDocumentName(obj.getName());
		toReturn.setDocumentDescription(obj.getDescr());
		SbiDomains docType = obj.getObjectType();
		toReturn.setDocumentType(docType.getValueCd());
		toReturn.setParameters(hibObj.getParameters());
		SbiEngines engine = obj.getSbiEngines();
		toReturn.setEngineName(engine.getName());
		return toReturn;
	}

	public boolean saveRememberMe(Integer docId, String userId, String parameters) throws EMFInternalError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Criterion userIdCriterion = Expression.eq("userName", userId);
			SbiObjects obj = (SbiObjects) aSession.load(SbiObjects.class, docId);
			Criterion docIdCriterion = Expression.eq("sbiObject", obj);
			Criterion parametersCriterion = Expression.eq("parameters", parameters);
			Criteria criteria = aSession.createCriteria(SbiRememberMe.class);
			criteria.add(userIdCriterion);
			criteria.add(docIdCriterion);
			criteria.add(parametersCriterion);
			//criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List list = criteria.list();
			if (list.isEmpty()) {
				SbiRememberMe temp = new SbiRememberMe();
				temp.setUserName(userId);
				temp.setSbiObject(obj);
				temp.setParameters(parameters);
				aSession.save(temp);
				tx.commit();
				return true;
			} else {
				logger.debug("RememberMe for user " + userId + " for document with id " + docId + " with parameters [" 
						+ parameters + "] is already present.");
				return false;
			}
		} catch (HibernateException he) {
			logException(he);
			if (tx != null) tx.rollback();	
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "100");  
		} finally {
			if (aSession != null) {
				if (aSession.isOpen()) aSession.close();
			}
			logger.debug("OUT");
		}
	}

}
