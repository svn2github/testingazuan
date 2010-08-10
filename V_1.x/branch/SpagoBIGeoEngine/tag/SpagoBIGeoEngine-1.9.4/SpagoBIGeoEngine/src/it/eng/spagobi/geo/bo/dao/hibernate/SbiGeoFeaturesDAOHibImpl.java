/**
 * 
 */
package it.eng.spagobi.geo.bo.dao.hibernate;

import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.geo.bo.SbiGeoFeatures;
import it.eng.spagobi.geo.bo.SbiGeoMaps;
import it.eng.spagobi.geo.bo.dao.ISbiGeoFeaturesDAO;
import it.eng.spagobi.geo.configuration.ConfigurationException;
import it.eng.spagobi.geo.configuration.Constants;

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
 * @author giachino
 *
 */
public class SbiGeoFeaturesDAOHibImpl extends AbstractHibernateDAO implements ISbiGeoFeaturesDAO{
	/**
	 * @see it.eng.spagobi.geo.bo.dao.ISbiGeoFeaturesDAO#loadFeatureByID(integer)
	 */
	public SbiGeoFeatures loadFeatureByID(Integer featureID) throws EMFUserError {
		SbiGeoFeatures toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			toReturn = (SbiGeoFeatures)aSession.load(SbiGeoFeatures.class,  featureID);			
			tx.commit();
			
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			/*
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
				
			}*/
		}
		
		return toReturn;
	}
	
	/**
	 * @see it.eng.spagobi.geo.bo.dao.ISbiGeoFeaturesDAO#loadFeatureByName(string)
	 */
	public SbiGeoFeatures loadFeatureByName(String name) throws EMFUserError {
		SbiGeoFeatures biFeature = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Criterion labelCriterrion = Expression.eq("name",
					name);
			Criteria criteria = aSession.createCriteria(SbiGeoFeatures.class);
			criteria.add(labelCriterrion);
			biFeature = (SbiGeoFeatures) criteria.uniqueResult();			
			tx.commit();
		} catch (HibernateException he) {
			logException(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			/*if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}*/
		}
		return biFeature;		
	}

	
	/**
	 * @see it.eng.spagobi.geo.bo.dao.IEngineDAO#modifyEngine(it.eng.spagobi.bo.Engine)
	 */
	public void modifyFeature(SbiGeoFeatures aFeature) throws EMFUserError {
		
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			SbiGeoFeatures hibFeature = (SbiGeoFeatures) aSession.load(SbiGeoFeatures.class, aFeature.getFeatureId());
			hibFeature.setName(aFeature.getName());
			hibFeature.setDescr(aFeature.getDescr());
			hibFeature.setType(aFeature.getType());			
			tx.commit();
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			/*if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
			*/
		}

	}

	/**
	 * @see it.eng.spagobi.geo.bo.dao.IEngineDAO#insertEngine(it.eng.spagobi.bo.Engine)
	 */
	public void insertFeature(SbiGeoFeatures aFeature) throws EMFUserError {		
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiGeoFeatures hibFeature = new SbiGeoFeatures();
			//hibFeature.setFeatureId(new Integer(-1));
			hibFeature.setName(aFeature.getName());
			hibFeature.setDescr(aFeature.getDescr());
			hibFeature.setType(aFeature.getType());
			aSession.save(hibFeature);
			tx.commit();
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			/*
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
			*/
		}
	}

	/**
	 * @see it.eng.spagobi.geo.bo.dao.IEngineDAO#eraseEngine(it.eng.spagobi.bo.Engine)
	 */
	public void eraseFeature(SbiGeoFeatures aFeature) throws EMFUserError {
		
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiGeoFeatures hibFeature = (SbiGeoFeatures) aSession.load(SbiGeoFeatures.class,
					aFeature.getFeatureId());

			aSession.delete(hibFeature);
			tx.commit();
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			/*
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
			*/
		}
	}
	
	/**
	 * @see it.eng.spagobi.geo.bo.dao.IEngineDAO#loadAllEngines()
	 */
	public List loadAllFeatures() throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			Query hibQuery = aSession.createQuery(" from SbiGeoFeatures");
			
			List hibList = hibQuery.list();
			tx.commit();
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			/*
			if (aSession!=null){
				if (aSession.isOpen()) aSession.close();
			}
			*/
		}
		return realResult;
	}
	/**
	 * @see it.eng.spagobi.geo.bo.dao.ISbiGeoMapsDAO#hasFeaturesAssociated(java.lang.String)
	 */
	public boolean hasMapsAssociated (String featureId) throws EMFUserError{
		boolean bool = false; 
		
		
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Integer mapIdInt = Integer.valueOf(featureId);
			
			String hql = " from SbiGeoMapFeatures s where s.id.featureId = "+ mapIdInt;
			Query aQuery = aSession.createQuery(hql);
			
			List biFeaturesAssocitedWithMap = aQuery.list();
			if (biFeaturesAssocitedWithMap.size() > 0)
				bool = true;
			else
				bool = false;
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
		return bool;
		
	}

}