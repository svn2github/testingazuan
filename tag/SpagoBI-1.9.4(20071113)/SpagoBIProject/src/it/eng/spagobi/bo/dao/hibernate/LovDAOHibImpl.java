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
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.dao.IModalitiesValueDAO;
import it.eng.spagobi.metadata.SbiDomains;
import it.eng.spagobi.metadata.SbiLov;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


/**
 * Defines the Hibernate implementations for all DAO methods,
 * for a list of values. 
 * 
 * @author sulis
 */

public class LovDAOHibImpl extends AbstractHibernateDAO implements IModalitiesValueDAO{

	/**
	 * @see it.eng.spagobi.bo.dao.IModalitiesValueDAO#loadModalitiesValueByID(Integer)
	 */
	public ModalitiesValue loadModalitiesValueByID(Integer modalitiesValueID) throws EMFUserError {
		ModalitiesValue modVal = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			SbiLov hibLov = (SbiLov)aSession.load(SbiLov.class, modalitiesValueID);
			modVal = toModalityValue(hibLov);
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
		return modVal;
	}
	
	
	/**
	 * @see it.eng.spagobi.bo.dao.IModalitiesValueDAO#loadModalitiesValueByID(Integer)
	 */
	public ModalitiesValue loadModalitiesValueByLabel(String label) throws EMFUserError {
		ModalitiesValue modVal = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			String hql = "from SbiLov s where s.label = '" + label + "'";
			Query hqlQuery = aSession.createQuery(hql);
			SbiLov hibLov = (SbiLov)hqlQuery.uniqueResult();
			modVal = toModalityValue(hibLov);
			tx.commit();
		} catch (HibernateException he) {
			logException(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			aSession.close();
		}
		return modVal;
	}
	
	
	
	/**
	 * @see it.eng.spagobi.bo.dao.IModalitiesValueDAO#modifyModalitiesValue(it.eng.spagobi.bo.ModalitiesValue)
	 */
	public void modifyModalitiesValue(ModalitiesValue aModalitiesValue)
			throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiLov hibLov = (SbiLov) aSession.load(SbiLov.class,
					aModalitiesValue.getId());
			hibLov.setName(aModalitiesValue.getName());
			hibLov.setLabel(aModalitiesValue.getLabel());
			hibLov.setDescr(aModalitiesValue.getDescription());
			SbiDomains inpType = (SbiDomains) aSession.load(SbiDomains.class,
					new Integer(aModalitiesValue.getITypeId()));
			hibLov.setInputType(inpType);
			hibLov.setInputTypeCd(aModalitiesValue.getITypeCd());
			hibLov.setLovProvider(aModalitiesValue.getLovProvider());
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
	 * @see it.eng.spagobi.bo.dao.IModalitiesValueDAO#insertModalitiesValue(it.eng.spagobi.bo.ModalitiesValue)
	 */
	public void insertModalitiesValue(ModalitiesValue aModalitiesValue)
			throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiLov hibLov = new SbiLov();
			hibLov.setName(aModalitiesValue.getName());
			hibLov.setLabel(aModalitiesValue.getLabel());
			hibLov.setDescr(aModalitiesValue.getDescription());
			SbiDomains inpType = (SbiDomains) aSession.load(SbiDomains.class,
					new Integer(aModalitiesValue.getITypeId()));
			hibLov.setInputType(inpType);
			hibLov.setInputTypeCd(aModalitiesValue.getITypeCd());
			hibLov.setLovProvider(aModalitiesValue.getLovProvider());
			aSession.save(hibLov);
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
	 * @see it.eng.spagobi.bo.dao.IModalitiesValueDAO#eraseModalitiesValue(it.eng.spagobi.bo.ModalitiesValue)
	 */
	public void eraseModalitiesValue(ModalitiesValue aModalitiesValue)
			throws EMFUserError {
		
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiLov hibLov = (SbiLov) aSession.load(SbiLov.class,
					aModalitiesValue.getId());
			aSession.delete(hibLov);
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
	 * @see it.eng.spagobi.bo.dao.IModalitiesValueDAO#loadAllModalitiesValue()
	 */
	
	public List loadAllModalitiesValue() throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Query hibQuery = aSession.createQuery(" from SbiLov");
			List hibList = hibQuery.list();
			tx.commit();

			Iterator it = hibList.iterator();
			while (it.hasNext()) {
				realResult.add(toModalityValue((SbiLov) it.next()));
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
	 * @see it.eng.spagobi.bo.dao.IModalitiesValueDAO#loadAllModalitiesValueOrderByCode()
	 */
	public List loadAllModalitiesValueOrderByCode() throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Query hibQuery = aSession
					.createQuery(" from SbiLov s order by s.inputTypeCd");
			List hibList = hibQuery.list();

			Iterator it = hibList.iterator();
			while (it.hasNext()) {
				realResult.add(toModalityValue((SbiLov) it.next()));
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
		return realResult;

	}

	/**
	 * @see it.eng.spagobi.bo.dao.IModalitiesValueDAO#hasParameters(java.lang.String)
	 */	
	public boolean hasParameters(String lovId) throws EMFUserError {
		boolean result = true;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Integer lovIdInt = Integer.valueOf(lovId);
			String hql = "from SbiParuse s where s.sbiLov.lovId=" + lovIdInt;

			Query hibQuery = aSession.createQuery(hql);
			List hibList = hibQuery.list();

			if (hibList.size() > 0) {
				result = true;
			} else {
				result = false;
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

		return result;
		
	}
	
	/**
	 * From the hibernate LOV at input, gives
	 * the corrispondent <code>ModalitiesValue</code> object.
	 * 
	 * @param hiObjPar The hybernate LOV
	 * @return The corrispondent <code>ModalitiesValue</code> object
	 */
	private ModalitiesValue toModalityValue(SbiLov hibLov){
		ModalitiesValue modVal = new ModalitiesValue();
		modVal.setDescription(hibLov.getDescr());
		modVal.setId(hibLov.getLovId());
		modVal.setITypeCd(hibLov.getInputTypeCd());
		modVal.setITypeId(String.valueOf(hibLov.getInputType().getValueId()));
		modVal.setLovProvider(hibLov.getLovProvider());
		modVal.setName(hibLov.getName());
		modVal.setLabel(hibLov.getLabel());
		return modVal;
	}
}
