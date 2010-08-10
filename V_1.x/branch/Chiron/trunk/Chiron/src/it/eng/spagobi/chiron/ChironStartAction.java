/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.chiron;

import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.utilities.service.AbstractBaseHttpAction;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class ChironStartAction extends AbstractBaseHttpAction {
	
	// INPUT-OUTPUT PARAMETERS
	public static final String MODE = "MODE";	
	
	public static final String DEBUG_MODE = "DEBUG_MODE";
	public static final String BUILD_MODE = "BUILD_MODE";
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(ChironStartAction.class);

	
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws Exception {
		setRequest( serviceRequest );
		setResponse( serviceResponse );
		
		String mode = null;
		 
		System.out.println("B OOOOOO M");
		logger.debug("IN");
		
		try {
			mode = this.getAttributeAsString( MODE );
			if( !(DEBUG_MODE.equalsIgnoreCase(mode) 
					|| BUILD_MODE.equalsIgnoreCase(mode)) ) {
				logger.debug("Input parameter [" + MODE + "] not defined");
				mode = DEBUG_MODE;
			}
			logger.info("Output parameter [" + MODE + "] is equal to: " + mode);
			setAttribute(MODE, mode);
		} catch (Exception e) {
			throw e;
		} finally {
			logger.debug("OUT");
		}
		
	}

}
