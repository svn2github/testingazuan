/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.map.provider;

import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.geo.configuration.Constants;
import it.eng.spagobi.geo.configuration.MapConfiguration;
import it.eng.spagobi.geo.configuration.MapProviderConfiguration;
import it.eng.spagobi.geo.datamart.Datamart;
import it.eng.spagobi.geo.map.utils.SVGMapLoader;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.w3c.dom.svg.SVGDocument;

/**
 * Defines methods to get an xml stream reader of the svg map.
 */
public class DBMapProvider extends AbstractMapProvider {

	/**
	 * Constructors
	 */
	public DBMapProvider() {
		 super();
	}
	
	public DBMapProvider(MapProviderConfiguration mapProviderConfiguration) {
		super(mapProviderConfiguration);
	}
	
	private String getMapUrl(String mapName) throws EMFUserError {
		try {
			return getMapProviderConfiguration().getParentConfiguration().getMapCatalogueAccessUtils().getMapUrl(mapName);
		} catch (Exception e) {
			return null;
		}  
	}
	
	/**
     * Gets the DOM of the svg map.
     * @param mapProviderConfiguration SourceBean object which contains the configuration for the 
     * map recovering
     */
	public SVGDocument getSVGMapDOMDocument(Datamart datamart) throws EMFUserError {
		SVGDocument svgDocument;
		String mapName; 
		String mapUrl = "?";		
		
		svgDocument = null;
		
			
		
		
		try {
			String targetFeatureName = datamart.getTargetFeatureName();
			
			mapName = mapProviderConfiguration.getParentConfiguration().getMapName();
			if(mapName == null) {
				List maps = MapConfiguration.getMapCatalogueAccessUtils().getMapNamesByFeature(targetFeatureName);
				mapName = (String)maps.get(0);
			}
			
			
			mapUrl = getMapUrl(mapName);
			//File file = new File(mapUrl);
			svgDocument = SVGMapLoader.loadMapAsDocument(mapUrl);
		} catch (Exception e) {
			TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, 
					            "DefaultMapProvider :: getSVGMapStreamReader : " +
					            "cannot load map file, path " + mapUrl);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "error.mapfile.notfound");
		}
		
		return svgDocument;
	}
	
	/**
     * Gets an xml stream reader of the svg map.
     * @param mapProviderConfiguration SourceBean object which contains the configuration for the 
     * map recovering
     */
	public XMLStreamReader getSVGMapStreamReader(SourceBean mapProviderConfiguration) throws EMFUserError {
		XMLStreamReader streamReader;
		String mapName; 
		String mapUrl;
		
		streamReader = null;
		
		mapName = (String)mapProviderConfiguration.getAttribute(Constants.MAP_NAME);		
		mapUrl = getMapUrl(mapName);
		
		try {
			streamReader = SVGMapLoader.getMapAsStream(mapUrl);
		} catch (FileNotFoundException e) {
			TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, 
					            "DefaultMapProvider :: getSVGMapStreamReader : " +
					            "map file not found, path " + mapUrl);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "error.mapfile.notfound");
		} catch (XMLStreamException e) {
			TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, 
        			"DefaultMapProvider :: getSVGMapStreamReader : " +
        			"Cannot load the stream of the map file, path " + mapUrl);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "error.mapfile.notloaded");
		}
		
		
		return streamReader;
	}

}