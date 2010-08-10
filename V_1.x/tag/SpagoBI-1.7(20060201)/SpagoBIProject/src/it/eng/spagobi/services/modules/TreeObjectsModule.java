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

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.services.dao.TreeObjectsDAO;
import it.eng.spagobi.utilities.PortletUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

/**
 * Loads an objects hyerarchy (Tree) or set inforamtion for a single object execution
 */
public class TreeObjectsModule extends AbstractModule {

    public static final String MODULE_PAGE = "TreeObjectsPage";
    private static final String PATH_SYS_FUNCT = "SPAGOBI.CMS_PATHS.SYSTEM_FUNCTIONALITIES_PATH";
    private static final String MODALITY = "MODALITY";
    private static final String SINGLE_OBJECT = "SINGLE_OBJECT";
    private static final String FILTER_TREE = "FILTER_TREE";
    private static final String PATH_SINGLE_OBJECT = "PATH_SINGLE_OBJECT";
    private static final String PATH_SUBTREE = "PATH_SUBTREE";
    private static final String ROLE_SINGLE_OBJECT = "ROLE_SINGLE_OBJECT";
    private static final String HEIGHT_AREA = "HEIGHT_AREA";
	
    SessionContainer sessionContainer = null;
    EMFErrorHandler errorHandler = null;
    
