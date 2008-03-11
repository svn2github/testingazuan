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
import it.eng.spagobi.utilities.engines.AbstractEngineAction;
import it.eng.spagobi.utilities.engines.AbstractEngineStartAction;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AbstractGeoEngineAction extends AbstractEngineAction {
	
	public static final String MAP_CONFIGURATION = "CONFIGURATION";
	
	/**
     * Logger component
     */
    private static transient Logger logger = Logger.getLogger(AbstractGeoEngineAction.class);
    	
	

	
	public MapConfiguration getMapConfiguration() {
		return (MapConfiguration)getAttributeFromSession( MAP_CONFIGURATION );
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
