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
 * Created on 21-giu-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.bo.dao.hibernate;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.Parameter;
import it.eng.spagobi.bo.dao.IBIObjectParameterDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.metadata.SbiObjPar;
import it.eng.spagobi.metadata.SbiObjParId;
import it.eng.spagobi.metadata.SbiObjects;
import it.eng.spagobi.metadata.SbiParameters;
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
 * for a BI Object Parameter.  
 * 
 * @author Zoppello
 */
public class BIObjectParameterDAOHibImpl extends AbstractHibernateDAO implements IBIObjectParameterDAO{

	/** 
	 * @see it.eng.spagobi.bo.dao.IBIObjectParameterDAO#loadBIObjectParameterForDetail(java.lang.Integer, java.lang.Integer)
	 */
	public BIObjectParameter loadBIObjectParameterForDetail(Integer biObjectID, Integer paramaterID) throws EMFUserError {
		BIObjectParameter toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
		
			
			Criterion aCriterion = Expression.and(
											  Expression.eq("id.sbiObjects.biobjId", biObjectID), 
											  Expression.eq("id.sbiParameters.parId", paramaterID));
			Criteria aCriteria = aSession.createCriteria(SbiObjPar.class);
			aCriteria.add(aCriterion);
			SbiObjPar hibObjectParameter = (SbiObjPar)aCriteria.uniqueResult();
			if (hibObjectParameter == null) return null;
			
			toReturn = toBIObjectParameter(hibObjectParameter);
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
	 * @see it.eng.spagobi.bo.dao.IBIObjectParameterDAO#modifyBIObjectParameter(it.eng.spagobi.bo.BIObjectParameter)
	 */
	public void modifyBIObjectParameter(BIObjectParameter aBIObjectParameter) throws EMFUserError {
		
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
		
			
			Criterion aCriterion = Expression.and(
					  Expression.eq("id.sbiObjects.biobjId", aBIObjectParameter.getBiObjectID()), 
					  Expression.eq("id.sbiParameters.parId", aBIObjectParameter.getParIdOld()));
			Criteria aCriteria = aSession.createCriteria(SbiObjPar.class);
			aCriteria.add(aCriterion);
			SbiObjPar hibObjectParameter = (SbiObjPar)aCriteria.uniqueResult();
			if (hibObjectParameter == null) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
					    "BIObjectParameterDAOHibImpl", 
					    "modifyBIObjectParameter", 
					    "the BIObjectParameter relevant to BIObject with id="+aBIObjectParameter.getBiObjectID()+" and to Parameter with id="+aBIObjectParameter.getParIdOld()+" does not exist.");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 1033);
			}
			
			aSession.delete(hibObjectParameter);
			
			SbiObjects aSbiObjectsKeyNew = (SbiObjects) aSession.load(SbiObjects.class, aBIObjectParameter.getBiObjectID());
			SbiParameters aSbiParametersKeyNew = (SbiParameters)aSession.load(SbiParameters.class, aBIObjectParameter.getParameter().getId());
			
			SbiObjParId idKeyNew = new SbiObjParId();
			
			idKeyNew.setSbiObjects(aSbiObjectsKeyNew);
			idKeyNew.setSbiParameters(aSbiParametersKeyNew);
			idKeyNew.setProg(new Integer(1));
			
			SbiObjPar hibObjectParameterNew = new SbiObjPar(idKeyNew);
			
			hibObjectParameterNew.setLabel(aBIObjectParameter.getLabel());
			hibObjectParameterNew.setReqFl(new Short(aBIObjectParameter.getRequired().shortValue()));
			hibObjectParameterNew.setModFl(new Short(aBIObjectParameter.getModifiable().shortValue()));
			hibObjectParameterNew.setViewFl(new Short(aBIObjectParameter.getVisible().shortValue()));
			hibObjectParameterNew.setMultFl(new Short(aBIObjectParameter.getMultivalue().shortValue()));
			hibObjectParameterNew.setParurlNm(aBIObjectParameter.getParameterUrlName());
			
			aSession.save(hibObjectParameterNew);
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
	 * @see it.eng.spagobi.bo.dao.IBIObjectParameterDAO#insertBIObjectParameter(it.eng.spagobi.bo.BIObjectParameter)
	 */
	public void insertBIObjectParameter(BIObjectParameter aBIObjectParameter) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
		
			SbiObjects aSbiObjectsKeyNew = (SbiObjects) aSession.load(SbiObjects.class, aBIObjectParameter.getBiObjectID());
			SbiParameters aSbiParametersKeyNew = (SbiParameters)aSession.load(SbiParameters.class, aBIObjectParameter.getParameter().getId());
			
			SbiObjParId idKeyNew = new SbiObjParId();
			
			idKeyNew.setSbiObjects(aSbiObjectsKeyNew);
			idKeyNew.setSbiParameters(aSbiParametersKeyNew);
			idKeyNew.setProg(new Integer(1));
			
			SbiObjPar hibObjectParameterNew = new SbiObjPar(idKeyNew);
			
			hibObjectParameterNew.setLabel(aBIObjectParameter.getLabel());
			hibObjectParameterNew.setReqFl(new Short(aBIObjectParameter.getRequired().shortValue()));
			hibObjectParameterNew.setModFl(new Short(aBIObjectParameter.getModifiable().shortValue()));
			hibObjectParameterNew.setViewFl(new Short(aBIObjectParameter.getVisible().shortValue()));
			hibObjectParameterNew.setMultFl(new Short(aBIObjectParameter.getMultivalue().shortValue()));
			hibObjectParameterNew.setParurlNm(aBIObjectParameter.getParameterUrlName());
			
			aSession.save(hibObjectParameterNew);
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
	 * @see it.eng.spagobi.bo.dao.IBIObjectParameterDAO#eraseBIObjectParameter(it.eng.spagobi.bo.BIObjectParameter)
	 */
	public void eraseBIObjectParameter(BIObjectParameter aBIObjectParameter) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
		
			
			Criterion aCriterion = Expression.and(
					  Expression.eq("id.sbiObjects.biobjId", aBIObjectParameter.getBiObjectID()), 
					  Expression.eq("id.sbiParameters.parId", aBIObjectParameter.getParameter().getId()));
			Criteria aCriteria = aSession.createCriteria(SbiObjPar.class);
			aCriteria.add(aCriterion);
			SbiObjPar hibObjectParameter = (SbiObjPar)aCriteria.uniqueResult();
			if (hibObjectParameter == null) {		
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
				    "BIObjectParameterDAOHibImpl", 
				    "eraseBIObjectParameter", 
				    "the BIObjectParameter relevant to BIObject with id="+aBIObjectParameter.getBiObjectID()+" and to Parameter with id="+aBIObjectParameter.getParameter().getId()+" does not exist.");
				throw new EMFUserError(EMFErrorSeverity.ERROR, 1034);
			}
			
			aSession.delete(hibObjectParameter);
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
	 * @see it.eng.spagobi.bo.dao.IBIObjectParameterDAO#hasObjParameters(java.lang.String)
	 */
	public boolean hasObjParameters(String parId) throws EMFUserError {
		boolean result = false;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			SbiParameters s;
			Integer intParId = Integer.valueOf(parId);
			Criterion aCriterion = Expression.eq("id.sbiParameters.parId",intParId);			
			Criteria aCriteria = aSession.createCriteria(SbiObjPar.class);
			aCriteria.add(aCriterion);
			
			List l = aCriteria.list();
			
			if (l.size() > 0)
				result = true;
			else
				result = false;
			
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
		
		return result;
	}
	
	/**
	 * From the hibernate BI object parameter at input, gives
	 * the corrispondent <code>BIObjectParameter</code> object.
	 * 
	 * @param hiObjPar The hybernate BI object parameter
	 * @return The corrispondent <code>BIObjectParameter</code>
	 */
	public BIObjectParameter toBIObjectParameter(SbiObjPar hiObjPar){
		BIObjectParameter aBIObjectParameter = new BIObjectParameter();
		
		aBIObjectParameter.setLabel(hiObjPar.getLabel());
		aBIObjectParameter.setModifiable(new Integer(hiObjPar.getModFl().intValue()));
		aBIObjectParameter.setMultivalue(new Integer(hiObjPar.getMultFl().intValue()));
		aBIObjectParameter.setBiObjectID(hiObjPar.getId().getSbiObjects().getBiobjId());
		aBIObjectParameter.setParameterUrlName(hiObjPar.getParurlNm());
		
		
		aBIObjectParameter.setParID(hiObjPar.getId().getSbiParameters().getParId());
		
		aBIObjectParameter.setRequired(new Integer(hiObjPar.getReqFl().intValue()));
		aBIObjectParameter.setVisible(new Integer(hiObjPar.getViewFl().intValue()));
		
		Parameter parameter = new Parameter();
		parameter.setId(hiObjPar.getId().getSbiParameters().getParId());
		aBIObjectParameter.setParameter(parameter);
		return aBIObjectParameter;
	}
	
	
	/**
	 * @see it.eng.spagobi.bo.dao.IBIObjectParameterDAO#loadBIObjectParametersById(java.lang.Integer)
	 */
	public List loadBIObjectParametersById(Integer biObjectID) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		List resultList = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			String hql = "from SbiObjPar s where s.id.sbiObjects.biobjId = " + biObjectID;

			Query hqlQuery = aSession.createQuery(hql);
			List hibObjectPars = hqlQuery.list();

			SbiObjPar hibObjPar = null;
			Iterator it = hibObjectPars.iterator();
			while (it.hasNext()){
				resultList.add(toBIObjectParameter((SbiObjPar)it.next()));
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
		return resultList;
	 	
	 }
	
}
