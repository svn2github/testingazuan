package it.eng.spagobi.engines.chart;



import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.engines.InternalEngineIFace;
import it.eng.spagobi.engines.drivers.exceptions.InvalidOperationRequest;

import org.apache.log4j.Logger;

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

/** Internal Engine
 *  * @author Giulio Gavardi
 *     giulio.gavardi@eng.it
 */


public class SpagoBIChartInternalEngine implements InternalEngineIFace {

	private static transient Logger logger = Logger.getLogger(SpagoBIChartInternalEngine.class);

	public static final String messageBundle = "component_spagobichartKPI_messages";

	/**
	 * Executes the document and populates the response 
	 * 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param obj The <code>BIObject</code> representing the document to be executed
	 * @param response The response <code>SourceBean</code> to be populated
	 */
	public void execute(RequestContainer requestContainer, BIObject obj, SourceBean response) throws EMFUserError{

		logger.debug("IN");

		try {

			if (obj == null) {
				logger.error("The input object is null.");
				throw new EMFUserError(EMFErrorSeverity.ERROR, "100", messageBundle);
			}

			if (!obj.getBiObjectTypeCode().equalsIgnoreCase("DASH")) {
				logger.error("The input object is not a dashboard.");
				throw new EMFUserError(EMFErrorSeverity.ERROR, "1001", messageBundle);
			}

			String documentId=obj.getId().toString();

			SessionContainer session = requestContainer.getSessionContainer();
			IEngUserProfile profile = (IEngUserProfile) session.getPermanentContainer().getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			String userId=(String)profile.getUserUniqueIdentifier();

			// create the title
			String title = "";
			title += obj.getName();
			String objDescr = obj.getDescription();
			if( (objDescr!=null) && !objDescr.trim().equals("") ) {
				title += ": " + objDescr;
			}

			//String result=getLovResult(profile, documentId,mode,dataParameters);

			//response.setAttribute("userid", userId);
			response.setAttribute("documentid", documentId);
			response.setAttribute("title", title);				

			// set information for the publisher
			//response.setAttribute("ACTION_NAME", "CREATE_CHART");
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "LOOPCHART");

		} catch (EMFUserError error) {
			throw error;
		} catch (Exception e) {
			logger.error("Cannot exec the dashboard", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "100", messageBundle);
		} finally {
			logger.debug("OUT");
		}

	}

	/**
	 * The <code>SpagoBIDashboardInternalEngine</code> cannot manage subobjects so this method must not be invoked
	 * 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param obj The <code>BIObject</code> representing the document
	 * @param response The response <code>SourceBean</code> to be populated
	 * @param subObjectInfo An object describing the subobject to be executed
	 */
	public void executeSubObject(RequestContainer requestContainer, BIObject obj, 
			SourceBean response, Object subObjectInfo) throws EMFUserError {
		// it cannot be invoked
		logger.error("SpagoBIDashboardInternalEngine cannot exec subobjects.");
		throw new EMFUserError(EMFErrorSeverity.ERROR, "101", messageBundle);
	}

	/**
	 * Function not implemented. Thid method should not be called
	 * 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param biobject The BIOBject to edit
	 * @param response The response <code>SourceBean</code> to be populated
	 * @throws InvalidOperationRequest
	 */
	public void handleNewDocumentTemplateCreation(RequestContainer requestContainer, 
			BIObject obj, SourceBean response) throws EMFUserError, InvalidOperationRequest {
		logger.error("SpagoBIDashboardInternalEngine cannot build document template.");
		throw new InvalidOperationRequest();

	}

	/**
	 * Function not implemented. Thid method should not be called
	 * 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param biobject The BIOBject to edit
	 * @param response The response <code>SourceBean</code> to be populated
	 * @throws InvalidOperationRequest
	 */
	public void handleDocumentTemplateEdit(RequestContainer requestContainer, 
			BIObject obj, SourceBean response) throws EMFUserError, InvalidOperationRequest {
		logger.error("SpagoBIDashboardInternalEngine cannot build document template.");
		throw new InvalidOperationRequest();
	}



}




