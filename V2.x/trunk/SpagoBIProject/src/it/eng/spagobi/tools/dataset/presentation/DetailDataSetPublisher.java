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
package it.eng.spagobi.tools.dataset.presentation;

import org.apache.log4j.Logger;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.presentation.PublisherDispatcherIFace;
import it.eng.spagobi.commons.utilities.GeneralUtilities;


/**
 * Publishes the results of a detail request for a dataSet into the correct 
 * jsp page according to what contained into request. If Any errors occurred during the 
 * execution of the <code>DetailDataSetModule</code> class, the publisher
 * is able to call the error page with the error message caught before and put into 
 * the error handler. If the input information don't fall into any of the cases declared,
 * another error is generated. 
 * 
 */
public class DetailDataSetPublisher implements PublisherDispatcherIFace {
	static private Logger logger = Logger.getLogger(DetailDataSetPublisher.class);
	private SourceBean listTestLovMR 	= null;
	private SourceBean detailMR 	= null;
	public static final String DETAIL_DATA_SET_MODULE = "DetailDataSetModule";
	public static final String LIST_TEST_DATA_SET_MODULE = "ListTestDataSetModule";
	
	/**
	 *Given the request at input, gets the name of the reference publisher,driving
	 * the execution into the correct jsp page, or jsp error page, if any error occurred.
	 * 
	 * @param requestContainer The object containing all request information
	 * @param responseContainer The object containing all response information
	 * @return A string representing the name of the correct publisher, which will
	 * 		   call the correct jsp reference.
	 */
	
	
	public SourceBean getModuleResponse(ResponseContainer responseContainer, String moduleName) {
		return (SourceBean) responseContainer.getServiceResponse().getAttribute(moduleName);
	}
	
	public void getModuleResponses(ResponseContainer responseContainer) {
		detailMR = getModuleResponse(responseContainer, DETAIL_DATA_SET_MODULE);
		listTestLovMR = getModuleResponse(responseContainer, LIST_TEST_DATA_SET_MODULE);
	}

	
	
	public String getPublisherName(RequestContainer requestContainer, ResponseContainer responseContainer) {
		logger.debug("IN");

		EMFErrorHandler errorHandler = responseContainer.getErrorHandler();
		
		// get the module response
		
		getModuleResponses(responseContainer);	
		
		// if the module response is null throws an error and return the name of the errors publisher

		if (noModuledResponse()) {
			logger.error("Module response null");
			EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 10 );
			errorHandler.addError(error);			
			return "error";
		}
		

		
		// if there are errors and they are only validation errors return the name for the detail publisher
		if(!errorHandler.isOK()) {
			if(GeneralUtilities.isErrorHandlerContainingOnlyValidationError(errorHandler)) {
				logger.info("Publish: detailDataSet"  );
				return "detailDataSet";
			}
		}
		
		// if there are some errors into the errorHandler (not validation errors), return the name for the errors publisher
		if(!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
			return new String("error");
		}

		
	      boolean afterTest = false;
	        Object testExecuted = getAttributeFromModuleResponse(listTestLovMR, "testExecuted");
	        if(testExecuted != null) {
		    		afterTest = true;
	        }
		
		
		
	        
	        // switch to correct publisher
			if (isLoop()) {
				return new String("detailDataSetLoop");
			} else if (afterTest) {
				return new String("detailDataSetTestResult");
//			} 
//			else if(fillProfAttr) {
//				return new String("detailLovFillProfileAttributes");
			} else {
				return new String("detailDataSet");
			}

	        
		
/*        Object loop = moduleResponse.getAttribute("loopback");
        if (loop != null) {
        	logger.info("Publish: detailDataSetLoop"  );
        	logger.debug("OUT");
        	return "detailDataSetLoop";
		} else {
			logger.info("Publish: detailDataSet"  );
			logger.debug("OUT");
			return "detailDataSet";
		}*/
	
	}

	private boolean noModuledResponse() {
		return (detailMR == null && listTestLovMR == null);
	}

	private Object getAttributeFromModuleResponse(SourceBean moduleResponse, String attributeName) {
		return ( (moduleResponse == null)? null: moduleResponse.getAttribute(attributeName));
	}
	
	private boolean isLoop() {
		return isAttrbuteDefinedInModuleResponse(detailMR, "loopback");
	}
	

	private boolean isAttrbuteDefinedInModuleResponse(SourceBean moduleResponse, String attributeName) {
		return (getAttributeFromModuleResponse(moduleResponse, attributeName) != null);
	}
	
}
