/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.map.provider;

import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.geo.configuration.MapConfiguration;

import javax.xml.stream.XMLStreamReader;

import org.w3c.dom.svg.SVGDocument;

/**
 * Defines methods to get an xml stream reader of the svg map.
 */
public class AbstractMapProvider implements IMapProvider {

	protected SourceBean mapProviderConfiguration;
	
    /**
     * Constructors
     */
	public AbstractMapProvider() {
        super();
    }
	
    public AbstractMapProvider(SourceBean mapProviderConfiguration) {
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
	public SVGDocument getSVGMapDOMDocument() throws EMFUserError {
		return null;
	}

	public SourceBean getMapProviderConfiguration() {
		return mapProviderConfiguration;
	}

	public void setMapProviderConfiguration(SourceBean mapProviderConfiguration) {
		this.mapProviderConfiguration = mapProviderConfiguration;
	}
    

}
