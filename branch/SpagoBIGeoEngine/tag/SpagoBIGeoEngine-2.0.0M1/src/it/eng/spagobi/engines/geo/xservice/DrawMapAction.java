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
package it.eng.spagobi.engines.geo.xservice;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.geo.Constants;
import it.eng.spagobi.engines.geo.commons.excpetion.GeoEngineException;
import it.eng.spagobi.engines.geo.commons.service.AbstractGeoEngineAction;
import it.eng.spagobi.engines.geo.commons.service.GeoEngineAnalysisState;
import it.eng.spagobi.engines.geo.map.utils.SVGMapConverter;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;
import it.eng.spagobi.utilities.engines.SpagoBIEngineException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.service.IStreamEncoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.log4j.Logger;


/**
 * The Class MapDrawAction.
 */
public class DrawMapAction extends AbstractGeoEngineAction {
	
	// REQUEST PARAMETERS	
	//...

	// RESPONSE PARAMETERS
	// ...
	
	// DEFAULT VALUES
	public static final String DEFAULT_OUTPUT_TYPE = Constants.DSVG;
	
	// Default serial version number (just to keep eclipse happy).
	private static final long serialVersionUID = 1L;
	
	// Logger component
    public static transient Logger logger = Logger.getLogger(DrawMapAction.class);
	
	
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) {
		
		String outputFormat = null;
		File maptmpfile = null;
	
		logger.debug("IN");
			
		try {		
			super.service(serviceRequest, serviceResponse);
			
			getAuditServiceProxy().notifyServiceStartEvent();
						
			outputFormat = (String)getGeoEngineInstance().getEnv().get(Constants.ENV_OUTPUT_TYPE);
			if(outputFormat == null) {
				outputFormat = DEFAULT_OUTPUT_TYPE;
			}
						
			maptmpfile = getGeoEngineInstance().renderMap( outputFormat );
			
			IStreamEncoder encoder = null;
			if(outputFormat.equalsIgnoreCase(Constants.JPEG)) {
				encoder = new SVGMapConverter();
			}
			
			try {
				writeBackToClient(maptmpfile, encoder, true, "map.svg", getContentType(outputFormat));
			} catch(IOException e) {
				logger.error("error while flushing output", e);
				getAuditServiceProxy().notifyServiceErrorEvent( "Error while flushing output" );
				throw new SpagoBIEngineServiceException(getActionName(), "Error while flushing output", e);
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
	
	
	
	
	
	

}