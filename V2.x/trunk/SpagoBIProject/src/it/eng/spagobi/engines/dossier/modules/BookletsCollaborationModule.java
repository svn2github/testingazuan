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
package it.eng.spagobi.engines.dossier.modules;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjTemplate;
import it.eng.spagobi.analiticalmodel.document.dao.IBIObjectDAO;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IDomainDAO;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.commons.utilities.PortletUtilities;
import it.eng.spagobi.commons.utilities.UploadedFile;
import it.eng.spagobi.engines.config.bo.Engine;
import it.eng.spagobi.engines.config.dao.IEngineDAO;
import it.eng.spagobi.engines.dossier.actions.DossierDownloadAction;
import it.eng.spagobi.engines.dossier.bo.DossierPresentation;
import it.eng.spagobi.engines.dossier.constants.BookletsConstants;
import it.eng.spagobi.engines.dossier.dao.DossierDAOHibImpl;
import it.eng.spagobi.engines.dossier.dao.IDossierDAO;
import it.eng.spagobi.engines.dossier.dao.IDossierPartsTempDAO;
import it.eng.spagobi.engines.dossier.dao.IDossierPresentationsDAO;
import it.eng.spagobi.engines.dossier.exceptions.OpenOfficeConnectionException;
import it.eng.spagobi.engines.dossier.utils.DossierUtilities;
import it.eng.spagobi.monitoring.dao.AuditManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.db.GraphSession;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.graph.exe.Token;
import org.jbpm.taskmgmt.exe.TaskInstance;
import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;

/**
 * This class implements a module which  handles pamphlets collaboration.
 */
public class BookletsCollaborationModule extends AbstractModule {
	
	static private Logger logger = Logger.getLogger(BookletsCollaborationModule.class);

	public void init(SourceBean config) {
	}

