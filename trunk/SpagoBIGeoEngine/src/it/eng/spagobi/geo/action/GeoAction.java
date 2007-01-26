/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.action;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.geo.configuration.Constants;
import it.eng.spagobi.geo.render.MapRenderer;

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
	 * The template xml message must have the following structure
	 * 
	 * 	<MAP name="logical name of the map">
	 * 		<DATAMART_PROVIDER connection_name=" name of one pool defined into data_access.xml " 
	 * 	    	               query=" the query that obtains data "
	 * 	        	           svg_attribute=" the svg attribute which is related to a column of the resultset "
	 * 	            	       column_name=" the name of a column of the resultser which is related to an attribute of the svg tags "/>
	 * 		<CONFIGURATION>
	 * 			<!-- x,y: position of the first element of the legend  -->
	 * 			<LEGEND x=" " y=" " width=" " height=" " style=" svg style properties ">
	 * 				<TITLE description=" title " style=" svg style properties "/>
	 * 				<!-- ordered by threshold -->
	 * 				<LEVELS>
	 * 					<LEVEL threshold="0" style="svg style properties">
	 * 						<TEXT description="Less then 1.000" style="svg style properties" />
	 * 					</LEVEL>
	 * 					<LEVEL threshold="1000" style="svg style properties">
	 * 						<TEXT description="Da 1.000 a 2.000" style="svg style properties" />
	 * 					</LEVEL>
	 * 					......
	 * 				</LEVELS>
	 * 			</LEGEND>
	 * 		</CONFIGURATION>
	 * 	</MAP>
	 * 
	 * 
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
		// set the content type 
		String contentType = getContentType(outputFormat);
		response.setContentType(contentType);
		// based on the format requested fill the response
		if(outputFormat.equalsIgnoreCase(Constants.JPEG)) {
			InputStream inputStream = new ByteArrayInputStream(map);
			mapRenderer.sVGToJPEGTransform(inputStream, outputStream);
		} else if(outputFormat.equalsIgnoreCase(Constants.PDF)) {
			InputStream inputStream = new ByteArrayInputStream(map);
			mapRenderer.sVGToPDFTransform(inputStream, outputStream);
		} else if(outputFormat.equalsIgnoreCase(Constants.SVG)) {
			outputStream.write(map);
		} else {
			TracerSingleton.log(Constants.LOG_NAME, 
					            TracerSingleton.CRITICAL, 
					            "GeoAction :: service : Output Format not specified");
			// TODO generate an emf error and check the error page appear 
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
	
	
	/**
	 * Returns the right content type for the output format
	 * @param outputFormat The code string of the output format
	 * @return the string code of the content type for the output format
	 */
	private String getContentType(String outFormat) {
		if (outFormat.equalsIgnoreCase(Constants.SVG))
			return Constants.SVG_MIME_TYPE;
		else if (outFormat.equalsIgnoreCase(Constants.PDF))
			return Constants.PDF_MIME_TYPE;
		else if (outFormat.equalsIgnoreCase(Constants.GIF))
			return Constants.GIF_MIME_TYPE;
		else if (outFormat.equalsIgnoreCase(Constants.JPEG))
			return Constants.JPEG_MIME_TYPE;
		else if (outFormat.equalsIgnoreCase(Constants.BMP))
			return Constants.BMP_MIME_TYPE;
		else if (outFormat.equalsIgnoreCase(Constants.X_PNG))
			return Constants.X_PNG_MIME_TYPE;
		else if (outFormat.equalsIgnoreCase(Constants.HTML))
			return Constants.HTML_MIME_TYPE;
		else if (outFormat.equalsIgnoreCase(Constants.XML))
			return Constants.XML_MIME_TYPE;
		else return Constants.TEXT_MIME_TYPE;
	}
	
	
}