/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.map.provider;

import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.geo.configuration.MapConfiguration;
import it.eng.spagobi.geo.configuration.MapProviderConfiguration;
import it.eng.spagobi.geo.datamart.Datamart;

import javax.xml.stream.XMLStreamReader;

import org.w3c.dom.svg.SVGDocument;

/**
 * Defines methods to get an xml stream reader of the svg map.
 */
public class AbstractMapProvider implements IMapProvider {

	protected MapProviderConfiguration mapProviderConfiguration;
	
    /**
     * Constructors
     */
	public AbstractMapProvider() {
        super();
    }
	
    public AbstractMapProvider(MapProviderConfiguration mapProviderConfiguration) {
        super();
        setMapProviderConfiguration(mapProviderConfiguration);
    }

    /**
     * Gets an xml stream reader of the svg map.
     * @param mapProviderConfiguration SourceBean object which contains the configuration for the 
     * map recovering
     */
    public XMLStreamReader getSVGMapStreamReader() throws EMFUserError {
    	return null;
    }
    
	/**
     * Gets the DOM of the svg map.
     * @param mapProviderConfiguration SourceBean object which contains the configuration for the 
     * map recovering
     */
	public SVGDocument getSVGMapDOMDocument(Datamart datamart) throws EMFUserError {
		return null;
	}

	
	public MapProviderConfiguration getMapProviderConfiguration() {
		return mapProviderConfiguration;
	}

	public void setMapProviderConfiguration(MapProviderConfiguration mapProviderConfiguration) {
		this.mapProviderConfiguration = mapProviderConfiguration;
	}
   

}
