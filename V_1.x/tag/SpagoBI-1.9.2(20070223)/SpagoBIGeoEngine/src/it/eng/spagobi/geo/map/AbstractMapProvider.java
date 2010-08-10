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
 * Defines methods to get an xml stream reader of the svg map.
 */
public abstract class AbstractMapProvider implements MapProviderIFace {

    /**
     * Constructor
     */
    public AbstractMapProvider() {
        super();
    }

    /**
     * Gets an xml stream reader of the svg map.
     * @param mapConf MapConfiguration object which contains the configuration for the 
     * map recovering
     */
    public abstract XMLStreamReader getSVGMapStreamReader(MapConfiguration mapConf) throws EMFUserError;
    

}
