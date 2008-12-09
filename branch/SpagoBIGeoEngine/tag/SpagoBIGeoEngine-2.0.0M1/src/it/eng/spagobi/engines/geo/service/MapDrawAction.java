/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
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
import it.eng.spagobi.utilities.engines.SpagoBIEngineException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.log4j.Logger;


// TODO: Auto-generated Javadoc
/**
 * The Class MapDrawAction.
 */
public class MapDrawAction extends AbstractGeoEngineAction {
	
	// request
	
	/** The Constant HIERARCHY_NAME. */
	public static final String HIERARCHY_NAME = "hierarchyName";
	
	/** The Constant HIERARCHY_LEVEL. */
	public static final String HIERARCHY_LEVEL = "level";
	
	/** The Constant MAP. */
	public static final String MAP = "map";
	
	/** The Constant LAYERS. */
	public static final String LAYERS = "layer";	
	
	/** The Constant MAP_CATALOGUE_MANAGER_URL. */
	public static final String MAP_CATALOGUE_MANAGER_URL = "mapCatalogueManagerUrl";

	// default values
	public static final String DEFAULT_OUTPUT_TYPE = Constants.DSVG;
	
	// Logger component
    public static transient Logger logger = Logger.getLogger(MapDrawAction.class);
	
	
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) {
		
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
		
			super.service(serviceRequest, serviceResponse);
			
			getAuditServiceProxy().notifyServiceStartEvent();
			
			selectedHierarchyName = getAttributeAsString( HIERARCHY_NAME );				
			selectedLevelName = getAttributeAsString( HIERARCHY_LEVEL );
			selectedMapName = getAttributeAsString( MAP );
			layers = getAttribute( LAYERS );
			
			selectedLayers = parseLayers( layers );		
			
			
			GeoEngineAnalysisState analysisState =  (GeoEngineAnalysisState)getGeoEngineInstance().getAnalysisState();;
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
		
			outputFormat = (String)getGeoEngineInstance().getEnv().get(Constants.ENV_OUTPUT_TYPE);
			if(outputFormat == null) {
				outputFormat = DEFAULT_OUTPUT_TYPE;
			}
			
		
			
			maptmpfile = getGeoEngineInstance().renderMap( outputFormat );
		
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
					getAuditServiceProxy().notifyServiceErrorEvent( "Error while transforming into jpeg" );
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
					getAuditServiceProxy().notifyServiceErrorEvent( "Error while flushing svg" );
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
			
			getAuditServiceProxy().notifyServiceEndEvent( );
			
			maptmpfile.delete();	
			
		} catch (Throwable t) {
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), t);
		} finally {
			// no resources need to be released
		}	
		
		logger.debug("OUT");
	}
	
	
	/**
	 * Parses the layers.
	 * 
	 * @param layers the layers
	 * 
	 * @return the list
	 */
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
	
	/**
	 * Gets the output stream.
	 * 
	 * @return the output stream
	 */
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
	 * Returns the right content type for the output format.
	 * 
	 * @param outFormat the out format
	 * 
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
	 * Checks if the output format requested is allowed.
	 * 
	 * @param outputFormat The code string of the output format
	 * 
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
	
	
	
	
	
	/**
	 * sends an error message to the client.
	 * 
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
	 * 
	 * @param is The input stream
	 * @param os The output stream
	 * @param closeStreams the close streams
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