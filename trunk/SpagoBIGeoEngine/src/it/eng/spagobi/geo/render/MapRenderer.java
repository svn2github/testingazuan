/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.geo.render;

import it.eng.geo.configuration.Constants;
import it.eng.geo.configuration.MapConfiguration;
import it.eng.geo.datamart.DatamartObject;
import it.eng.geo.datamart.DatamartProviderFactory;
import it.eng.geo.datamart.DatamartProviderIFace;
import it.eng.geo.document.XMLDocumentIFace;
import it.eng.geo.map.MapProviderFactory;
import it.eng.geo.map.MapProviderIFace;
import it.eng.spago.base.SourceBean;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.w3c.dom.Element;

/**
 * Classe <code>SessionExpiredAction</code> Gestione dell'avvenuta scadenza
 * della sessione
 * 
 * @author
 * @version 1.0
 */
public class MapRenderer {

	/**
	 * <p>
	 * Represents mimeType
	 * </p>
	 */
	private String contentType = null;

	/**
	 * <code>service</code> Metodo contenente la logica per l'esecuzione del
	 * comando
	 * 
	 * @param outputSream
	 * 
	 * @param arg0
	 *            di tipo <code>String</code>
	 * @exception Exception
	 *                Se si sono verificati problemi, in realta' usato per
	 *                forzare l'uso del blocco try-catch
	 */
	public byte[] renderMap(byte[] template) throws Exception {

		MapConfiguration mapConfiguration = new MapConfiguration(template);

		setContentType(mapConfiguration.getType());

		SourceBean mapProviderConfiguration = mapConfiguration
				.getMapProviderConfiguration();
		MapProviderIFace mapProvider = MapProviderFactory
				.getMapProvider((String) mapProviderConfiguration
						.getAttribute(Constants.CLASS_NAME));
		XMLDocumentIFace xMLDocument = (XMLDocumentIFace) mapProvider
				.getSVGMapDocument(mapConfiguration
						.getMapProviderConfiguration());

		SourceBean datamartProviderConfiguration = mapConfiguration
				.getDatamartProviderConfiguration();
		DatamartProviderIFace datamartProvider = DatamartProviderFactory
				.getDatamartProvider((String) datamartProviderConfiguration
						.getAttribute(Constants.CLASS_NAME));
		DatamartObject datamartObject = (DatamartObject) datamartProvider
				.getDatamartObject(datamartProviderConfiguration);

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

	public void sVGToJPEGTransform(InputStream inputStream,
			OutputStream outputStream) throws Exception {
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

	public String getContentType() {
		return contentType;
	}

	/*
	 * This Method sets the right MIME type for a particular format <p> @param
	 * String format ex: xml or HTML etc. @return String MIMEtype
	 */
	public void setContentType(String type) {
		if (type.equalsIgnoreCase(Constants.SVG))
			this.contentType = Constants.SVG_MIME_TYPE;
		else if (type.equalsIgnoreCase(Constants.PDF))
			this.contentType = Constants.PDF_MIME_TYPE;
		else if (type.equalsIgnoreCase(Constants.GIF))
			this.contentType = Constants.GIF_MIME_TYPE;
		else if (type.equalsIgnoreCase(Constants.JPEG))
			this.contentType = Constants.JPEG_MIME_TYPE;
		else if (type.equalsIgnoreCase(Constants.BMP))
			this.contentType = Constants.BMP_MIME_TYPE;
		else if (type.equalsIgnoreCase(Constants.X_PNG))
			this.contentType = Constants.X_PNG_MIME_TYPE;
		else if (type.equalsIgnoreCase(Constants.HTML))
			this.contentType = Constants.HTML_MIME_TYPE;
		else if (type.equalsIgnoreCase(Constants.XML))
			this.contentType = Constants.XML_MIME_TYPE;
	}
}