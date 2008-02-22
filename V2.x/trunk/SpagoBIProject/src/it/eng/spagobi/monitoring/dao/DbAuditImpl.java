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
package it.eng.spagobi.monitoring.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.engines.config.metadata.SbiEngines;
import it.eng.spagobi.hotlink.rememberme.bo.HotLink;
import it.eng.spagobi.monitoring.metadata.SbiAudit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DbAuditImpl extends AbstractHibernateDAO implements IAuditDAO {
	
	private static transient Logger logger = Logger.getLogger(DbAuditImpl.class);
	
	/**
	 * @see it.eng.spagobi.monitoring.dao.IAuditDAO#insertAudit(it.eng.spagobi.bo.SbiAudit)
	 * 
	 */
	public void insertAudit(SbiAudit aSbiAudit) throws EMFUserError {
		logger.debug("IN");
		Session session = null;
		Transaction tx = null;
		try {
			session = getSession();;
			tx = session.beginTransaction();
			if (aSbiAudit.getSbiObject() == null) {
				Integer objectId = aSbiAudit.getDocumentId();
				SbiObjects sbiObject = (SbiObjects) session.load(SbiObjects.class, objectId);
				aSbiAudit.setSbiObject(sbiObject);
			}
			if (aSbiAudit.getSbiEngine() == null) {
				Integer engineId = aSbiAudit.getEngineId();
				SbiEngines sbiEngine = (SbiEngines) session.load(SbiEngines.class, engineId);
				aSbiAudit.setSbiEngine(sbiEngine);
			}
			session.save(aSbiAudit);
			session.flush();
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (session != null) {
				if (session.isOpen()) session.close();
			}
			logger.debug("OUT");
		}
	}

	public List loadAllAudits() throws EMFUserError {
		logger.debug("IN");
		logger.error("this method is not implemented!!");
		logger.debug("OUT");
		return null;
	}

	public SbiAudit loadAuditByID(Integer id) throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		SbiAudit aSbiAudit = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			aSbiAudit = (SbiAudit) aSession.load(SbiAudit.class, id);
			aSbiAudit.getSbiObject();
			aSbiAudit.getDocumentLabel();
			aSbiAudit.getDocumentId();
			aSbiAudit.getDocumentName();
			aSbiAudit.getDocumentParameters();
			aSbiAudit.getDocumentState();
			aSbiAudit.getDocumentType();
			aSbiAudit.getSbiEngine();
			aSbiAudit.getEngineClass();
			aSbiAudit.getEngineDriver();
			aSbiAudit.getEngineId();
			aSbiAudit.getEngineLabel();
			aSbiAudit.getEngineName();
			aSbiAudit.getEngineType();
			aSbiAudit.getEngineUrl();
			aSbiAudit.getExecutionModality();
			aSbiAudit.getRequestTime();
			aSbiAudit.getId();
			aSbiAudit.getUserName();
			aSbiAudit.getUserGroup();
			aSbiAudit.getExecutionStartTime();
			aSbiAudit.getExecutionEndTime();
			aSbiAudit.getExecutionTime();
			aSbiAudit.getExecutionState();
			aSbiAudit.getError();
			aSbiAudit.getErrorMessage();
			aSbiAudit.getErrorCode();
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
			logger.debug("OUT");
		}
		return aSbiAudit;
	}

	public void modifyAudit(SbiAudit aSbiAudit) throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			// TODO forse mettere un controllo per vedere se ci sono sbiobject e sbiengine?
			aSession.saveOrUpdate(aSbiAudit);
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
			logger.debug("OUT");
		}	

	}

	public List loadAuditsByDocumentLabel(String documentLabel) throws EMFUserError {
		logger.debug("IN");
		logger.error("this method is not implemented!!");
		logger.debug("OUT");
		return null;
	}

	public List loadAuditsByEngineLabel(String engineLabel) throws EMFUserError {
		logger.debug("IN");
		logger.error("this method is not implemented!!");
		logger.debug("OUT");
		return null;
	}

	public List loadAuditsByUserName(String userName) throws EMFUserError {
		logger.debug("IN");
		logger.error("this method is not implemented!!");
		logger.debug("OUT");
		return null;
	}

	public void eraseAudit(Integer id) throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();			
			SbiAudit sbiAudit = (SbiAudit) aSession.load(SbiAudit.class, id);
			aSession.delete(sbiAudit);
			tx.commit();
		} catch (HibernateException he) {
			logger.error(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} catch (Exception ex) {
			logger.error(ex);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100); 
		}	finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
			logger.debug("OUT");
		}
		
	}

	public List getMostPopular(Collection roles, int limit) throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		List toReturn = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String usergroups = "";
			Iterator it = roles.iterator();
			while (it.hasNext()) {
				String roleName = (String) it.next();
				usergroups += "'" + roleName + "'";
				if (it.hasNext()) usergroups += ",";
			}
			StringBuffer hql = new StringBuffer();
			hql.append("select ");
			hql.append("		count(a.sbiObject.biobjId), ");
			hql.append(	"		a.sbiObject.biobjId, ");
			hql.append(	"		a.sbiObject.label, ");
			hql.append(	"		a.sbiObject.name, ");
			hql.append(	"		a.sbiObject.descr, ");
			hql.append(	"		a.sbiObject.objectTypeCode, ");
			hql.append(	"		a.documentParameters, ");
			hql.append(	"		a.sbiEngine.name "); 
			hql.append(	"from ");
			hql.append(	"		SbiAudit a ");
			hql.append(	"where 	");
			hql.append(	"		a.sbiObject is not null and ");
			hql.append(	"		a.sbiEngine is not null and ");
			hql.append(	"		a.sbiObject.label not like 'SBI_%' and ");
			hql.append(	"		a.userGroup in (" + usergroups + ") ");
			hql.append(	"group by 	a.sbiObject.biobjId, ");
			hql.append(	"			a.sbiObject.label, ");
			hql.append(	"			a.sbiObject.name, ");
			hql.append(	"			a.sbiObject.descr, ");
			hql.append(	"			a.sbiObject.objectTypeCode, ");
			hql.append(	"			a.documentParameters, ");
			hql.append(	"			a.sbiEngine.name ");
			hql.append(	"order by count(a.sbiObject.biobjId) desc ");
			Query hqlQuery = aSession.createQuery(hql.toString());
			hqlQuery.setMaxResults(limit);
			List result = hqlQuery.list();
			Iterator resultIt = result.iterator();
			while (resultIt.hasNext()) {
				Object[] row = (Object[]) resultIt.next();
				toReturn.add(toHotLink(row));
			}
		} catch (Exception ex) {
			logger.error(ex);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100); 
		} finally {
			if (aSession != null){
				if (aSession.isOpen()) aSession.close();
			}
			logger.debug("OUT");
		}
		return toReturn;
	}

	public List getMyRecentlyUsed(String userId, int limit) throws EMFUserError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		List toReturn = new ArrayList();
		if (userId == null || userId.trim().equals("")) {
			logger.warn("The user id in input is null or empty.");
			return toReturn;
		}
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			StringBuffer hql = new StringBuffer();
			hql.append(	"select ");
			hql.append(	"		max(a.request_time) as t, ");
			hql.append(	"		a.sbiObject.biobjId, ");
			hql.append(	"		a.sbiObject.label, ");
			hql.append(	"		a.sbiObject.name, ");
			hql.append(	"		a.sbiObject.descr, ");
			hql.append(	"		a.sbiObject.objectTypeCode, ");
			hql.append(	"		a.documentParameters, ");
			hql.append(	"		a.sbiEngine.name "); 
			hql.append(	"from ");
			hql.append(	"		SbiAudit a ");
			hql.append(	"where 	");
			hql.append(	"		a.sbiObject is not null and ");
			hql.append(	"		a.sbiEngine is not null and ");
			hql.append(	"		a.sbiObject.label not like 'SBI_%' and ");
			hql.append(	"		a.userName = '" + userId + "' ");
			hql.append(	"group by 	a.sbiObject.biobjId, ");
			hql.append(	"			a.sbiObject.label, ");
			hql.append(	"			a.sbiObject.name, ");
			hql.append(	"			a.sbiObject.descr, ");
			hql.append(	"			a.sbiObject.objectTypeCode, ");
			hql.append(	"			a.documentParameters, ");
			hql.append(	"			a.sbiEngine.name ");
			hql.append(	"order by t desc ");
			Query hqlQuery = aSession.createQuery(hql.toString());
			hqlQuery.setMaxResults(limit);
			List result = hqlQuery.list();
			Iterator resultIt = result.iterator();
			while (resultIt.hasNext()) {
				Object[] row = (Object[]) resultIt.next();
				toReturn.add(toHotLink(row));
			}
		} catch (Exception ex) {
			logger.error(ex);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100); 
		} finally {
			if (aSession != null){
				if (aSession.isOpen()) aSession.close();
			}
			logger.debug("OUT");
		}
		return toReturn;
	}
	
	private Object toHotLink(Object[] row) {
		HotLink toReturn = new HotLink();
		toReturn.setObjId((Integer) row[1]);
		toReturn.setDocumentLabel((String) row[2]);
		toReturn.setDocumentName((String) row[3]);
		toReturn.setDocumentDescription((String) row[4]);
		toReturn.setDocumentType((String) row[5]);
		toReturn.setParameters((String) row[6]);
		toReturn.setEngineName((String) row[7]);
		return toReturn;
	}

}
