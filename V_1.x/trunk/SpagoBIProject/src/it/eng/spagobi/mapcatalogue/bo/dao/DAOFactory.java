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
 * Created on 1-giu-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.mapcatalogue.bo.dao;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.constants.SpagoBIConstants;

/**
 * Contains all the data access object for all the BO objects defined into
 * it.eng.spagobi.geo.bo package.
 * 
 * @author Giachino
 */
public class DAOFactory {
	
	/**
	 * Given, for a defined BO, its DAO name, creates the correct DAO instance 
	 * 
	 * 
	 * @param daoName The BO DAO name
	 * @return An object representing the DAO instance
	 * @throws EMFUserError If an Exception occurred
	 */
	
	private static Object createDAOInstance(String daoName) throws EMFUserError {
		TracerSingleton.log(SpagoBIConstants.NAME_MODULE, TracerSingleton.DEBUG, "Begin Istantiation of DAO ["+daoName+"]");
		Object daoObject = null;
		try{
			SourceBean daoConfigSourceBean =(SourceBean) ConfigSingleton.getInstance().getFilteredSourceBeanAttribute("GEOENGINE.DAO-CONF.DAO","name", daoName);
			String daoClassName = (String)daoConfigSourceBean.getAttribute("implementation");
			TracerSingleton.log(SpagoBIConstants.NAME_MODULE, TracerSingleton.DEBUG, "DAO ["+daoName+"] Implementation class ["+daoClassName+"]");			
			daoObject = Class.forName(daoClassName).newInstance();
			TracerSingleton.log(SpagoBIConstants.NAME_MODULE, TracerSingleton.DEBUG, "DAO ["+daoName+"] Instatiate successfully");	
		}catch(Exception e){
			TracerSingleton.log(SpagoBIConstants.NAME_MODULE, TracerSingleton.CRITICAL, "Error occurred"+ e.getLocalizedMessage());
			throw new EMFUserError(EMFErrorSeverity.ERROR, 1000);
		}
		return daoObject;
		
	}
	/**
	 * Creates a DAO instance for a BI object
	 * 
	 * @return a DAO instance for the BIObject
	 * @throws EMFUserError If an Exception occurred
	 */
	public static ISbiGeoMapsDAO getSbiGeoMapsDAO() throws EMFUserError {
		return (ISbiGeoMapsDAO)createDAOInstance("GeoMapDAO");
	}
	
	/**
	 * Creates a DAO instance for a BI object
	 * 
	 * @return a DAO instance for the BIObject
	 * @throws EMFUserError If an Exception occurred
	 */
	public static ISbiGeoFeaturesDAO getSbiGeoFeaturesDAO() throws EMFUserError {
		return (ISbiGeoFeaturesDAO)createDAOInstance("GeoFeatureDAO");
	}
	/**
	 * Creates a DAO instance for a BI object
	 * 
	 * @return a DAO instance for the BIObject
	 * @throws EMFUserError If an Exception occurred
	 */
	public static ISbiGeoMapFeaturesDAO getSbiGeoMapFeaturesDAO() throws EMFUserError {
		return (ISbiGeoMapFeaturesDAO)createDAOInstance("GeoMapFeatureDAO");
	}
	
}
