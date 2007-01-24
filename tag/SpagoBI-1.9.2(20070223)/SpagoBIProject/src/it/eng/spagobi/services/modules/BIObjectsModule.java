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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObject.BIObjectSnapshot;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectCMSDAO;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.scheduler.SchedulerUtilities;
import it.eng.spagobi.utilities.PortletUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

/**
 * Presentation page for the BIObjects.
 * 
 * @author zerbetto
 *
 */

public class BIObjectsModule extends AbstractModule {
	
	public static final String MODULE_PAGE = "BIObjectsPage";
    public static final String MODALITY = "MODALITY";
    public static final String SINGLE_OBJECT = "SINGLE_OBJECT";
    public static final String FILTER_TREE = "FILTER_TREE";
    public static final String LABEL_SINGLE_OBJECT = "LABEL_SINGLE_OBJECT";
    public static final String PARAMETERS_SINGLE_OBJECT = "PARAMETERS_SINGLE_OBJECT";    
    public static final String PATH_SUBTREE = "PATH_SUBTREE";
    public static final String HEIGHT_AREA = "HEIGHT_AREA";
    public static final String SNAPSHOT_NAME = "SNAPSHOT_NAME";
    public static final String SNAPSHOT_HISTORY = "SNAPSHOT_HISTORY";
	
