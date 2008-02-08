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

package it.eng.spagobi.engines.dashboardscomposition;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IDomainDAO;
import it.eng.spagobi.commons.utilities.SpagoBITracer;
import it.eng.spagobi.engines.InternalEngineIFace;
import it.eng.spagobi.engines.drivers.exceptions.InvalidOperationRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpagoBIDashboardsCompositionInternalEngine implements InternalEngineIFace {

	public static final String messageBundle = "component_spagobidashboardscompositionIE_messages";
	public static final String DASHBOARDS_COMPOSITION = "DASHBOARDS_COMPOSITION";
	public static final String DASHBOARDS_CONFIGURATION = "DASHBOARDS_CONFIGURATION";
	public static final String DASHBOARDS = "DASHBOARDS";
	public static final String DASHBOARD = "DASHBOARD";
	public static final String LAYOUT = "LAYOUT";
	public static final String DATA = "DATA";
	public static final String LOV_LABEL = "lovlabel";
	public static final String REFRESH_RATE = "refreshRate";
	
	
	/**
	 * Executes the document and populates the response 
	 * 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param obj The <code>BIObject</code> representing the document to be executed
	 * @param response The response <code>SourceBean</code> to be populated
	 */
	public void execute(RequestContainer requestContainer, BIObject obj, SourceBean response) throws EMFUserError{
		
		SpagoBITracer.debug("SpagoBIDashboardsCompositionInternalEngine",
	            this.getClass().getName(),
	            "execute",
	            "Start execute method.");
		
		if (obj == null) {
			SpagoBITracer.major("SpagoBIDashboardsCompositionInternalEngine",
		            this.getClass().getName(),
		            "execute",
		            "The input object is null.");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "100", messageBundle);
		}
		
		if (!obj.getBiObjectTypeCode().equalsIgnoreCase("DASH")) {
			SpagoBITracer.major("SpagoBIDashboardsCompositionInternalEngine",
		            this.getClass().getName(),
		            "execute",
		            "The input object is not a dashboard.");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "1001", messageBundle);
		}
		
		
		
		try {
			byte[] contentBytes = null;
			try{
				ObjTemplate template = DAOFactory.getObjTemplateDAO().getBIObjectActiveTemplate(obj.getId());
	            if(template==null) throw new Exception("Active Template null");
	            contentBytes = template.getContent();
	            if(contentBytes==null) throw new Exception("Content of the Active template null");
			} catch (Exception e) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
			            			"execute", "Error while recovering template content: \n" + e);
				throw new EMFUserError(EMFErrorSeverity.ERROR, "1002", messageBundle);
			}
			// get bytes of template and transform them into a SourceBean
			SourceBean content = null;
			try {
				String contentStr = new String(contentBytes);
				content = SourceBean.fromXMLString(contentStr);
			} catch (Exception e) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
			            			"execute", "Error while converting the Template bytes into a SourceBean object");
				throw new EMFUserError(EMFErrorSeverity.ERROR, "1003", messageBundle);
			}
			
			// create the title
			String title = "";
			title += obj.getName();
			String objDescr = obj.getDescription();
			if( (objDescr!=null) && !objDescr.trim().equals("") ) {
				title += ": " + objDescr;
			}
			
			// get the actor
			SessionContainer session = requestContainer.getSessionContainer();
			String actor = (String) session.getAttribute(SpagoBIConstants.ACTOR);
			// get the possible state changes
			IDomainDAO domaindao = DAOFactory.getDomainDAO();
			List states = domaindao.loadListDomainsByType("STATE");
		    List possibleStates = new ArrayList();
		    if (actor.equalsIgnoreCase(SpagoBIConstants.DEV_ACTOR)){
		    	Iterator it = states.iterator();
		    	 while(it.hasNext()) {
		      		    	Domain state = (Domain)it.next();
		      		    	if (state.getValueCd().equalsIgnoreCase("TEST")){ 
		      					possibleStates.add(state);
		      				}
		      	}  
		    } else if (actor.equalsIgnoreCase(it.eng.spagobi.commons.constants.SpagoBIConstants.TESTER_ACTOR)){
		    	Iterator it = states.iterator();
		    	 while(it.hasNext()) {
		      		    	Domain state = (Domain)it.next();
		      		    	if ((state.getValueCd().equalsIgnoreCase("DEV")) || ((state.getValueCd().equalsIgnoreCase("REL")))) { 
		      					possibleStates.add(state);
		      				}
		      	}  
		    }
			response.setAttribute("title", title);
			response.setAttribute("possibleStateChanges", possibleStates);
			// set information for the publisher
			response.setAttribute(content);
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, DASHBOARDS_COMPOSITION);
			
		} catch (EMFUserError error) {
			
			throw error;
			
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBIDashboardsCompositionInternalEngine", 
						this.getClass().getName(),
					    "execute", 
					    "Cannot exec the dashboard", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "100", messageBundle);
		}

	}

	/**
	 * The <code>SpagoBIDashboardsCompositionInternalEngine</code> cannot manage subobjects so this method must not be invoked
	 * 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param obj The <code>BIObject</code> representing the document
	 * @param response The response <code>SourceBean</code> to be populated
	 * @param subObjectInfo An object describing the subobject to be executed
	 */
	public void executeSubObject(RequestContainer requestContainer, BIObject obj, 
			SourceBean response, Object subObjectInfo) throws EMFUserError {
		// it cannot be invoked
		SpagoBITracer.major("SpagoBIDashboardsCompositionInternalEngine", 
				this.getClass().getName(),
	            "executeSubObject", 
	            "SpagoBIDashboardsCompositionInternalEngine cannot exec subobjects.");
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
		SpagoBITracer.major("SpagoBIDashboardsCompositionInternalEngine", 
				this.getClass().getName(),
	            "handleNewDocumentTemplateCreation", 
	            "SpagoBIDashboardsCompositionInternalEngine cannot build document template.");
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
		SpagoBITracer.major("SpagoBIDashboardsCompositionInternalEngine", 
				this.getClass().getName(),
	            "handleDocumentTemplateEdit", 
	            "SpagoBIDashboardsCompositionInternalEngine cannot build document template.");
		throw new InvalidOperationRequest();
	}
	
}
