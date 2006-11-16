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
package it.eng.spagobi.booklets.engines;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.booklets.constants.BookletsConstants;
import it.eng.spagobi.booklets.dao.BookletsCmsDaoImpl;
import it.eng.spagobi.booklets.dao.IBookletsCmsDao;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.drivers.exceptions.InvalidOperationRequest;
import it.eng.spagobi.engines.InternalEngineIFace;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.List;

public class SpagoBIBookletInternalEngine implements InternalEngineIFace {

	public static final String messageBundle = "component_booklets_messages";
	
	/**
	 * Executes the document and populates the response 
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param obj The <code>BIObject</code> representing the document to be executed
	 * @param response The response <code>SourceBean</code> to be populated
	 */
	public void execute(RequestContainer requestContainer, BIObject biobj, SourceBean response) throws EMFUserError {
		SpagoBITracer.debug(BookletsConstants.NAME_MODULE, this.getClass().getName(),
    			            "execute", "Start execute method");
        
		String pathBiObj = biobj.getPath();
		String pathBook = pathBiObj + "/template";
		SpagoBITracer.debug(BookletsConstants.NAME_MODULE, this.getClass().getName(),
    			            "execute", "using path " + pathBook);
		IBookletsCmsDao bookDao = new BookletsCmsDaoImpl();
		List presVersions = bookDao.getPresentationVersions(pathBook);
		SpagoBITracer.debug(BookletsConstants.NAME_MODULE, this.getClass().getName(),
	            			"execute", "Version list retrived " + presVersions);
		try{
			response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletsPresentationVersion");
			response.setAttribute(BookletsConstants.BOOKLET_PRESENTATION_VERSIONS, presVersions);
			response.setAttribute(BookletsConstants.PATH_BOOKLET_CONF, pathBook);
		} catch (Exception e) {
			SpagoBITracer.major(BookletsConstants.NAME_MODULE, this.getClass().getName(), 
					            "execute", "error while setting response attribute " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		SpagoBITracer.debug(BookletsConstants.NAME_MODULE, this.getClass().getName(),
	            "execute", "End execute method");
	}
	
	/**
	 * Executes the subobject of the document and populates the response  
	 * @param requestContainer The <code>RequestContainer</code> object (the session can be retrieved from this object)
	 * @param obj The <code>BIObject</code> representing the document
	 * @param response The response <code>SourceBean</code> to be populated
	 * @param subObjectInfo An object describing the subobject to be executed
	 */
	public void executeSubObject(RequestContainer requestContainer, BIObject obj, SourceBean response, Object subObjectInfo) throws EMFUserError {
		SpagoBITracer.debug(BookletsConstants.NAME_MODULE, this.getClass().getName(),
    			            "executeSubObject", "Start executeSubObject method");
		SpagoBITracer.warning(BookletsConstants.NAME_MODULE, this.getClass().getName(),
	                        "executeSubObject", "Method not implemented");
	}

	public void handleDocumentTemplateEdit(RequestContainer requestContainer, BIObject obj, SourceBean response) throws EMFUserError, InvalidOperationRequest {
		try {
			response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletsManagementLoopCall");
			response.setAttribute(SpagoBIConstants.OPERATION, SpagoBIConstants.EDIT_DOCUMENT_TEMPLATE);
			response.setAttribute(SpagoBIConstants.PATH, obj.getPath());
		} catch (Exception e) {
			SpagoBITracer.major(BookletsConstants.NAME_MODULE, this.getClass().getName(), 
					            "handleDocumentTemplateEdit", "error while setting response attribute " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}

	public void handleNewDocumentTemplateCreation(RequestContainer requestContainer, BIObject obj, SourceBean response) throws EMFUserError, InvalidOperationRequest {
		try {
			response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletsManagementLoopCall");
			response.setAttribute(SpagoBIConstants.OPERATION, SpagoBIConstants.NEW_DOCUMENT_TEMPLATE);
			response.setAttribute(SpagoBIConstants.PATH, obj.getPath());
		} catch (Exception e) {
			SpagoBITracer.major(BookletsConstants.NAME_MODULE, this.getClass().getName(), 
					            "handleDocumentTemplateEdit", "error while setting response attribute " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	

}
