/**
 *	LICENSE: see COPYING file
**/
package it.eng.spagobi.geo.render;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.geo.configuration.Constants;
import it.eng.spagobi.geo.configuration.MapConfiguration;
import it.eng.spagobi.geo.datamart.DatamartObject;
import it.eng.spagobi.geo.datamart.DatamartProviderFactory;
import it.eng.spagobi.geo.datamart.DatamartProviderIFace;
import it.eng.spagobi.geo.map.MapProviderFactory;
import it.eng.spagobi.geo.map.MapProviderIFace;

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
public class MapRenderer {

	/**
	 * Gets the original svg map and the datawarehouse data and then transfor the svg map
	 * based on the template configuration and data recovered. The new map is stored in a 
	 * temporary file 
	 * @param template the configuration of the map rendering
	 * @param servRequest The Spago Service Request
	 * @return the File object associated to the temporary file of the new svg map 
	 * @throws Exception If some errors occur during the elaboration
	 */
	public File renderMap(byte[] template, SourceBean servRequest) throws Exception {

		MapConfiguration mapConfiguration = new MapConfiguration(template, servRequest);
		SourceBean mapProviderConfiguration = mapConfiguration.getMapProviderConfiguration();
		String mapProvClassName = (String) mapProviderConfiguration.getAttribute(Constants.CLASS_NAME);
		MapProviderIFace mapProvider = MapProviderFactory.getMapProvider(mapProvClassName);
		XMLStreamReader svgMapStreamReader = mapProvider.getSVGMapStreamReader(mapConfiguration);
		
		SourceBean datamartProviderConfiguration = mapConfiguration.getDatamartProviderConfiguration();
		String dataProvClassName = (String)datamartProviderConfiguration.getAttribute(Constants.CLASS_NAME);
		DatamartProviderIFace datamartProvider = DatamartProviderFactory.getDatamartProvider(dataProvClassName);
		DatamartObject datamartObject = (DatamartObject)datamartProvider.getDatamartObject(datamartProviderConfiguration);		
		
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
		            	// write the tag to output
		            	fos.write(("<" + tagname + " ").getBytes());    
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
		            				String style = datamartObject.getStyleForId(attrValue, mapConfiguration);
		            				fos.write( (" style=\"" + style + "\" ").getBytes());
		            			}
		            		}
		            		fos.write((attrName + "=\"" + attrValue + "\" ").getBytes());
		            	}
		            	// if the tag is svg and there isn't a xmlns attribute the set it (otherwise firefox don't work)
		            	if((issvgtag) && !xmlnsfound) {
		            		fos.write((" xmlns=\"http://www.w3.org/2000/svg\" ").getBytes());
		            	}
		            	fos.write(" > \n".getBytes());
		            	break;
		            case XMLStreamConstants.CHARACTERS:
		                  break;
		            case XMLStreamConstants.END_ELEMENT:
		                tagname = svgMapStreamReader.getLocalName();
		                if(tagname.trim().equalsIgnoreCase("svg")) {
		                	String legendString = mapConfiguration.getLegend();
		                	fos.write(legendString.getBytes());
		                }
		            	fos.write(("</" + svgMapStreamReader.getLocalName() + "> \n").getBytes());    
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

	
	
	
	
	/**
	 * Transform the svg file into a jpeg image
	 * @param inputStream the strema of the svg map
	 * @param outputStream the output stream where the jpeg image is written 
	 * @throws Exception raised if some errors occur during the elaboration
	 */
	public void sVGToJPEGTransform(InputStream inputStream,	OutputStream outputStream) throws Exception {
		// create a JPEG transcoder
		JPEGTranscoder t = new JPEGTranscoder();
		// set the transcoding hints
		t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(1));
		// create the transcoder input
		Reader reader = new InputStreamReader(inputStream);
		TranscoderInput input = new TranscoderInput(reader);
		// create the transcoder output
		TranscoderOutput output = new TranscoderOutput(outputStream);
		// save the image
		t.transcode(input, output);
	}

	/* 
	public void sVGToPDFTransform(InputStream inputStream, OutputStream outputStream) throws Exception {
		
	}
    */

}