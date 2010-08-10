/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.map;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.geo.configuration.MapConfiguration;

import javax.xml.stream.XMLStreamReader;

/**
 * Defines methods for the svg map stream recovering
 */
public interface MapProviderIFace {
    
	/**
	 * Gets an xml stream of the svg map. The svg map is located usign the configuration 
	 * contained into the input parameter
	 * @param mapConf contains the configuration for the svg map recovering
	 * @return an xml stream of the svg map
	 * @throws EMFUserError if some errors occur during the xml stream recovering
	 */
	public abstract XMLStreamReader getSVGMapStreamReader(MapConfiguration mapConf) throws EMFUserError;

}
