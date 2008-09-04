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


import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFInternalError;
import it.eng.spagobi.qbe.commons.exception.QbeEngineException;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.qbe.commons.service.JSONFailure;
import it.eng.spagobi.utilities.engines.EngineException;

import java.io.IOException;
import java.util.Iterator;

import org.apache.log4j.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class ServiceExceptionAction.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class ServiceExceptionAction extends AbstractQbeEngineAction {
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(ServiceExceptionAction.class);
	
   
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws EngineException  {
		
		freezeHttpResponse();
		
		Iterator it = getErrorHandler().getErrors().iterator();
		while(it.hasNext()) {
			Object o = it.next();
			if(o instanceof EMFInternalError) {
				EMFInternalError error = (EMFInternalError)o;
				Exception e = error.getNativeException();
				if(e instanceof QbeEngineException) {
					QbeEngineException qbeError = (QbeEngineException)e;
					logError(qbeError);
					
					try {
						writeBackToClient( new JSONFailure( qbeError ) );
					} catch (IOException ioe) {
						throw new EngineException("Impossible to write back the responce to the client", e);
					}
				}
			}
			
		}
	}


	private void logError(QbeEngineException qbeError) {
		logger.error(qbeError.getMessage());
		logger.error("The error root cause is: " + qbeError.getRootCause());	
		if(qbeError.getHints().size() > 0) {
			Iterator hints = qbeError.getHints().iterator();
			while(hints.hasNext()) {
				String hint = (String)hints.next();
				logger.info("hint: " + hint);
			}
			
		}
		logger.error("The error root cause stack trace is:",  qbeError.getCause());	
		logger.error("The error full stack trace is:", qbeError);			
	}
}
