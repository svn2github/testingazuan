/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.map.provider;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.geo.configuration.MapProviderConfiguration;
import it.eng.spagobi.geo.datamart.Datamart;

import javax.xml.stream.XMLStreamReader;

import org.w3c.dom.svg.SVGDocument;

/**
 * Defines methods for the svg map stream recovering
 */
public interface IMapProvider {
    
	/**
     * Gets an xml stream reader of the svg map.
     * @param mapProviderConfiguration SourceBean object which contains the configuration for the 
     * map recovering
     */
	public abstract XMLStreamReader getSVGMapStreamReader() throws EMFUserError;
	
	/**
     * Gets the DOM of the svg map.
     * @param mapProviderConfiguration SourceBean object which contains the configuration for the 
     * map recovering
     */
	public abstract SVGDocument getSVGMapDOMDocument(Datamart datamart) throws EMFUserError;

	
	public abstract MapProviderConfiguration getMapProviderConfiguration();

	public abstract void setMapProviderConfiguration(MapProviderConfiguration mapProviderConfiguration);
	
}
