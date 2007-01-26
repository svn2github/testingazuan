/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.map;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.geo.configuration.MapConfiguration;

import javax.xml.stream.XMLStreamReader;

public abstract class AbstractMapProvider implements MapProviderIFace {


    public AbstractMapProvider() {
        super();
    }

    public abstract XMLStreamReader getSVGMapStreamReader(MapConfiguration mapConf) throws EMFUserError;
    

}
