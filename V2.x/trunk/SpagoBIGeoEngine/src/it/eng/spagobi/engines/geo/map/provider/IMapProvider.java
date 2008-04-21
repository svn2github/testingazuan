/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.geo.map.provider;

import java.util.List;

import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.engines.geo.IGeoEngineComponent;
import it.eng.spagobi.engines.geo.commons.excpetion.GeoEngineException;
import it.eng.spagobi.engines.geo.dataset.DataSet;

import javax.xml.stream.XMLStreamReader;

import org.w3c.dom.svg.SVGDocument;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public interface IMapProvider extends IGeoEngineComponent {
    
	
	XMLStreamReader getSVGMapStreamReader() throws GeoEngineException;
	XMLStreamReader getSVGMapStreamReader(String mapName) throws GeoEngineException;
	
	SVGDocument getSVGMapDOMDocument() throws GeoEngineException;	
	SVGDocument getSVGMapDOMDocument(String mapName) throws GeoEngineException;
	
	String getSelectedMapName();
	void setSelectedMapName(String mapName);
	
	List getMapNamesByFeature(String featureName) throws Exception;
	List getFeatureNamesInMap(String mapName) throws Exception;
}