    SessionContainer sessionContainer = null;
    EMFErrorHandler errorHandler = null;
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		
		try {
			debug("service", "enter service method");
			errorHandler = getErrorHandler();
			debug("service", "error handler retrived");
			RequestContainer requestContainer = getRequestContainer();
            sessionContainer = requestContainer.getSessionContainer();
			debug("service", "sessionContainer and permanentContainer retrived");
			String actor = (String)request.getAttribute(SpagoBIConstants.ACTOR);
			debug("service", "using "+actor+" actor");
	        sessionContainer.setAttribute(SpagoBIConstants.ACTOR, actor);
            debug("service", "user profile retrived");
            PortletRequest portReq = PortletUtilities.getPortletRequest();
			PortletPreferences prefs = portReq.getPreferences();
            debug("service", "portlet preferences retrived");
            
            String modality = (String)prefs.getValue(MODALITY, "");
            debug("service", "using "+modality+" modality");
            if (modality != null) {
                if (modality.equalsIgnoreCase(SINGLE_OBJECT)) {
                	singleObjectModalityHandler(request, response, prefs, actor);
                } else if (modality.equalsIgnoreCase(FILTER_TREE)) {
                	String initialPath = (String) prefs.getValue(TreeObjectsModule.PATH_SUBTREE, "");
                	treeModalityHandler(request, response, prefs, actor, initialPath);
                } else {
                	treeModalityHandler(request, response, prefs, actor, null);
                }
            } else {
            	treeModalityHandler(request, response, prefs, actor, null);
            }
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
								"BIObjectsModule", 
								"service", 
								"Error while processing request of service",e);
			EMFUserError emfu = new EMFUserError(EMFErrorSeverity.ERROR, 101);
			errorHandler.addError(emfu); 
		}
		
	}
	
		
	/**
	 * Set information into response for entering a 
	 * 
	 * @param request
	 *            The Spago Request SourceBean
	 * @param response
	 *            The Spago Response SourceBean
	 * @param actor
	 *            Actor type
	 */
	private void treeModalityHandler(SourceBean request,
			SourceBean response, PortletPreferences prefs, String actor, 
			String initialPath) throws SourceBeanException {

		String objectsView = null;
		String operation = (String) request.getAttribute(SpagoBIConstants.OPERATION);
		if (operation != null && operation.equals(SpagoBIConstants.FUNCTIONALITIES_OPERATION)) {
			objectsView = SpagoBIConstants.VIEW_OBJECTS_AS_TREE;
		} else {
			objectsView = (String) request.getAttribute(SpagoBIConstants.OBJECTS_VIEW);
			if (objectsView == null) {
				// finds objects view modality from portlet preferences
				objectsView = (String) prefs.getValue(
						SpagoBIConstants.OBJECTS_VIEW, "");
			}
			// default value in case it is not specified or in case the value is not valid
			if (objectsView == null
					|| (!objectsView
							.equalsIgnoreCase(SpagoBIConstants.VIEW_OBJECTS_AS_LIST) && !objectsView
							.equalsIgnoreCase(SpagoBIConstants.VIEW_OBJECTS_AS_TREE)))
				objectsView = SpagoBIConstants.VIEW_OBJECTS_AS_TREE;
		}

		if (initialPath != null && !initialPath.trim().equals("")) 
			response.setAttribute(TreeObjectsModule.PATH_SUBTREE, initialPath);
		
		response.setAttribute(SpagoBIConstants.OBJECTS_VIEW, objectsView);
		response.setAttribute(SpagoBIConstants.ACTOR, actor);

	}
		

	
	/**
	 * Set information into response for the execution of a single object
	 * 
	 * @param request
	 *            The request SourceBean
	 * @param response
	 *            The response SourceBean
	 * @param prefs
	 *            Portlet Preferences
	 * @param actor
	 *            Actor's type
	 */
	private void singleObjectModalityHandler(SourceBean request, SourceBean response,
			                                 PortletPreferences prefs, String actor) 
											 throws Exception {
		debug("singleObjectModalityHandler", "enter singleObjectModalityHandler");
		// get from preferences the label of the object
		String label = prefs.getValue(LABEL_SINGLE_OBJECT, "");
		debug("singleObjectModalityHandler", "using object label " + label);
		// if label is not set then throw an exception
		if(label == null || label.trim().equals("")) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
								"BIObjectsModule", 
								"singleObjectModalityHandler",  
								"Object's label not set");
        	throw new Exception("Label not set");
        }
		// get from preferences the parameters used by the object during execution
		String parameters = prefs.getValue(PARAMETERS_SINGLE_OBJECT, "");
		debug("singleObjectModalityHandler", "using parameters " + parameters);
		// get from preferences the height of the area
		String heightArea = prefs.getValue(HEIGHT_AREA, "");
		debug("singleObjectModalityHandler", "using height of area " + heightArea);
		// get from preferences the snapshot name
		String snapName = prefs.getValue(SNAPSHOT_NAME, "");
		debug("singleObjectModalityHandler", "using snapshot name " + snapName);
		// get from preferences the snapshot history
		String snapHistStr = prefs.getValue(SNAPSHOT_HISTORY, "0");
		debug("singleObjectModalityHandler", "using snapshot history " + snapHistStr);
		
		// load biobject
		BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectByLabel(label);
		// if the height of the area is set put it into the session
        if (heightArea != null && !heightArea.trim().equals("")) 
        	sessionContainer.setAttribute(SpagoBIConstants.HEIGHT_OUTPUT_AREA, heightArea);
        // put in session the modality and the actor
        sessionContainer.setAttribute(SpagoBIConstants.MODALITY, 
        		                      SpagoBIConstants.SINGLE_OBJECT_EXECUTION_MODALITY);
		
        if(!snapName.trim().equalsIgnoreCase("")) {
        	// get the snapshot history
        	int snapHist = 0;
        	try{
        		Integer snapHistI = new Integer(snapHistStr);
        		snapHist = snapHistI.intValue();
        	} catch (Exception e) {
        		SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
									"singleObjectModalityHandler", 
									"Snapshot history preference value is not a valid integer number, using default 0");
        		snapHist = 0;
        	}
        	// get the all the snapshots of the objects
        	List allsnapshots = null;
        	try {
    			IBIObjectCMSDAO biObjCmsDAO = DAOFactory.getBIObjectCMSDAO();
    			String objectPath = obj.getPath();
    			allsnapshots =  biObjCmsDAO.getSnapshots(objectPath);
    		} catch (Exception e) {
    			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
                        			"singleObjectModalityHandler", "Error while retriving the snapshot list", e);
    			throw new Exception("Error while retriving the snapshot list", e);
    		}
    		// recover only the snapshot with the name requested
    		BIObject.BIObjectSnapshot snap = SchedulerUtilities.getNamedHistorySnapshot(allsnapshots,snapName,snapHist);
    		response.setAttribute(SpagoBIConstants.SNAPSHOT_PATH, snap.getPath());
    		// set into the reponse the publisher name for object execution
            response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "loopbackSnapshotExecution");
        } else {
        	// set into the response the right information for loopback
            response.setAttribute(SpagoBIConstants.ACTOR, actor);
            response.setAttribute(ObjectsTreeConstants.OBJECT_ID, obj.getId().toString());
    		// set into the reponse the publisher name for object execution
            response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, 
            					  SpagoBIConstants.PUBLISHER_LOOPBACK_SINGLE_OBJECT_EXEC);
            // if the parameters is set put it into the session
            if (parameters != null && !parameters.trim().equals("")) 
            	sessionContainer.setAttribute(SpagoBIConstants.PARAMETERS, parameters);
        }
        
        debug("singleObjectModalityHandler", "end method");
	}
	
	
	/**
	 * Trace a debug message into the log
	 * @param method Name of the method to store into the log
	 * @param message Message to store into the log
	 */
	private void debug(String method, String message) {
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, 
							"BIObjectsModule", 
							method, 
        					message);
	}


}
