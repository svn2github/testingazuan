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
package it.eng.spagobi.bo.dao.hibernate;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.ObjParuse;
import it.eng.spagobi.bo.dao.IObjParuseDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.metadata.SbiObjPar;
import it.eng.spagobi.metadata.SbiObjParuse;
import it.eng.spagobi.metadata.SbiObjParuseId;
import it.eng.spagobi.metadata.SbiParuse;
import it.eng.spagobi.utilities.SpagoBITracer;

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
 * for a ObjParuse object.  
 * 
 * @author Zerbetto
 */
public class ObjParuseDAOHibImpl extends AbstractHibernateDAO implements IObjParuseDAO {

	/** 
	 * @see it.eng.spagobi.bo.dao.IObjParuseDAO#loadObjParuse(java.lang.Integer, java.lang.Integer)
	 */
	public ObjParuse loadObjParuse(Integer objParId, Integer paruseId) throws EMFUserError {
		ObjParuse toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
		
			Criterion aCriterion = Expression.and(
											  Expression.eq("id.sbiObjPar.objParId", objParId), 
											  Expression.eq("id.sbiParuse.useId", paruseId));
			Criteria aCriteria = aSession.createCriteria(SbiObjParuse.class);
			aCriteria.add(aCriterion);
			SbiObjParuse sbiObjParuse = (SbiObjParuse) aCriteria.uniqueResult();
			if (sbiObjParuse == null) return null;
			
			toReturn = toObjParuse(sbiObjParuse);
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
	 * @see it.eng.spagobi.bo.dao.IObjParuseDAO#modifyObjParuse(it.eng.spagobi.bo.ObjParuse)
	 */
	public void modifyObjParuse(ObjParuse aObjParuse) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			Criterion aCriterion = Expression.and(
					  Expression.eq("id.sbiObjPar.objParId", aObjParuse.getObjParId()), 
					  Expression.eq("id.sbiParuse.useId", aObjParuse.getParuseId()));
			
			Criteria aCriteria = aSession.createCriteria(SbiObjParuse.class);
			aCriteria.add(aCriterion);
			SbiObjParuse sbiObjParuse = (SbiObjParuse) aCriteria.uniqueResult();
			if (sbiObjParuse == null) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
					    "ObjParuseDAOHibImpl", 
					    "modifyObjParuse", 
					    "the ObjParuse relevant to BIObjectParameter with id="+aObjParuse.getObjParId()+" and ParameterUse with id="+aObjParuse.getParuseId()+" does not exist.");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 1043);
			}
			
			aSession.delete(sbiObjParuse);
			
			SbiObjPar aSbiObjParKeyNew = (SbiObjPar) aSession.load(SbiObjPar.class, aObjParuse.getObjParId());
			SbiParuse aSbiParuseKeyNew = (SbiParuse) aSession.load(SbiParuse.class, aObjParuse.getParuseId());
			
			SbiObjParuseId idKeyNew = new SbiObjParuseId();
			
			idKeyNew.setSbiObjPar(aSbiObjParKeyNew);
			idKeyNew.setSbiParuse(aSbiParuseKeyNew);
			
			SbiObjParuse newObjParuse = new SbiObjParuse(idKeyNew);
			
			SbiObjPar sbiObjParFather = (SbiObjPar) aSession.load(SbiObjPar.class, aObjParuse.getObjParFatherId());
			
			if (sbiObjParFather == null) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
					    "ObjParuseDAOHibImpl", 
					    "modifyObjParuse", 
					    "the BIObjectParameter with id="+aObjParuse.getObjParFatherId()+" does not exist.");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 1043);
			}
			
			newObjParuse.setSbiObjParFather(sbiObjParFather);
			newObjParuse.setFilterColumn(aObjParuse.getFilterColumn());
			newObjParuse.setFilterOperation(aObjParuse.getFilterOperation());
			
			aSession.save(newObjParuse);
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
	 * @see it.eng.spagobi.bo.dao.IObjParuseDAO#insertObjParuse(it.eng.spagobi.bo.ObjParuse)
	 */
	public void insertObjParuse(ObjParuse aObjParuse) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
		
			SbiObjPar aSbiObjParKeyNew = (SbiObjPar) aSession.load(SbiObjPar.class, aObjParuse.getObjParId());
			SbiParuse aSbiParuseKeyNew = (SbiParuse) aSession.load(SbiParuse.class, aObjParuse.getParuseId());
			
			SbiObjParuseId idKeyNew = new SbiObjParuseId();
			
			idKeyNew.setSbiObjPar(aSbiObjParKeyNew);
			idKeyNew.setSbiParuse(aSbiParuseKeyNew);
			
			SbiObjParuse newObjParuse = new SbiObjParuse(idKeyNew);
			
			SbiObjPar sbiObjParFather = (SbiObjPar) aSession.load(SbiObjPar.class, aObjParuse.getObjParFatherId());
			
			if (sbiObjParFather == null) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
					    "ObjParuseDAOHibImpl", 
					    "insertObjParuse", 
					    "the BIObjectParameter with id="+aObjParuse.getObjParFatherId()+" does not exist.");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 1044);
			}
			
			newObjParuse.setSbiObjParFather(sbiObjParFather);
			newObjParuse.setFilterColumn(aObjParuse.getFilterColumn());
			newObjParuse.setFilterOperation(aObjParuse.getFilterOperation());
			
			aSession.save(newObjParuse);
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
	 * @see it.eng.spagobi.bo.dao.IObjParuseDAO#eraseObjParuse(ObjParuse)
	 */
	public void eraseObjParuse(ObjParuse aObjParuse) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			Criterion aCriterion = Expression.and(
					  Expression.eq("id.sbiObjPar.objParId", aObjParuse.getObjParId()), 
					  Expression.eq("id.sbiParuse.useId", aObjParuse.getParuseId()));
			
			Criteria aCriteria = aSession.createCriteria(SbiObjParuse.class);
			aCriteria.add(aCriterion);
			SbiObjParuse sbiObjParuse = (SbiObjParuse)aCriteria.uniqueResult();
			if (sbiObjParuse == null) {		
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
				    "ObjParuseDAOHibImpl", 
				    "eraseObjParuse", 
				    "the ObjParuse relevant to BIObjectParameter with id="+aObjParuse.getObjParId()+" and ParameterUse with id="+aObjParuse.getParuseId()+" does not exist.");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 1045);
			}
			
			aSession.delete(sbiObjParuse);
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
	 * @see it.eng.spagobi.bo.dao.IObjParuseDAO#loadObjParuses(Integer)
	 */
	public List loadObjParuses(Integer objParId) throws EMFUserError {
		List toReturn = new ArrayList();
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
		
			String hql = "from SbiObjParuse s where s.id.sbiObjPar.objParId = " + objParId;
			
			Query hqlQuery = aSession.createQuery(hql);
			List sbiObjParuses = hqlQuery.list();
			
			Iterator it = sbiObjParuses.iterator();
			while (it.hasNext()){
				toReturn.add(toObjParuse((SbiObjParuse)it.next()));
			}
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
	 * From the hibernate SbiObjParuse at input, gives
	 * the corrispondent <code>ObjParuse</code> object.
	 * 
	 * @param aSbiObjParuse The hybernate SbiObjParuse
	 * @return The corrispondent <code>ObjParuse</code>
	 */
	public ObjParuse toObjParuse (SbiObjParuse aSbiObjParuse) {
		if (aSbiObjParuse == null) return null;
		ObjParuse toReturn = new ObjParuse();
		toReturn.setObjParId(aSbiObjParuse.getId().getSbiObjPar().getObjParId());
		toReturn.setParuseId(aSbiObjParuse.getId().getSbiParuse().getUseId());
		toReturn.setObjParFatherId(aSbiObjParuse.getSbiObjParFather().getObjParId());
		toReturn.setFilterColumn(aSbiObjParuse.getFilterColumn());
		toReturn.setFilterOperation(aSbiObjParuse.getFilterOperation());
		return toReturn;
	}

	/** 
	 * @see it.eng.spagobi.bo.dao.IObjParuseDAO#getDependencies(Integer)
	 */
	public List getDependencies(Integer objParFatherId) throws EMFUserError {
		List toReturn = new ArrayList();
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			String hql = "from SbiObjParuse s where s.sbiObjParFather=" + objParFatherId;
			Query query = aSession.createQuery(hql);
			List objParuses = query.list();
			
			if (objParuses == null || objParuses.size() == 0) return toReturn;
			
			Iterator it = objParuses.iterator();
			while (it.hasNext()) {
				SbiObjParuse objParuseHib = (SbiObjParuse) it.next();
				Integer objParId = objParuseHib.getId().getSbiObjPar().getObjParId();
				SbiObjPar hibObjPar = (SbiObjPar) aSession.load(SbiObjPar.class, objParId);
				toReturn.add(hibObjPar.getLabel());
			}
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
	 * @see it.eng.spagobi.bo.dao.IObjParuseDAO#getAllDependenciesForParameterUse(java.lang.Integer)
	 */
	public List getAllDependenciesForParameterUse(Integer useId) throws EMFUserError {
		List toReturn = new ArrayList();
		
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String hql = "from SbiObjParuse s where s.id.sbiParuse.useId = " + useId;
			Query query = aSession.createQuery(hql);
			List result = query.list();
			Iterator it = result.iterator();
			while (it.hasNext()){
				toReturn.add(toObjParuse((SbiObjParuse) it.next()));
			}
			tx.commit();
		} catch(HibernateException he){
			logException(he);
			if (tx != null) tx.rollback();	
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		
		return toReturn;
	}

	/**
	 * @see it.eng.spagobi.bo.dao.IObjParuseDAO#getDocumentLabelsListWithAssociatedDependencies(java.lang.Integer)
	 */
	public List getDocumentLabelsListWithAssociatedDependencies(Integer useId) throws EMFUserError {
		List toReturn = new ArrayList();
		
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String hql = 
					"select " +
					"	distinct(obj.label) " +
					"from " +
					"	SbiObjects obj, SbiObjParuse s " +
					"where " +
					"	obj.sbiObjPars.objParId = s.id.sbiObjPar.objParId and " +
					"	s.id.sbiParuse.useId = " + useId;
			Query query = aSession.createQuery(hql);
			List result = query.list();
			toReturn = result;
			tx.commit();
		} catch(HibernateException he){
			logException(he);
			if (tx != null) tx.rollback();	
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);  
		} finally {
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
		}
		
		return toReturn;
	}
}
