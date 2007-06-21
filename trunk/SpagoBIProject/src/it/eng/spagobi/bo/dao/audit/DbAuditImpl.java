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
package it.eng.spagobi.bo.dao.audit;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.dao.IAuditDAO;
import it.eng.spagobi.bo.dao.hibernate.AbstractHibernateDAO;
import it.eng.spagobi.metadata.SbiAudit;
import it.eng.spagobi.metadata.SbiEngines;
import it.eng.spagobi.metadata.SbiObjects;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DbAuditImpl extends AbstractHibernateDAO implements IAuditDAO {
	
	/**
	 * @see it.eng.spagobi.bo.dao.IAuditDAO#insertAudit(it.eng.spagobi.bo.SbiAudit)
	 * 
	 */
	public void insertAudit(SbiAudit aSbiAudit) throws EMFUserError {
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
			logException(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (session != null) {
				if (session.isOpen()) session.close();
			}
		}
	}

	public List loadAllAudits() throws EMFUserError {
		// TODO Auto-generated method stub
		return null;
	}

	public SbiAudit loadAuditByID(Integer id) throws EMFUserError {
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
			logException(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		return aSbiAudit;
	}

	public void modifyAudit(SbiAudit aSbiAudit) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			// TODO forse mettere un controllo per vedere se ci sono sbiobject e sbiengine?
			aSession.saveOrUpdate(aSbiAudit);
			tx.commit();
		} catch (HibernateException he) {
			logException(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}	

	}

	public List loadAuditsByDocumentLabel(String documentLabel) throws EMFUserError {
		// TODO Auto-generated method stub
		return null;
	}

	public List loadAuditsByEngineLabel(String engineLabel) throws EMFUserError {
		// TODO Auto-generated method stub
		return null;
	}

	public List loadAuditsByUserName(String userName) throws EMFUserError {
		// TODO Auto-generated method stub
		return null;
	}

	public void eraseAudit(Integer id) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();			
			SbiAudit sbiAudit = (SbiAudit) aSession.load(SbiAudit.class, id);
			aSession.delete(sbiAudit);
			tx.commit();
		} catch (HibernateException he) {
			logException(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} catch (Exception ex) {
			logException(ex);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100); 
		}	finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		
	}

}
