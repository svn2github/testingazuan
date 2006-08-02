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
package it.eng.spagobi.services.modules;

import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.List;

/**
 * Loads an objects hyerarchy (Tree) or set inforamtion for a single object execution
 */
public class TreeObjectsModule extends AbstractModule {

    public static final String PATH_SUBTREE = "PATH_SUBTREE";
    SessionContainer sessionContainer = null;
    EMFErrorHandler errorHandler = null;
    
	public void init(SourceBean config) {	}

	public void service(SourceBean request, SourceBean response) throws Exception {
		
		String actor = (String) request.getAttribute(SpagoBIConstants.ACTOR);
		String initialPath = (String) request.getAttribute(TreeObjectsModule.PATH_SUBTREE);
		List functionalities = new ArrayList();
		boolean recoverBIObjects = true;
		String operation = (String) request.getAttribute(SpagoBIConstants.OPERATION);
		if (operation != null && operation.equals(SpagoBIConstants.FUNCTIONALITIES_OPERATION)) {
			// it means that only the functionalities will be displayed
			recoverBIObjects = false;
		} else if (operation != null && operation.equals(SpagoBIConstants.IMPORTEXPORT_OPERATION)) {
			// it means that all the tree documents and functionalities will be displayed
			response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "importexportHome");
			recoverBIObjects = true;
		}
		try {
			if (initialPath != null && !initialPath.trim().equals("")) {
				functionalities = DAOFactory.getLowFunctionalityDAO().loadSubLowFunctionalities(initialPath, recoverBIObjects);
				response.setAttribute(SpagoBIConstants.MODALITY, SpagoBIConstants.FILTER_TREE_MODALITY);
				response.setAttribute(TreeObjectsModule.PATH_SUBTREE, initialPath);
			} else {
				functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(recoverBIObjects);
				response.setAttribute(SpagoBIConstants.MODALITY, SpagoBIConstants.ENTIRE_TREE_MODALITY);
			}
		} catch (EMFUserError e) {
			SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, 
					"TreeObjectsMOdule", 
					"defaultModalityHandler", 
					"Error loading functionalities", e);
		}
		response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
		response.setAttribute(SpagoBIConstants.ACTOR, actor);
	}
	
	
	
	/**
	 * Based on the modality (setted into portlet preference) loads the xml format of 
	 * the entire/filtered tree and puts it into response, or set into the reponse the necessary
	 * inforamtion for the direct execution of a single object
	 * @param request The request Source Bean
	 * @param response The response Source Bean
	 */
//	public void service(SourceBean request, SourceBean response) throws Exception {
//		
//		try{
//			debug("service", "enter service method");
//			errorHandler = getErrorHandler();
//			debug("service", "error handler retrived");
//			RequestContainer requestContainer = getRequestContainer();
//            sessionContainer = requestContainer.getSessionContainer();
//            SessionContainer permanentSession = sessionContainer.getPermanentContainer();
//			debug("service", "sessionContainer and permanentContainer retrived");
//			String actor = (String)request.getAttribute(SpagoBIConstants.ACTOR);
//			debug("service", "using "+actor+" actor");
//	        sessionContainer.setAttribute(SpagoBIConstants.ACTOR, actor);
//            IEngUserProfile profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
//            debug("service", "user profile retrived");
//            PortletRequest portReq = PortletUtilities.getPortletRequest();
//			PortletPreferences prefs = portReq.getPreferences();
//            debug("service", "portlet preferences retrived");
//            String modality = (String)prefs.getValue(MODALITY, "");
//            String path = (String)prefs.getValue(PATH_SUBTREE, "");
//            
//            debug("service", "using "+modality+" modality");
//            if(modality.equalsIgnoreCase(SINGLE_OBJECT)) {
//            	singleObjectModalityHandler(request, response, prefs, actor);
//            } else if(modality.equalsIgnoreCase(FILTER_TREE)) {
//            	filterTreeModalityHandler(request, response, actor, profile, path);
//            } else {
//            	defaultModalityHandler(request, response, actor, profile);
//            }
//		} catch (Exception e) {
//			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
//								"TreeObjectsMOdule", 
//								"service", 
//								"Error while processing request of service",e);
//			EMFUserError emfu = new EMFUserError(EMFErrorSeverity.ERROR, 101);
//			errorHandler.addError(emfu); 
//		}
//	}
	
	
	/**
	 * Set information into response for the execution of a single object
	 * @param request The request SourceBean
	 * @param response The response SourceBean 
	 * @param prefs Portlet Preferences
	 * @param actor Actor's type
	 */
