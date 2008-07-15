/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.hotlink.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;

import org.apache.log4j.Logger;

/**
 * 
 * @author Zerbetto (davide.zerbetto@eng.it)
 *
 */
public class HotLinkModule extends AbstractModule {

	private static transient Logger logger = Logger.getLogger(HotLinkModule.class);
	public static final String MODULE_NAME = "HotLinkModule"; 
	
	/* (non-Javadoc)
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response)
			throws Exception {
		logger.debug("IN");
		try {
			String operation = (String) request.getAttribute("OPERATION");
			if (operation == null || operation.equalsIgnoreCase("GET_HOT_LINK_LIST")) {
				getHotLinkListHandler(request, response);
			} else if (operation.equalsIgnoreCase("DELETE_REMEMBER_ME")) {
				deleteRememberMeHandler(request, response);
			} 
		} catch (EMFUserError error) {
			logger.error(error);
			getErrorHandler().addError(error);
		} catch (Exception e) {
			logger.error(e);
			EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, e);
			getErrorHandler().addError(internalError);
		} finally {
			logger.debug("OUT");
		}
	}

	private void getHotLinkListHandler(SourceBean request, SourceBean response) throws Exception {
		logger.debug("IN");
		try {
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "HOT_LINK_HOME");
		} finally {
			logger.debug("OUT");
		}
	}
	
	private void deleteRememberMeHandler(SourceBean request, SourceBean response) throws Exception {
		logger.debug("IN");
		try {
			String rememberMeIdStr = (String) request.getAttribute("REMEMBER_ME_ID");
			Integer rememberMeId = new Integer(rememberMeIdStr);
			DAOFactory.getRememberMeDAO().delete(rememberMeId);
			getHotLinkListHandler(request, response);
		} finally {
			logger.debug("OUT");
		}
	}
	
}
