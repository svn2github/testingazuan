/**
 * 
 */
package it.eng.spagobi.geo.bo.dao.hibernate;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.geo.bo.SbiGeoFeatures;
import it.eng.spagobi.geo.bo.SbiGeoMapFeatures;
import it.eng.spagobi.geo.bo.SbiGeoMapFeaturesId;
import it.eng.spagobi.geo.bo.SbiGeoMaps;
import it.eng.spagobi.geo.bo.dao.ISbiGeoMapFeaturesDAO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author giachino
 *
 */
public class SbiGeoMapFeaturesDAOHibImpl extends AbstractHibernateDAO implements ISbiGeoMapFeaturesDAO {
	
	/** 
	 * @see it.eng.spagobi.bo.dao.ISbiGeoMapFeaturesDAO#loadFeaturesByMapId(java.lang.Integer)
	 */
	public List loadFeaturesByMapId(Integer mapId) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		String hql = null;
		Query hqlQuery = null;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			hql = " from SbiMapFeatures as mf " + 
	         "where mf.id.mapId = " + mapId.toString();
			
			hqlQuery = aSession.createQuery(hql);
			List hibList = hqlQuery.list();
			
			Iterator it = hibList.iterator();

			while (it.hasNext()) {
				realResult.add((SbiGeoFeatures) it.next());
			}
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
		return realResult;
	}
	
	/** 
	 * @see it.eng.spagobi.bo.dao.ISbiGeoMapFeaturesDAO#loadMapsByFeatureId(java.lang.Integer)
	 */	
	public List loadMapsByFeatureId(Integer featureId) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		String hql = null;
		Query hqlQuery = null;
		
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			hql = " from SbiMapFeatures as mf " + 
	         "where mf.id.FeatureId = " + featureId.toString();
			
			hqlQuery = aSession.createQuery(hql);
			List hibList = hqlQuery.list();
			
			Iterator it = hibList.iterator();

			while (it.hasNext()) {
				realResult.add((SbiGeoMaps) it.next());
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
	 * @see it.eng.spagobi.bo.dao.ISbiGeoMapFeaturesDAO#loadMapFeatures(java.lang.Integer, java.lang.Integer)
	 */
	public List loadMapFeatures(Integer mapId, Integer featureId) throws EMFUserError {
		List objMapFeatures = new ArrayList();
		SbiGeoMapFeatures toReturn = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			String hql = "from SbiObjMapFeature s where s.id.mapId=" + mapId.toString() + 
			             " and s.id.featureId=" +  featureId.toString();
			Query query = aSession.createQuery(hql);
			List sbiMapFeatures = query.list();
			if(sbiMapFeatures==null) 
				return objMapFeatures;
			
			Iterator itersbiOP = sbiMapFeatures.iterator();
			while(itersbiOP.hasNext()) {
				SbiGeoMapFeatures sbiop = (SbiGeoMapFeatures)itersbiOP.next();
				objMapFeatures.add(sbiop);
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
		return objMapFeatures;
	}

	/**
	 * @see it.eng.spagobi.geo.geo.bo.dao.ISbiGeoMapFeaturesDAO#modifySbiGeoMapFeatures(it.eng.spagobi.geo.bo.SbiGeoMapFeatures)
	 */
	public void modifyMapFeatures(SbiGeoMapFeatures aMapFeature) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			SbiGeoMapFeatures hibFeature = (SbiGeoMapFeatures) aSession.load(SbiGeoMapFeatures.class, aMapFeature.getId());
			hibFeature.setSvgGroup(aMapFeature.getSvgGroup());
			hibFeature.setVisibleFlag(aMapFeature.getVisibleFlag());			
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
	 * @see it.eng.spagobi.geo.bo.dao.ISbiGeoMapFeaturesDAO#insertMapFeatures(it.eng.spagobi.geo.bo.SbiGeoMapFeatures)
	 */
	public void insertMapFeatures(SbiGeoMapFeatures aMapFeature) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiGeoMapFeatures hibMapFeature = new SbiGeoMapFeatures();	
			hibMapFeature.setId(aMapFeature.getId());
			hibMapFeature.setSvgGroup(aMapFeature.getSvgGroup());
			hibMapFeature.setVisibleFlag(aMapFeature.getVisibleFlag());
			aSession.save(hibMapFeature);
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
	 * @see it.eng.spagobi.bo.dao.ISbiGeoMapFeaturesDAO#eraseObjParuse(it.eng.spagobi.geo.bo.SbiGeoMapFeatures)
	 */
	public void eraseMapFeatures(SbiGeoMapFeatures aMapFeature) throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiGeoMapFeatures hibMapFeature = (SbiGeoMapFeatures) aSession.load(SbiGeoMapFeatures.class,
					aMapFeature.getId());

			aSession.delete(hibMapFeature);
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

}
