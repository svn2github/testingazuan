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

	private SourceBean detailMR 			= null;
	private SourceBean listTestFixedListMR 	= null;
	private SourceBean listTestQueryMR 		= null;
	private SourceBean listTestScriptMR 	= null;
	private SourceBean listTestJavaClassMR 	= null;
	
	public static final String DETAIL_MODALITIES_VALUE_MODULE = "DetailModalitiesValueModule";
	public static final String LIST_TEST_FIXEDLIST_MODULE = "ListTestFixedListModule";
	public static final String LIST_TEST_QUERY_MODULE = "ListTestQueryModule";
	public static final String LIST_TEST_SCRIPT_MODULE = "ListTestScriptModule";
	public static final String LIST_TEST_JAVACLASS_MODULE = "ListTestJavaClassModule";
	
	public SourceBean getModuleResponse(ResponseContainer responseContainer, String moduleName) {
		return (SourceBean) responseContainer.getServiceResponse().getAttribute(moduleName);
	}
	
	public void getModuleResponses(ResponseContainer responseContainer) {
		detailMR = getModuleResponse(responseContainer, DETAIL_MODALITIES_VALUE_MODULE);
		listTestFixedListMR = getModuleResponse(responseContainer, LIST_TEST_FIXEDLIST_MODULE);
		listTestQueryMR = getModuleResponse(responseContainer, LIST_TEST_QUERY_MODULE);
		listTestScriptMR = getModuleResponse(responseContainer, LIST_TEST_SCRIPT_MODULE);
		listTestJavaClassMR = getModuleResponse(responseContainer, LIST_TEST_JAVACLASS_MODULE);
	}
	
	private boolean noModuledResponse() {
		return (detailMR == null 
				&& listTestFixedListMR == null
				&& listTestQueryMR == null 
				&& listTestScriptMR == null 
				&& listTestJavaClassMR == null);
	}
	
	private String getErrorPublisherName() {
		return "error";
	}
	
	private String getTestErrorPublisherName() {
		if (listTestFixedListMR != null && isTestExecuted(listTestFixedListMR)) 
			return "detailLovTestResult";
		else if (listTestQueryMR != null && isTestExecuted(listTestQueryMR)) 
			return "detailLovTestResult";
		else if (listTestJavaClassMR != null && isTestExecuted(listTestJavaClassMR)) 
			return "detailLovTestResult";
		else if (listTestScriptMR != null && isTestExecuted(listTestScriptMR)) 
			return "detailLovTestResult";
		else
			return getErrorPublisherName();
	}
	
	private String getModuleDefaultPublisherName() {
		if (detailMR != null) {
			return new String("detailModalitiesValue");
		} else if (listTestFixedListMR != null) {
			return "detailLovTestResult";
		} else if (listTestQueryMR != null) {
			return "detailLovTestResult";
		} else if (listTestJavaClassMR != null) {
			return "detailLovTestResult";
		} if (listTestScriptMR != null) {
			return "detailLovTestResult";
		}	
		return getErrorPublisherName();
	}
	
	private boolean isTestExecuted(SourceBean moduleResponse){
		return ("yes".equalsIgnoreCase((String) moduleResponse.getAttribute("testExecuted")));
	}
	
	private Object getAttributeFromModuleResponse(SourceBean moduleResponse, String attributeName) {
		return ( (moduleResponse == null)? null: moduleResponse.getAttribute(attributeName));
	}
	
	private boolean isAttrbuteDefinedInModuleResponse(SourceBean moduleResponse, String attributeName) {
		return (getAttributeFromModuleResponse(moduleResponse, attributeName) != null);
	}
	
	private boolean isLoop() {
		return isAttrbuteDefinedInModuleResponse(detailMR, "loopback");
	}
	
	private void notifyError(EMFErrorHandler errorHandler, String message) {
		SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
	            "DetailModalitiesValuePublisher", 
	            "getPublisherName", 
	            message);
		EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 10 );
		errorHandler.addError(error);
	}
	
	public boolean isTestExecutedSuccesfully(SourceBean testModuleResponse) {		
		return (isAttrbuteDefinedInModuleResponse(testModuleResponse, "testExecuted"));
	}
	
	
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

		getModuleResponses(responseContainer);	
		
		if (noModuledResponse()) {
			notifyError(errorHandler, "Module response is null");
			return getErrorPublisherName();
		}
		
		// if there are errors and they are only validation errors return the name for the detail publisher
		if(!errorHandler.isOK()) {
			if(GeneralUtilities.isErrorHandlerContainingOnlyValidationError(errorHandler)) {
				return getModuleDefaultPublisherName();
			}
		}
		
		// if there are some errors into the errorHandler, return the name for the errors publisher or the detail publisher
		if(!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
			return  getTestErrorPublisherName();
		}
				
        
        boolean afterTest = false;
        
        Object testedObject = getAttributeFromModuleResponse(detailMR, "testedObject");;
        
        if (testedObject != null) {
        	String testedObjectStr = (String) testedObject;
        	SourceBean testModuleResponse = null;
        	boolean validTestObject = true;
        	
        	if ("FIXED_LIST".equalsIgnoreCase(testedObjectStr)) {
        		testModuleResponse = listTestFixedListMR;
        	} else if ("QUERY".equalsIgnoreCase(testedObjectStr)) {
        		testModuleResponse = listTestQueryMR;
        	} else if ("SCRIPT".equalsIgnoreCase(testedObjectStr)) {
        		testModuleResponse = listTestScriptMR; 	
        	} else if ("JAVA_CLASS".equalsIgnoreCase(testedObjectStr)) {
        		testModuleResponse = listTestJavaClassMR;       		     	
        	} else {
        		validTestObject = false;
        	}
        	
        	if(validTestObject) {
	        	if(isTestExecutedSuccesfully(testModuleResponse)) {
	    			afterTest = true;
	    		} else {
	    			notifyError(errorHandler, "Problems occurred during test execution");
	    			return getErrorPublisherName();
	    		}   
        	}
        	
        	
        } else if (detailMR == null) {
        	if (listTestQueryMR != null && listTestScriptMR != null) {
    			notifyError(errorHandler,  "The ListTestQueryModule and ListTestScriptModule modules were called both");
    			return getErrorPublisherName();
        	}
        	if (listTestFixedListMR != null) {
    			if (isAttrbuteDefinedInModuleResponse(listTestFixedListMR, "testExecuted")) afterTest = true;
    			else return getErrorPublisherName();
        	}
        	if (listTestQueryMR != null) {
    			if (isAttrbuteDefinedInModuleResponse(listTestQueryMR, "testExecuted")) afterTest = true;
    			else return getErrorPublisherName();
        	}
        	if (listTestJavaClassMR != null) {    			
    			if (isAttrbuteDefinedInModuleResponse(listTestJavaClassMR, "testExecuted")) afterTest = true;
    			else return getErrorPublisherName();
        	}
        	if (listTestScriptMR != null) {    			
    			if (isAttrbuteDefinedInModuleResponse(listTestScriptMR, "testExecuted")) afterTest = true;
    			else return getErrorPublisherName();
        	}
        }        

		if (isLoop()) {
			return new String("detailModalitiesValueLoop");
		} else if (afterTest) {
			return new String("detailLovTestResult");
		} else {
			return new String("detailModalitiesValue");
		}

	}

}