	public void init(SourceBean config) {	}

	
	/**
	 * Based on the modality (setted into portlet preference) loads the xml format of 
	 * the entire/filtered tree and puts it into response, or set into the reponse the necessary
	 * inforamtion for the direct execution of a single object
	 * @param request The request Source Bean
	 * @param response The response Source Bean
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		try{
			debug("service", "enter service method");
			errorHandler = getErrorHandler();
			debug("service", "error handler retrived");
			RequestContainer requestContainer = getRequestContainer();
            sessionContainer = requestContainer.getSessionContainer();
            SessionContainer permanentSession = sessionContainer.getPermanentContainer();
			debug("service", "sessionContainer and permanentContainer retrived");
			String actor = (String)request.getAttribute(SpagoBIConstants.ACTOR);
			debug("service", "using "+actor+" actor");
	        sessionContainer.setAttribute(SpagoBIConstants.ACTOR, actor);
            IEngUserProfile profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
            debug("service", "user profile retrived");
            PortletRequest portReq = PortletUtilities.getPortletRequest();
			PortletPreferences prefs = portReq.getPreferences();
            debug("service", "portlet preferences retrived");
            String modality = (String)prefs.getValue(MODALITY, "");
            debug("service", "using "+modality+" modality");
            if(modality.equalsIgnoreCase(SINGLE_OBJECT)) {
            	singleObjectModalityHandler(request, response, prefs, actor);
            } else if(modality.equalsIgnoreCase(FILTER_TREE)) {
            	filterTreeModalityHandler(request, response);
            } else {
            	defaultModalityHandler(request, response, actor, profile);
            }
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
								"TreeObjectsMOdule", 
								"service", 
								"Error while processing request of service",e);
			EMFUserError emfu = new EMFUserError(EMFErrorSeverity.ERROR, 101);
			errorHandler.addError(emfu); 
		}
	}
	
	
	/**
	 * Set information into response for the execution of a single object
	 * @param request The request SourceBean
	 * @param response The response SourceBean 
	 * @param prefs Portlet Preferences
	 * @param actor Actor's type
	 */
	private void singleObjectModalityHandler(SourceBean request, SourceBean response,
			                                 PortletPreferences prefs, String actor) 
											 throws Exception {
		debug("singleObjectModalityHandler", "enter singleObjectModalityHandler");
		// get from preferences the path of the object
		String path = prefs.getValue(PATH_SINGLE_OBJECT, "");
		debug("singleObjectModalityHandler", "using object path " + path);
		// if path is not set then throw an exception
		if(path.equals("")) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
								"TreeObjectsMOdule", 
								"singleObjectModalityHandler", 
								"Object's path not found");
        	throw new Exception("Path not found");
        }
		// get from preferences the height of the area
		String heightArea = prefs.getValue(HEIGHT_AREA, "");
		debug("singleObjectModalityHandler", "using height of area " + heightArea);
        // set into the reponse the publisher name for object execution
        response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, 
        					  SpagoBIConstants.PUBLISHER_LOOPBACK_SINGLE_OBJECT_EXEC);
        // set into the response the righr inforamtion for loopback
        response.setAttribute(SpagoBIConstants.ACTOR, actor);
        response.setAttribute(SpagoBIConstants.PATH, path);
        // put in session the modality and the actor
        sessionContainer.setAttribute(SpagoBIConstants.MODALITY, 
        		                      SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY);
        // if the height of the area in set put it into the session
        if(!heightArea.equals("")) 
        	sessionContainer.setAttribute(SpagoBIConstants.HEIGHT_OUTPUT_AREA, heightArea);
        debug("singleObjectModalityHandler", "data stored into response");
	}
	
	
	/**
	 * Load a filtered tree starting from a given path (But not yet implemented).
	 * @param request The Request SourceBean
	 * @param response The Response SourceBean
	 */
	private void filterTreeModalityHandler(SourceBean request, SourceBean response) throws SourceBeanException {
		debug("filterTreeModalityHandler", "enter filterTreeModalityHandler");
		// put in session the modality
        sessionContainer.setAttribute(SpagoBIConstants.MODALITY, 
        		                      SpagoBIConstants.FILTER_TREE_MODALITY);
	}
	
	
	/**
	 * Load the xml format of the entire object tree and put it into response
	 * @param request The Spago Request SourceBean
	 * @param response The Spago Response  SourceBean
	 * @param actor Actor type
	 * @param profile Profile of the actor
	 */
	private void defaultModalityHandler(SourceBean request, SourceBean response, 
			                            String actor, IEngUserProfile profile) 
										throws SourceBeanException {
		debug("defaultModalityHandler", "enter defaultModalityHandler");
		SourceBean dataResponseSysFunct = getFunctioanlitiesTree(profile);
		debug("defaultModalityHandler", "SourceBean of the tree retrived: \n " + dataResponseSysFunct.toXML(false));
		response.setAttribute(dataResponseSysFunct);
		response.setAttribute(SpagoBIConstants.ACTOR, actor);
		response.setAttribute(SpagoBIConstants.MODALITY, SpagoBIConstants.ENTIRE_TREE_MODALITY);
		Object objview = request.getAttribute(SpagoBIConstants.OBJECTS_VIEW);
		if(objview!=null) {
			response.setAttribute(SpagoBIConstants.OBJECTS_VIEW, objview);
			debug("defaultModalityHandler", "using tree view");
		}
		Object listPage = request.getAttribute(SpagoBIConstants.LIST_PAGE);
		if(listPage!=null) {
			response.setAttribute(SpagoBIConstants.LIST_PAGE, listPage);
			debug("defaultModalityHandler", "using list view");
		}
		debug("defaultModalityHandler", "end set data into reponse");
        // put in session the modality and the actor
        sessionContainer.setAttribute(SpagoBIConstants.MODALITY, 
        		                      SpagoBIConstants.ENTIRE_TREE_MODALITY);
        debug("defaultModalityHandler", "end set data into session");
	}
	
	
	
	/**
	 * Load the sourceBean of the entire object tree
	 * @param profile User Profile
	 * @return SourceBean of the entire objects tree
	 */
	private SourceBean getFunctioanlitiesTree(IEngUserProfile profile) {
		debug("getFunctioanlitiesTree", "enter getFunctioanlitiesTree");
		SourceBean pathSysFunctSB = (SourceBean)ConfigSingleton.getInstance().getAttribute(PATH_SYS_FUNCT);
	    String pathSysFunct = pathSysFunctSB.getCharacters();
	    debug("getFunctioanlitiesTree", "using intial path" + pathSysFunct);
	    TreeObjectsDAO objDao = new TreeObjectsDAO();
        SourceBean dataResponseSysFunct = objDao.getXmlTreeObjects(pathSysFunct, profile);
        debug("getFunctioanlitiesTree", "objects tree sourceBean loaded: \n" + dataResponseSysFunct.toXML(false));
        return dataResponseSysFunct;
	}
	
	
	
	/**
	 * Trace a debug message into the log
	 * @param method Name of the method to store into the log
	 * @param message Message to store into the log
	 */
	private void debug(String method, String message) {
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, 
							"TreeObjectsMOdule", 
							method, 
        					message);
	}
	
	
}	
	
	