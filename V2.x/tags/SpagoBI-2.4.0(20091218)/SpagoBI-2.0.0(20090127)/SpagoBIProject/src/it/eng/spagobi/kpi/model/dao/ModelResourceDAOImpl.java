package it.eng.spagobi.kpi.model.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.dao.AbstractHibernateDAO;
import it.eng.spagobi.kpi.model.metadata.SbiKpiModelInst;
import it.eng.spagobi.kpi.model.metadata.SbiKpiModelResources;
import it.eng.spagobi.kpi.model.metadata.SbiResources;

public class ModelResourceDAOImpl extends AbstractHibernateDAO implements
		IModelResourceDAO {

	public void addModelResource(Integer modelId, Integer resourceId) throws EMFUserError {
		Session aSession = getSession();
		Transaction tx = null;
		try {
			tx = aSession.beginTransaction();
			SbiKpiModelResources aModelResources = new SbiKpiModelResources();
			
			SbiKpiModelInst aModelInst = (SbiKpiModelInst) aSession.load(
					SbiKpiModelInst.class, modelId);
			SbiResources aResource = (SbiResources) aSession.load(
					SbiResources.class, resourceId);
			aModelResources.setSbiKpiModelInst(aModelInst);
			aModelResources.setSbiResources(aResource);
			
			aSession.save(aModelResources);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw new EMFUserError(EMFErrorSeverity.ERROR, 101);

		} finally {
			aSession.close();
		}


	}

	public boolean isSelected(Integer modelId, Integer resourceId) throws EMFUserError {
		Session aSession = getSession();
		Transaction tx = null;
		boolean toReturn = false;
		try {
			tx = aSession.beginTransaction();
			List modelResourceList = getModelResource(aSession, modelId, resourceId);
			if (modelResourceList.isEmpty())
				toReturn = false;
			else
				toReturn = true;
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw new EMFUserError(EMFErrorSeverity.ERROR, 101);

		} finally {
			aSession.close();
		}
		return toReturn;
	}

	public void removeModelResource(Integer modelId, Integer resourceId) throws EMFUserError {
		Session aSession = getSession();
		Transaction tx = null;
		try {
			tx = aSession.beginTransaction();
			List modelResourceList = getModelResource(aSession, modelId, resourceId);
			for (Iterator iterator = modelResourceList.iterator(); iterator
					.hasNext();) {
				SbiKpiModelResources modelResource = (SbiKpiModelResources) iterator.next();
				aSession.delete(modelResource);
			}
			tx.commit();

		} catch (HibernateException e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw new EMFUserError(EMFErrorSeverity.ERROR, 101);

		} finally {
			aSession.close();
		}
	}

	private List getModelResource(Session aSession, Integer modelId,
			Integer resourceId) {
		SbiKpiModelInst aModelInst = (SbiKpiModelInst) aSession.load(
				SbiKpiModelInst.class, modelId);
		SbiResources aResource = (SbiResources) aSession.load(
				SbiResources.class, resourceId);
		Criteria crit = aSession.createCriteria(SbiKpiModelResources.class);
		crit.add(Expression.eq("sbiKpiModelInst", aModelInst));
		crit.add(Expression.eq("sbiResources", aResource));
		return crit.list();	
	}
	
	private List getModelResource(Session aSession, Integer modelId) {
		SbiKpiModelInst aModelInst = (SbiKpiModelInst) aSession.load(
				SbiKpiModelInst.class, modelId);
		Criteria crit = aSession.createCriteria(SbiKpiModelResources.class);
		crit.add(Expression.eq("sbiKpiModelInst", aModelInst));
		return crit.list();	
	}

	public void removeAllModelResource(Integer modelId) throws EMFUserError {
		Session aSession = getSession();
		Transaction tx = null;
		try {
			tx = aSession.beginTransaction();
			List modelResourceList = getModelResource(aSession, modelId);
			for (Iterator iterator = modelResourceList.iterator(); iterator
					.hasNext();) {
				SbiKpiModelResources modelResource = (SbiKpiModelResources) iterator.next();
				aSession.delete(modelResource);
			}
			tx.commit();

		} catch (HibernateException e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			throw new EMFUserError(EMFErrorSeverity.ERROR, 101);

		} finally {
			aSession.close();
		}
	}
	
}
