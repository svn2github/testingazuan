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
package it.eng.spagobi.presentation.publishers;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.presentation.PublisherDispatcherIFace;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

/**
 * Publishes the results of a detail request for a LOV value into the correct 
 * jsp page according to what contained into request. If Any errors occurred during the 
 * execution of the <code>DetailModalitiesValueModule</code> class, the publisher
 * is able to call the error page with the error message caught before and put into 
 * the error handler. If the input information don't fall into any of the cases declared,
 * another error is generated. 
 * 
 * @author sulis
 */
public class DetailModalitiesValuePublisher implements PublisherDispatcherIFace {

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

		// get the module response
		SourceBean detailMR = (SourceBean) responseContainer.getServiceResponse().getAttribute("DetailModalitiesValueModule");
		SourceBean listTestQueryMR = (SourceBean) responseContainer.getServiceResponse().getAttribute("ListTestQueryModule");
		SourceBean listTestScriptMR = (SourceBean) responseContainer.getServiceResponse().getAttribute("ListTestScriptModule");
		
		// if the module response is null throws an error and return the name of the errors publisher
		if (detailMR == null && listTestQueryMR == null && listTestScriptMR == null) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
		            "DetailModalitiesValuePublisher", 
		            "getPublisherName", 
		            "Module response null");
			EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 10 );
			errorHandler.addError(error);
			return new String("error");
		}
		
		// if there are errors and they are only validation errors return the name for the detail publisher
		if(!errorHandler.isOK()) {
			if(GeneralUtilities.isErrorHandlerContainingOnlyValidationError(errorHandler)) {
				if (detailMR != null) {
					return new String("detailModalitiesValue");
				} else if (listTestQueryMR != null) {
					return "detailLovTestResult";
				}  if (listTestScriptMR != null) {
					return "detailLovTestResult";
				}	
			}
		}
		
		// if there are some errors into the errorHandler, return the name for the errors publisher or the detail publisher
		if(!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
			if (detailMR != null) {
				return "error";
			} else if (listTestQueryMR != null) {
				if ("yes".equalsIgnoreCase((String) listTestQueryMR.getAttribute("testExecuted"))) return "detailLovTestResult";
				else return "error";
			}  if (listTestScriptMR != null) {
				if ("yes".equalsIgnoreCase((String) listTestScriptMR.getAttribute("testExecuted"))) return "detailLovTestResult";
				else return "error";
			} else return "error";
		}
		
		boolean isLoop = false;
        Object loop = detailMR == null ? null : detailMR.getAttribute("loopback");
        if(loop != null) {
			isLoop = true;
		} 
        
        boolean afterTest = false;
        
        Object testedObject = detailMR == null ? null : detailMR.getAttribute("testedObject");
        if (testedObject != null) {
        	String testedObjectStr = (String) testedObject;
        	if ("MAN_IN".equalsIgnoreCase(testedObjectStr) || "FIX_LOV".equalsIgnoreCase(testedObjectStr)) 
        		return "detailLovTestResult";
        	else if ("QUERY".equalsIgnoreCase(testedObjectStr)) {
        		if (listTestQueryMR == null) {
        			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
        		            "DetailModalitiesValuePublisher", 
        		            "getPublisherName", 
        		            "The tested object is a query but the ListTestQueryModule response is null");
        			EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 10 );
        			errorHandler.addError(error);
        			return new String("error");
        		} else {
        			Object testExecuted = listTestQueryMR.getAttribute("testExecuted");
        			if (testExecuted != null) afterTest = true;
        			else return "error";
        		}
        	} else if ("SCRIPT_LIST_OF_VALUES".equalsIgnoreCase(testedObjectStr)) {
        		if (listTestScriptMR == null) {
        			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
        		            "DetailModalitiesValuePublisher", 
        		            "getPublisherName", 
        		            "The tested object is a script but the ListTestScriptModule response is null");
        			EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 10 );
        			errorHandler.addError(error);
        			return new String("error");
        		} else {
        			Object testExecuted = listTestScriptMR.getAttribute("testExecuted");
        			if (testExecuted != null) afterTest = true;
        			else return "error";
        		}
        	} else if ("SCRIPT_SINGLE_VALUE".equalsIgnoreCase(testedObjectStr)) {
    			Object testExecuted = detailMR.getAttribute("testExecuted");
    			if (testExecuted != null) afterTest = true;
    			else return "error";
        	}
        } else if (detailMR == null) {
        	if (listTestQueryMR != null && listTestScriptMR != null) {
    			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
    		            "DetailModalitiesValuePublisher", 
    		            "getPublisherName", 
    		            "The ListTestQueryModule and ListTestScriptModule modules were called both");
    			EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 10 );
    			errorHandler.addError(error);
    			return new String("error");
        	}
        	if (listTestQueryMR != null) {
    			Object testExecuted = listTestQueryMR.getAttribute("testExecuted");
    			if (testExecuted != null) afterTest = true;
    			else return "error";
        	}
        	if (listTestScriptMR != null) {
    			Object testExecuted = listTestScriptMR.getAttribute("testExecuted");
    			if (testExecuted != null) afterTest = true;
    			else return "error";
        	}
        }
        
//      Object wizardObj = moduleResponse.getAttribute(SpagoBIConstants.WIZARD);
//        
//		boolean isQueryWizard = false;
//		if(wizardObj!=null) {
//			String wizard = (String)wizardObj;
//			isQueryWizard = wizard.equals(SpagoBIConstants.WIZARD_QUERY);
//		}
//        
//		boolean isLovWizard = false;
//		if(wizardObj!=null) {
//			String wizard = (String)wizardObj;
//			isLovWizard = wizard.equals(SpagoBIConstants.WIZARD_FIX_LOV);
//		}
//		
//		boolean isScriptWizard = false;
//		if(wizardObj!=null) {
//			String wizard = (String)wizardObj;
//			isScriptWizard = wizard.equals(SpagoBIConstants.WIZARD_SCRIPT);
//		}
                

//		if (isQueryWizard){
//			return new String ("querySelectionWizard");
//		} else if (isLovWizard){
//			return new String ("lovSelectionWizard");
//		} else if(isScriptWizard) {
//			return new String ("scriptSelectionWizard");
//		} else 
		if (isLoop) {
			return new String("detailModalitiesValueLoop");
		} else if (afterTest) {
			return new String("detailLovTestResult");
		} else {
			return new String("detailModalitiesValue");
		}

	}

}