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
 * Created on 21-giu-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.analiticalmodel.document.dao;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjTemplates;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.commons.metadata.SbiBinContents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ObjTemplateDAOHibImpl extends AbstractHibernateDAO implements IObjTemplateDAO {

	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.analiticalmodel.document.dao.IObjTemplateDAO#loadBIObjectTemplate(java.lang.Integer)
	 */
	public ObjTemplate loadBIObjectTemplate(Integer tempId) throws EMFInternalError {
		ObjTemplate objTemp = new ObjTemplate();
		Session aSession = null;
		Transaction tx = null;		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiObjTemplates hibObjTemp = (SbiObjTemplates)aSession.load(SbiObjTemplates.class,  tempId);
			objTemp = toObjTemplate(hibObjTemp);
			tx.commit();
		}catch(HibernateException he){
			logException(he);
			if (tx != null) tx.rollback();	
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "100");  
		}finally{
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		return objTemp;
	}
	
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.analiticalmodel.document.dao.IObjTemplateDAO#getBIObjectActiveTemplate(java.lang.Integer)
	 */
	public ObjTemplate getBIObjectActiveTemplate(Integer biobjId) throws EMFInternalError {
		ObjTemplate objTemp = new ObjTemplate();
		Session aSession = null;
		Transaction tx = null;		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			//String hql = "from SbiObjTemplates sot where sot.active=true and sot.sbiObject.biobjId="+biobjId;
			String hql = "from SbiObjTemplates sot where sot.active=true and sot.sbiObject.biobjId=?";
			
			Query query = aSession.createQuery(hql);
			query.setInteger(0, biobjId.intValue());
			SbiObjTemplates hibObjTemp = (SbiObjTemplates)query.uniqueResult();
			if(hibObjTemp==null) {
				objTemp = null;
			} else {
				objTemp = toObjTemplate(hibObjTemp);
			}
			tx.commit();
		}catch(HibernateException he){
			logException(he);
			if (tx != null) tx.rollback();	
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "100");  
		}finally{
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		return objTemp;
	}

	
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.analiticalmodel.document.dao.IObjTemplateDAO#getBIObjectTemplateList(java.lang.Integer)
	 */
	public List getBIObjectTemplateList(Integer biobjId) throws EMFInternalError {
		List templates = new ArrayList();
		Session aSession = null;
		Transaction tx = null;		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			//String hql = "from SbiObjTemplates sot where sot.sbiObject.biobjId="+biobjId+" order by sot.prog desc";
			String hql = "from SbiObjTemplates sot where sot.sbiObject.biobjId=? order by sot.prog desc";
			
			Query query = aSession.createQuery(hql);
			query.setInteger(0, biobjId.intValue());
			List result = query.list();
			Iterator it = result.iterator();
 			while (it.hasNext()){
				templates.add(toObjTemplate((SbiObjTemplates)it.next()));
			}
			tx.commit();
		}catch(HibernateException he){
			logException(he);
			if (tx != null) tx.rollback();	
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "100");  
		}finally{
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		return templates;
	}

	
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.analiticalmodel.document.dao.IObjTemplateDAO#getNextProgForTemplate(java.lang.Integer)
	 */
	public Integer getNextProgForTemplate(Integer biobjId) throws EMFInternalError {
		Integer maxProg = null;
		Session aSession = null;
		Transaction tx = null;		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			//String hql = "select max(sot.prog) as maxprog from SbiObjTemplates sot where sot.sbiObject.biobjId="+biobjId;
			String hql = "select max(sot.prog) as maxprog from SbiObjTemplates sot where sot.sbiObject.biobjId=?";
			Query query = aSession.createQuery(hql);
			
			query.setInteger(0, biobjId.intValue());
			List result = query.list();
			Iterator it = result.iterator();
			while (it.hasNext()){
				maxProg = (Integer)it.next();
			}
			if(maxProg==null){
				maxProg = new Integer(1);
			} else {
				maxProg = new Integer(maxProg.intValue() + 1);
			}
			tx.commit();
		}catch(HibernateException he){
			logException(he);
			if (tx != null) tx.rollback();	
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "100");  
		}finally{
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		return maxProg;
	}
	
	
	
	/**
	 * To obj template.
	 * 
	 * @param hibObjTemp the hib obj temp
	 * 
	 * @return the obj template
	 */
	public ObjTemplate toObjTemplate(SbiObjTemplates hibObjTemp){
		ObjTemplate objTemp = new ObjTemplate();
		objTemp.setActive(hibObjTemp.getActive());
		objTemp.setBinId(hibObjTemp.getSbiBinContents().getId());
		objTemp.setBiobjId(hibObjTemp.getSbiObject().getBiobjId());
		objTemp.setCreationDate(hibObjTemp.getCreationDate());
		objTemp.setId(hibObjTemp.getObjTempId());
		objTemp.setName(hibObjTemp.getName());
		objTemp.setProg(hibObjTemp.getProg());
		// metadata
		objTemp.setDimension(hibObjTemp.getDimension());
		objTemp.setCreationUser(hibObjTemp.getCreationUser());
		return objTemp;
	}


	/* (non-Javadoc)
	 * @see it.eng.spagobi.analiticalmodel.document.dao.IObjTemplateDAO#deleteBIObjectTemplate(java.lang.Integer)
	 */
	public void deleteBIObjectTemplate(Integer tempId) throws EMFInternalError {
		Session aSession = null;
		Transaction tx = null;		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiObjTemplates hibObjTemp = (SbiObjTemplates)aSession.load(SbiObjTemplates.class,  tempId);
			SbiBinContents hibBinCont = hibObjTemp.getSbiBinContents();
			aSession.delete(hibBinCont);
			aSession.delete(hibObjTemp);
			tx.commit();
		}catch(HibernateException he){
			logException(he);
			if (tx != null) tx.rollback();	
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "100");  
		}finally{
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}		
	}

}
