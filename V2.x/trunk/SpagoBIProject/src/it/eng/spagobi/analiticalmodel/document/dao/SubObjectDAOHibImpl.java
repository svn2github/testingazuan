/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.analiticalmodel.document.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.SubObject;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiSubObjects;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.metadata.SbiBinContents;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SubObjectDAOHibImpl extends AbstractHibernateDAO implements ISubObjectDAO {

	static private Logger logger = Logger.getLogger(SubObjectDAOHibImpl.class);
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.analiticalmodel.document.dao.ISubObjectDAO#getAccessibleSubObjects(java.lang.Integer, it.eng.spago.security.IEngUserProfile)
	 */
	public List getAccessibleSubObjects(Integer idBIObj, IEngUserProfile profile) throws EMFUserError {
		List subs = new ArrayList();
		Session aSession = null;
		Transaction tx = null;		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String hql = "from SbiSubObjects sso where sso.sbiObject.biobjId="+idBIObj + " " +
						 "and (isPublic = true or owner = '"+profile.getUserUniqueIdentifier().toString()+"')";
			Query query = aSession.createQuery(hql);
			List result = query.list();
			Iterator it = result.iterator();
			while (it.hasNext()){
				subs.add(toSubobject((SbiSubObjects)it.next()));
			}
			tx.commit();
		}catch(HibernateException he){
			logger.error(he);
			if (tx != null) tx.rollback();	
			throw new EMFUserError(EMFErrorSeverity.ERROR, "100");  
		}finally{
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		return subs;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.analiticalmodel.document.dao.ISubObjectDAO#getSubObjects(java.lang.Integer)
	 */
	public List getSubObjects(Integer idBIObj) throws EMFUserError {
		List subs = new ArrayList();
		Session aSession = null;
		Transaction tx = null;		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String hql = "from SbiSubObjects sso where sso.sbiObject.biobjId="+idBIObj;
			Query query = aSession.createQuery(hql);
			List result = query.list();
			Iterator it = result.iterator();
			while (it.hasNext()){
				subs.add(toSubobject((SbiSubObjects)it.next()));
			}
			tx.commit();
		}catch(HibernateException he){
			logger.error(he);
			if (tx != null) tx.rollback();	
			throw new EMFUserError(EMFErrorSeverity.ERROR, "100");  
		}finally{
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		return subs;
	}

	
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.analiticalmodel.document.dao.ISubObjectDAO#deleteSubObject(java.lang.Integer)
	 */
	public void deleteSubObject(Integer idSub) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiSubObjects hibSubobject = (SbiSubObjects)aSession.load(SbiSubObjects.class, idSub);
			SbiBinContents hibBinCont = hibSubobject.getSbiBinContents();
			aSession.delete(hibSubobject);
			aSession.delete(hibBinCont);
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

	/* (non-Javadoc)
	 * @see it.eng.spagobi.analiticalmodel.document.dao.ISubObjectDAO#getSubObject(java.lang.Integer)
	 */
	public SubObject getSubObject(Integer idSubObj) throws EMFUserError {
		SubObject sub = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiSubObjects hibSub = (SbiSubObjects)aSession.load(SbiSubObjects.class, idSubObj);
			sub = toSubobject(hibSub);
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
		return sub;
	}

	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.analiticalmodel.document.dao.ISubObjectDAO#saveSubObject(java.lang.Integer, it.eng.spagobi.analiticalmodel.document.bo.SubObject)
	 */
	public void saveSubObject(Integer idBIObj, SubObject subObj) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiObjects hibBIObject = (SbiObjects) aSession.load(SbiObjects.class, idBIObj);
			SbiBinContents hibBinContent = new SbiBinContents();
			byte[] bytes = null;
			try {
				bytes = subObj.getContent();
			} catch (EMFInternalError e) {
				logger.error("Could not retrieve content of SubObject object in input.");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
			hibBinContent.setContent(bytes);
			
			Integer idBin = (Integer)aSession.save(hibBinContent);
			// recover the saved binary hibernate object
			hibBinContent = (SbiBinContents) aSession.load(SbiBinContents.class, idBin);
			// store the subobject
			Date now = new Date();
			SbiSubObjects hibSub = new SbiSubObjects();
			hibSub.setOwner(subObj.getOwner());
			hibSub.setLastChangeDate(now);
			hibSub.setIsPublic(subObj.getIsPublic());
			hibSub.setCreationDate(now);
			hibSub.setDescription(subObj.getDescription());
			hibSub.setName(subObj.getName());
			hibSub.setSbiBinContents(hibBinContent);
			hibSub.setSbiObject(hibBIObject);
			aSession.save(hibSub);
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
    
	
	private SubObject toSubobject(SbiSubObjects hibsub) {
		SubObject subobj = new SubObject();
		subobj.setBiobjId(hibsub.getSbiObject().getBiobjId());
		subobj.setCreationDate(hibsub.getCreationDate());
		subobj.setDescription(hibsub.getDescription());
		subobj.setId(hibsub.getSubObjId());
		subobj.setIsPublic(hibsub.getIsPublic());
		subobj.setLastChangeDate(hibsub.getLastChangeDate());
		subobj.setName(hibsub.getName());
		subobj.setOwner(hibsub.getOwner());
		subobj.setContent(hibsub.getSbiBinContents().getContent());
		subobj.setBinaryContentId(hibsub.getSbiBinContents().getId());
		return subobj;
	}

	

}
