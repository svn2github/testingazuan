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
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.engines.dossier.metadata.SbiDossierBinaryContentsTemp;
import it.eng.spagobi.engines.dossier.metadata.SbiDossierPartsTemp;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
public class DossierPartsTempDAOHibImpl extends AbstractHibernateDAO implements IDossierPartsTempDAO {

	static public final String IMAGE = "IMAGE";
	static public final String NOTE = "NOTE";
	
	static private Logger logger = Logger.getLogger(DossierPartsTempDAOHibImpl.class);
	
	public Map getImagesOfDossierPart(Integer dossierId, int pageNum, Long workflowProcessId) throws EMFInternalError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		Integer pageId = new Integer(pageNum);
		Map toReturn = new HashMap();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String hql = "from SbiDossierBinaryContentsTemp binTemp where binTemp.sbiDossierPartsTemp.pageId=" + pageId.toString() +
					" and binTemp.sbiDossierPartsTemp.sbiObject.biobjId=" + dossierId + 
					" and binTemp.sbiDossierPartsTemp.workflowProcessId = " + workflowProcessId + 
					" and binTemp.type='" + IMAGE + "'";
			Query query = aSession.createQuery(hql);
			List list = query.list();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				SbiDossierBinaryContentsTemp hibObjTemp = (SbiDossierBinaryContentsTemp) it.next();
				toReturn.put(hibObjTemp.getName(), hibObjTemp.getBinContent());
			}
			return toReturn;
		} catch (HibernateException he) {
			logger.error("Error while storing image content: ", he);
			if (tx != null) tx.rollback();	
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "100");  
		} finally {
			if (aSession != null) {
				if (aSession.isOpen()) aSession.close();
			}
			logger.debug("OUT");
		}
	}

	public void storeNote(Integer dossierId, int pageNum, byte[] noteContent, Long workflowProcessId) throws EMFInternalError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		Integer pageId = new Integer(pageNum);
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String hql = "from SbiDossierPartsTemp partTemp where partTemp.sbiObject.biobjId=" + dossierId + " " +
					"and partTemp.pageId=" + pageId.toString() + " and partTemp.workflowProcessId = " + workflowProcessId;
			Query query = aSession.createQuery(hql);
			SbiDossierPartsTemp hibObjTemp = (SbiDossierPartsTemp) query.uniqueResult();
			if (hibObjTemp == null) {
				hibObjTemp = new SbiDossierPartsTemp();
				SbiObjects objHib = (SbiObjects) aSession.load(SbiObjects.class, dossierId);
				hibObjTemp.setSbiObject(objHib);
				hibObjTemp.setPageId(pageId);
				hibObjTemp.setSbiDossierBinaryContentsTemps(new HashSet());
				hibObjTemp.setWorkflowProcessId(workflowProcessId);
				aSession.save(hibObjTemp);
			}
			hql = "from SbiDossierBinaryContentsTemp binTemp where binTemp.sbiDossierPartsTemp.pageId=" + pageId.toString() +
				" and binTemp.sbiDossierPartsTemp.sbiObject.biobjId=" + dossierId + 
				" and binTemp.sbiDossierPartsTemp.workflowProcessId = " + workflowProcessId + 
				" and binTemp.type='" + NOTE + "'";
			query = aSession.createQuery(hql);
			SbiDossierBinaryContentsTemp temp = (SbiDossierBinaryContentsTemp) query.uniqueResult();
			if (temp != null) {
				// updates note row
				temp.setBinContent(noteContent);
				temp.setCreationDate(new Date());
			} else {
				// creates a new note row
				temp = new SbiDossierBinaryContentsTemp();
				temp.setSbiDossierPartsTemp(hibObjTemp);
				temp.setBinContent(noteContent);
				temp.setCreationDate(new Date());
				temp.setName(NOTE);
				temp.setType(NOTE);
			}
			aSession.save(temp);
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while storing image content: ", he);
			if (tx != null) tx.rollback();	
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "100");  
		} finally {
			if (aSession != null) {
				if (aSession.isOpen()) aSession.close();
			}
			logger.debug("OUT");
		}
	}
	
	public void storeImage(Integer dossierId, byte[] image,
			String docLogicalName, int pageNum, Long workflowProcessId) throws EMFInternalError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		Integer pageId = new Integer(pageNum);
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String hql = "from SbiDossierPartsTemp partTemp where partTemp.sbiObject.biobjId=" + dossierId + " " +
					"and partTemp.pageId=" + pageId.toString() + " and partTemp.workflowProcessId = " + workflowProcessId;
			Query query = aSession.createQuery(hql);
			SbiDossierPartsTemp hibObjTemp = (SbiDossierPartsTemp) query.uniqueResult();
			if (hibObjTemp == null) {
				hibObjTemp = new SbiDossierPartsTemp();
				SbiObjects objHib = (SbiObjects) aSession.load(SbiObjects.class, dossierId);
				hibObjTemp.setSbiObject(objHib);
				hibObjTemp.setPageId(pageId);
				hibObjTemp.setSbiDossierBinaryContentsTemps(new HashSet());
				hibObjTemp.setWorkflowProcessId(workflowProcessId);
				aSession.save(hibObjTemp);
			}
			SbiDossierBinaryContentsTemp temp = new SbiDossierBinaryContentsTemp();
			temp.setSbiDossierPartsTemp(hibObjTemp);
			temp.setBinContent(image);
			temp.setCreationDate(new Date());
			temp.setName(docLogicalName);
			temp.setType(IMAGE);
			aSession.save(temp);
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while storing image content: ", he);
			if (tx != null) tx.rollback();	
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "100");  
		} finally {
			if (aSession != null) {
				if (aSession.isOpen()) aSession.close();
			}
			logger.debug("OUT");
		}
	}
	
	public byte[] getNotesOfDossierPart(Integer dossierId, int pageNum, Long workflowProcessId) throws EMFInternalError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		Integer pageId = new Integer(pageNum);
		byte[] toReturn = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String hql = "from SbiDossierBinaryContentsTemp binTemp where binTemp.sbiDossierPartsTemp.pageId=" + pageId.toString() +
					" and binTemp.sbiDossierPartsTemp.sbiObject.biobjId=" + dossierId + 
					" and binTemp.sbiDossierPartsTemp.workflowProcessId = " + workflowProcessId + 
					" and binTemp.type='" + NOTE + "'";
			Query query = aSession.createQuery(hql);
			SbiDossierBinaryContentsTemp hibObjTemp = (SbiDossierBinaryContentsTemp) query.uniqueResult();
			if (hibObjTemp != null) toReturn = hibObjTemp.getBinContent();
			return toReturn;
		} catch (HibernateException he) {
			logger.error("Error while storing image content: ", he);
			if (tx != null) tx.rollback();	
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "100");  
		} finally {
			if (aSession != null) {
				if (aSession.isOpen()) aSession.close();
			}
			logger.debug("OUT");
		}
	}

	public void cleanDossierParts(Integer dossierId, Long workflowProcessId)
			throws EMFInternalError {
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		byte[] toReturn = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String hql = "from SbiDossierPartsTemp partTemp where partTemp.sbiObject.biobjId=" + dossierId + " " +
				" and partTemp.workflowProcessId = " + workflowProcessId;
			Query query = aSession.createQuery(hql);
			List list = query.list();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				SbiDossierPartsTemp hibObj = (SbiDossierPartsTemp) it.next();
				aSession.delete(hibObj);
				// the temporary binary contents in table SbiDossierBinaryContentsTemp are deleted because 
				// the foreign key is defined with the ON DELETE CASCADE clause
			}
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while storing image content: ", he);
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
