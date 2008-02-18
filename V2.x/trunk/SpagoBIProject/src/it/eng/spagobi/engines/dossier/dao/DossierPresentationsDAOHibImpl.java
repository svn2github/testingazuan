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
package it.eng.spagobi.engines.dossier.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.metadata.SbiBinContents;
import it.eng.spagobi.engines.dossier.bo.DossierPresentation;
import it.eng.spagobi.engines.dossier.metadata.SbiDossierPresentations;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * 
 * @author Zerbetto (davide.zerbetto@eng.it)
 *
 */
public class DossierPresentationsDAOHibImpl extends AbstractHibernateDAO implements IDossierPresentationsDAO {

	static private Logger logger = Logger.getLogger(DossierPresentationsDAOHibImpl.class);
	
	public byte[] getPresentationVersionContent(Integer dossierId,
			Integer versionId) throws EMFInternalError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		byte[] toReturn = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String hql = "from SbiDossierPresentations sdp where sdp.sbiObject.biobjId=" + dossierId + " and sdp.prog=" + versionId;
			Query query = aSession.createQuery(hql);
			SbiDossierPresentations hibObjTemp = (SbiDossierPresentations) query.uniqueResult();
			if (hibObjTemp == null) {
				return null;
			} else {
				toReturn = hibObjTemp.getSbiBinaryContent().getContent();
				return toReturn;
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
	
	public List getPresentationVersions(Integer dossierId) throws EMFInternalError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			// TODO controllare sintassi is not null
			String hql = "from SbiDossierPresentations sdp where sdp.sbiObject.biobjId=" + dossierId + " and prog is not null";
			Query query = aSession.createQuery(hql);
			List list = query.list();
			if (list == null) {
				return null;
			} else {
				List toReturn = new ArrayList();
				Iterator it = list.iterator();
				while (it.hasNext()) {
					SbiDossierPresentations presentation = (SbiDossierPresentations) it.next();
					toReturn.add(toDossierPresentation(presentation));
				}
				return toReturn;
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

	public void deletePresentationVersion(Integer dossierId, Integer versionId) throws EMFInternalError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String hql = "from SbiDossierPresentations sdp where sdp.sbiObject.biobjId=" + dossierId + " and sdp.prog=" + versionId;
			Query query = aSession.createQuery(hql);
			SbiDossierPresentations hibObjTemp = (SbiDossierPresentations) query.uniqueResult();
			if (hibObjTemp != null) aSession.delete(hibObjTemp);
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
	
	public void insertPresentation(DossierPresentation dossierPresentation) throws EMFUserError, EMFInternalError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			// store the binary content
			SbiBinContents hibBinContent = new SbiBinContents();
			byte[] bytes = null;
			try {
				bytes = dossierPresentation.getContent();
			} catch (EMFInternalError e) {
				logger.error("Could not retrieve content of DossierPresentation object in input.");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
			hibBinContent.setContent(bytes);
			Integer idBin = (Integer)aSession.save(hibBinContent);
			// recover the saved binary hibernate object
			hibBinContent = (SbiBinContents) aSession.load(SbiBinContents.class, idBin);
			// recover the associated biobject
			SbiObjects obj = (SbiObjects) aSession.load(SbiObjects.class, dossierPresentation.getBiobjectId());
			// store the object template
			SbiDossierPresentations hibObj = new SbiDossierPresentations();
			hibObj.setWorkflowProcessId(dossierPresentation.getWorkflowProcessId());
			hibObj.setCreationDate(new Date());
			hibObj.setName(dossierPresentation.getName());
			hibObj.setProg(null);
			hibObj.setSbiBinaryContent(hibBinContent);
			hibObj.setSbiObject(obj);
			hibObj.setApproved(null);
			aSession.save(hibObj);
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
	
	public void updatePresentation(DossierPresentation dossierPresentation) throws EMFInternalError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiDossierPresentations hibObj = (SbiDossierPresentations) aSession.load(SbiDossierPresentations.class, 
					dossierPresentation.getId());
			hibObj.setProg(dossierPresentation.getProg());
			boolean approved = dossierPresentation.getApproved().booleanValue();
			hibObj.setApproved(approved ? new Short((short) 1) : new Short((short) 0));
			aSession.save(hibObj);
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
	
	public Integer getNextProg(Integer dossierId) throws EMFInternalError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String hql = "select max(sdp.prog) as maxprog from SbiDossierPresentations sdp where sdp.sbiObject.biobjId=" 
				+ dossierId;
			Query query = aSession.createQuery(hql);
			Integer maxProg = (Integer) query.uniqueResult();
			Integer nextProg = null;
			if (maxProg == null) {
				nextProg = new Integer(1);
			} else {
				nextProg = new Integer(maxProg.intValue() + 1);
			}
			return nextProg;
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
	
	public DossierPresentation getCurrentPresentation(Integer dossierId, Long workflowProcessId) throws EMFInternalError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		DossierPresentation toReturn = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			// TODO mettere i criteria
			String hql = "from SbiDossierPresentations sdp where sdp.sbiObject.biobjId=" + dossierId + " " +
					"and sdp.workflowProcessId=" + workflowProcessId;
			Query query = aSession.createQuery(hql);
			SbiDossierPresentations hibObjTemp = (SbiDossierPresentations) query.uniqueResult();
			if (hibObjTemp == null) {
				return null;
			} else {
				toReturn = toDossierPresentation(hibObjTemp);
				return toReturn;
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
	
	
	private DossierPresentation toDossierPresentation(SbiDossierPresentations presentation) {
		DossierPresentation toReturn = new DossierPresentation();
		toReturn.setId(presentation.getPresentationId());
		toReturn.setWorkflowProcessId(presentation.getWorkflowProcessId());
		toReturn.setBinId(presentation.getSbiBinaryContent().getId());
		toReturn.setBiobjectId(presentation.getSbiObject().getBiobjId());
		Short approvedFl = presentation.getApproved();
		if (approvedFl == null) {
			toReturn.setApproved(null);
		} else {
			boolean approved = presentation.getApproved().shortValue() == 1;
			toReturn.setApproved(new Boolean(approved));
		}
		toReturn.setCreationDate(presentation.getCreationDate());
		toReturn.setName(presentation.getName());
		toReturn.setProg(presentation.getProg());
		return toReturn;
	}
	
}
