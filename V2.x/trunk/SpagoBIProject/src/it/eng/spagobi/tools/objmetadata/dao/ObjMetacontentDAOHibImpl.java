/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
/*
 * Created on 20-giu-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.tools.objmetadata.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiSubObjects;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.metadata.SbiBinContents;
import it.eng.spagobi.tools.objmetadata.bo.ObjMetacontent;
import it.eng.spagobi.tools.objmetadata.metadata.SbiObjMetacontents;

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
 * Defines the Hibernate implementations for all DAO methods,
 * for a metadata content
 */
public class ObjMetacontentDAOHibImpl extends AbstractHibernateDAO implements IObjMetacontentDAO{
	static private Logger logger = Logger.getLogger(ObjMetacontentDAOHibImpl.class);
	
	/**
	 * Load object's metadata content by id.
	 * 
	 * @param id the identifier
	 * 
	 * @return the metadata content
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.tools.objmetadata.dao.IObjMetacontentDAO#loadObjMetaContentByID(java.lang.Integer)
	 */
	public ObjMetacontent loadObjMetaContentByID(Integer id) throws EMFUserError {		
		logger.debug("IN");
		
		ObjMetacontent toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiObjMetacontents hibContent = (SbiObjMetacontents)aSession.load(SbiObjMetacontents.class,  id);
			toReturn = toObjMetacontent(hibContent);
			tx.commit();
			
		} catch (HibernateException he) {
			logger.error("Error while loading the data source with id " + id.toString(), he);			

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

	/**
	 * Load object's metadata by objMetaId.
	 * 
	 * @param objMetaId the objMetaId
	 * 
	 * @return A list containing all metacontent of specific metadata
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.tools.objmetadata.dao.IObjMetacontentDAO#loadObjMetacontentByObjMetaId(java.lang.Integer)
	 */	
	public List loadObjMetacontentByObjMetaId(Integer objMetaId) throws EMFUserError {
	
		logger.debug("IN");
		List realResult = new ArrayList();
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			
			String hql = " from SbiObjMetacontents c where c.objmetaId = ?";
			Query aQuery = tmpSession.createQuery(hql);
			aQuery.setInteger(0, objMetaId.intValue());
			List hibList = aQuery.list();			
			if (hibList == null) return null;			
			
			Iterator it = hibList.iterator();
			while (it.hasNext()) {
				realResult.add(toObjMetacontent((SbiObjMetacontents) it.next()));
			}
			
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while loading the metadata content list with metaid " + objMetaId, he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}
		}
		logger.debug("OUT");
		return realResult;	

	}

