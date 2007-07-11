/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.map.provider;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.geo.configuration.Constants;
import it.eng.spagobi.geo.configuration.MapProviderConfiguration;

/**
 * Factory for the creation of instances of the classes  which implements the MapProviderIFace
 */
public class MapProviderFactory {

	/**
	 * Creates a new instance of the class identified by the name passed as input. 
	 * The class must implement the MapProviderIFace
	 * @param datamartProviderConfiguration The configuration of the provider to create
	 * @return The instace of the class 
	 * @throws Exception raised if the class don't exist or it isn't an implementation of the MapProviderIFace
	 */ 
    public static IMapProvider getMapProvider(MapProviderConfiguration mapProviderConfiguration) throws Exception {
    	IMapProvider mapProvider = null;
    	String mapProvClassName = (String) mapProviderConfiguration.getClassName();
    	mapProvider = (IMapProvider) Class.forName(mapProvClassName).newInstance();
    	mapProvider.setMapProviderConfiguration(mapProviderConfiguration);    	
        return mapProvider;
    }
}