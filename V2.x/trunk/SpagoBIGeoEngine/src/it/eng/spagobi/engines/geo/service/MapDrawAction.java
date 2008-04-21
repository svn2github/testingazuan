/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.geo.service;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.geo.Constants;
import it.eng.spagobi.engines.geo.commons.excpetion.GeoEngineException;
import it.eng.spagobi.engines.geo.commons.service.AbstractGeoEngineAction;
import it.eng.spagobi.engines.geo.commons.service.GeoEngineAnalysisState;
import it.eng.spagobi.engines.geo.map.utils.SVGMapConverter;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;
import it.eng.spagobi.utilities.engines.EngineException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.log4j.Logger;


public class MapDrawAction extends AbstractGeoEngineAction {
	
	/**
     * Request parameters
     */
	public static final String HIERARCHY_NAME = "hierarchyName";
	public static final String HIERARCHY_LEVEL = "level";
	public static final String MAP = "map";
	public static final String LAYERS = "layer";	
	
	public static final String MAP_CATALOGUE_MANAGER_URL = "mapCatalogueManagerUrl";

	
	/**
     * Logger component
     */
    public static transient Logger logger = Logger.getLogger(MapDrawAction.class);
	
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws EngineException {
		
		String selectedHierarchyName = null;
		String selectedLevelName = null;
		String selectedMapName = null;
		Object layers = null;
		List selectedLayers = null;
		String outputFormat = null;
		File maptmpfile = null;
		AuditAccessUtils auditAccessUtils = null;
		
		logger.debug("IN");
		
		try {
		
		auditAccessUtils = (AuditAccessUtils)getAttributeFromSession("SPAGOBI_AUDIT_UTILS");
		if (auditAccessUtils != null) {
			auditAccessUtils.updateAudit(getHttpSession(), getUserId(), getAuditId(), new Long(System.currentTimeMillis()), null, 
					"EXECUTION_STARTED", null, null);
		}
		
		super.service(serviceRequest, serviceResponse);
		
		
		
		selectedHierarchyName = getAttributeAsString( HIERARCHY_NAME );				
		selectedLevelName = getAttributeAsString( HIERARCHY_LEVEL );
		selectedMapName = getAttributeAsString( MAP );
		layers = getAttribute( LAYERS );
		
		selectedLayers = parseLayers( layers );		
		
		GeoEngineAnalysisState analysisState =  (GeoEngineAnalysisState)getAnalysisState();
		if(selectedMapName != null) {
			analysisState.setSelectedMapName( selectedMapName );
		}
		if(selectedHierarchyName != null) {
			analysisState.setSelectedHierarchyName( selectedHierarchyName );
		}
		if(selectedLevelName != null) {
			analysisState.setSelectedLevelName( selectedLevelName );
		}
		if(selectedLayers != null && selectedLayers.size() > 0) {
			analysisState.setSelectedLayers( selectedLayers );
		}
		
		getGeoEngineInstance().setAnalysisState( analysisState );
	
		outputFormat = Constants.DSVG;
		
	
		
		maptmpfile = getGeoEngineInstance().renderMap( outputFormat );
		
		} catch (Exception e) {
			if(e instanceof GeoEngineException) throw (GeoEngineException)e;
			
			String description = "An unpredicted error occurred while executing " + getActionName() + " service.";
			Throwable rootException = e;
			while(rootException.getCause() != null) rootException = rootException.getCause();
			String str = rootException.getMessage()!=null? rootException.getMessage(): rootException.getClass().getName();
			description += "<br>The root cause of the error is: " + str;
			List hints = new ArrayList();
			hints.add("Sorry, there are no hints available right now on how to fix this problem");
			throw new GeoEngineException("Service error", description, hints, e);
		}
		
		this.freezeHttpResponse();	
		
		String contentType = getContentType(outputFormat);
		getHttpResponse().setContentType(contentType);
		
		
		// based on the format requested fill the response
		if(outputFormat.equalsIgnoreCase(Constants.JPEG)) {
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(maptmpfile);
				SVGMapConverter.SVGToJPEGTransform(inputStream, getOutputStream());
			} catch (Exception e) {
				logger.error("error while transforming into jpeg", e);
				sendError(getOutputStream());
				// AUDIT UPDATE
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(getHttpSession(), getUserId(), getAuditId(), null, new Long(System.currentTimeMillis()), 
						"EXECUTION_FAILED", "Error while transforming into jpeg", null);
				return;
			}
			try{
				inputStream.close();
			} catch (Exception e ){
				logger.error("error while closing input stream", e);
			}
		} else if(outputFormat.equalsIgnoreCase(Constants.SVG) 
				|| outputFormat.equalsIgnoreCase(Constants.DSVG)
				|| outputFormat.equalsIgnoreCase(Constants.XDSVG)) {
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(maptmpfile);
				flushFromInputStreamToOutputStream(inputStream, getOutputStream(), false);
			} catch (Exception e) {
				logger.error("error while flushing svg", e);
				sendError(getOutputStream());
				// AUDIT UPDATE
				if (auditAccessUtils != null) auditAccessUtils.updateAudit(getHttpSession(), getUserId(), getAuditId(), null, new Long(System.currentTimeMillis()), 
						"EXECUTION_FAILED", "Error while flushing svg", null);
				return;
			}
			try{
				inputStream.close();
			} catch (Exception e ){
				logger.error("error while closing input stream", e);
			}
		} else {
			logger.error("Output Format not specified");
			sendError(getOutputStream());
			return;
		}
		
		// AUDIT UPDATE
		if (auditAccessUtils != null) auditAccessUtils.updateAudit(getHttpSession(), getUserId(), getAuditId(), null, new Long(System.currentTimeMillis()), 
				"EXECUTION_PERFORMED", null, null);
		
		// delete tmp map file
		maptmpfile.delete();		
	}
	
	
	private List parseLayers(Object layers) {
		List selectedLayers = null;
		if(layers != null) {
			if(layers instanceof ArrayList) {
				selectedLayers = (List)layers;
			} else if (layers instanceof String) {
				selectedLayers = new ArrayList();
				selectedLayers.add( layers );
			} else {
				selectedLayers = new ArrayList();
				selectedLayers.add(layers.toString());
			}
		}
		return selectedLayers;
	}
	
	private ServletOutputStream getOutputStream() {
		ServletOutputStream outputStream = null;
		try{
			outputStream = getHttpResponse().getOutputStream();
		} catch (Exception e) {
			logger.error("Error while getting output stream", e);
		}
		return outputStream;
	}
	
	/**
	 * Returns the right content type for the output format
	 * @param outputFormat The code string of the output format
	 * @return the string code of the content type for the output format
	 */
	private String getContentType(String outFormat) {
		if (outFormat.equalsIgnoreCase(Constants.SVG)
				|| outFormat.equalsIgnoreCase(Constants.DSVG)
				|| outFormat.equalsIgnoreCase(Constants.XDSVG))
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
		
		if( !outputFormat.equalsIgnoreCase(Constants.SVG) && 
			!outputFormat.equalsIgnoreCase(Constants.JPEG) ) {
				return false;
		}
		
		return true;
	}
	
	
	
	
	private void auditExecutionFailure(String msg) {
		String auditId = getHttpRequest().getParameter("SPAGOBI_AUDIT_ID");
		AuditAccessUtils auditAccessUtils = 
			(AuditAccessUtils) getHttpRequest().getSession().getAttribute("SPAGOBI_AUDIT_UTILS");
		if (auditAccessUtils != null) auditAccessUtils.updateAudit(getHttpSession(), getUserId(), auditId, null, new Long(System.currentTimeMillis()), 
				"EXECUTION_FAILED", msg, null);
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
			logger.error("Unable to write into output stream ", e);
		}
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
			logger.error("Error while flushing", ioe);
		}
	}
	
}