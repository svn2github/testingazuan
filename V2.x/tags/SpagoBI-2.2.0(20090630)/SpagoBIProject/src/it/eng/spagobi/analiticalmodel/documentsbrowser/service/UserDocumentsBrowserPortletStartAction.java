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


import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.commons.services.PortletLoginAction;
import it.eng.spagobi.commons.utilities.ChannelUtilities;
import it.eng.spagobi.commons.utilities.PortletUtilities;
import it.eng.spagobi.utilities.exceptions.SpagoBIException;


import org.apache.log4j.Logger;
import org.json.JSONObject;

/**
 * @author Antonella Giachino (antonella.giachino@eng.it)
 */
public class UserDocumentsBrowserPortletStartAction extends PortletLoginAction {
	
	// logger component
	private static Logger logger = Logger.getLogger(UserDocumentsBrowserPortletStartAction.class);
	
	public static final String LABEL_SUBTREE_NODE = "PATH_SUBTREE";
	public static final String HEIGHT = "HEIGHT";
	public static final String PORTLET = "PORTLET";
	

	public void service(SourceBean request, SourceBean response) throws Exception {
		
		logger.debug("IN");
		
		try {
			super.service(request, response);
			
			
			String labelSubTreeNode = null;
			String height = null;
			
			String channelType = getRequestContainer().getChannelType();			
			if( PORTLET.equalsIgnoreCase(channelType) ) {
				PortletRequest portReq = PortletUtilities.getPortletRequest();
				PortletPreferences prefs = portReq.getPreferences();
				labelSubTreeNode = (String)prefs.getValue(LABEL_SUBTREE_NODE, "");
				height = (String)prefs.getValue(HEIGHT, "600");
				if (labelSubTreeNode != null && !labelSubTreeNode.trim().equals("")) {
					response.setAttribute("labelSubTreeNode", labelSubTreeNode);
				}
				if (height != null && !height.trim().equals("")) {
					response.setAttribute("height", height);
				}				
			} else {
				DocumentsBrowserConfig config = DocumentsBrowserConfig.getInstance();
				JSONObject jsonObj  = config.toJSON();
				// read value from db
				//labelSubTreeNode = ...;
				//jsonObj.put("labelSubTreeNode", labelSubTreeNode);
				response.setAttribute("metaConfiguration", jsonObj);
			}			
			
		} catch (Throwable t) {
			throw new SpagoBIException("An unexpected error occured while executing UserDocumentsBrowserPortletStartAction", t);
		} finally {
			logger.debug("OUT");
		}
	}	
}