	/**
	 * Load object's metadata by objMetaId and biObjId.
	 * 
	 * @param objMetaId the objMetaId
	 * @param biObjId the biObjId
	 * 
	 * @return A list containing all metadata contents objects of a specific biobjid
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.tools.objmetadata.dao.IObjMetacontentDAO#loadObjMetacontentByObjId(java.lang.Integer, java.lang.Integer)
	 */	
	public List loadObjMetacontentByObjId(Integer objMetaId, Integer biObjId) throws EMFUserError {
		logger.debug("IN");
		List realResult = new ArrayList();
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			
			String hql = " from SbiObjMetacontents c where c.objmetaId = ? and c.biobjId = ? ";
			Query aQuery = tmpSession.createQuery(hql);
			aQuery.setInteger(0, objMetaId.intValue());
			aQuery.setInteger(1, biObjId.intValue());
			List hibList = aQuery.list();			
			if (hibList == null) return null;
			
			Iterator it = hibList.iterator();
			while (it.hasNext()) {
				realResult.add(toObjMetacontent((SbiObjMetacontents) it.next()));
			}
			
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while loading the metadata content list with metaid " + objMetaId, he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}
		}
		logger.debug("OUT");
		return realResult;	

	}
	
	/**
	 * Load object's metadata by objMetaId, biObjId and subobjId.
	 * 
	 * @param objMetaId the objMetaId
	 * @param biObjId the biObjId
	 * @param subObjId the subObjId
	 * 
	 * @return A list containing all metadata contents objects of a specific subObjId
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.tools.objmetadata.dao.IObjMetacontentDAO#loadObjMetacontentByObjId(java.lang.Integer, java.lang.Integer)
	 */	
	public ObjMetacontent loadObjMetacontentBySubobjId(Integer objMetaId, Integer biObjId, Integer subObjId) throws EMFUserError{
		logger.debug("IN");
		ObjMetacontent realResult = null;
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			
			String hql = " from SbiObjMetacontents c where c.objmetaId = ? and c.biobjId = ? ";
			if(subObjId!=null){
				hql += "and c.subobjId = ? ";
			}
			Query aQuery = tmpSession.createQuery(hql);
			aQuery.setInteger(0, objMetaId.intValue());
			aQuery.setInteger(1, biObjId.intValue());
			if(subObjId!=null){
				aQuery.setInteger(2, subObjId.intValue());
			}
			SbiObjMetacontents res =(SbiObjMetacontents) aQuery.uniqueResult();
			realResult = toObjMetacontent(res);

		} catch (HibernateException he) {
			logger.error("Error while loading the metadata content list with metaid " + objMetaId, he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}
		}
		logger.debug("OUT");
		return realResult;	

	}
	/**
	 * Load all metadata content.
	 * 
	 * @return the list
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.tools.objmetadata.dao.IObjMetacontentDAO#loadAllObjMetacontent()
	 */
	public List loadAllObjMetacontent() throws EMFUserError {
		
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			Query hibQuery = aSession.createQuery(" from SbiObjMetacontents");
			
			List hibList = hibQuery.list();
			Iterator it = hibList.iterator();

			while (it.hasNext()) {
				realResult.add(toObjMetacontent((SbiObjMetacontents) it.next()));
			}
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while loading all meta contents ", he);

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
	
	/**
	 * Modify metadata content.
	 * 
	 * @param aObjMetacontent the meta content
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.tools.objmetadata.dao.IObjMetadataDAO#modifyObjMetacontent(it.eng.spagobi.tools.objmetadata.bo.ObjMetacontent)
	 */
	public void modifyObjMetacontent(ObjMetacontent aObjMetacontent) throws EMFUserError {
		
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		Criterion aCriterion = null;
		Criteria criteria = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
		
			SbiObjMetacontents hibContents = (SbiObjMetacontents) aSession.load(SbiObjMetacontents.class,
					new Integer(aObjMetacontent.getObjMetacontentId()));	
			
			//gets foreign object 		
			aCriterion = Expression.eq("biobjId",	aObjMetacontent.getBiobjId());
			criteria = aSession.createCriteria(SbiObjects.class);
			criteria.add(aCriterion);
			SbiObjects biobj = (SbiObjects) criteria.uniqueResult();
			
			//gets foreign subobject 		
			aCriterion = Expression.eq("subobjId", aObjMetacontent.getSubobjId());
			criteria = aSession.createCriteria(SbiSubObjects.class);
			criteria.add(aCriterion);
			SbiSubObjects subobj = (SbiSubObjects) criteria.uniqueResult();
			
			//gets foreign binary content 		
			aCriterion = Expression.eq("id", aObjMetacontent.getBinaryContentId());
			criteria = aSession.createCriteria(SbiBinContents.class);
			criteria.add(aCriterion);
			SbiBinContents bincontent = (SbiBinContents) criteria.uniqueResult();
			
			hibContents.setObjmetaId(aObjMetacontent.getObjmetaId());
			hibContents.setSbiObjects(biobj);
			hibContents.setSbiSubObjects(subobj);			
			hibContents.setSbiBinContents(bincontent);
			hibContents.setLastChangeDate(aObjMetacontent.getLastChangeDate());
			
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while modifing the meta content with id " + ((aObjMetacontent == null)?"":String.valueOf(aObjMetacontent.getObjMetacontentId())), he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		}finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
				logger.debug("OUT");
			}
		}
	
	}

	/**
	 * Insert object's metadata content.
	 * 
	 * @param aObjMetacontent the metadata content
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.tools.objmetadata.dao.IObjMetadataDAO#insertObjMetacontent(it.eng.spagobi.tools.objmetadata.bo.ObjMetacontent)
	 */
	public void insertObjMetacontent(ObjMetacontent aObjMetacontent) throws EMFUserError {
	
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		Criterion aCriterion = null;
		Criteria criteria = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			SbiObjMetacontents hibContents = (SbiObjMetacontents) aSession.load(SbiObjMetacontents.class,
					new Integer(aObjMetacontent.getObjMetacontentId()));	
			

			//gets foreign object 		
			aCriterion = Expression.eq("biobjId",	aObjMetacontent.getBiobjId());
			criteria = aSession.createCriteria(SbiObjects.class);
			criteria.add(aCriterion);
			SbiObjects biobj = (SbiObjects) criteria.uniqueResult();
			
			//gets foreign subobject 		
			aCriterion = Expression.eq("subobjId", aObjMetacontent.getSubobjId());
			criteria = aSession.createCriteria(SbiSubObjects.class);
			criteria.add(aCriterion);
			SbiSubObjects subobj = (SbiSubObjects) criteria.uniqueResult();
			
			//gets foreign binary content 		
			aCriterion = Expression.eq("id", aObjMetacontent.getBinaryContentId());
			criteria = aSession.createCriteria(SbiBinContents.class);
			criteria.add(aCriterion);
			SbiBinContents bincontent = (SbiBinContents) criteria.uniqueResult();
			
			hibContents.setObjmetaId(aObjMetacontent.getObjmetaId());
			hibContents.setSbiObjects(biobj);
			hibContents.setSbiSubObjects(subobj);			
			hibContents.setSbiBinContents(bincontent);
			hibContents.setLastChangeDate(aObjMetacontent.getLastChangeDate());
			
			aSession.save(hibContents);
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while inserting the metadata content with id " + ((aObjMetacontent == null)?"":String.valueOf(aObjMetacontent.getObjMetacontentId())), he);

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

	/**
	 * Erase object's metadata content
	 * 
	 * @param ObjMetacontent the metadata content
	 * 
	 * @throws EMFUserError the EMF user error
	 * 
	 * @see it.eng.spagobi.tools.objmetadata.dao.IObjMetadataDAO#eraseObjMetadata(it.eng.spagobi.tools.objmetadata.bo.ObjMetacontent)
	 */
	public void eraseObjMetadata(ObjMetacontent aObjMetacontent) throws EMFUserError{
		logger.debug("IN");
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiObjMetacontents hibContents = (SbiObjMetacontents) aSession.load(SbiObjMetacontents.class,
					new Integer(aObjMetacontent.getObjMetacontentId()));

			aSession.delete(hibContents);
			tx.commit();
		} catch (HibernateException he) {
			logger.error("Error while erasing the data source with id " + ((aObjMetacontent == null)?"":String.valueOf(aObjMetacontent.getObjMetacontentId())), he);

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
	
	/**
	 * From the hibernate SbiObjMetacontent at input, gives
	 * the corrispondent <code>ObjMetacontent</code> object.
	 * 
	 * @param hibObjMetadata The hybernate metadata content
	 * 
	 * @return The corrispondent <code>ObjMetacontent</code> object
	 */
	private ObjMetacontent toObjMetacontent(SbiObjMetacontents hibObjMetacontent){
		ObjMetacontent meta = new ObjMetacontent();
		
		meta.setObjMetacontentId(hibObjMetacontent.getObjMetacontentId());
		meta.setObjmetaId(hibObjMetacontent.getObjmetaId());
		meta.setBiobjId(hibObjMetacontent.getSbiObjects().getBiobjId());
		meta.setSubobjId(hibObjMetacontent.getSbiSubObjects().getSubObjId());
		meta.setBinaryContentId(hibObjMetacontent.getSbiBinContents().getId());
		meta.setContent(hibObjMetacontent.getSbiBinContents().getContent()); 
		meta.setCreationDate(hibObjMetacontent.getCreationDate());
		meta.setLastChangeDate(hibObjMetacontent.getLastChangeDate());

		return meta;
	}
	
}


