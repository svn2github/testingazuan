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
package it.eng.spagobi.booklets.modules;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.booklets.constants.BookletsConstants;
import it.eng.spagobi.booklets.dao.BookletsCmsDaoImpl;
import it.eng.spagobi.booklets.dao.IBookletsCmsDao;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

/**
 * This class implements a module which  handles pamphlets collaboration.
 */
public class BookletsCollaborationModule extends AbstractModule {
	

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
			} else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_OPEN_NOTE_EDITOR)) {
				openNoteEditorHandler(request, response);
			} else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_SAVE_NOTE)) {
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

	
	
	private void openNoteEditorHandler(SourceBean request, SourceBean response) {
		try{
			// recover task instance and variables
			String activityKey = (String)request.getAttribute("activityKey");
			JbpmConfiguration jbpmConfiguration = JbpmConfiguration.getInstance();
			JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
			TaskInstance taskInstance = jbpmContext.getTaskInstance(new Long(activityKey).longValue());
			String index = (String)taskInstance.getVariable(BookletsConstants.BOOKLET_PART_INDEX);
			ContextInstance contextInstance = taskInstance.getContextInstance();
			String pathConfBook = (String)contextInstance.getVariable(BookletsConstants.PATH_BOOKLET_CONF);
			
			// recover from cms images and notes
		    String notes = recoverNotes(pathConfBook, index);
		    Map imageurl = recoverImageUrls(pathConfBook, index);
		    
			// put attributes into response
			response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletEditNotesTemplatePart");
			response.setAttribute(BookletsConstants.BOOKLET_PART_INDEX, index);
			response.setAttribute(BookletsConstants.PATH_BOOKLET_CONF, pathConfBook);
			response.setAttribute(SpagoBIConstants.ACTIVITYKEY, activityKey);
			response.setAttribute("mapImageUrls", imageurl);
			response.setAttribute("notes", notes);
			
			
		} catch(Exception e){
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
		                        "saveNoteHandler","Error while saving notes", e);
		}
	    
	}
	
	
	
	private String recoverNotes(String pathConfBook, String index) {
		IBookletsCmsDao bookdao = new BookletsCmsDaoImpl();
	    byte[] notesByte = 	bookdao.getNotesTemplatePart(pathConfBook, index);
	    String notes = new String(notesByte);
	    return notes;
	}
	
	
	
	private Map recoverImageUrls(String pathConfBook, String index) throws Exception {
		IBookletsCmsDao bookdao = new BookletsCmsDaoImpl();
		Map images = bookdao.getImagesOfTemplatePart(pathConfBook, index);
		 // get temp directory for the pamphlet module
	    ConfigSingleton configSing = ConfigSingleton.getInstance();
		SourceBean pathTmpFoldSB = (SourceBean)configSing.getAttribute("BOOKLETS.PATH_TMP_FOLDER");
		String pathTmpFold = (String)pathTmpFoldSB.getAttribute("path");
		File tempDir = new File(pathTmpFold); 
		tempDir.mkdirs();
		// for each image store into the temp directory and save the url useful to recover it into the map
	    Map imageurl = new HashMap();
	    Iterator iterImgs = images.keySet().iterator();
	    while(iterImgs.hasNext()){
	    	String logicalName = (String)iterImgs.next();
	    	String logicalNameForStoring = pathConfBook.replace('/', '_') + logicalName + ".jpg";
	    	byte[] content = (byte[])images.get(logicalName);
	    	File img = new File(tempDir, logicalNameForStoring);
	    	FileOutputStream fos = new FileOutputStream(img);
	    	fos.write(content);
	    	fos.flush();
	    	fos.close();
	    	// the url to recover the image is a spagobi servlet url
	    	String contextAddress = GeneralUtilities.getSpagoBiContextAddress();
	    	String recoverUrl = contextAddress + "/BookletsImageService?task=getTemplateImage&pathimg=" + 
	    	pathTmpFold + "/" + logicalNameForStoring;
	    	imageurl.put(logicalName, recoverUrl); 
	    }
	   return imageurl;
	}
	
	
	
	
	private void initializeHandler(SourceBean response) throws EMFUserError {
		try{
			/*
			PortletRequest portletRequest = PortletUtilities.getPortletRequest(); 
			String remoteUser = portletRequest.getRemoteUser();
			Principal principal = portletRequest.getUserPrincipal();
			String engUserProfileFactoryClass =  ((SourceBean)ConfigSingleton.getInstance().getAttribute("SPAGOBI.SECURITY.USER-PROFILE-FACTORY-CLASS")).getCharacters();
			IUserProfileFactory engUserProfileFactory = (IUserProfileFactory)Class.forName(engUserProfileFactoryClass).newInstance();
			IEngUserProfile userProfile = engUserProfileFactory.createUserProfile(portletRequest, principal);
			AnonymousCMSUserProfile prof = new AnonymousCMSUserProfile(userProfile.getUserUniqueIdentifier().toString());
	    	SessionContainer session = getRequestContainer().getSessionContainer();
	    	session.setAttribute(IEngUserProfile.ENG_USER_PROFILE, prof);
	    	response.setAttribute(BookletsConstants.PUBLISHER_NAME, "PamphletWorkList");
	    	*/
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
					            "initializerHandler","Error while building profile for logged user", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
		
	
	private void saveNoteHandler(SourceBean request, SourceBean response) {
		try{
			String indexPart = (String)request.getAttribute(BookletsConstants.BOOKLET_PART_INDEX);
			String pathBookConf = (String)request.getAttribute(BookletsConstants.PATH_BOOKLET_CONF);
			String noteSent = (String)request.getAttribute("notes");
			String activityKey = (String)request.getAttribute(SpagoBIConstants.ACTIVITYKEY);
			IBookletsCmsDao pampdao = new BookletsCmsDaoImpl();
			pampdao.storeNote(pathBookConf, indexPart, noteSent.getBytes());
			// recover from cms images and notes
		    String notes = recoverNotes(pathBookConf, indexPart);
		    Map imageurl = recoverImageUrls(pathBookConf, indexPart);
			// put attributes into response
			response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletEditNotesTemplatePart");
			response.setAttribute(BookletsConstants.BOOKLET_PART_INDEX, indexPart);
			response.setAttribute(BookletsConstants.PATH_BOOKLET_CONF, pathBookConf);
			response.setAttribute(SpagoBIConstants.ACTIVITYKEY, activityKey);
			response.setAttribute("mapImageUrls", imageurl);
			response.setAttribute("notes", notes);
			
		} catch(Exception e){
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
		                        "saveNoteHandler","Error while saving notes", e);
		}
	    
	}
	
	
	
	
}