//	private void singleObjectModalityHandler(SourceBean request, SourceBean response,
//			                                 PortletPreferences prefs, String actor) 
//											 throws Exception {
//		debug("singleObjectModalityHandler", "enter singleObjectModalityHandler");
//		
//		// get from preferences the label of the object
//		String label = prefs.getValue(LABEL_SINGLE_OBJECT, "");
//		debug("singleObjectModalityHandler", "using object label " + label);
//		
//		// if label is not set then throw an exception
//		if(label.trim().equals("")) {
//			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
//								"TreeObjectsMOdule", 
//								"singleObjectModalityHandler",  
//								"Object's label not set");
//        	throw new Exception("Label not set");
//        }
//		
//		// get from preferences the parameters used by the object during execution
//		String parameters = prefs.getValue(PARAMETERS_SINGLE_OBJECT, "");
//		debug("singleObjectModalityHandler", "using parameters " + parameters);
//		
//		// get from preferences the height of the area
//		String heightArea = prefs.getValue(HEIGHT_AREA, "");
//		debug("singleObjectModalityHandler", "using height of area " + heightArea);
//        
//		// set into the reponse the publisher name for object execution
//        response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, 
//        					  SpagoBIConstants.PUBLISHER_LOOPBACK_SINGLE_OBJECT_EXEC);
//        
//        // set into the response the righr inforamtion for loopback
//        response.setAttribute(SpagoBIConstants.ACTOR, actor);
//        BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectByLabel(label);
//        response.setAttribute(ObjectsTreeConstants.OBJECT_ID, obj.getId().toString());
//                
//        // put in session the modality and the actor
//        sessionContainer.setAttribute(SpagoBIConstants.MODALITY, 
//        		                      SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY);
//        
//        // if the parameters is set put it into the session
//        if(!parameters.equals("")) 
//        	sessionContainer.setAttribute(SpagoBIConstants.PARAMETERS, parameters);
//        
//        // if the height of the area is set put it into the session
//        if(!heightArea.equals("")) 
//        	sessionContainer.setAttribute(SpagoBIConstants.HEIGHT_OUTPUT_AREA, heightArea);
//        debug("singleObjectModalityHandler", "data stored into response");
//	}
	
	
	/**
	 * Load a filtered tree starting from a given path (But not yet implemented).
	 * @param request The Request SourceBean
	 * @param response The Response SourceBean
	 */
