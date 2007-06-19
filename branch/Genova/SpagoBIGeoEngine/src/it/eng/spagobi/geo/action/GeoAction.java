/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.action;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.geo.configuration.Constants;
import it.eng.spagobi.geo.render.MapRenderer;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
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
	 * <li>OutputFormat: the format of the response (possible values are SVG,JPEG). Default is SVG</li>
	 * </ul>
	 * The template xml message must have the following structure
	 * 
	 * 	&lt;MAP name="logical name of the map"&gt;
	 * 		&lt;DATAMART_PROVIDER connection_name=" name of one pool defined into data_access.xml " 
	 * 	    	               query=" the query that obtains data "
	 * 	        	           column_id=" the name of a column of the resultset which is related to the id attribute of the svg tags "
	 * 	            	       column_value=" the name of a column of the resultset which contains the value related to the svg element "/&gt;
	 * 		&lt;CONFIGURATION&gt;
	 * 			&lt;!-- x,y: position of the first element of the legend  --&gt;
	 * 			&lt;LEGEND x=" " y=" " width=" " height=" " style=" svg style properties "&gt;
	 * 				&lt;TITLE description=" title " style=" svg style properties "/&gt;
	 * 				&lt;!-- ordered by threshold --&gt;
	 * 				&lt;LEVELS&gt;
	 * 					&lt;LEVEL threshold="0" style="svg style properties"&gt;
	 * 						&lt;TEXT description="Less then 1.000" style="svg style properties" /&gt;
	 * 					&lt;/LEVEL&gt;
	 * 					&lt;LEVEL threshold="1000" style="svg style properties"&gt;
	 * 						&lt;TEXT description="Da 1.000 a 2.000" style="svg style properties" /&gt;
	 * 					&lt;/LEVEL&gt;
	 * 					......
	 * 				&lt;/LEVELS&gt;
	 * 			&lt;/LEGEND&gt;
	 * 		&lt;/CONFIGURATION&gt;
	 * 	&lt;/MAP&gt;
	 * 
	 * The query attribute can contain some parameters. 
	 * Each parameter must be specified with the sintax ${parameter_name} 
	 * During the execution the parameter sintax is substituted with the value ot the 
	 * same-name parameter send into request. If the request doesn't contain any parameter 
	 * with name equal to the one of the query the rendering is not performed
	 * 
	 * @param serviceRequest the Spago request SourceBean 
	 * @param serviceResponse the Spago response SourceBean 
	 */
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws Exception {
		HttpServletRequest request = this.getHttpRequest(); 
		// AUDIT UPDATE
		String auditId = request.getParameter("SPAGOBI_AUDIT_ID");
		AuditAccessUtils auditAccessUtils = 
			(AuditAccessUtils) request.getSession().getAttribute("SPAGOBI_AUDIT_UTILS");
		if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, new Long(System.currentTimeMillis()), null, 
				"EXECUTION_STARTED", null, null);
		
		// get the http response 
		HttpServletResponse response = this.getHttpResponse();
		this.freezeHttpResponse();
		
		ServletOutputStream outputStream = null;
		try{
			outputStream = response.getOutputStream();
		} catch (Exception e) {
			TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, 
								"GeoAction :: service : " +
								"Error while getting output stream", e);
		}
		// get the output format parameter (SVG is the default)
		String outputFormat = (String) serviceRequest.getAttribute(Constants.OUTPUT_FORMAT_PARAMETER);
		if(!checkOutputFormat(outputFormat)) {
			outputFormat = Constants.SVG;
		}
		// get the bytes od the template xml file from the request (the template is encoded in byte64)
		String templateBase64Coded = (String) serviceRequest.getAttribute(Constants.TEMPLATE_PARAMETER);
		BASE64Decoder bASE64Decoder = new BASE64Decoder();
		byte[] template = null;
		try{
			template = bASE64Decoder.decodeBuffer(templateBase64Coded);
		} catch (Exception e) {
			TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, 
        						"GeoAction :: service : " +
        						"Error while decoding base64 template", e);
			sendError(outputStream);
			// AUDIT UPDATE
			if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
					"EXECUTION_FAILED", "Error while decoding base64 template", null);
			return;
		}
		
		// create the map renderer and render the map
		MapRenderer mapRenderer = new MapRenderer();
		File maptmpfile = null;
		try{
			maptmpfile = mapRenderer.renderMap(template, serviceRequest);
		} catch (Exception e) {
			TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, 
								"GeoAction :: service : " +
								"Error while rendering the map", e);
			sendError(outputStream);
			// AUDIT UPDATE
			if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
					"EXECUTION_FAILED", "Error while rendering the map", null);
			return;
		}
		
		// set the content type 
		String contentType = getContentType(outputFormat);
		response.setContentType(contentType);
		//response.setHeader("Content-Type", Constants.SVG_MIME_TYPE + "; charset=UTF-8");
		//response.setHeader("Cache-Control", "Public, max-age=6000");
		//response.setHeader("Expires", "6000");
		
		// based on the format requested fill the response
		if(outputFormat.equalsIgnoreCase(Constants.JPEG)) {
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(maptmpfile);
				mapRenderer.sVGToJPEGTransform(inputStream, outputStream);
			} catch (Exception e) {
				TracerSingleton.log(Constants.LOG_NAME, 
			            			TracerSingleton.CRITICAL, 
			            			"GeoAction :: service : error while transforming into jpeg", e);
				sendError(outputStream);
				// AUDIT UPDATE
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
						"EXECUTION_FAILED", "Error while transforming into jpeg", null);
				return;
			}
			try{
				inputStream.close();
			} catch (Exception e ){
				TracerSingleton.log(Constants.LOG_NAME, 
            						TracerSingleton.CRITICAL, 
            						"GeoAction :: service : error while closing input stream", e);
			}
		} /* else if(outputFormat.equalsIgnoreCase(Constants.PDF)) {
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(maptmpfile);
				mapRenderer.sVGToPDFTransform(inputStream, outputStream);
			} catch (Exception e) {
				TracerSingleton.log(Constants.LOG_NAME, 
            						TracerSingleton.CRITICAL, 
            						"GeoAction :: service : error while transforming into pdf", e);
				sendError(outputStream);
				return;
			}
			try{
				inputStream.close();
			} catch (Exception e ){
				TracerSingleton.log(Constants.LOG_NAME, 
            						TracerSingleton.CRITICAL, 
            						"GeoAction :: service : error while closing input stream", e);
			}
		}*/ else if(outputFormat.equalsIgnoreCase(Constants.SVG)) {
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(maptmpfile);
				flushFromInputStreamToOutputStream(inputStream, outputStream, false);
			} catch (Exception e) {
				TracerSingleton.log(Constants.LOG_NAME, 
            						TracerSingleton.CRITICAL, 
            						"GeoAction :: service : error while flushing svg", e);
				sendError(outputStream);
				// AUDIT UPDATE
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
						"EXECUTION_FAILED", "Error while flushing svg", null);
				return;
			}
			try{
				inputStream.close();
			} catch (Exception e ){
				TracerSingleton.log(Constants.LOG_NAME, 
									TracerSingleton.CRITICAL, 
									"GeoAction :: service : error while closing input stream", e);
			}
		} else {
			TracerSingleton.log(Constants.LOG_NAME, 
					            TracerSingleton.CRITICAL, 
					            "GeoAction :: service : Output Format not specified");
			sendError(outputStream);
			return;
		}
		
		// AUDIT UPDATE
		if (auditAccessUtils != null) auditAccessUtils.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
				"EXECUTION_PERFORMED", null, null);
		
		// delete tmp map file
		maptmpfile.delete();
		
	}
	
	
	/**
	 * sends an error message to the client
	 * @param out The servlet output stream
	 */
	private void sendError(ServletOutputStream out)  {
		try{
			out.write("<html>".getBytes());
			out.write("<body>".getBytes());
			out.write("<br/><br/><center><h2><span style=\"color:red;\">Unable to produce map</span></h2></center>".getBytes());
			out.write("</body>".getBytes());
			out.write("</html>".getBytes());
		} catch (Exception e) {
			TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, 
								"GeoAction :: sendError : " +
								"Unable to write into output stream ", e);
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
		/*
		if( !outputFormat.equalsIgnoreCase(Constants.SVG) && 
			!outputFormat.equalsIgnoreCase(Constants.JPEG) &&
			!outputFormat.equalsIgnoreCase(Constants.PDF)  ) {
			return false;
		}*/
		if( !outputFormat.equalsIgnoreCase(Constants.SVG) && 
			!outputFormat.equalsIgnoreCase(Constants.JPEG) ) {
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
	
	
	/**
	 * Given an <code>InputStream</code> as input flushs the content into an OutputStream 
	 * and then close the input and output stream.
	 * @param is The input stream 
	 * @param os The output stream
	 * @param closeStreams, if true close both stream 
	 */
	public static void flushFromInputStreamToOutputStream(InputStream is, OutputStream os, boolean closeStreams) {
		try{	
			int c = 0;
			byte[] b = new byte[1024];
			while ((c = is.read(b)) != -1) {
				if (c == 1024)
					os.write(b);
				else
					os.write(b, 0, c);
			}
			os.flush();
			if(closeStreams) {
				os.close();
				is.close();
			}
		} catch (IOException ioe) {
			TracerSingleton.log(Constants.LOG_NAME, 
		            TracerSingleton.CRITICAL, 
		            "GeoAction :: flushFromInputStreamToOutputStream : Error while flushing", ioe);
		}
	}
	
}