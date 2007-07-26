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

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.Domain;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.drivers.EngineURL;
import it.eng.spagobi.drivers.IEngineDriver;
import it.eng.spagobi.drivers.exceptions.InvalidOperationRequest;
import it.eng.spagobi.engines.InternalEngineIFace;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.Vector;

/**
 * Permits the dynamic creation or modification of a document template.
 */
public class DocumentTemplateBuildModule extends AbstractModule {
	
	protected EMFErrorHandler errorHandler = null;
	protected RequestContainer requestContainer = null;
	protected SessionContainer session = null;
	protected SessionContainer permanentSession = null;
	protected String actor = null;
	
	public void init(SourceBean config) {}
	
	
	/**
	 * Manage all the request in order to exec all the different BIObject template creation phases 
	 * @param request	The request source bean
	 * @param response 	The response Source bean
	 * @throws Exception If an Exception occurred
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		debug("service", "start service method");
		String messageExec = (String) request.getAttribute(SpagoBIConstants.MESSAGEDET);
		debug("service", "using message" + messageExec);
		errorHandler = getErrorHandler();
		requestContainer = this.getRequestContainer();
		session = requestContainer.getSessionContainer();
		permanentSession = session.getPermanentContainer();
		debug("service", "errorHanlder, requestContainer, session, permanentSession retrived ");
        actor = (String) request.getAttribute(SpagoBIConstants.ACTOR);
        
		try {
			if(messageExec == null) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
				SpagoBITracer.critical(AdmintoolsConstants.NAME_MODULE, this.getClass().getName(), 
									  "service", "The execution-message parameter is null");
				throw userError;
			}
			
			if (messageExec.equalsIgnoreCase(SpagoBIConstants.NEW_DOCUMENT_TEMPLATE))  {
				newDocumentTemplateHandler(request, response);
			} else if(messageExec.equalsIgnoreCase(SpagoBIConstants.EDIT_DOCUMENT_TEMPLATE)) {
				editDocumentTemplateHandler(request, response);
			} else {	
		   	    SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, 
		   	    					this.getClass().getName(), 
		   	    		            "service", 
		   	    		            "Illegal request of service");
		   		errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 102)); 
		   	}
	    } catch (EMFUserError e) { 
	    	errorHandler.addError(e); 
	    }
    }

	private void newDocumentTemplateHandler(SourceBean request, SourceBean response) throws Exception {
		debug("newDocumentTemplateHandler", "start method");
		// get the id of the object
		String idStr = (String) request.getAttribute(ObjectsTreeConstants.OBJECT_ID);
		Integer biObjectID = new Integer(idStr);
		BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectById(biObjectID);
		Engine engine = obj.getEngine();
		
		// GET THE TYPE OF ENGINE (INTERNAL / EXTERNAL) AND THE SUITABLE BIOBJECT TYPES
		Domain engineType = null;
		Domain compatibleBiobjType = null;
		engineType = DAOFactory.getDomainDAO().loadDomainById(engine.getEngineTypeId());
		compatibleBiobjType = DAOFactory.getDomainDAO().loadDomainById(engine.getBiobjTypeId());
		String compatibleBiobjTypeCd = compatibleBiobjType.getValueCd();
		String biobjTypeCd = obj.getBiObjectTypeCode();
		
		// CHECK IF THE BIOBJECT IS COMPATIBLE WITH THE TYPES SUITABLE FOR THE ENGINE
		if (!compatibleBiobjTypeCd.equalsIgnoreCase(biobjTypeCd)) {
			// the engine document type and the biobject type are not compatible
			 SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, 
		 				this.getClass().getName(), 
		 				"execute", 
		 				"Engine cannot execute input document type: " +
		 				"the engine " + engine.getName() + " can execute '" + compatibleBiobjTypeCd + "' type documents " +
		 						"while the input document is a '" + biobjTypeCd + "'.");
			Vector params = new Vector();
			params.add(engine.getName());
			params.add(compatibleBiobjTypeCd);
			params.add(biobjTypeCd);
			errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 2002, params));
			return;
		}
				
		// IF THE ENGINE IS EXTERNAL
		if ("EXT".equalsIgnoreCase(engineType.getValueCd())) {
			try {
				// instance the driver class
				String driverClassName = obj.getEngine().getDriverName();
				IEngineDriver aEngineDriver = (IEngineDriver)Class.forName(driverClassName).newInstance();
				EngineURL templateBuildUrl = null;
				try {
					templateBuildUrl = aEngineDriver.getNewDocumentTemplateBuildUrl(obj);
				} catch (InvalidOperationRequest ior) {
					SpagoBITracer.info(SpagoBIConstants.NAME_MODULE, 
			 				this.getClass().getName(), 
			 				"newDocumentTemplateHandler", 
			 				"Engine " + engine.getName() + " cannot build document template");
					Vector params = new Vector();
					params.add(engine.getName());
					errorHandler.addError(new EMFUserError(EMFErrorSeverity.INFORMATION, "1076", params));
					response.setAttribute(SpagoBIConstants.ACTOR, actor);
					response.setAttribute(ObjectsTreeConstants.OBJECT_ID, idStr);
					response.setAttribute("biobject", obj);
					return;
				}
			    // set into the reponse the url to be invoked	
				response.setAttribute(ObjectsTreeConstants.CALL_URL, templateBuildUrl);
				response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "TemplateBuildPublisher");
				response.setAttribute(SpagoBIConstants.ACTOR, actor);
				response.setAttribute("biobject", obj);
				response.setAttribute("operation", "newDocumentTemplate");
				
			} catch (Exception e) {
				 SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, 
						 				this.getClass().getName(), 
						 				"newDocumentTemplateHandler", 
						 				"Error retrieving template build url", e);
			   	 errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 100)); 
			}	
			
		// IF THE ENGINE IS INTERNAL	
		} else {
			
			String className = engine.getClassName();
			debug("execute", "Try instantiating class " + className + " for internal engine " + engine.getName() + "...");
			InternalEngineIFace internalEngine = null;
			// tries to instantiate the class for the internal engine
			try {
				if (className == null && className.trim().equals("")) throw new ClassNotFoundException();
				internalEngine = (InternalEngineIFace) Class.forName(className).newInstance();
			} catch (ClassNotFoundException cnfe) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, 
			 				this.getClass().getName(), 
			 				"execute", 
			 				"The class ['" + className + "'] for internal engine " + engine.getName() + " was not found.", cnfe);
				Vector params = new Vector();
				params.add(className);
				params.add(engine.getName());
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 2001, params));
				return;
			} catch (Exception e) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, 
		 				this.getClass().getName(), 
		 				"newDocumentTemplateHandler", 
		 				"Error while instantiating class " + className, e);
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 100));
				return;
			}
			
			debug("execute", "Class " + className + " instantiated successfully. Now engine's execution starts.");
			
			// starts engine's execution
			try {
				internalEngine.handleNewDocumentTemplateCreation(requestContainer, obj, response);
			} catch (InvalidOperationRequest ior) {
				SpagoBITracer.info(SpagoBIConstants.NAME_MODULE, 
		 				this.getClass().getName(), 
		 				"newDocumentTemplateHandler", 
		 				"Engine " + engine.getName() + " cannot build document template");
				Vector params = new Vector();
				params.add(engine.getName());
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.INFORMATION, "1076", params));
				response.setAttribute(SpagoBIConstants.ACTOR, actor);
				response.setAttribute(ObjectsTreeConstants.OBJECT_ID, idStr);
				return;
			} catch (Exception e) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, 
		 				this.getClass().getName(), 
		 				"execute", 
		 				"Error while engine execution", e);
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 100));
			}
		}
		debug("newDocumentTemplateHandler", "end method");
	}
	
	private void editDocumentTemplateHandler(SourceBean request, SourceBean response) throws Exception {
		debug("editDocumentTemplateHandler", "start method");
		// get the id of the object
		String idStr = (String) request.getAttribute(ObjectsTreeConstants.OBJECT_ID);
		Integer biObjectID = new Integer(idStr);
		BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(biObjectID);
		Engine engine = obj.getEngine();
		
		// GET THE TYPE OF ENGINE (INTERNAL / EXTERNAL) AND THE SUITABLE BIOBJECT TYPES
		Domain engineType = null;
		Domain compatibleBiobjType = null;
		engineType = DAOFactory.getDomainDAO().loadDomainById(engine.getEngineTypeId());
		compatibleBiobjType = DAOFactory.getDomainDAO().loadDomainById(engine.getBiobjTypeId());
		String compatibleBiobjTypeCd = compatibleBiobjType.getValueCd();
		String biobjTypeCd = obj.getBiObjectTypeCode();
		
		// CHECK IF THE BIOBJECT IS COMPATIBLE WITH THE TYPES SUITABLE FOR THE ENGINE
		if (!compatibleBiobjTypeCd.equalsIgnoreCase(biobjTypeCd)) {
			// the engine document type and the biobject type are not compatible
			 SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, 
		 				this.getClass().getName(), 
		 				"execute", 
		 				"Engine cannot execute input document type: " +
		 				"the engine " + engine.getName() + " can execute '" + compatibleBiobjTypeCd + "' type documents " +
		 						"while the input document is a '" + biobjTypeCd + "'.");
			Vector params = new Vector();
			params.add(engine.getName());
			params.add(compatibleBiobjTypeCd);
			params.add(biobjTypeCd);
			errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 2002, params));
			return;
		}
		
		// IF THE ENGINE IS EXTERNAL
		if ("EXT".equalsIgnoreCase(engineType.getValueCd())) {
			try {
				// instance the driver class
				String driverClassName = obj.getEngine().getDriverName();
				IEngineDriver aEngineDriver = (IEngineDriver)Class.forName(driverClassName).newInstance();
				EngineURL templateBuildUrl = null;
				try {
					templateBuildUrl = aEngineDriver.getEditDocumentTemplateBuildUrl(obj);
				} catch (InvalidOperationRequest ior) {
					SpagoBITracer.info(SpagoBIConstants.NAME_MODULE, 
			 				this.getClass().getName(), 
			 				"newDocumentTemplateHandler", 
			 				"Engine " + engine.getName() + " cannot build document template");
					Vector params = new Vector();
					params.add(engine.getName());
					errorHandler.addError(new EMFUserError(EMFErrorSeverity.INFORMATION, "1076", params));
					response.setAttribute(SpagoBIConstants.ACTOR, actor);
					response.setAttribute(ObjectsTreeConstants.OBJECT_ID, idStr);
					return;
				}
			    // set into the reponse the url to be invoked	
				response.setAttribute(ObjectsTreeConstants.CALL_URL, templateBuildUrl);
				response.setAttribute(SpagoBIConstants.PUBLISHER_NAME, "TemplateBuildPublisher");
				response.setAttribute(SpagoBIConstants.ACTOR, actor);
				response.setAttribute("biobject", obj);
				response.setAttribute("operation", "newDocumentTemplate");
				
			} catch (Exception e) {
				 SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, 
						 				this.getClass().getName(), 
						 				"newDocumentTemplateHandler", 
						 				"Error retrieving template build url", e);
			   	 errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 100)); 
			}	
			
		// IF THE ENGINE IS INTERNAL	
		} else {
			
			String className = engine.getClassName();
			debug("execute", "Try instantiating class " + className + " for internal engine " + engine.getName() + "...");
			InternalEngineIFace internalEngine = null;
			// tries to instantiate the class for the internal engine
			try {
				if (className == null && className.trim().equals("")) throw new ClassNotFoundException();
				internalEngine = (InternalEngineIFace) Class.forName(className).newInstance();
			} catch (ClassNotFoundException cnfe) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, 
			 				this.getClass().getName(), 
			 				"execute", 
			 				"The class ['" + className + "'] for internal engine " + engine.getName() + " was not found.", cnfe);
				Vector params = new Vector();
				params.add(className);
				params.add(engine.getName());
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 2001, params));
				return;
			} catch (Exception e) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, 
		 				this.getClass().getName(), 
		 				"newDocumentTemplateHandler", 
		 				"Error while instantiating class " + className, e);
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 100));
				return;
			}
			
			debug("execute", "Class " + className + " instantiated successfully. Now engine's execution starts.");
			
			// starts engine's execution
			try {
				internalEngine.handleDocumentTemplateEdit(requestContainer, obj, response);
			} catch (InvalidOperationRequest ior) {
				SpagoBITracer.info(SpagoBIConstants.NAME_MODULE, 
		 				this.getClass().getName(), 
		 				"newDocumentTemplateHandler", 
		 				"Engine " + engine.getName() + " cannot build document template");
				Vector params = new Vector();
				params.add(engine.getName());
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.INFORMATION, "1076", params));
				response.setAttribute(SpagoBIConstants.ACTOR, actor);
				response.setAttribute(ObjectsTreeConstants.OBJECT_ID, idStr);
				return;
			} catch (Exception e) {
				SpagoBITracer.critical(SpagoBIConstants.NAME_MODULE, 
		 				this.getClass().getName(), 
		 				"execute", 
		 				"Error while engine execution", e);
				errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 100));
			}
		}
		debug("editDocumentTemplateHandler", "end method");
	}

	/**
	 * Trace a debug message into the log
	 * @param method Name of the method to store into the log
	 * @param message Message to store into the log
	 */
	private void debug(String method, String message) {
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, 
							this.getClass().getName(), 
							method, 
        					message);
	}
	
}
