/**
 *	LICENSE: see COPYING file
**/
package it.eng.spagobi.geo.render;

import it.eng.spagobi.geo.configuration.Constants;
import it.eng.spagobi.geo.configuration.MapConfiguration;
import it.eng.spagobi.geo.datamart.DatamartObject;
import it.eng.spagobi.geo.datamart.DatamartProviderFactory;
import it.eng.spagobi.geo.datamart.DatamartProviderIFace;
import it.eng.spagobi.geo.document.XMLDocumentIFace;
import it.eng.spagobi.geo.map.MapProviderFactory;
import it.eng.spagobi.geo.map.MapProviderIFace;
import it.eng.spago.base.SourceBean;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;

import javax.xml.stream.XMLStreamReader;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.w3c.dom.Element;


public class MapRenderer {

	public byte[] renderMap(byte[] template) throws Exception {

		MapConfiguration mapConfiguration = new MapConfiguration(template);
		SourceBean mapProviderConfiguration = mapConfiguration.getMapProviderConfiguration();
		String mapProvClassName = (String) mapProviderConfiguration.getAttribute(Constants.CLASS_NAME);
		MapProviderIFace mapProvider = MapProviderFactory.getMapProvider(mapProvClassName);
		XMLStreamReader svgMapStreamReader = mapProvider.getSVGMapStreamReader(mapConfiguration);
		SourceBean datamartProviderConfiguration = mapConfiguration.getDatamartProviderConfiguration();
		String dataProvClassName = (String)datamartProviderConfiguration.getAttribute(Constants.CLASS_NAME);
		DatamartProviderIFace datamartProvider = DatamartProviderFactory.getDatamartProvider(dataProvClassName);
		DatamartObject datamartObject = (DatamartObject)datamartProvider.getDatamartObject(datamartProviderConfiguration);

		
		
		
		ArrayList elementIdList = datamartObject.getElementIdList();
		ArrayList valueList = datamartObject.getValueList();
		for (int i = 0; i < elementIdList.size(); i++) {
			String elementId = (String) elementIdList.get(i);
			String value = (String) valueList.get(i);
			Element element = xMLDocument.getElemetByID(elementId);
			String style = mapConfiguration.getStyle(Integer.parseInt(value));
			if(element!=null)
				element.setAttribute(Constants.STYLE, style);
		}
		String mapString = xMLDocument.getDocumentAsXMLString();
		String legendString = mapConfiguration.getLegend();
		mapString = mapString.substring(0, mapString.indexOf("</svg>"))
				+ legendString + "</svg>";
		return mapString.getBytes();
	}

	
	
	
	
	
	public void sVGToJPEGTransform(InputStream inputStream,	OutputStream outputStream) throws Exception {
		// create a JPEG transcoder
		JPEGTranscoder t = new JPEGTranscoder();
		// set the transcoding hints
		t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));
		// create the transcoder input
		Reader reader = new InputStreamReader(inputStream);
		TranscoderInput input = new TranscoderInput(reader);
		// create the transcoder output
		TranscoderOutput output = new TranscoderOutput(outputStream);
		// save the image
		t.transcode(input, output);
	}

	public void sVGToPDFTransform(InputStream inputStream, OutputStream outputStream) throws Exception {
		
	}


}