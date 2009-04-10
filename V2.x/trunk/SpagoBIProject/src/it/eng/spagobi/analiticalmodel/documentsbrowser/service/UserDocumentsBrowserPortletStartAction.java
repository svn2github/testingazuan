/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2009 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.analiticalmodel.documentsbrowser.service;


import it.eng.spago.base.SourceBean;
import it.eng.spagobi.commons.services.PortletLoginAction;
import it.eng.spagobi.utilities.exceptions.SpagoBIException;


import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 * @author Antonella Giachino (antonella.giachino@eng.it)
 */
public class UserDocumentsBrowserPortletStartAction extends PortletLoginAction{
	
	// logger component
	private static Logger logger = Logger.getLogger(UserDocumentsBrowserPortletStartAction.class);
	

	public void service(SourceBean request, SourceBean response) throws Exception {
		
		logger.debug("IN");
		
		try {
			super.service(request, response);
			DocumentsBrowserUtility util = new DocumentsBrowserUtility();
			JSONObject jsonObj  = util.defineConfiguration(response);
			
			response.setAttribute("metaConfiguration", jsonObj);
			
		} catch (Throwable t) {
			throw new SpagoBIException("An unexpected error occured while executing UserDocumentsBrowserPortletStartAction", t);
		} finally {
			logger.debug("OUT");
		}
	}	
}
