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

package it.eng.spagobi.engines.dashboard;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.drivers.exceptions.InvalidOperationRequest;
import it.eng.spagobi.engines.InternalEngineIFace;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SpagoBIDashboardInternalEngine implements InternalEngineIFace {

	public static final String messageBundle = "component_spagobidashboardIE_messages";

	/**
	 * Executes the document and populates the response 
	 * 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param obj The <code>BIObject</code> representing the document to be executed
	 * @param response The response <code>SourceBean</code> to be populated
	 */
	public void execute(RequestContainer requestContainer, BIObject obj, SourceBean response) throws EMFUserError{
		
		SpagoBITracer.debug("SpagoBIDashboardInternalEngine",
	            this.getClass().getName(),
	            "execute",
	            "Start execute method.");
		
		if (obj == null) {
			SpagoBITracer.major("SpagoBIDashboardInternalEngine",
		            this.getClass().getName(),
		            "execute",
		            "The input object is null.");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "100", messageBundle);
		}
		
		if (!obj.getBiObjectTypeCode().equalsIgnoreCase("DASH")) {
			SpagoBITracer.major("SpagoBIDashboardInternalEngine",
		            this.getClass().getName(),
		            "execute",
		            "The input object is not a dashboard.");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "1001", messageBundle);
		}
		
		
		
		try{	
			
			// get the template of the object
			obj.loadTemplate();
			UploadedFile template = obj.getTemplate();
			if (template==null) { 
				SpagoBITracer.major("SpagoBIDashboardInternalEngine",
						            this.getClass().getName(),
						            "execute",
						            "Template biobject null");
				throw new EMFUserError(EMFErrorSeverity.ERROR, "1002", messageBundle);
			}
			// get bytes of template and transform them into a SourceBean
			SourceBean content = null;
			try {
				byte[] contentBytes = template.getFileContent();
				String contentStr = new String(contentBytes);
				content = SourceBean.fromXMLString(contentStr);
			} catch (Exception e) {
				SpagoBITracer.major("SpagoBIDashboardInternalEngine",
			            			this.getClass().getName(),
			            			"execute",
			            			"Error while converting the Template bytes into a SourceBean object");
				throw new EMFUserError(EMFErrorSeverity.ERROR, "1003", messageBundle);
			}
			// get information from the conf SourceBean and pass them into the response
			String displayTitleBar = (String)content.getAttribute("displayTitleBar");
			String movie = (String)content.getAttribute("movie");
			String width = (String)content.getAttribute("DIMENSION.width");
			String height = (String)content.getAttribute("DIMENSION.height");
			
			
			String dataurl = (String)content.getAttribute("DATA.url");
			// get all the parameters for data url
			Map dataParameters = new HashMap();
			SourceBean dataSB = (SourceBean)content.getAttribute("DATA");
			List dataAttrsList = dataSB.getContainedSourceBeanAttributes();
			Iterator dataAttrsIter = dataAttrsList.iterator();
			while(dataAttrsIter.hasNext()) {
				SourceBeanAttribute paramSBA = (SourceBeanAttribute)dataAttrsIter.next();
				SourceBean param = (SourceBean)paramSBA.getValue();
				String nameParam = (String)param.getAttribute("name");
				String valueParam = (String)param.getAttribute("value");
				dataParameters.put(nameParam, valueParam);
			}
			
			
			// get all the parameters for dash configuration
			Map confParameters = new HashMap();
			SourceBean confSB = (SourceBean)content.getAttribute("CONF");
			List confAttrsList = confSB.getContainedSourceBeanAttributes();
			Iterator confAttrsIter = confAttrsList.iterator();
			while(confAttrsIter.hasNext()) {
				SourceBeanAttribute paramSBA = (SourceBeanAttribute)confAttrsIter.next();
				SourceBean param = (SourceBean)paramSBA.getValue();
				String nameParam = (String)param.getAttribute("name");
				String valueParam = (String)param.getAttribute("value");
				confParameters.put(nameParam, valueParam);
			}
			// create the title
			String title = "";
			title += obj.getName();
			String objDescr = obj.getDescription();
			if( (objDescr!=null) && !objDescr.trim().equals("") ) {
				title += ": " + objDescr;
			}
			
			List possibleStates = new ArrayList();
			/*
			// get the actor
			SessionContainer session = requestContainer.getSessionContainer();
			String actor = (String) session.getAttribute(SpagoBIConstants.ACTOR);
			// get the possible state changes
			IDomainDAO domaindao = DAOFactory.getDomainDAO();
			List states = domaindao.loadListDomainsByType("STATE");
		    if (actor.equalsIgnoreCase(SpagoBIConstants.DEV_ACTOR)){
		    	Iterator it = states.iterator();
		    	 while(it.hasNext()) {
		      		    	Domain state = (Domain)it.next();
		      		    	if (state.getValueCd().equalsIgnoreCase("TEST")){ 
		      					possibleStates.add(state);
		      				}
		      	}  
		    } else if (actor.equalsIgnoreCase(it.eng.spagobi.constants.SpagoBIConstants.TESTER_ACTOR)){
		    	Iterator it = states.iterator();
		    	 while(it.hasNext()) {
		      		    	Domain state = (Domain)it.next();
		      		    	if ((state.getValueCd().equalsIgnoreCase("DEV")) || ((state.getValueCd().equalsIgnoreCase("REL")))) { 
		      					possibleStates.add(state);
		      				}
		      	}  
		    } 
		    */
			
			
			// set information into reponse
			response.setAttribute("movie", movie);
			response.setAttribute("dataurl", dataurl);
			response.setAttribute("width", width);
			response.setAttribute("height", height);
			response.setAttribute("displayTitleBar", displayTitleBar);
			response.setAttribute("title", title);
			response.setAttribute("confParameters", confParameters);
			response.setAttribute("dataParameters", dataParameters);
			response.setAttribute("possibleStateChanges", possibleStates);
			// set information for the publisher
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "DASHBOARD");
			
		} catch (EMFUserError error) {
			
			throw error;
			
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBIDashboardInternalEngine", 
						this.getClass().getName(),
					    "execute", 
					    "Cannot exec the dashboard", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "100", messageBundle);
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
		SpagoBITracer.major("SpagoBIDashboardInternalEngine", 
				this.getClass().getName(),
	            "executeSubObject", 
	            "SpagoBIDashboardInternalEngine cannot exec subobjects.");
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
		SpagoBITracer.major("SpagoBIDashboardInternalEngine", 
				this.getClass().getName(),
	            "handleNewDocumentTemplateCreation", 
	            "SpagoBIDashboardInternalEngine cannot build document template.");
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
		SpagoBITracer.major("SpagoBIDashboardInternalEngine", 
				this.getClass().getName(),
	            "handleDocumentTemplateEdit", 
	            "SpagoBIDashboardInternalEngine cannot build document template.");
		throw new InvalidOperationRequest();
	}

}
