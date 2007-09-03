/**
 * 
 */
package it.eng.spagobi.mapcatalogue.bo.dao.hibernate;

import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.mapcatalogue.bo.GeoFeature;
import it.eng.spagobi.mapcatalogue.bo.GeoMap;
import it.eng.spagobi.mapcatalogue.bo.dao.ISbiGeoMapsDAO;
import it.eng.spagobi.mapcatalogue.metadata.SbiGeoMapFeaturesId;
import it.eng.spagobi.mapcatalogue.metadata.SbiGeoMaps;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

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
public class SbiGeoMapsDAOHibImpl extends AbstractHibernateDAO implements ISbiGeoMapsDAO{
	/**
	 * @see it.eng.spagobi.geo.bo.dao.ISbiGeoMapsDAO#loadMapByID(integer)
	 */
	public GeoMap loadMapByID(Integer mapID) throws EMFUserError {
		GeoMap toReturn = null;
		Session tmpSession = null;
		Transaction tx = null;

		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			//toReturn = (SbiGeoMaps)tmpSession.load(SbiGeoMaps.class,  mapID);	
			SbiGeoMaps hibMap = (SbiGeoMaps)tmpSession.load(SbiGeoMaps.class,  mapID);
			toReturn = toGeoMap(hibMap);
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
	 * @see it.eng.spagobi.geo.bo.dao.ISbiGeoMapsDAO#loadMapByName(string)
	 */	
	public GeoMap loadMapByName(String name) throws EMFUserError {
		GeoMap biMap = null;
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			Criterion labelCriterrion = Expression.eq("name",
					name);
			Criteria criteria = tmpSession.createCriteria(SbiGeoMaps.class);
			criteria.add(labelCriterrion);	
			//List tmpLst = criteria.list();
			//return first map (unique)
			//if (tmpLst != null && tmpLst.size()>0) biMap = (SbiGeoMaps)tmpLst.get(0);		
			//if (tmpLst != null && tmpLst.size()>0) biMap = (SbiGeoMaps)tmpLst.get(0);
			SbiGeoMaps hibMap = (SbiGeoMaps) criteria.uniqueResult();
			if (hibMap == null) return null;
			biMap = toGeoMap(hibMap);				
			
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
		return biMap;		
	}

	/**
	 * @see it.eng.spagobi.geo.bo.dao.IEngineDAO#modifyEngine(it.eng.spagobi.bo.Engine)
	 */
	public void modifyMap(GeoMap aMap) throws EMFUserError {
		
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();

			SbiGeoMaps hibMap = (SbiGeoMaps) tmpSession.load(SbiGeoMaps.class, new Integer(aMap.getMapId()));
			hibMap.setName(aMap.getName());
			hibMap.setDescr(aMap.getDescr());
			hibMap.setUrl(aMap.getUrl());			
			hibMap.setFormat(aMap.getFormat());
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
	public void insertMap(GeoMap aMap) throws EMFUserError {		
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			SbiGeoMaps hibMap = new SbiGeoMaps();
			//hibMap.setMapId(new Integer(-1));
			hibMap.setName(aMap.getName());
			hibMap.setDescr(aMap.getDescr());
			hibMap.setUrl(aMap.getUrl());
			hibMap.setFormat(aMap.getFormat());
			tmpSession.save(hibMap);
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
	public void eraseMap(GeoMap aMap) throws EMFUserError {
		
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			
			SbiGeoMaps hibMap = (SbiGeoMaps) tmpSession.load(SbiGeoMaps.class,
					new Integer(aMap.getMapId()));

			tmpSession.delete(hibMap);
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
	public List loadAllMaps() throws EMFUserError {
		Session tmpSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();

			Query hibQuery = tmpSession.createQuery(" from SbiGeoMaps");
			
			List hibList = hibQuery.list();
			Iterator it = hibList.iterator();			
			while (it.hasNext()) {			
				SbiGeoMaps hibMap = (SbiGeoMaps) it.next();	
				if (hibMap != null) {
					GeoMap biMap = toGeoMap(hibMap);	
					realResult.add(biMap);
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
	public boolean hasFeaturesAssociated (String mapId) throws EMFUserError{
		boolean bool = false; 
		
		
		Session tmpSession = null;
		Transaction tx = null;
		try {
			tmpSession = getSession();
			tx = tmpSession.beginTransaction();
			Integer mapIdInt = Integer.valueOf(mapId);
			
			String hql = " from SbiGeoMapFeatures s where s.id.mapId = "+ mapIdInt;
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
	 * Gets the features (tag <g>) from the SVG File.
	 * @param url The relative url about svg file
	 * @throws Exception raised If there are some problems
	 */ 
	public List getFeaturesFromSVG(String url) throws Exception {
		// load a svg file
		XMLInputFactory xmlIF =XMLInputFactory.newInstance();		
		//xmlIF.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES,Boolean.TRUE);
		//xmlIF.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES,Boolean.FALSE);     
		xmlIF.setProperty(XMLInputFactory.IS_COALESCING , Boolean.TRUE);
		//defines assolute path
		String pathMapFile = ConfigSingleton.getRootPath() + url;  
		FileInputStream fisMap = null;		
		List lstFeatures = null;
		HashMap feature;
		
		try {
			fisMap = new FileInputStream(pathMapFile);
		} catch (FileNotFoundException e) {
			TracerSingleton.log(SpagoBIConstants.NAME_MODULE, TracerSingleton.MAJOR, 
					            "SbiGeoMapsDAOHibImpl :: getFeaturesFromSVG : " +
					            "file svg not found, path " + pathMapFile);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "error.mapfile.notfound");
		}
		XMLStreamReader streamReader = null;
		try {
			streamReader = xmlIF.createXMLStreamReader(fisMap);
		} catch (XMLStreamException e) {
			TracerSingleton.log(SpagoBIConstants.NAME_MODULE, TracerSingleton.MAJOR, 
		            			"SbiGeoMapsDAOHibImpl :: getFeaturesFromSVG : " +
		            			"Cannot load the stream of the file svg, path " + pathMapFile);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "error.mapfile.notloaded");
		}
		if(streamReader==null) {
			throw new EMFUserError(EMFErrorSeverity.ERROR, "error.mapfile.notloaded");
		}	
		streamReader.next();

		try{
			int event = streamReader.getEventType();
			int nFeature=-1;
			lstFeatures = new ArrayList();
		    while (true) { 
		    	switch (event) {
		            case XMLStreamConstants.START_DOCUMENT:
		                 break;
		            case XMLStreamConstants.START_ELEMENT:
		            	// get the tag name
		            	String tagname = streamReader.getLocalName();       
		            	if(tagname.trim().equalsIgnoreCase("g")) {		            		
			            	for(int i=0, n=streamReader.getAttributeCount(); i<n; ++i) {
			            		String attrName = streamReader.getAttributeName(i).toString();
			            		String attrValue = streamReader.getAttributeValue(i);
			            		// if the attribute is the id, search values and set the style		
			            		feature = new HashMap();
			            		if(attrName.equalsIgnoreCase("id")) {	
			            			nFeature++;
			            			feature.put("id",attrValue);
				        		}
			            		if(attrName.equalsIgnoreCase("descr")) {		            			
			            			feature.put("descr",attrValue);
				        		}
			            		if(attrName.equalsIgnoreCase("type")) {		            			
			            			feature.put("type",attrValue);
				        		}		
			            		if (feature.size()>0) lstFeatures.add(feature);						            		
			            	}			            				            	
		            	}
		            	break;
		            case XMLStreamConstants.END_ELEMENT:
		               //tagname = streamReader.getLocalName();
		                //if(tagname.trim().equalsIgnoreCase("g")) {
		                //}
		            	break;
		            case XMLStreamConstants.END_DOCUMENT:
		                  break;
		            }
		            if (!streamReader.hasNext())
		                  break;
		            event = streamReader.next();
		      }
		} finally {
			streamReader.close();
		}
		
		return lstFeatures;
	}
	/**
	 * From the Hibernate Map object at input, gives the corrispondent 
	 * <code>GeoMap</code> object.
	 * 
	 * @param hibMap	The Hibernate Map object
	 * @param recoverFeatures If true the <code>GeoMap</code> at output will have the 
	 * list of contained <code>GeoFeatures</code> objects
	 * @return the corrispondent output <code>GeoMap</code>
	 */
	public GeoMap toGeoMap(SbiGeoMaps hibMap){
		
		GeoMap map = new GeoMap();
		map.setMapId(hibMap.getMapId());
		map.setName(hibMap.getName());
		map.setDescr(hibMap.getDescr());
		map.setFormat(hibMap.getFormat());
		map.setUrl(hibMap.getUrl());
		
		/*
		List features = new ArrayList();	
		Set hibFeatures = hibMap.getSbiGeoMapFeatureses();			
		for (Iterator it = hibFeatures.iterator(); it.hasNext(); ) {
			SbiGeoMapFeatures hibMapFeatures = (SbiGeoMapFeatures) it.next();				
			Integer featureId = hibMapFeatures.getId().getFeatureId();				
			features.add(featureId);
		}			
		map.setBiFeatures(features);
		*/
		return map;
	}

}