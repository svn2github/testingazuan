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

package it.eng.spagobi.pamphlets.publishers;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.presentation.PublisherDispatcherIFace;
import it.eng.spagobi.pamphlets.constants.PamphletsConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

public class PamphletsCollaborationPublisher implements PublisherDispatcherIFace {

	/**
	 *Given the request at input, gets the name of the reference publisher,driving
	 * the execution into the correct jsp page, or jsp error page, if any error occurred.
	 * 
	 * @param requestContainer The object containing all request information
	 * @param responseContainer The object containing all response information
	 * @return A string representing the name of the correct publisher, which will
	 * 		   call the correct jsp reference.
	 */
	public String getPublisherName(RequestContainer requestContainer, ResponseContainer responseContainer) {
		EMFErrorHandler errorHandler = responseContainer.getErrorHandler();
		SourceBean serviceResp = responseContainer.getServiceResponse();
		SourceBean moduleResponse = (SourceBean)serviceResp.getAttribute(PamphletsConstants.PAMPHLET_COLLABORATION_MODULE);
		if(moduleResponse==null) {
			SpagoBITracer.major(PamphletsConstants.NAME_MODULE, this.getClass().getName(), 
					            "getPublisherName", "Module response null");
			EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 100, "component_pamphlets_messages");
			errorHandler.addError(error);
			return "error";
		}
		if(errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
			String pubName = (String)moduleResponse.getAttribute(PamphletsConstants.PUBLISHER_NAME);
			if((pubName!=null) && !(pubName.trim().equals("")))
				return pubName;
			else return new String("PamphletsCollaborationPub");
		} else {
			return new String("error");
		}
	}

}
