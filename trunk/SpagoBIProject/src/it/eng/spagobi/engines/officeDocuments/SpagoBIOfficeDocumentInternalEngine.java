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

package it.eng.spagobi.engines.officeDocuments;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.TemplateVersion;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.drivers.exceptions.InvalidOperationRequest;
import it.eng.spagobi.engines.InternalEngineIFace;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;

public class SpagoBIOfficeDocumentInternalEngine implements InternalEngineIFace {

	public static final String messageBundle = "component_spagobiofficedocIE_messages";
	
	/**
	 * Executes the document and populates the response 
	 * 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param obj The <code>BIObject</code> representing the document to be executed
	 * @param response The response <code>SourceBean</code> to be populated
	 */
	public void execute(RequestContainer requestContainer, BIObject obj,
			SourceBean response) throws EMFUserError {
		
		SpagoBITracer.debug("SpagoBIOfficeDocumentInternalEngine",
	            this.getClass().getName(),
	            "execute",
	            "Start execute method.");
		
		if (obj == null) {
			SpagoBITracer.major("SpagoBIOfficeDocumentInternalEngine",
		            this.getClass().getName(),
		            "execute",
		            "The input object is null.");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "100", messageBundle);
		}

		if (!obj.getBiObjectTypeCode().equalsIgnoreCase("OFFICE_DOC")) {
			SpagoBITracer.major("SpagoBIOfficeDocumentInternalEngine",
		            this.getClass().getName(),
		            "execute",
		            "The input object is not a office document.");
			throw new EMFUserError(EMFErrorSeverity.ERROR, "1001", messageBundle);
		}
		
		try {
			response.setAttribute("biobjectId", obj.getId());
			TemplateVersion templateVersion = obj.getCurrentTemplateVersion();
			String templateFileName = null;
			if (templateVersion == null) {
				obj.loadTemplate();
				UploadedFile templateFile = obj.getTemplate();
				if (templateFile == null) {
					SpagoBITracer.major("SpagoBIOfficeDocumentInternalEngine", 
						this.getClass().getName(),
					    "execute", 
					    "The document template is null!!!");
					throw new EMFUserError(EMFErrorSeverity.ERROR, "1004", messageBundle);
				}
				templateFileName = templateFile.getFileName();
			} else {
				templateFileName = templateVersion.getNameFileTemplate();
			}
			response.setAttribute("templateFileName", templateFileName);
			// create the title
			String title = "";
			title += obj.getName();
			String objDescr = obj.getDescription();
			if( (objDescr!=null) && !objDescr.trim().equals("") ) {
				title += ": " + objDescr;
			}
			response.setAttribute("title", title);
			
			// set information for the publisher
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "OFFICE_DOC");
			
		} catch (Exception e) {
			SpagoBITracer.major("SpagoBIOfficeDocumentInternalEngine", 
						this.getClass().getName(),
					    "execute", 
					    "Cannot exec the Office document", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, "100", messageBundle);
		}
	}

	/**
	 * The <code>SpagoBIOfficeDocumentInternalEngine</code> cannot manage subobjects so this method must not be invoked
	 * 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param obj The <code>BIObject</code> representing the document
	 * @param response The response <code>SourceBean</code> to be populated
	 * @param subObjectInfo An object describing the subobject to be executed
	 */
	public void executeSubObject(RequestContainer requestContainer,
			BIObject obj, SourceBean response, Object subObjectInfo)
			throws EMFUserError {
		// it cannot be invoked
		SpagoBITracer.major("SpagoBIOfficeDocumentInternalEngine", 
				this.getClass().getName(),
	            "executeSubObject", 
	            "SpagoBIOfficeDocumentInternalEngine cannot exec subobjects.");
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
		SpagoBITracer.major("SpagoBIOfficeDocumentInternalEngine", 
				this.getClass().getName(),
	            "handleNewDocumentTemplateCreation", 
	            "SpagoBIOfficeDocumentInternalEngine cannot build document template.");
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
		SpagoBITracer.major("SpagoBIOfficeDocumentInternalEngine", 
				this.getClass().getName(),
	            "handleDocumentTemplateEdit", 
	            "SpagoBIOfficeDocumentInternalEngine cannot build document template.");
		throw new InvalidOperationRequest();
	}
}
