/**
 * 
 */
package it.eng.spagobi.mapcatalogue.bo.dao.hibernate;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.mapcatalogue.bo.GeoFeature;
import it.eng.spagobi.mapcatalogue.bo.dao.ISbiGeoFeaturesDAO;
import it.eng.spagobi.mapcatalogue.metadata.SbiGeoFeatures;

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
	public GeoFeature loadFeatureByID(Integer featureID) throws EMFUserError {
		GeoFeature toReturn = null;
		Session tmpSession = null;
		Transaction tx = null;

		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			//toReturn = (SbiGeoFeatures)tmpSession.load(SbiGeoFeatures.class,  featureID);
			SbiGeoFeatures hibFeature = (SbiGeoFeatures)tmpSession.load(SbiGeoFeatures.class,  featureID);
			toReturn = toGeoFeature(hibFeature);
			tx.commit();
			
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
				
			}
		}
		
		return toReturn;
	}
	
	/**
	 * @see it.eng.spagobi.geo.bo.dao.ISbiGeoFeaturesDAO#loadFeatureByName(string)
	 */
	public GeoFeature loadFeatureByName(String name) throws EMFUserError {
		GeoFeature biFeature = null;
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			Criterion labelCriterrion = Expression.eq("name",
					name);
			Criteria criteria = tmpSession.createCriteria(SbiGeoFeatures.class);
			criteria.add(labelCriterrion);	
			//List tmpLst = criteria.list();
			//return first feature (unique)
			//if (tmpLst != null && tmpLst.size()>0 ) biFeature = (SbiGeoFeatures)tmpLst.get(0);
			SbiGeoFeatures hibFeature = (SbiGeoFeatures) criteria.uniqueResult();
			if (hibFeature == null) return null;
			biFeature = toGeoFeature(hibFeature);	
			
			tx.commit();
		} catch (HibernateException he) {
			logException(he);
			if (tx != null)
				tx.rollback();
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}
			
		}
		return biFeature;		
	}

	
	/**
	 * @see it.eng.spagobi.geo.bo.dao.IEngineDAO#modifyEngine(it.eng.spagobi.bo.Engine)
	 */
	public void modifyFeature(GeoFeature aFeature) throws EMFUserError {
		
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();

			SbiGeoFeatures hibFeature = (SbiGeoFeatures) tmpSession.load(SbiGeoFeatures.class, new Integer(aFeature.getFeatureId()));
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
			
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}
			
		}

	}

	/**
	 * @see it.eng.spagobi.geo.bo.dao.IEngineDAO#insertEngine(it.eng.spagobi.bo.Engine)
	 */
	public void insertFeature(GeoFeature aFeature) throws EMFUserError {		
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			SbiGeoFeatures hibFeature = new SbiGeoFeatures();
			//hibFeature.setFeatureId(new Integer(-1));
			hibFeature.setName(aFeature.getName());
			hibFeature.setDescr(aFeature.getDescr());
			hibFeature.setType(aFeature.getType());
			tmpSession.save(hibFeature);
			tx.commit();
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}
			
		}
	}

	/**
	 * @see it.eng.spagobi.geo.bo.dao.IEngineDAO#eraseEngine(it.eng.spagobi.bo.Engine)
	 */
	public void eraseFeature(GeoFeature aFeature) throws EMFUserError {
		
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			SbiGeoFeatures hibFeature = (SbiGeoFeatures) tmpSession.load(SbiGeoFeatures.class,
					new Integer(aFeature.getFeatureId()));

			tmpSession.delete(hibFeature);
			tx.commit();
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}
			
		}
	}
	
	/**
	 * @see it.eng.spagobi.geo.bo.dao.IEngineDAO#loadAllEngines()
	 */
	public List loadAllFeatures() throws EMFUserError {
		Session tmpSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();

			Query hibQuery = tmpSession.createQuery(" from SbiGeoFeatures");
			
			List hibList = hibQuery.list();
			Iterator it = hibList.iterator();			
			while (it.hasNext()) {			
				SbiGeoFeatures hibFeature = (SbiGeoFeatures) it.next();	
				if (hibFeature != null) {
					GeoFeature bifeature = toGeoFeature(hibFeature);	
					realResult.add(bifeature);
				}
			}

			tx.commit();
		} catch (HibernateException he) {
			logException(he);

			if (tx != null)
				tx.rollback();

			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);

		} finally {
			
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}
			
		}
		return realResult;
	}
	/**
	 * @see it.eng.spagobi.geo.bo.dao.ISbiGeoMapsDAO#hasFeaturesAssociated(java.lang.String)
	 */
	public boolean hasMapsAssociated (String featureId) throws EMFUserError{
		boolean bool = false; 
		
		
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			Integer mapIdInt = Integer.valueOf(featureId);
			
			String hql = " from SbiGeoMapFeatures s where s.id.featureId = "+ mapIdInt;
			Query aQuery = tmpSession.createQuery(hql);
			
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
			if (tmpSession!=null){
				if (tmpSession.isOpen()) tmpSession.close();
			}
		}
		return bool;
		
	}

	/**
	 * From the Hibernate Feature object at input, gives the corrispondent 
	 * <code>GeoFeature</code> object.
	 * 
	 * @param hibFeature	The Hibernate Feature object  
	 * @return the corrispondent output <code>GeoFeature</code>
	 */
	public GeoFeature toGeoFeature(SbiGeoFeatures hibFeature){
		
		GeoFeature feature = new GeoFeature();
		feature.setFeatureId(hibFeature.getFeatureId());
		feature.setName(hibFeature.getName());
		feature.setDescr(hibFeature.getDescr());
		feature.setType(hibFeature.getType());
	
		/*
		List maps = new ArrayList();	
		Set hibMaps = hibFeature.getSbiGeoMapFeatureses();			
		for (Iterator it = hibMaps.iterator(); it.hasNext(); ) {
			SbiGeoMapFeatures hibMapFeatures = (SbiGeoMapFeatures) it.next();				
			Integer mapId = hibMapFeatures.getId().getFeatureId();				
			maps.add(mapId);
		}
			
		feature.setBiMaps(maps);
		*/
		return feature;
	}
}