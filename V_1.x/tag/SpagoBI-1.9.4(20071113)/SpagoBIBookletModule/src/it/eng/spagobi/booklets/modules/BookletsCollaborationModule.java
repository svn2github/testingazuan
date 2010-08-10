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
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.Domain;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.bo.dao.IDomainDAO;
import it.eng.spagobi.bo.dao.IEngineDAO;
import it.eng.spagobi.bo.dao.audit.AuditManager;
import it.eng.spagobi.booklets.constants.BookletsConstants;
import it.eng.spagobi.booklets.dao.BookletsCmsDaoImpl;
import it.eng.spagobi.booklets.dao.IBookletsCmsDao;
import it.eng.spagobi.booklets.exceptions.OpenOfficeConnectionException;
import it.eng.spagobi.booklets.profile.AnonymousWorkflowProfile;
import it.eng.spagobi.booklets.utils.BookletServiceUtils;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.PortletUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.db.GraphSession;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
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
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
						            "service", "The operation parameter is null");
				throw new Exception("The operation parameter is null");
			} else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_OPEN_NOTE_EDITOR)) {
				openNoteEditorHandler(request, response);
			} else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_SAVE_NOTE)) {
				saveNoteHandler(request, response);
			} else if (operation.equalsIgnoreCase(BookletsConstants.OPERATION_APPROVE_PRESENTATION)) {
				approveHandler(request, response);
			} else if(operation.equalsIgnoreCase(BookletsConstants.OPERATION_RUN_NEW_COLLABORATION)) {
				runCollaborationHandler(request, response);
			} else if(operation.equalsIgnoreCase(BookletsConstants.OPERATION_DELETE_PRESENTATION_VERSION)) {
				deletePresVerHandler(request, response);
			} else if(operation.equalsIgnoreCase(BookletsConstants.OPERATION_PREPARE_PUBLISH_PRESENTATION_PAGE)) {
				preparePublishPageHandler(request, response);
			} else if(operation.equalsIgnoreCase(BookletsConstants.OPERATION_PUBLISH_PRESENTATION)) {
				publishHandler(request, response);
			} 
			
		} catch (EMFUserError emfue) {
			errorHandler.addError(emfue);
		} catch (Exception ex) {
			EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, ex);
			errorHandler.addError(internalError);
			return;
		}
	}

	
	
	private void publishHandler(SourceBean request, SourceBean response) {
		try{
			String label = (String)request.getAttribute("label");
			if(label==null) label = "";
			String description = (String)request.getAttribute("description");
			if(description==null) description = "";
			String name = (String)request.getAttribute("name");
			if(name==null) name = "";
			String state = (String)request.getAttribute("state");
			Integer stateId = new Integer(57);
			String stateCode = "REL";
			if(state==null) {
				state = "";			
			} else {
				StringTokenizer tokenState = new StringTokenizer(state, ",");
				String stateIdStr = tokenState.nextToken();
				stateId = new Integer(stateIdStr);
				stateCode = tokenState.nextToken();
			}
			
			
			
			boolean visible = true;
			String visibleStr = (String)request.getAttribute("visible");
			if(visibleStr != null && visibleStr.equalsIgnoreCase("0")) visible = false;
			
			String pathConfBook = (String)request.getAttribute(BookletsConstants.PATH_BOOKLET_CONF);
			String presVerName = (String)request.getAttribute(BookletsConstants.BOOKLET_PRESENTATION_VERSION_NAME);
			List functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(false);
			EMFErrorHandler errorHandler = getResponseContainer().getErrorHandler();
			if(!errorHandler.isOK()){
				if(GeneralUtilities.isErrorHandlerContainingOnlyValidationError(errorHandler)) {
					response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
					response.setAttribute(BookletsConstants.PUBLISHER_NAME, "publishPresentation");
					response.setAttribute("label", label);
					response.setAttribute("name", name);
					response.setAttribute("description", description);
					response.setAttribute(BookletsConstants.PATH_BOOKLET_CONF, pathConfBook);
					response.setAttribute(BookletsConstants.BOOKLET_PRESENTATION_VERSION_NAME, presVerName);
					return;
				}
			} else {
				// recover office document sbidomains
				IDomainDAO domainDAO = DAOFactory.getDomainDAO();
				Domain officeDocDom = domainDAO.loadDomainByCodeAndValue("BIOBJ_TYPE", "OFFICE_DOC");
				// recover development sbidomains
				Domain devDom = domainDAO.loadDomainByCodeAndValue("STATE", "DEV");
				// recover engine
				IEngineDAO engineDAO = DAOFactory.getEngineDAO();
				List engines = engineDAO.loadAllEnginesForBIObjectType(officeDocDom.getValueCd());
				Engine engine = (Engine)engines.get(0);
				// load the template
				UploadedFile uploadedFile = new UploadedFile();
				IBookletsCmsDao bookletsCmsDao = new BookletsCmsDaoImpl();
				byte[] tempCont = bookletsCmsDao.getPresentationVersionContent(pathConfBook, presVerName);
				String bookName = bookletsCmsDao.getBookletName(pathConfBook);
				uploadedFile.setFieldNameInForm("template");
				uploadedFile.setFileName(bookName + ".ppt");
				uploadedFile.setSizeInBytes(tempCont.length);
				uploadedFile.setFileContent(tempCont);
				// load all functionality
				List storeInFunctionalities = new ArrayList();
				List functIds = request.getAttributeAsList("FUNCT_ID");
				Iterator iterFunctIds = functIds.iterator();
				while(iterFunctIds.hasNext()) {
					String functIdStr = (String)iterFunctIds.next();
					Integer functId = new Integer(functIdStr);
					storeInFunctionalities.add(functId);
				}
				// create biobject
				BIObject biobj = new BIObject();
				biobj.setDescription(description);
				biobj.setLabel(label);
				biobj.setName(name);
				biobj.setEncrypt(new Integer(0));
				biobj.setEngine(engine);
				biobj.setRelName("");
				biobj.setBiObjectTypeCode(officeDocDom.getValueCd());
				biobj.setBiObjectTypeID(officeDocDom.getValueId());
				biobj.setStateCode(devDom.getValueCd());
				biobj.setStateID(devDom.getValueId());
				biobj.setVisible(new Integer(0));
				biobj.setTemplate(uploadedFile);
				biobj.setFunctionalities(storeInFunctionalities);
				biobj.setStateCode(stateCode);
				biobj.setStateID(stateId);
				biobj.setVisible((visible? new Integer(1): new Integer(0)));
				
				IBIObjectDAO objectDAO = DAOFactory.getBIObjectDAO();
				objectDAO.insertBIObject(biobj);
				// put data into response
				response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
				response.setAttribute(BookletsConstants.PUBLISHER_NAME, "publishPresentation");
				response.setAttribute("label", "");
				response.setAttribute("name", "");
				response.setAttribute("description", "");
				
				// load list of states and engines
				IDomainDAO domaindao = DAOFactory.getDomainDAO();
				List states = domaindao.loadListDomainsByType("STATE");
			    response.setAttribute(BookletsConstants.BOOKLET_PRESENTATION_LIST_STATES, states);
				
				response.setAttribute("PublishMessage", PortletUtilities.getMessage("book.presPublished", "component_booklets_messages"));
				response.setAttribute(BookletsConstants.PATH_BOOKLET_CONF, pathConfBook);
				response.setAttribute(BookletsConstants.BOOKLET_PRESENTATION_VERSION_NAME, presVerName);
				
				
			}
		} catch(Exception e){
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
		                        "publishHandler","Error while publishing presentation", e);
		}
	    
	}
	
	
	
	
	private void deletePresVerHandler(SourceBean request, SourceBean response) throws Exception {
		SpagoBITracer.debug(BookletsConstants.NAME_MODULE, this.getClass().getName(),
	            			"deletePresVerHandler", "method execution start");
		String pathBookConf = null;
		List presVersions = null;
		IBookletsCmsDao bookDao = null;
		try{
			pathBookConf = (String)request.getAttribute(BookletsConstants.PATH_BOOKLET_CONF);
			String verName = (String)request.getAttribute(BookletsConstants.BOOKLET_PRESENTATION_VERSION_NAME);
			bookDao = new BookletsCmsDaoImpl();
			bookDao.deletePresentationVersion(pathBookConf, verName);
		} catch (Exception e) {
			SpagoBITracer.major(BookletsConstants.NAME_MODULE, this.getClass().getName(), 
                    			"deletePresVerHandler", "error while deleting version " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		try{
			presVersions = bookDao.getPresentationVersions(pathBookConf);
			SpagoBITracer.debug(BookletsConstants.NAME_MODULE, this.getClass().getName(),
		            			"deletePresVerHandler", "Version list retrived " + presVersions);
		} catch (Exception e) {
			SpagoBITracer.major(BookletsConstants.NAME_MODULE, this.getClass().getName(), 
		                        "deletePresVerHandler", "error while getting version list " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		try{
			response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletsPresentationVersion");
			response.setAttribute(BookletsConstants.BOOKLET_PRESENTATION_VERSIONS, presVersions);
			response.setAttribute(BookletsConstants.PATH_BOOKLET_CONF, pathBookConf);
		} catch (Exception e) {
			SpagoBITracer.major(BookletsConstants.NAME_MODULE, this.getClass().getName(), 
					            "deletePresVerHandler", "error while setting response attribute " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		SpagoBITracer.debug(BookletsConstants.NAME_MODULE, this.getClass().getName(),
	                        "deletePresVerHandler", "end method execution");

	}
	
	
	
	
	private void preparePublishPageHandler(SourceBean request, SourceBean response) {
		try{
			String pathConfBook = (String)request.getAttribute(BookletsConstants.PATH_BOOKLET_CONF);
			String presVerName = (String)request.getAttribute(BookletsConstants.BOOKLET_PRESENTATION_VERSION_NAME);
			List functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(false);
			response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
			response.setAttribute(BookletsConstants.PUBLISHER_NAME, "publishPresentation");
			response.setAttribute(BookletsConstants.PATH_BOOKLET_CONF, pathConfBook);
			response.setAttribute(BookletsConstants.BOOKLET_PRESENTATION_VERSION_NAME, presVerName);
			
			 // load list of states and engines
			IDomainDAO domaindao = DAOFactory.getDomainDAO();
			List states = domaindao.loadListDomainsByType("STATE");
		    response.setAttribute(BookletsConstants.BOOKLET_PRESENTATION_LIST_STATES, states);
		} catch(Exception e){
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
		                        "publishPresHandler","Error while preparing page for publishing", e);
		}
	    
	}
	
	
	

	private void runCollaborationHandler(SourceBean request, SourceBean response) {
		JbpmContext jbpmContext = null;
		String pathBookConf = (String)request.getAttribute(BookletsConstants.PATH_BOOKLET_CONF);
		String executionMsg = null;
		IBookletsCmsDao bookDao = new BookletsCmsDaoImpl();
		InputStream procDefIS = bookDao.getBookletProcessDefinitionContent(pathBookConf);
		String objPath = bookDao.getBiobjectPath(pathBookConf);
		BIObject biObject = null;
		try {
			biObject = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(objPath);
		} catch (EMFUserError e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
    			    "runCollaborationHandler", "Error while recovering BIObject", e);
		}
		
		IEngUserProfile profile = new AnonymousWorkflowProfile();
	    // AUDIT
		AuditManager auditManager = AuditManager.getInstance();
		Integer auditId = null;
		if (biObject != null) {
			auditId = auditManager.insertAudit(biObject, profile, "", "WORKFLOW");
		}
		
		try {
			// parse process definition
			ProcessDefinition processDefinition = null;
			try{
				processDefinition = ProcessDefinition.parseXmlInputStream(procDefIS);
			} catch(Exception e) {
				executionMsg = PortletUtilities.getMessage("book.processDefNotCorrect", "component_booklets_messages"); 
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
		            			    "runCollaborationHandler", "Process definition xml file not correct", e);
				throw e;
			}	
			procDefIS.close();
		    // get name of the process
			String nameProcess = processDefinition.getName();
			// get jbpm context
			JbpmConfiguration jbpmConfiguration = JbpmConfiguration.getInstance();
			jbpmContext = jbpmConfiguration.createJbpmContext();
			// deploy process   
			try{
				jbpmContext.deployProcessDefinition(processDefinition);  
			} catch (Exception e) {
				executionMsg = PortletUtilities.getMessage("book.workProcessStartError", "component_booklets_messages");
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
        			                "runCollaborationHandler", "Error while deploying process definition", e);
				throw e;
			}
			// create process instance
			ProcessInstance processInstance = new ProcessInstance(processDefinition);
			// get context instance and set the booklet path variable
			ContextInstance contextInstance = processInstance.getContextInstance();
			contextInstance.createVariable(BookletsConstants.PATH_BOOKLET_CONF, pathBookConf);
			
			// adding parameters for AUDIT updating
			if (auditId != null) {
				contextInstance.createVariable(AuditManager.AUDIT_ID, auditId);
			}
			
			// start workflow
			Token token = processInstance.getRootToken();
			GraphSession graphSess = jbpmContext.getGraphSession();
			try{
				token.signal();
			} catch (Exception e) {
				if(e.getCause() instanceof OpenOfficeConnectionException ) {
					executionMsg = PortletUtilities.getMessage("book.errorConnectionOO", "component_booklets_messages");
				}
			    throw e;
			}
			// save workflow data
			jbpmContext.save(processInstance); 
			
	    } catch (Exception e){
	    	if(executionMsg==null) {
	    		executionMsg = PortletUtilities.getMessage("book.workProcessStartError", "component_booklets_messages");
	    	}
	    	SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
                                "runCollaborationHandler","Error while starting workflow",  e);
			// AUDIT UPDATE
			auditManager.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
					"STARTUP_FAILED", e.getMessage(), null);
	    } finally {
	    	if(executionMsg==null){
	    		executionMsg = PortletUtilities.getMessage("book.workProcessStartCorrectly", "component_booklets_messages");
	    		// AUDIT UPDATE
	    		auditManager.updateAudit(auditId, new Long(System.currentTimeMillis()), null, 
						"EXECUTION_STARTED", null, null);
	    	}
	    	if(jbpmContext!=null){
	    		jbpmContext.close();
	    	}
	    } 
	    try{
	    	response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletsExecution");
	    	response.setAttribute(BookletsConstants.EXECUTION_MESSAGE, executionMsg);
	    } catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
                                "runCollaborationHandler","Error while setting attributes into response", e);
	    }
	}
	

	
	
	
	
	
	private void approveHandler(SourceBean request, SourceBean response) {
		ContextInstance contextInstance = null;
		JbpmContext jbpmContext = null;
		try {
			// recover task instance and variables
			String activityKey = (String)request.getAttribute(SpagoBIConstants.ACTIVITYKEY);
			String approved = (String)request.getAttribute("approved");
			JbpmConfiguration jbpmConfiguration = JbpmConfiguration.getInstance();
			jbpmContext = jbpmConfiguration.createJbpmContext();
			TaskInstance taskInstance = jbpmContext.getTaskInstance(new Long(activityKey).longValue());
			contextInstance = taskInstance.getContextInstance();
			String pathConfBook = (String)contextInstance.getVariable(BookletsConstants.PATH_BOOKLET_CONF);
			// store presentation
			IBookletsCmsDao bookDao = new BookletsCmsDaoImpl();
			byte[] currPresCont = bookDao.getCurrentPresentationContent(pathConfBook);
			boolean approvedBool = false;
			if(approved.equalsIgnoreCase("true")) {
				approvedBool = true;
			}
			bookDao.versionPresentation(pathConfBook, currPresCont, approvedBool);
			// put attributes into response
			if(approved.equalsIgnoreCase("true")) {
				response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletCompleteActivityLoopback");
			} else {
				response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletRejecrActivityLoopback");
			}
			response.setAttribute(SpagoBIConstants.ACTIVITYKEY, activityKey);
			
			// AUDIT UPDATE
			if (contextInstance != null) {
				Object auditIdObj = contextInstance.getVariable(AuditManager.AUDIT_ID);
				Integer auditId = convertIdType(auditIdObj);
				if(auditId!=null) {
					AuditManager auditManager = AuditManager.getInstance();
					auditManager.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
							"EXECUTION_PERFORMED", null, null);
				}
			}
			
		} catch(Exception e){
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
		                        "approveHandler","Error while versioning presentation", e);
			// AUDIT UPDATE
			if (contextInstance != null) {
				Object auditIdObj = contextInstance.getVariable(AuditManager.AUDIT_ID);
				Integer auditId = convertIdType(auditIdObj);
				if(auditId!=null) {
					AuditManager auditManager = AuditManager.getInstance();
					auditManager.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
							"EXECUTION_FAILED", e.getMessage(), null);
				}
			}
		} finally {
	    	if (jbpmContext != null) {
	    		jbpmContext.close();
	    	}
		}
	    
	}
	
	
	private Integer convertIdType(Object auditIdObj) {
		Integer auditId = null;
		if(auditIdObj instanceof Long) {
			Long auditLong = (Long)auditIdObj;
			auditId = new Integer(auditLong.intValue());
		} else if (auditIdObj instanceof Integer) {
			auditId = (Integer)auditIdObj;
		} else {
			try {
				auditId = new Integer(auditIdObj.toString());
			} catch (Exception e ) {
				SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
						            "convertIdType", "Error while converting audit id type, " +
						            "the audit log row will not be recorded", e);
			}
		}
		return auditId;
	}
	
	
	
	private void openNoteEditorHandler(SourceBean request, SourceBean response) {
		JbpmContext jbpmContext = null;
		try {
			// recover task instance and variables
			String activityKey = (String)request.getAttribute("activityKey");
			JbpmConfiguration jbpmConfiguration = JbpmConfiguration.getInstance();
			jbpmContext = jbpmConfiguration.createJbpmContext();
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
		} finally {
	    	if (jbpmContext != null) {
	    		jbpmContext.close();
	    	}
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
		String pathTmpFoldBook = null;
		if (pathTmpFold.startsWith("/") || pathTmpFold.charAt(1) == ':') {
			pathTmpFoldBook = pathTmpFold;
		} else {
			String root = ConfigSingleton.getRootPath();
			pathTmpFoldBook = root + "/" + pathTmpFold;
		}
		File tempDir = new File(pathTmpFoldBook); 
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
	    	
	    	String recoverUrl = BookletServiceUtils.getBookletServiceUrl() + "?" + 
	    						BookletsConstants.BOOKLET_SERVICE_TASK + "=" +
	    						BookletsConstants.BOOKLET_SERVICE_TASK_GET_TEMPLATE_IMAGE + "&" +
	    						BookletsConstants.BOOKLET_SERVICE_PATH_IMAGE + "=" +
	    						pathTmpFold + "/" + logicalNameForStoring;
	    	imageurl.put(logicalName, recoverUrl); 
	    }
	   return imageurl;
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
