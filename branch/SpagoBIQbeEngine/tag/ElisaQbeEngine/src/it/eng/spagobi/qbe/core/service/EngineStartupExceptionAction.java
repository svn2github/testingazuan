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
package it.eng.spagobi.qbe.core.service;


import java.util.Iterator;

import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFInternalError;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.utilities.engines.SpagoBIEngineStartupException;

/**
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class EngineStartupExceptionAction extends AbstractQbeEngineAction {
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(EngineStartupExceptionAction.class);
	
   
	public void service(SourceBean serviceRequest, SourceBean serviceResponse)  {
		
		
		Iterator it = getErrorHandler().getErrors().iterator();
		while(it.hasNext()) {
			Object o = it.next();
			if(o instanceof EMFInternalError) {
				EMFInternalError error = (EMFInternalError)o;
				Exception e = error.getNativeException();
				if(e instanceof SpagoBIEngineStartupException) {
					SpagoBIEngineStartupException serviceError = (SpagoBIEngineStartupException)e;
					logError(serviceError);
				} else {
					logger.error(e);
				}
	
			}
			
		}
	}


	private void logError(SpagoBIEngineStartupException serviceError) {
		logger.error(serviceError.getMessage());
		logger.error("The error root cause is: " + serviceError.getRootCause());	
		if(serviceError.getHints().size() > 0) {
			Iterator hints = serviceError.getHints().iterator();
			while(hints.hasNext()) {
				String hint = (String)hints.next();
				logger.info("hint: " + hint);
			}
			
		}
		logger.error("The error root cause stack trace is:",  serviceError.getCause());	
		logger.error("The error full stack trace is:", serviceError);			
	}
}
