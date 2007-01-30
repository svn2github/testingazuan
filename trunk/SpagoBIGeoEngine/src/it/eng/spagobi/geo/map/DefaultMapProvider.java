/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.map;

import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.geo.configuration.Constants;
import it.eng.spagobi.geo.configuration.MapConfiguration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Defines methods to get an xml stream reader of the svg map.
 */
public class DefaultMapProvider extends AbstractMapProvider {

	/**
	 * Constructor
	 */
	public DefaultMapProvider() {
		super();
	}
	
	/**
     * Gets an xml stream reader of the svg map.
     * @param mapConf MapConfiguration object which contains the configuration for the 
     * map recovering
     */
	public XMLStreamReader getSVGMapStreamReader(MapConfiguration mapConf) throws EMFUserError {
		XMLInputFactory xmlIF = null ;
		xmlIF = XMLInputFactory.newInstance();
		xmlIF.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES,Boolean.TRUE);
		xmlIF.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES,Boolean.FALSE);     
		xmlIF.setProperty(XMLInputFactory.IS_COALESCING , Boolean.TRUE);
		String pathMapFile = ConfigSingleton.getRootPath() + "/maps/" + mapConf.getMapName() + "/map.svg";  
		FileInputStream fisMap = null;
		try {
			fisMap = new FileInputStream(pathMapFile);
		} catch (FileNotFoundException e) {
			TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, 
					            "DefaultMapProvider :: getSVGMapStreamReader : " +
					            "map file not found, path " + pathMapFile);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "error.mapfile.notfound");
		}
		XMLStreamReader streamReader = null;
		try {
			streamReader = xmlIF.createXMLStreamReader(fisMap);
		} catch (XMLStreamException e) {
			TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, 
		            			"DefaultMapProvider :: getSVGMapStreamReader : " +
		            			"Cannot load the stream of the map file, path " + pathMapFile);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "error.mapfile.notloaded");
		}
		if(streamReader==null) {
			throw new EMFUserError(EMFErrorSeverity.ERROR, "error.mapfile.notloaded");
		}
		return streamReader;
	}

}