	/**
	 * Reads the operation asked by the user and calls the right operation handler
	 * @param request The Source Bean containing all request parameters
	 * @param response The Source Bean containing all response parameters
	 * @throws exception If an exception occurs
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		logger.debug("IN");
		EMFErrorHandler errorHandler = getErrorHandler();
		String operation = (String) request.getAttribute(SpagoBIConstants.OPERATION);
		try{
			if((operation==null)||(operation.trim().equals(""))) {
				logger.error("The operation parameter is null");
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
		} finally {
			logger.debug("OUT");
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
			
			String dossierIdStr = (String) request.getAttribute(BookletsConstants.DOSSIER_ID);
			String versionIdStr = (String)request.getAttribute(BookletsConstants.VERSION_ID);
			List functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(false);
			EMFErrorHandler errorHandler = getResponseContainer().getErrorHandler();
			if(!errorHandler.isOK()){
				if(GeneralUtilities.isErrorHandlerContainingOnlyValidationError(errorHandler)) {
					response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
					response.setAttribute(BookletsConstants.PUBLISHER_NAME, "publishPresentation");
					response.setAttribute("label", label);
					response.setAttribute("name", name);
					response.setAttribute("description", description);
					response.setAttribute(BookletsConstants.DOSSIER_ID, dossierIdStr);
					response.setAttribute(BookletsConstants.VERSION_ID, versionIdStr);
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
				IDossierPresentationsDAO dpDAO = DAOFactory.getDossierPresentationDAO();
				Integer dossierId = new Integer(dossierIdStr);
				Integer versionId = new Integer(versionIdStr);
				byte[] tempCont = dpDAO.getPresentationVersionContent(dossierId, versionId);
				BIObject dossier = DAOFactory.getBIObjectDAO().loadBIObjectById(dossierId);
				String bookName = dossier.getName();
				ObjTemplate templ = new ObjTemplate();
				templ.setActive(new Boolean(true));
		        templ.setName(bookName + ".ppt");
		        templ.setContent(tempCont);
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
				biobj.setDataSourceId(engine.getDataSourceId());
				biobj.setRelName("");
				biobj.setBiObjectTypeCode(officeDocDom.getValueCd());
				biobj.setBiObjectTypeID(officeDocDom.getValueId());
				biobj.setStateCode(devDom.getValueCd());
				biobj.setStateID(devDom.getValueId());
				biobj.setVisible(new Integer(0));
				biobj.setFunctionalities(storeInFunctionalities);
				biobj.setStateCode(stateCode);
				biobj.setStateID(stateId);
				biobj.setVisible((visible? new Integer(1): new Integer(0)));
				
				IBIObjectDAO objectDAO = DAOFactory.getBIObjectDAO();
				objectDAO.insertBIObject(biobj, templ);
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
				response.setAttribute(BookletsConstants.DOSSIER_ID, dossierIdStr);
				response.setAttribute(BookletsConstants.VERSION_ID, versionIdStr);
				
			}
		} catch(Exception e){
			logger.error("Error while publishing presentation", e);
		}
	    
	}
	
	
	
	
	private void deletePresVerHandler(SourceBean request, SourceBean response) throws Exception {
		logger.debug("IN");
		String dossierIdStr = null;
		List presVersions = null;
		IDossierPresentationsDAO dpDAO = null;
		try{
			dossierIdStr = (String) request.getAttribute(BookletsConstants.DOSSIER_ID);
			String versionIdStr = (String) request.getAttribute(BookletsConstants.VERSION_ID);
			dpDAO = DAOFactory.getDossierPresentationDAO();
			Integer dossierId = new Integer(dossierIdStr);
			Integer versionId = new Integer(versionIdStr);
			dpDAO.deletePresentationVersion(dossierId, versionId);
			presVersions = dpDAO.getPresentationVersions(dossierId);
			response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletsPresentationVersion");
			response.setAttribute(BookletsConstants.BOOKLET_PRESENTATION_VERSIONS, presVersions);
			response.setAttribute(BookletsConstants.DOSSIER_ID, dossierId);
		} catch (Exception e) {
			logger.error("error while setting response attribute " + e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			logger.debug("OUT");
		}
	}
	
	
	
	
	private void preparePublishPageHandler(SourceBean request, SourceBean response) {
		logger.debug("IN");
		try {
			String dossierIdStr = (String) request.getAttribute(BookletsConstants.DOSSIER_ID);
			String versionIdStr = (String)request.getAttribute(BookletsConstants.VERSION_ID);
			List functionalities = DAOFactory.getLowFunctionalityDAO().loadAllLowFunctionalities(false);
			response.setAttribute(SpagoBIConstants.FUNCTIONALITIES_LIST, functionalities);
			response.setAttribute(BookletsConstants.PUBLISHER_NAME, "publishPresentation");
			response.setAttribute(BookletsConstants.DOSSIER_ID, dossierIdStr);
			response.setAttribute(BookletsConstants.VERSION_ID, versionIdStr);
			 // load list of states and engines
			IDomainDAO domaindao = DAOFactory.getDomainDAO();
			List states = domaindao.loadListDomainsByType("STATE");
		    response.setAttribute(BookletsConstants.BOOKLET_PRESENTATION_LIST_STATES, states);
		} catch(Exception e){
			logger.error("Error while preparing page for publishing", e);
		} finally {
			logger.debug("OUT");
		}
	    
	}
	
	
	

	private void runCollaborationHandler(SourceBean request, SourceBean response) throws EMFUserError {
		logger.debug("IN");
		String dossierIdStr = (String) request.getAttribute(BookletsConstants.DOSSIER_ID);
		Integer dossierId = new Integer(dossierIdStr);
		IDossierDAO dossierDAO = DAOFactory.getDossierDAO();
		BIObject dossier;
		String pathTempFolder;
		try {
			dossier = DAOFactory.getBIObjectDAO().loadBIObjectById(dossierId);
			pathTempFolder = dossierDAO.init(dossier);
		} catch (EMFUserError e) {
			logger.error("Error while recovering dossier information: " + e);
			throw e;
		}
		JbpmContext jbpmContext = null;
		InputStream procDefIS = null;
		String executionMsg = null;
		IEngUserProfile profile = UserProfile.createWorkFlowUserProfile();
		AuditManager auditManager = AuditManager.getInstance();
	    // AUDIT
		Integer auditId = null;
		if (dossier != null) {
			auditId = auditManager.insertAudit(dossier, profile, "", "WORKFLOW");
		}
		try {
			try {
				procDefIS = dossierDAO.getProcessDefinitionContent(pathTempFolder);
			} catch (Exception e) {
				logger.error("Error while reading process definition file content from dossier template: " + e);
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
			}
			
			// parse process definition
			ProcessDefinition processDefinition = null;
			try{
				processDefinition = ProcessDefinition.parseXmlInputStream(procDefIS);
			} catch(Exception e) {
				executionMsg = PortletUtilities.getMessage("book.processDefNotCorrect", "component_booklets_messages"); 
				logger.error("Process definition xml file not correct", e);
				throw e;
			}	
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
				logger.error("Error while deploying process definition", e);
				throw e;
			}
			// create process instance
			ProcessInstance processInstance = new ProcessInstance(processDefinition);
			// get context instance and set the booklet path variable
			ContextInstance contextInstance = processInstance.getContextInstance();
			contextInstance.createVariable(BookletsConstants.DOSSIER_ID, dossierIdStr);
			
			// adding parameters for AUDIT updating
			if (auditId != null) {
				contextInstance.createVariable(AuditManager.AUDIT_ID, auditId);
			}
			
			// start workflow
			Token token = processInstance.getRootToken();
			GraphSession graphSess = jbpmContext.getGraphSession();
			try {
				token.signal();
			} catch (Exception e) {
				if(e.getCause() instanceof OpenOfficeConnectionException ) {
					executionMsg = PortletUtilities.getMessage("book.errorConnectionOO", "component_booklets_messages");
				}
			    throw e;
			}
			// save workflow data
			jbpmContext.save(processInstance); 
			
		    try {
		    	response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletsExecution");
		    	response.setAttribute(BookletsConstants.EXECUTION_MESSAGE, executionMsg);
		    } catch (Exception e) {
		    	logger.error("Error while setting attributes into response", e);
		    }
			
	    } catch (Exception e) {
	    	if (executionMsg == null) {
	    		executionMsg = PortletUtilities.getMessage("book.workProcessStartError", "component_booklets_messages");
	    	}
	    	logger.error("Error while starting workflow", e);
			// AUDIT UPDATE
			auditManager.updateAudit(auditId, null, new Long(System.currentTimeMillis()), 
					"STARTUP_FAILED", e.getMessage(), null);
	    } finally {
	    	if (executionMsg == null) {
	    		executionMsg = PortletUtilities.getMessage("book.workProcessStartCorrectly", "component_booklets_messages");
	    		try {
					response.setAttribute(BookletsConstants.EXECUTION_MESSAGE, executionMsg);
				} catch (SourceBeanException e) {
					logger.error(e);
				}
	    		// AUDIT UPDATE
	    		auditManager.updateAudit(auditId, new Long(System.currentTimeMillis()), null, 
						"EXECUTION_STARTED", null, null);
	    	}
	    	if (jbpmContext != null) {
	    		jbpmContext.close();
	    	}
	    	if (procDefIS != null)
				try {
					procDefIS.close();
				} catch (IOException e) {
					logger.error(e);
				}
	    	logger.debug("OUT");
	    } 

	}
	

	
	
	
	
	
	private void approveHandler(SourceBean request, SourceBean response) {
		logger.debug("IN");
		ContextInstance contextInstance = null;
		JbpmContext jbpmContext = null;
		try {
			// recover task instance and variables
			String activityKey = (String) request.getAttribute(SpagoBIConstants.ACTIVITYKEY);
			String approved = (String) request.getAttribute("approved");
			JbpmConfiguration jbpmConfiguration = JbpmConfiguration.getInstance();
			jbpmContext = jbpmConfiguration.createJbpmContext();
			TaskInstance taskInstance = jbpmContext.getTaskInstance(new Long(activityKey).longValue());
			contextInstance = taskInstance.getContextInstance();
			ProcessInstance processInstance = contextInstance.getProcessInstance();
			Long workflowProcessId = new Long(processInstance.getId());
			String dossierIdStr = (String) contextInstance.getVariable(BookletsConstants.DOSSIER_ID);
			Integer dossierId = new Integer(dossierIdStr);
			// store presentation
			IDossierPresentationsDAO dpDAO = DAOFactory.getDossierPresentationDAO();
			DossierPresentation currPresentation = dpDAO.getCurrentPresentation(dossierId, workflowProcessId);
			boolean approvedBool = false;
			if (approved.equalsIgnoreCase("true")) {
				approvedBool = true;
			}
			currPresentation.setApproved(new Boolean(approvedBool));
			Integer nextProg = dpDAO.getNextProg(dossierId);
			currPresentation.setProg(nextProg);
			dpDAO.updatePresentation(currPresentation);
			// put attributes into response
			if (approved.equalsIgnoreCase("true")) {
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
			logger.error("Error while versioning presentation", e);
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
	    	logger.debug("OUT");
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
				logger.error("Error while converting audit id type, " +
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
			int pageNum = Integer.parseInt(index);
			ContextInstance contextInstance = taskInstance.getContextInstance();
			ProcessInstance processInstance = contextInstance.getProcessInstance();
			Long workflowProcessId = new Long(processInstance.getId());
			String dossierIdStr = (String)contextInstance.getVariable(BookletsConstants.DOSSIER_ID);
			Integer dossierId = new Integer(dossierIdStr);
			
			// recovers images and notes
		    String notes = recoverNotes(dossierId, pageNum, workflowProcessId);
		    Map imageurl = recoverImageUrls(dossierId, pageNum, workflowProcessId);
		    
			// put attributes into response
			response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletEditNotesTemplatePart");
			response.setAttribute(BookletsConstants.BOOKLET_PART_INDEX, index);
			response.setAttribute(BookletsConstants.DOSSIER_ID, dossierIdStr);
			response.setAttribute(SpagoBIConstants.ACTIVITYKEY, activityKey);
			response.setAttribute("mapImageUrls", imageurl);
			response.setAttribute("notes", notes);
			
		} catch(Exception e){
			logger.error("Error while saving notes", e);
		} finally {
	    	if (jbpmContext != null) {
	    		jbpmContext.close();
	    	}
		}
	    
	}
	
	
	
	private String recoverNotes(Integer dossierId, int pageNum, Long workflowProcessId) throws Exception {
		IDossierPartsTempDAO dptDAO = DAOFactory.getDossierPartsTempDAO();
	    byte[] notesByte = 	dptDAO.getNotesOfDossierPart(dossierId, pageNum, workflowProcessId);
	    String notes = notesByte != null ? new String(notesByte) : "";
	    return notes;
	}
	
	
	
	private Map recoverImageUrls(Integer dossierId, int pageNum, Long workflowProcessId) throws Exception {
		IDossierPartsTempDAO dptDAO = DAOFactory.getDossierPartsTempDAO();
		Map images = dptDAO.getImagesOfDossierPart(dossierId, pageNum, workflowProcessId);
		 // get temp directory for the pamphlet module
	    ConfigSingleton configSing = ConfigSingleton.getInstance();
		File tempDir = DossierDAOHibImpl.tempBaseFolder; 
		tempDir.mkdirs();
		// for each image store into the temp directory and save the url useful to recover it into the map
	    Map imageurl = new HashMap();
	    Iterator iterImgs = images.keySet().iterator();
	    while(iterImgs.hasNext()){
	    	String logicalName = (String) iterImgs.next();
	    	//String logicalNameForStoring = pathConfBook.replace('/', '_') + logicalName + ".jpg";
			UUIDGenerator uuidGenerator = UUIDGenerator.getInstance();
			UUID uuidObj = uuidGenerator.generateTimeBasedUUID();
			String uuid = uuidObj.toString();
			// TODO perché salvare su file system? tanto vale tenere in memoria le immagini, oppure scriverli sul file system e far puntare l'immagine al file
	    	String logicalNameForStoring = uuid + logicalName + ".jpg";
	    	byte[] content = (byte[])images.get(logicalName);
	    	File img = new File(tempDir, logicalNameForStoring);
	    	FileOutputStream fos = new FileOutputStream(img);
	    	fos.write(content);
	    	fos.flush();
	    	fos.close();
	    	// the url to recover the image is a spagobi servlet url
	    	
	    	String recoverUrl = DossierUtilities.getDossierServiceUrl() + "&" + 
	    						BookletsConstants.BOOKLET_SERVICE_TASK + "=" +
	    						BookletsConstants.BOOKLET_SERVICE_TASK_GET_TEMPLATE_IMAGE + "&" +
	    						BookletsConstants.BOOKLET_SERVICE_PATH_IMAGE + "=" +
	    						tempDir.getAbsolutePath() + "/" + logicalNameForStoring;
	    	imageurl.put(logicalName, recoverUrl); 
	    }
	   return imageurl;
	}
	
			
	
	private void saveNoteHandler(SourceBean request, SourceBean response) {
		JbpmContext jbpmContext = null;
		try {
			String indexPart = (String)request.getAttribute(BookletsConstants.BOOKLET_PART_INDEX);
			int pageNum = Integer.parseInt(indexPart);
			String dossierIdStr = (String)request.getAttribute(BookletsConstants.DOSSIER_ID);
			Integer dossierId = new Integer(dossierIdStr);
			String noteSent = (String)request.getAttribute("notes");
			String activityKey = (String)request.getAttribute(SpagoBIConstants.ACTIVITYKEY);
			// TODO vedere se si può prendere dalla request
			JbpmConfiguration jbpmConfiguration = JbpmConfiguration.getInstance();
			jbpmContext = jbpmConfiguration.createJbpmContext();
			TaskInstance taskInstance = jbpmContext.getTaskInstance(new Long(activityKey).longValue());
			ContextInstance contextInstance = taskInstance.getContextInstance();
			ProcessInstance processInstance = contextInstance.getProcessInstance();
			Long workflowProcessId = new Long(processInstance.getId());
			IDossierPartsTempDAO dptDAO = DAOFactory.getDossierPartsTempDAO();
			dptDAO.storeNote(dossierId, pageNum, noteSent.getBytes(), workflowProcessId);
			// recover from cms images and notes
		    String notes = recoverNotes(dossierId, pageNum, workflowProcessId);
		    Map imageurl = recoverImageUrls(dossierId, pageNum, workflowProcessId);
			// put attributes into response
			response.setAttribute(BookletsConstants.PUBLISHER_NAME, "BookletEditNotesTemplatePart");
			response.setAttribute(BookletsConstants.BOOKLET_PART_INDEX, indexPart);
			response.setAttribute(BookletsConstants.DOSSIER_ID, dossierIdStr);
			response.setAttribute(SpagoBIConstants.ACTIVITYKEY, activityKey);
			response.setAttribute("mapImageUrls", imageurl);
			response.setAttribute("notes", notes);
			
		} catch(Exception e){
			logger.error("Error while saving notes", e);
		}
	    
	}

}
