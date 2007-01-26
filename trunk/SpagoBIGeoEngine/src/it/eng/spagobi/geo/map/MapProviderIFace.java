/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.map;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.geo.configuration.MapConfiguration;

import javax.xml.stream.XMLStreamReader;

public interface MapProviderIFace {
    
	public abstract XMLStreamReader getSVGMapStreamReader(MapConfiguration mapConf) throws EMFUserError;

}
