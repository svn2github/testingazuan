/**
 * 
 */
package it.eng.spagobi.geo.bo.dao.hibernate;

import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.geo.bo.SbiGeoFeatures;
import it.eng.spagobi.geo.bo.SbiGeoMaps;
import it.eng.spagobi.geo.bo.dao.ISbiGeoMapsDAO;
import it.eng.spagobi.geo.configuration.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
	public SbiGeoMaps loadMapByID(Integer mapID) throws EMFUserError {
		SbiGeoMaps toReturn = null;
		Session aSession = null;
		Transaction tx = null;

		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			toReturn = (SbiGeoMaps)aSession.load(SbiGeoMaps.class,  mapID);			
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
	 * @see List#loadMapByName(string)
	 *//*
	public List loadMapByName(String name) throws EMFUserError {
		List results = new ArrayList();		
		File f = new File(ConfigSingleton.getRootPath() + "/maps/genova/" + name + ".svg");
		String url = null;
		try {
			url = f.toURL().toString();
		} catch (MalformedURLException e) {
			TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, 
		            "DefaultMapProvider :: getSVGMapStreamReader : " +
		            "map file not found, path " + f.toString());
			throw new EMFUserError(EMFErrorSeverity.ERROR, "error.mapfile.notfound");
		}
		
		SbiGeoMaps map = new  SbiGeoMaps(1, name, name.toUpperCase(), 
				url,
				null);
		results.add(map);
		return results;		
	}
	*/  
	/**
	 * @see it.eng.spagobi.geo.bo.dao.ISbiGeoMapsDAO#loadMapByName(string)
	 */	
	public SbiGeoMaps loadMapByName(String name) throws EMFUserError {
		SbiGeoMaps biMap = null;
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Criterion labelCriterrion = Expression.eq("name",
					name);
			Criteria criteria = aSession.createCriteria(SbiGeoMaps.class);
			criteria.add(labelCriterrion);
			biMap = (SbiGeoMaps) criteria.uniqueResult();			
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
		return biMap;		
	}

	/**
	 * @see it.eng.spagobi.geo.bo.dao.IEngineDAO#modifyEngine(it.eng.spagobi.bo.Engine)
	 */
	public void modifyMap(SbiGeoMaps aMap) throws EMFUserError {
		
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			SbiGeoMaps hibMap = (SbiGeoMaps) aSession.load(SbiGeoMaps.class, aMap.getMapId());
			hibMap.setName(aMap.getName());
			hibMap.setDescr(aMap.getDescr());
			hibMap.setUrl(aMap.getUrl());			
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
	public void insertMap(SbiGeoMaps aMap) throws EMFUserError {		
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			SbiGeoMaps hibMap = new SbiGeoMaps();
			//hibMap.setMapId(new Integer(-1));
			hibMap.setName(aMap.getName());
			hibMap.setDescr(aMap.getDescr());
			hibMap.setUrl(aMap.getUrl());
			aSession.save(hibMap);
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
	public void eraseMap(SbiGeoMaps aMap) throws EMFUserError {
		
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			
			SbiGeoMaps hibMap = (SbiGeoMaps) aSession.load(SbiGeoMaps.class,
					aMap.getMapId());

			aSession.delete(hibMap);
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
	public List loadAllMaps() throws EMFUserError {
		Session aSession = null;
		Transaction tx = null;
		List realResult = new ArrayList();
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();

			Query hibQuery = aSession.createQuery(" from SbiGeoMaps");
			
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
	public boolean hasFeaturesAssociated (String mapId) throws EMFUserError{
		boolean bool = false; 
		
		
		Session aSession = null;
		Transaction tx = null;
		try {
			aSession = getSession();
			tx = aSession.beginTransaction();
			Integer mapIdInt = Integer.valueOf(mapId);
			
			String hql = " from SbiGeoMapFeatures s where s.id.mapId = "+ mapIdInt;
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
	
	/**
	 * Gets the features (tag <g>) from the SVG File.
	 * @param url The url about svg file
	 * @throws Exception raised If there are some problems
	 */ 
	public List getFeaturesFromSVG(String url) throws Exception {
		// load a svg file
		XMLInputFactory xmlIF =XMLInputFactory.newInstance();		
		//xmlIF.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES,Boolean.TRUE);
		//xmlIF.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES,Boolean.FALSE);     
		xmlIF.setProperty(XMLInputFactory.IS_COALESCING , Boolean.TRUE);
		String pathMapFile = url;  
		FileInputStream fisMap = null;		
		List lstFeatures = null;
		HashMap feature;
		
		try {
			fisMap = new FileInputStream(pathMapFile);
		} catch (FileNotFoundException e) {
			TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, 
					            "SbiGeoMapsDAOHibImpl :: getFeaturesFromSVG : " +
					            "file svg not found, path " + pathMapFile);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "error.mapfile.notfound");
		}
		XMLStreamReader streamReader = null;
		try {
			streamReader = xmlIF.createXMLStreamReader(fisMap);
		} catch (XMLStreamException e) {
			TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, 
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

}