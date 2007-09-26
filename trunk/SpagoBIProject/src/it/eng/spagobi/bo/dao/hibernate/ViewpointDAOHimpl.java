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
/*
 * Created on 20-giu-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.bo.dao.hibernate;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Viewpoint;
import it.eng.spagobi.bo.dao.IViewpointDAO;
import it.eng.spagobi.mapcatalogue.metadata.SbiGeoMaps;
import it.eng.spagobi.metadata.SbiObjects;
import it.eng.spagobi.metadata.SbiViewpoints;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;

/**
 * Defines the Hibernate implementations for all DAO methods,
 * for a viewpoint.  
 * 
 * @author Giachino
 */
public class ViewpointDAOHimpl extends AbstractHibernateDAO implements IViewpointDAO {

	

	/**
	 * @see it.eng.spagobi.bo.dao.IViewpointDAO#loadAllViewpoints()
	 */
	public List loadAllViewpoints() throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;

		List realResult = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			Query hibQuery = aSession.createQuery(" from SbiViewpoints");
			List hibList = hibQuery.list();

			tx.commit();
			

			Iterator it = hibList.iterator();

			while (it.hasNext()) {
				realResult.add(toViewpoint((SbiViewpoints) it.next()));
			}
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
		return realResult;
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IViewpointDAO#loadAllViewpoints()
	 */
	public List loadAllViewpointsByObjID(Integer objId) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;

		List realResult = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			String hql = "from SbiViewpoints vp where vp.sbiObject.biobjId = " + objId;

			Query hqlQuery = aSession.createQuery(hql);
			List hibList = hqlQuery.list();
			
			tx.commit();
			
			Iterator it = hibList.iterator();
			while (it.hasNext()) {
				realResult.add(toViewpoint((SbiViewpoints) it.next()));
			}
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
		return realResult;
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IViewpointDAO#loadViewpointByID(java.lang.Integer)
	 */
	public Viewpoint loadViewpointByID(Integer id) throws EMFUserError {
		Viewpoint toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
		
			SbiViewpoints hibViewpoint = (SbiViewpoints)aSession.load(SbiViewpoints.class,  id);
			
			toReturn = toViewpoint(hibViewpoint);
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
		
		return toReturn;
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IViewpointDAO#loadViewpointByName(java.lang.String)
	 */
	public Viewpoint loadViewpointByName(String name) throws EMFUserError {
		Viewpoint toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			Criterion labelCriterrion = Expression.eq("vpName",
					name);
			Criteria criteria = aSession.createCriteria(SbiViewpoints.class);
			criteria.add(labelCriterrion);	
			SbiViewpoints hibViewpoint = (SbiViewpoints)criteria.uniqueResult();
			if (hibViewpoint == null) return null;
			
			toReturn = toViewpoint(hibViewpoint);
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
		
		return toReturn;
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IViewpointDAO#eraseViewpoint(it.eng.spagobi.bo.Viewpoint)
	 */
	public void eraseViewpoint(Integer id) throws EMFUserError {

		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			SbiViewpoints hibViewpoint = (SbiViewpoints) aSession.load(SbiViewpoints.class, id);

			aSession.delete(hibViewpoint);
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

	/**
	 * @see it.eng.spagobi.bo.dao.IViewpointDAO#insertViewpoint(it.eng.spagobi.bo.Viewpoint)
	 */
	public void insertViewpoint(Viewpoint viewpoint) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiViewpoints hibViewpoint = new SbiViewpoints();

			//hibViewpoint.setVpId(vpId);
			SbiObjects aSbiObject = (SbiObjects) aSession.load(SbiObjects.class, viewpoint.getBiobjId());
			hibViewpoint.setSbiObject(aSbiObject);
			hibViewpoint.setVpDesc(viewpoint.getVpDesc());
			hibViewpoint.setVpOwner(viewpoint.getVpOwner());
			hibViewpoint.setVpName(viewpoint.getVpName());
			hibViewpoint.setVpScope(viewpoint.getVpScope());
			hibViewpoint.setVpValueParams(viewpoint.getVpValueParams());
			hibViewpoint.setVpCreationDate(viewpoint.getVpCreationDate());
			
			aSession.save(hibViewpoint);
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

	/**
	 * @see it.eng.spagobi.bo.dao.IViewpointDAO#modifyViewpoint(it.eng.spagobi.bo.Viewpoint)
	 */
	/*
	public void modifyViewpoint(Viewpoint viewpoint) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			
			aSession = getSession();
			tx = aSession.beginTransaction();

			SbiViewpoints hibViewpoint = (SbiViewpoints) aSession.load(SbiViewpoints.class,
					viewpoint.getVpId());
			 
//			hibViewpoint.setVpId(vpId);					
			SbiObjects aSbiObject = (SbiObjects) aSession.load(SbiObjects.class, viewpoint.getBiobjId());
			hibViewpoint.setSbiObject(aSbiObject);
			hibViewpoint.setVpDesc(viewpoint.getVpDesc());
			hibViewpoint.setVpName(viewpoint.getVpName());
			hibViewpoint.setVpScope(viewpoint.getVpScope());
			hibViewpoint.setVpValueParams(viewpoint.getVpValueParams());

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
	*/
	/**
	 * From the hibernate BI value viewpoint at input, gives
	 * the corrispondent <code>Viepoint</code> object.
	 * 
	 * @param hibViewpoint The hybernate viewpoint at input
	 * @return The corrispondent <code>Viewpoint</code> object
	 */
	public Viewpoint toViewpoint(SbiViewpoints hibViewpoint){
		Viewpoint aViewpoint = new Viewpoint();
		aViewpoint.setVpId(hibViewpoint.getVpId());
		aViewpoint.setBiobjId(hibViewpoint.getSbiObject().getBiobjId());
		aViewpoint.setVpOwner(hibViewpoint.getVpOwner());
		aViewpoint.setVpName(hibViewpoint.getVpName());
		aViewpoint.setVpDesc(hibViewpoint.getVpDesc());
		aViewpoint.setVpScope(hibViewpoint.getVpScope());
		aViewpoint.setVpValueParams(hibViewpoint.getVpValueParams());
		aViewpoint.setVpCreationDate(hibViewpoint.getVpCreationDate());
		return  aViewpoint;
	}
	

}
