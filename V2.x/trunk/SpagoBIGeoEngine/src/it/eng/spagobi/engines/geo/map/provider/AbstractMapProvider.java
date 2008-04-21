/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.geo.map.provider;

import java.util.List;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.engines.geo.AbstractGeoEngineComponent;
import it.eng.spagobi.engines.geo.commons.excpetion.GeoEngineException;
import it.eng.spagobi.engines.geo.dataset.DataSet;
import it.eng.spagobi.engines.geo.dataset.provider.SQLDatasetProvider;
import it.eng.spagobi.engines.geo.dataset.provider.configurator.AbstractDatasetProviderConfigurator;
import it.eng.spagobi.engines.geo.map.provider.configurator.AbstractMapProviderConfigurator;

import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;
import org.w3c.dom.svg.SVGDocument;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AbstractMapProvider extends AbstractGeoEngineComponent implements IMapProvider {

	private String selectedMapName;
		
	/**
     * Logger component
     */
    public static transient Logger logger = Logger.getLogger(AbstractMapProvider.class);
	
	
	public AbstractMapProvider() {
        super();
    }
	
	public void init(Object conf) throws GeoEngineException {
		super.init(conf);
		AbstractMapProviderConfigurator.configure( this, getConf() );
	}
	
   
	public XMLStreamReader getSVGMapStreamReader() throws GeoEngineException {
    	return getSVGMapStreamReader(selectedMapName);
    }
	
    public XMLStreamReader getSVGMapStreamReader(String mapName) throws GeoEngineException {
    	return null;
    }
    
    public SVGDocument getSVGMapDOMDocument() throws GeoEngineException {
		return getSVGMapDOMDocument(selectedMapName);
	}
    
	public SVGDocument getSVGMapDOMDocument(String mapName) throws GeoEngineException {
		return null;
	}


	public String getSelectedMapName() {
		return selectedMapName;
	}


	public void setSelectedMapName(String selectedMapName) {
		this.selectedMapName = selectedMapName;
	}

	public List getMapNamesByFeature(String featureName) throws Exception {
		return null;
	}

	public List getFeatureNamesInMap(String mapName) throws Exception {
		return null;
	}

	
   

}
