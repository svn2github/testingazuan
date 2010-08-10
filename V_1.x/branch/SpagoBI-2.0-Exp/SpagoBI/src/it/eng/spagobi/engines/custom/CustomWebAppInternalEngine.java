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

package it.eng.spagobi.engines.custom;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.AbstractHttpModule;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.drivers.exceptions.InvalidOperationRequest;
import it.eng.spagobi.engines.InternalEngineIFace;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;

public class CustomWebAppInternalEngine extends AbstractHttpModule implements InternalEngineIFace {
	
	public static final String messageBundle = "messages";
	
	/**
	 * Executes the document and populates the response 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param obj The <code>BIObject</code> representing the document to be executed
	 * @param response The response <code>SourceBean</code> to be populated
	 */
	public void execute(RequestContainer requestContainer, BIObject obj, SourceBean response) throws EMFUserError {
		
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
	            			"execute", "Start execute method.");
		if(obj == null) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
		            			"execute", "The input object is null.");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "100", messageBundle);
		}
		try{
			obj.loadTemplate();
			UploadedFile uplFile = obj.getTemplate();
			byte[] tempContBys = uplFile.getFileContent();
			String tempContStr = new String(tempContBys);
			SourceBean tempContSB = SourceBean.fromXMLString(tempContStr);
			SourceBean entryPointSB = (SourceBean)tempContSB.getAttribute("ENTRY_POINT");
			String publisher = (String)entryPointSB.getAttribute("publisher");
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "CustomWAIE");
			response.setAttribute("WA_PUBLISHER_NAME", publisher);
			response.setAttribute("TITLE", obj.getName());
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					    		"execute", "Cannot exec the document", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "100", messageBundle);
		}
	}
	
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		String pubName = (String)request.getAttribute("WA_PUBLISHER_NAME");
		response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, pubName);
	}

	
	
	/*
	public void execute(RequestContainer requestContainer, BIObject obj, SourceBean response) throws EMFUserError {
		
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
	            			"execute", "Start execute method.");		
		if(obj == null){
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
		            			"execute", "The input object is null.");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "100");
		}
		if(!ChannelUtilities.isWebRunning()) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
        						"execute", "Not Running in web mode.");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "100");
		}
		try {
			obj.loadTemplate();
			UploadedFile uplFile = obj.getTemplate();
			byte[] tempContBys = uplFile.getFileContent();
			String tempContStr = new String(tempContBys);
			SourceBean tempContSB = SourceBean.fromXMLString(tempContStr);
			SourceBean entryPointSB = (SourceBean)tempContSB.getAttribute("ENTRY_POINT");
			
			String publisher = (String)entryPointSB.getAttribute("publisher");
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, publisher);
			
		
//			String code = (String)entryPointSB.getAttribute("code");
//			String pathJsp = (String)entryPointSB.getAttribute("jsp");
//			
//			String newPubStr = "<PUBLISHER name=\""+code+"\">" +
//			                   	"<RENDERING channel=\"HTTP\" mode=\"\" type=\"JSP\">" +
//			                   		"<RESOURCES>" +
//			                   			"<ITEM prog=\"0\" resource=\""+pathJsp+"\"/>" +
//			                   		"</RESOURCES>" +
//			                   	"</RENDERING>" +
//			                   "</PUBLISHER>";
//			SourceBean newPub = SourceBean.fromXMLString(newPubStr);
//			ConfigSingleton config = ConfigSingleton.getInstance();
//			SourceBean pubsSB = (SourceBean)config.getAttribute("PUBLISHERS");
//			SourceBean existingNewPub = (SourceBean)pubsSB.getFilteredSourceBeanAttribute("PUBLISHER", "name", code);
//			if(existingNewPub==null) {
//				pubsSB.setAttribute(newPub);
//				config.updAttribute(pubsSB);
//			}
//			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, code);
              
				
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
								"execute", "Error while executing", e);
		}
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
    						"execute", "End execute method.");			
	}
	 */

	public void executeSubObject(RequestContainer requestContainer, BIObject obj, SourceBean response, Object subObjectInfo) throws EMFUserError {
		SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
    			            "executeSubObject",  "Cannot exec subobjects.");
		throw new EMFUserError(EMFErrorSeverity.ERROR, "101");	
	}

	public void handleDocumentTemplateEdit(RequestContainer requestContainer, BIObject obj, SourceBean response) throws EMFUserError, InvalidOperationRequest {
		SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
    						"handleDocumentTemplateEdit", "Not implemented: Cannot build document template");
		throw new InvalidOperationRequest();
	}

	public void handleNewDocumentTemplateCreation(RequestContainer requestContainer, BIObject obj, SourceBean response) throws EMFUserError, InvalidOperationRequest {
		SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
	            			"handleNewDocumentTemplateCreation", "Not implemented: Cannot build document template");
		throw new InvalidOperationRequest();
	}

	
	
	
}
