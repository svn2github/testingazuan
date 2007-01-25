/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.geo.action;

import it.eng.geo.configuration.Constants;
import it.eng.geo.render.MapRenderer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.tracing.TracerSingleton;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import sun.misc.BASE64Decoder;

/**
 * Spago Action which executes the map producing request  
 */
public class GeoAction extends AbstractHttpAction {

	/**
	 * Method called automatically by Spago framework when the action is invoked.
	 * The method search into the request two parameters
	 * <ul>
	 * <li>Template:an xml message (encodend in base64) which contains the configuration of the request</li>
	 * <li>OutputFormat: the format of the response (possible values are SVG,PDF,JPEG). Default is SVG</li>
	 * </ul>
	 * @param serviceRequest the Spago request SourceBean 
	 * @param serviceResponse the Spago response SourceBean 
	 */
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws Exception {
		// get the http response and the output stream associated
		HttpServletResponse response = this.getHttpResponse();
		ServletOutputStream outputStream = response.getOutputStream();
		this.freezeHttpResponse();
		// get the bytes od the template xml file from the request (the template is encoded in byte64)
		String templateBase64Coded = (String) serviceRequest.getAttribute(Constants.TEMPLATE_PARAMETER);
		BASE64Decoder bASE64Decoder = new BASE64Decoder();
		byte[] template = bASE64Decoder.decodeBuffer(templateBase64Coded);
		// get the output format parameter (SVG is the default)
		String outputFormat = (String) serviceRequest.getAttribute(Constants.OUTPUT_FORMAT_PARAMETER);
		if(!checkOutputFormat(outputFormat)) {
			outputFormat = Constants.SVG;
		}
		// create the map renderer and render the map
		MapRenderer mapRenderer = new MapRenderer();
		byte[] map = mapRenderer.renderMap(template);
		// based on the format requested fill the response
		if(outputFormat.equalsIgnoreCase(Constants.JPEG)) {
			response.setContentType(Constants.JPEG_MIME_TYPE);
			InputStream inputStream = new ByteArrayInputStream(map);
			mapRenderer.sVGToJPEGTransform(inputStream, outputStream);
		} else if(outputFormat.equalsIgnoreCase(Constants.PDF)) {
			response.setContentType(Constants.PDF_MIME_TYPE);
			InputStream inputStream = new ByteArrayInputStream(map);
			mapRenderer.sVGToPDFTransform(inputStream, outputStream);
		} else if(outputFormat.equalsIgnoreCase(Constants.SVG)) {
			response.setContentType(Constants.SVG_MIME_TYPE);
			outputStream.write(map);
		} else {
			TracerSingleton.log(Constants.LOG_NAME, 
					            TracerSingleton.CRITICAL, 
					            "GeoAction :: service : Output Format not specified");
			
		}
	}
	
	
	/**
	 * Checks if the output format requested is allowed
	 * @param outputFormat The code string of the output format
	 * @return true if the output format is allowed, false otherwise
	 */
	private boolean checkOutputFormat(String outputFormat) {
		if(outputFormat==null) {
			return false;
		}
		if(outputFormat.trim().equals("")) {
			return false;
		}
		if( !outputFormat.equalsIgnoreCase(Constants.SVG) || 
			!outputFormat.equalsIgnoreCase(Constants.JPEG) ||
			!outputFormat.equalsIgnoreCase(Constants.PDF)  ) {
			return false;
		}
		return true;
	}
	
}