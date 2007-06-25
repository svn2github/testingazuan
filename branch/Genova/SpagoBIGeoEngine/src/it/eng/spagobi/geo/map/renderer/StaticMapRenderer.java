/**
 *	LICENSE: see COPYING file
**/
package it.eng.spagobi.geo.map.renderer;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.geo.configuration.Constants;
import it.eng.spagobi.geo.configuration.MapConfiguration;
import it.eng.spagobi.geo.datamart.Datamart;
import it.eng.spagobi.geo.datamart.provider.DatamartProviderFactory;
import it.eng.spagobi.geo.datamart.provider.IDatamartProvider;
import it.eng.spagobi.geo.map.provider.MapProviderFactory;
import it.eng.spagobi.geo.map.provider.IMapProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;

/**
 * Defines methods which transform and render the map 
 * @author fiscato
 */
public class StaticMapRenderer extends AbstractMapRenderer{

	private boolean closeLink = false;
	
	public StaticMapRenderer() {
		super();
	}
	
	/**
	 * @see it.eng.spagobi.geo.map.renderer.IMapRenderer#renderMap(MapConfiguration)
	 */
	public File renderMap(IMapProvider mapProvider, IDatamartProvider datamartProvider) throws Exception {

		XMLStreamReader svgMapStreamReader = mapProvider.getSVGMapStreamReader();
		
		
		Datamart datamartObject = (Datamart)datamartProvider.getDatamartObject();		
		
		String tmpDirPath = System.getProperty("java.io.tmpdir");
		File tmpDir = new File(tmpDirPath);
		File tmpMap = File.createTempFile("SpagoBIGeoEngine_", "_tmpMap", tmpDir);
		FileOutputStream fos = new FileOutputStream(tmpMap);
		
		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?> \n";
		fos.write(header.getBytes());
		
		try{
			int event = svgMapStreamReader.getEventType();
		    while (true) {
		    	switch (event) {
		            case XMLStreamConstants.START_DOCUMENT:
		                 break;
		            case XMLStreamConstants.START_ELEMENT:
		            	// get the tag name
		            	String tagname = svgMapStreamReader.getLocalName();       	
			            // if the tag name is the svg tag set the correspondent flag to true
		            	boolean issvgtag = false;
		            	if(tagname.trim().equalsIgnoreCase("svg")) {
		            		issvgtag = true;
			            }
		            	String svgcode = "";
		            	boolean hasLink = false;
		            	String startlinkcode = "";
		            	svgcode += "<" + tagname + " ";    
		            	// for each attribute check if is an xmlsn attribute and write to output
		            	boolean xmlnsfound = false;
		            	for(int i=0, n=svgMapStreamReader.getAttributeCount(); i<n; ++i) {
		            		String attrName = svgMapStreamReader.getAttributeName(i).toString();
		            		String attrValue = svgMapStreamReader.getAttributeValue(i);
		            		if(attrName.equalsIgnoreCase("xmlns")) {
		            			xmlnsfound = true;
		            		}
		            		// if the attribute is the id, search a relation wit dwh data and set the style
		            		if(attrName.equalsIgnoreCase("id")) {
		            			if(datamartObject.hasId(attrValue)) {
		            				//FIXME
		            				//String style = datamartObject.getStyleById(attrValue, mapConfiguration);
		            				//svgcode += " style=\"" + style + "\" ";
		            			}
		            			String link = datamartObject.getLinkForId(attrValue);
		            			if(link!=null){
		            				hasLink = true;
		            				closeLink = true;
		            				startlinkcode = "<a xlink:href=\""+link+"\">";
		            			}
		            		}
		            		svgcode += attrName + "=\"" + attrValue + "\" ";
		            	}
		            	// if the tag is svg and there isn't a xmlns attribute the set it (otherwise firefox don't work)
		            	if((issvgtag) && !xmlnsfound) {
		            		svgcode += " xmlns=\"http://www.w3.org/2000/svg\" ";
		            	}
		            	if(issvgtag) {
		            		svgcode += " xmlns:xlink=\"http://www.w3.org/1999/xlink\" ";
		            	}
		            	svgcode += " > \n";
		            	
		            	if(hasLink) {
		            		svgcode = startlinkcode + svgcode;
		            	}
		            	
		            	// write the tag to output
		            	fos.write(svgcode.getBytes());
		            	
		            	break;
		            case XMLStreamConstants.CHARACTERS:
		            	String text = svgMapStreamReader.getText();
		            	if( (text!=null) && !text.trim().equalsIgnoreCase("") ){
		            		fos.write(text.getBytes());
		            	}
		            	break;
		            case XMLStreamConstants.END_ELEMENT:
		                tagname = svgMapStreamReader.getLocalName();
		                if(tagname.trim().equalsIgnoreCase("svg")) {
		                	//FIXME
		                	//String legendString = mapConfiguration.getLegend();
		                	//fos.write(legendString.getBytes());
		                }
		            	fos.write(("</" + svgMapStreamReader.getLocalName() + "> \n").getBytes());   
		            	
		            	if(closeLink) {
		            		fos.write("</a>".getBytes());
		            		closeLink = false;
		            	}
		            	break;
		            case XMLStreamConstants.END_DOCUMENT:
		                  break;
		            }
		            if (!svgMapStreamReader.hasNext())
		                  break;
		            event = svgMapStreamReader.next();
		      }
		} finally {
			svgMapStreamReader.close();
			fos.flush();
			fos.close();
		}
		return tmpMap;
	}

}