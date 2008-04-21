/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.geo.map.provider;

import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.engines.geo.Constants;
import it.eng.spagobi.engines.geo.commons.excpetion.GeoEngineException;
import it.eng.spagobi.engines.geo.dataset.DataSet;
import it.eng.spagobi.engines.geo.dataset.provider.SQLDatasetProvider;
import it.eng.spagobi.engines.geo.map.provider.configurator.AbstractMapProviderConfigurator;
import it.eng.spagobi.engines.geo.map.provider.configurator.SOMapProviderConfigurator;
import it.eng.spagobi.engines.geo.map.utils.SVGMapLoader;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;
import org.w3c.dom.svg.SVGDocument;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SOMapProvider extends AbstractMapProvider {

	private MapCatalogueAccessUtils mapCatalogueServiceProxy;
	
	
	/**
     * Logger component
     */
    public static transient Logger logger = Logger.getLogger(SOMapProvider.class);
	
	
	public SOMapProvider() {
		 super();
	}
	
	public void init(Object conf) throws GeoEngineException {
		super.init(conf);
		SOMapProviderConfigurator.configure( this, getConf() );
	}
	
	public SVGDocument getSVGMapDOMDocument(String mapName) throws GeoEngineException {
		SVGDocument svgDocument = null;		
		String mapUrl = null;		
		
			
		try {
			mapUrl = mapCatalogueServiceProxy.getMapUrl(mapName);
		} catch (Exception e) {
			logger.error("An error occurred while invoking mapCatalogueService method: getMapUrl()");
			throw new GeoEngineException("Impossible to load map from url: " + mapUrl, e);
		}
		
		try {
			svgDocument = SVGMapLoader.loadMapAsDocument(mapUrl);
		} catch (IOException e) {
			logger.error("Impossible to load map from url: " + mapUrl);
			throw new GeoEngineException("Impossible to load map from url: " + mapUrl, e);
		}
		
		
		
		
		return svgDocument;
	}
	
	public XMLStreamReader getSVGMapStreamReader(String mapName) throws GeoEngineException {
		XMLStreamReader streamReader = null;
		String mapUrl = null;		
		
		
		try {
			mapUrl = mapCatalogueServiceProxy.getMapUrl(mapName);
		} catch (Exception e) {
			logger.error("An error occurred while invoking mapCatalogueService method: getMapUrl()");
			throw new GeoEngineException("Impossible to load map from url: " + mapUrl, e);
		}
		
		try {
			streamReader = SVGMapLoader.getMapAsStream(mapUrl);
		} catch (XMLStreamException e) {
			logger.error("An error occurred while processing xml stream of the svg map");
			throw new GeoEngineException("An error occurred while processing xml stream of the svg map", e);
		} catch (FileNotFoundException e) {
			logger.error("Map file not found at url: " + mapUrl);
			throw new GeoEngineException("Map file not found at url: " + mapUrl, e);
		}
		
		
		return streamReader;
	}

	public MapCatalogueAccessUtils getMapCatalogueServiceProxy() {
		return mapCatalogueServiceProxy;
	}

	public void setMapCatalogueServiceProxy(
			MapCatalogueAccessUtils mapCatalogueServiceProxy) {
		this.mapCatalogueServiceProxy = mapCatalogueServiceProxy;
	}

	
	public List getMapNamesByFeature(String featureName) throws Exception {
		 return getMapCatalogueServiceProxy().getMapNamesByFeature(featureName);
	}
	
	public List getFeatureNamesInMap(String mapName) throws Exception{
		 return getMapCatalogueServiceProxy().getFeatureNamesInMap(mapName);
	}
}