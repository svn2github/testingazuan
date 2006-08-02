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
package it.eng.spagobi.pamphlets.modules;

import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.pamphlets.constants.PamphletsConstants;
import it.eng.spagobi.pamphlets.dao.IPamphletsCmsDao;
import it.eng.spagobi.pamphlets.dao.PamphletsCmsDaoImpl;
import it.eng.spagobi.security.AnonymousCMSUserProfile;
import it.eng.spagobi.security.IUserProfileFactory;
import it.eng.spagobi.utilities.PortletUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.security.Principal;

import javax.portlet.PortletRequest;

/**
 * This class implements a module which  handles pamphlets collaboration.
 */
public class PamphletsCollaborationModule extends AbstractModule {
	

	public void init(SourceBean config) {
	}

	/**
	 * Reads the operation asked by the user and calls the right operation handler
	 * @param request The Source Bean containing all request parameters
	 * @param response The Source Bean containing all response parameters
	 * @throws exception If an exception occurs
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		EMFErrorHandler errorHandler = getErrorHandler();
		String operation = (String) request.getAttribute(SpagoBIConstants.OPERATION);
		try{
			if((operation==null)||(operation.trim().equals(""))) {
				SpagoBITracer.info(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
						            "service", "The operation parameter is null");
				initializeHandler(response);
			} else if (operation.equalsIgnoreCase(PamphletsConstants.OPERATION_SAVE_NOTE)) {
				saveNoteHandler(request, response);
			} 
		} catch (EMFUserError emfue) {
			errorHandler.addError(emfue);
		} catch (Exception ex) {
			EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, ex);
			errorHandler.addError(internalError);
			return;
		}
	}

	
	
	
	private void initializeHandler(SourceBean response) throws EMFUserError {
		try{
			PortletRequest portletRequest = PortletUtilities.getPortletRequest(); 
			String remoteUser = portletRequest.getRemoteUser();
			Principal principal = portletRequest.getUserPrincipal();
			String engUserProfileFactoryClass =  ((SourceBean)ConfigSingleton.getInstance().getAttribute("SPAGOBI.SECURITY.USER-PROFILE-FACTORY-CLASS")).getCharacters();
			IUserProfileFactory engUserProfileFactory = (IUserProfileFactory)Class.forName(engUserProfileFactoryClass).newInstance();
			IEngUserProfile userProfile = engUserProfileFactory.createUserProfile(portletRequest, principal);
			AnonymousCMSUserProfile prof = new AnonymousCMSUserProfile(userProfile.getUserUniqueIdentifier().toString());
	    	SessionContainer session = getRequestContainer().getSessionContainer();
	    	session.setAttribute(IEngUserProfile.ENG_USER_PROFILE, prof);
	    	response.setAttribute(PamphletsConstants.PUBLISHER_NAME, "PamphletWorkList");
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "initializerHandler","Error while building profile for logged user", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
		
	private void saveNoteHandler(SourceBean request, SourceBean response) {
		try{
			String indexPart = (String)request.getAttribute(PamphletsConstants.PAMPHLET_PART_INDEX);
			String pathPamp = (String)request.getAttribute(PamphletsConstants.PATH_PAMPHLET);
			String notes = (String)request.getAttribute("notes");
			String activityKey = (String)request.getAttribute("ActivityKey");
			IPamphletsCmsDao pampdao = new PamphletsCmsDaoImpl();
			//byte[] noteBytes = pampdao.getNotesTemplatePart(pathPamp, indexPart);
            //String notesStr = new String(noteBytes);
			//notesStr = notesStr + "\n\n" + newNote;
			pampdao.storeNote(pathPamp, indexPart, notes.getBytes());
			response.setAttribute(PamphletsConstants.PUBLISHER_NAME, "PamphletEditNotesTemplatePart");
			response.setAttribute(PamphletsConstants.PAMPHLET_PART_INDEX, indexPart);
			response.setAttribute(PamphletsConstants.PATH_PAMPHLET, pathPamp);
			response.setAttribute("ActivityKey", activityKey);
		} catch(Exception e){
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
		                        "saveNoteHandler","Error while saving notes", e);
		}
	    
	}
	
	
	
	
}
