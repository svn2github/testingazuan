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
package it.eng.spagobi.services.modules;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.PortletUtilities;

/**
 * Presentation page for the BIObjects.
 * 
 * @author zerbetto
 *
 */

public class BIObjectsModule extends AbstractModule {

	public void service(SourceBean request, SourceBean response) throws Exception {
		
		String objectsView = (String) request.getAttribute(SpagoBIConstants.OBJECTS_VIEW);
		if (objectsView == null) {
			// finds objects view modality from portlet preferences
            PortletRequest portReq = PortletUtilities.getPortletRequest();
			PortletPreferences prefs = portReq.getPreferences();
            objectsView = (String) prefs.getValue(SpagoBIConstants.OBJECTS_VIEW, "");
		} 
        // default value in case it is not specified or in case the value is not valid
        if (objectsView == null || (!objectsView.equalsIgnoreCase(SpagoBIConstants.VIEW_OBJECTS_AS_LIST) && 
				!objectsView.equalsIgnoreCase(SpagoBIConstants.VIEW_OBJECTS_AS_TREE)))
        	objectsView = SpagoBIConstants.VIEW_OBJECTS_AS_TREE;
        
		response.setAttribute(SpagoBIConstants.OBJECTS_VIEW, objectsView);
	}

}