//	private void filterTreeModalityHandler(SourceBean request, SourceBean response, 
//            String actor, IEngUserProfile profile, String path)  throws SourceBeanException {
//		
//		debug("filterTreeModalityHandler", "enter filterTreeModalityHandler");
//		//SourceBean dataResponseSysFunct = getFunctioanlitiesTree(profile, path);
//		//debug("filterTreeModalityHandler", "SourceBean of the tree retrived: \n " + dataResponseSysFunct.toXML(false));
//		//response.setAttribute(dataResponseSysFunct);
//		
//		List functionalities = new ArrayList();
//		boolean recoverBIObjects = true;
//		String operation = (String) request.getAttribute(SpagoBIConstants.OPERATION);
//		if (operation != null && operation.equals(SpagoBIConstants.FUNCTIONALITIES_OPERATION)) {
//			// it means that only the functionalities will be displayed
//			recoverBIObjects = false;
//		}
//		try {
//			functionalities = DAOFactory.getLowFunctionalityDAO().loadSubLowFunctionalities(path, recoverBIObjects);
//		} catch (EMFUserError e) {
//			SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, 
//					"TreeObjectsMOdule", 
//					"defaultModalityHandler", 
//					"Error loading functionalities", e);
//		}
//		response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
//		response.setAttribute(SpagoBIConstants.ACTOR, actor);
//		response.setAttribute(SpagoBIConstants.MODALITY, SpagoBIConstants.FILTER_TREE_MODALITY);
//		response.setAttribute(TreeObjectsModule.PATH_SUBTREE, path);
//		
////		Object objview = request.getAttribute(SpagoBIConstants.OBJECTS_VIEW);
////		if(objview!=null) {
////			response.setAttribute(SpagoBIConstants.OBJECTS_VIEW, objview);
////			debug("filterTreeModalityHandler", "using tree view");
////		}
////		Object listPage = request.getAttribute(SpagoBIConstants.LIST_PAGE);
////		if(listPage!=null) {
////			response.setAttribute(SpagoBIConstants.LIST_PAGE, listPage);
////			debug("defaultModalityHandler", "using list view");
////		}
//		debug("filterTreeModalityHandler", "end set data into reponse");
//        
//		// put in session the modality and the actor
//        sessionContainer.setAttribute(SpagoBIConstants.MODALITY, 
//        		                      SpagoBIConstants.FILTER_TREE_MODALITY);
//        debug("filterTreeModalityHandler", "end set data into session");
//
//	}
	
	
	/**
	 * Load the xml format of the entire object tree and put it into response
	 * @param request The Spago Request SourceBean
	 * @param response The Spago Response  SourceBean
	 * @param actor Actor type
	 * @param profile Profile of the actor
	 */
//	private void defaultModalityHandler(SourceBean request, SourceBean response, 
//			                            String actor, IEngUserProfile profile) 
//										throws SourceBeanException {
//		debug("defaultModalityHandler", "enter defaultModalityHandler");
//		//SourceBean dataResponseSysFunct = getFunctioanlitiesTree(profile);
//		//debug("defaultModalityHandler", "SourceBean of the tree retrived: \n " + dataResponseSysFunct.toXML(false));
//		//response.setAttribute(dataResponseSysFunct);
//		List functionalities = new ArrayList();
//		boolean recoverBIObjects = true;
//		String operation = (String) request.getAttribute(SpagoBIConstants.OPERATION);
//		if (operation != null && operation.equals(SpagoBIConstants.FUNCTIONALITIES_OPERATION)) {
//			// it means that only the functionalities will be displayed
//			recoverBIObjects = false;
//		}
//		try {
//			functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(recoverBIObjects);
//		} catch (EMFUserError e) {
//			SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, 
//					"TreeObjectsMOdule", 
//					"defaultModalityHandler", 
//					"Error loading functionalities", e);
//		}
//		response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
//		response.setAttribute(SpagoBIConstants.ACTOR, actor);
//		response.setAttribute(SpagoBIConstants.MODALITY, SpagoBIConstants.ENTIRE_TREE_MODALITY);
////		Object objview = request.getAttribute(SpagoBIConstants.OBJECTS_VIEW);
////		if(objview!=null) {
////			response.setAttribute(SpagoBIConstants.OBJECTS_VIEW, objview);
////			debug("defaultModalityHandler", "using tree view");
////		}
////		Object listPage = request.getAttribute(SpagoBIConstants.LIST_PAGE);
////		if(listPage!=null) {
////			response.setAttribute(SpagoBIConstants.LIST_PAGE, listPage);
////			debug("defaultModalityHandler", "using list view");
////		}
//		debug("defaultModalityHandler", "end set data into reponse");
//        // put in session the modality and the actor
//        sessionContainer.setAttribute(SpagoBIConstants.MODALITY, 
//        		                      SpagoBIConstants.ENTIRE_TREE_MODALITY);
//        debug("defaultModalityHandler", "end set data into session");
//	}
	
	
	
    /*
	private SourceBean getFunctioanlitiesTree(IEngUserProfile profile) {
		debug("getFunctioanlitiesTree", "enter getFunctioanlitiesTree");
		SourceBean pathSysFunctSB = (SourceBean)ConfigSingleton.getInstance().getAttribute(PATH_SYS_FUNCT);
	    String pathSysFunct = pathSysFunctSB.getCharacters();
	    debug("getFunctioanlitiesTree", "using intial path" + pathSysFunct);
	    TreeObjectsDAO objDao = new TreeObjectsDAO();
        SourceBean dataResponseSysFunct = objDao.getXmlTreeObjects(pathSysFunct);
        debug("getFunctioanlitiesTree", "objects tree sourceBean loaded: \n" + dataResponseSysFunct.toXML(false));
        return dataResponseSysFunct;
	}*/

