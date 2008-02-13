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
package it.eng.spagobi.engines.geo.commons.service;

import java.io.IOException;
import java.util.List;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.geo.commons.excpetion.GeoEngineException;
import it.eng.spagobi.engines.geo.configuration.MapConfiguration;
import it.eng.spagobi.engines.geo.service.initializer.GeoEngineStartAction;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AbstractGeoEngineAction extends AbstractBaseHttpAction {
	
	public static final String SPAGOBI_REQUEST = "SPAGOBI_REQUEST";
	public static final String SPAGOBI_SUBOBJECT_DETAILS = "SUBOBJECT";
	public static final String MAP_CONFIGURATION = "CONFIGURATION";
	
	/**
     * Logger component
     */
    private static transient Logger logger = Logger.getLogger(AbstractGeoEngineAction.class);
    
	
	public void init(SourceBean config) {
        super.init(config);
    } 
	
	public void service(SourceBean request, SourceBean response) throws GeoEngineException {
		setRequest( request );
		setResponse( response );
	}
	
	private SpagoBIRequest getSpagoBIRequest() {
		return (SpagoBIRequest)getAttributeFromSession( SPAGOBI_REQUEST );
	}
	
	public GeoEngineStartAction.SubObjectDetails getSpagoBISubObjectDetails() {
		return (GeoEngineStartAction.SubObjectDetails)getAttributeFromSession( SPAGOBI_SUBOBJECT_DETAILS );
	}
	
	public MapConfiguration getMapConfiguration() {
		return (MapConfiguration)getAttributeFromSession( MAP_CONFIGURATION );
	}
	
	public String getUserId() {
		String userId = null;
		if( getSpagoBIRequest() != null) {
			userId = getSpagoBIRequest().getUserId();
		}
		return userId;
	}
	
	public String getDocumentId() {
		String documentId = null;
		if( getSpagoBIRequest() != null) {
			documentId = getSpagoBIRequest().getDocumentId();
		}
		return documentId;
	}
	
	private MapCatalogueAccessUtils getMapCatalogue(){
		MapCatalogueAccessUtils mapCatalogueClient = null;
		mapCatalogueClient = MapConfiguration.getMapCatalogueAccessUtils();
		if(mapCatalogueClient == null) {
			logger.warn("Impossible to get MapCatalogueAccessUtils from MapConfiguration");
		}
		return mapCatalogueClient;
	}
	
	public List getFeatureNamesInMap(String mapName) throws GeoEngineException  {
		try {
			return getMapCatalogue().getFeatureNamesInMap(mapName);
		} catch (Exception e) {
			throw new GeoEngineException("Impossible to get names of features enbedded in map " + mapName, e);
		}
	}
	
	public List getMapNamesByFeature(String featureName) throws GeoEngineException {
		try {
			return getMapCatalogue().getMapNamesByFeature(featureName);
		} catch (Exception e) {
			throw new GeoEngineException("Impossible to get names of maps that contain feature " + featureName, e);
		}
	}
}