//	private SourceBean getFunctioanlitiesTree(IEngUserProfile profile) {
//		debug("getFunctioanlitiesTree", "enter getFunctioanlitiesTree");
//		SourceBean pathSysFunctSB = (SourceBean)ConfigSingleton.getInstance().getAttribute(PATH_SYS_FUNCT);
//	    String pathSysFunct = pathSysFunctSB.getCharacters();
//	    debug("getFunctioanlitiesTree", "using intial path" + pathSysFunct);
//	    TreeObjectsDAO objDao = new TreeObjectsDAO();
//        SourceBean dataResponseSysFunct = objDao.getXmlTreeObjects(pathSysFunct, profile);
//        debug("getFunctioanlitiesTree", "objects tree sourceBean loaded: \n" + dataResponseSysFunct.toXML(false));
//        return dataResponseSysFunct;
//	}

	
  /*
	private SourceBean getFunctioanlitiesTree(IEngUserProfile profile, String path) {
		//debug("getFunctioanlitiesTree", "enter getFunctioanlitiesTree");
		//SourceBean pathSysFunctSB = (SourceBean)ConfigSingleton.getInstance().getAttribute(PATH_SYS_FUNCT);
	    //String pathSysFunct = pathSysFunctSB.getCharacters();
	    debug("getFunctioanlitiesTree", "using intial path" + path);
	    TreeObjectsDAO objDao = new TreeObjectsDAO();
        SourceBean dataResponseSysFunct = objDao.getXmlTreeObjects(path);
        debug("getFunctioanlitiesTree", "objects tree sourceBean loaded: \n" + dataResponseSysFunct.toXML(false));
        return dataResponseSysFunct;
	}
	*/

//	private SourceBean getFunctioanlitiesTree(IEngUserProfile profile, String path) {
//		//debug("getFunctioanlitiesTree", "enter getFunctioanlitiesTree");
//		//SourceBean pathSysFunctSB = (SourceBean)ConfigSingleton.getInstance().getAttribute(PATH_SYS_FUNCT);
//	    //String pathSysFunct = pathSysFunctSB.getCharacters();
//	    debug("getFunctioanlitiesTree", "using intial path" + path);
//	    TreeObjectsDAO objDao = new TreeObjectsDAO();
//        SourceBean dataResponseSysFunct = objDao.getXmlTreeObjects(path, profile);
//        debug("getFunctioanlitiesTree", "objects tree sourceBean loaded: \n" + dataResponseSysFunct.toXML(false));
//        return dataResponseSysFunct;
//	}

	
	
	
	/**
	 * Trace a debug message into the log
	 * @param method Name of the method to store into the log
	 * @param message Message to store into the log
	 */
//	private void debug(String method, String message) {
//		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, 
//							"TreeObjectsModule", 
//							method, 
//        					message);
//	}
	
	
}	
	
	
