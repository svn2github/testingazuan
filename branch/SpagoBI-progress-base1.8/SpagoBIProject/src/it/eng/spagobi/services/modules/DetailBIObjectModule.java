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

import it.eng.spago.base.Constants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.cms.CmsManager;
import it.eng.spago.cms.operations.DeleteOperation;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dbaccess.DataConnectionManager;
import it.eng.spago.dbaccess.SQLStatements;
import it.eng.spago.dbaccess.Utils;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.SQLCommand;
import it.eng.spago.dbaccess.sql.result.DataResult;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spago.validation.coordinator.ValidationCoordinator;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.ObjParuse;
import it.eng.spagobi.bo.Parameter;
import it.eng.spagobi.bo.Subreport;
import it.eng.spagobi.bo.TemplateVersion;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.bo.dao.IBIObjectParameterDAO;
import it.eng.spagobi.bo.dao.IDomainDAO;
import it.eng.spagobi.bo.dao.IObjParuseDAO;
import it.eng.spagobi.bo.dao.ISubreportDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.services.dao.TreeObjectsDAO;
import it.eng.spagobi.utilities.PortletUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;

import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.commons.validator.GenericValidator;

/**
 * Implements a module which  handles all BI objects management: 
 * has methods for BI Objects load, detail, modify/insertion and deleting operations. 
 * The <code>service</code> method has  a switch for all these operations, differentiated the ones 
 * from the others by a <code>message</code> String.
 * 
 * @author sulis
 */

public class DetailBIObjectModule extends AbstractModule {
		
	public final static String MODULE_PAGE = "DetailBIObjectPage";
	public final static String NAME_ATTR_OBJECT = "BIObjects";
	public final static String NAME_ATTR_LIST_OBJ_TYPES = "types";
	public final static String NAME_ATTR_LIST_ENGINES = "engines";
	public final static String NAME_ATTR_LIST_STATES = "states";		
	public final static String NAME_ATTR_OBJECT_PAR = "OBJECT_PAR";
	private String actor = null;
	private EMFErrorHandler errorHandler = null;
	
	SessionContainer session = null;
	
	public void init(SourceBean config) {
	}
	
	
	/**
	 * Reads the operation asked by the user and calls the insertion, modify, detail and 
	 * deletion methods.
	 * 
	 * @param request The Source Bean containing all request parameters
	 * @param response The Source Bean containing all response parameters
	 * @throws exception If an exception occurs
	 * 
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
	
		PortletRequest portletRequest = PortletUtilities.getPortletRequest();
		if (portletRequest instanceof ActionRequest) {
			ActionRequest actionRequest = (ActionRequest) portletRequest;
			if (PortletFileUpload.isMultipartContent(actionRequest)) {
				request = PortletUtilities.getServiceRequestFromMultipartPortletRequest(portletRequest);
			}
		}
		
		RequestContainer requestContainer = this.getRequestContainer();		
		session = requestContainer.getSessionContainer();
				
		// get attributes from request		
		String message = (String) request.getAttribute("MESSAGEDET");
		SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","service"," MESSAGEDET = " + message);
		
		actor = (String) request.getAttribute(SpagoBIConstants.ACTOR);
		SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","service"," ACTOR = " + actor);
		
		// these attributes, if defined, represent events triggered by one 
		// of the submit buttons present in the main form 
		boolean parametersLookupButtonClicked =  request.getAttribute("loadParametersLookup") != null;
		boolean subreportsLookupButtonClicked =  request.getAttribute("Edit") != null;
				
		errorHandler = getErrorHandler();
		
		try {
			if (message == null) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
				SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule", "service", "The message parameter is null");
				throw userError;
			} 
			
			// check for events first...
			if (parametersLookupButtonClicked){
				SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","service","loadParametersLookup != null");
				startParametersLookupHandler (request, message, response);
			} else if(subreportsLookupButtonClicked){
				SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","service","editSubreports != null");
				startSubreportLookupHandler (request, message, response);
		    } // ...then check for other service request types 
			else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.RETURN_FROM_SUBREPORTS_LOOKUP)){
				String pageNumberStr = (String)session.getAttribute("PAGE_NUMBER");
				
				// events rised by navigation buttons defined in CheckListTag class (method makeNavigationButton)
				if(request.getAttribute("prevPage") != null){
					SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","service","MOVE TO PREVIOUS PAGE [" + pageNumberStr + " - 1]");
					// move backwards
					moveIntoSubreportsListHandler(request,response, false);
					return;
				}
				
				if(request.getAttribute("nextPage") != null){
					SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","service","MOVE TO NEXT PAGE [" + pageNumberStr + " + 1]");
					//move forewards
					moveIntoSubreportsListHandler(request,response, true);
					return;
				}
				
				//	events rised by action buttons defined in module.xml file (module name="ListLookupReportsModule")
				if(request.getAttribute("saveback") != null){
					SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","service", "SAVE & BACK");
					endSubreportLookupHandler(request,response, true);
					return;
				}
								
				if(request.getAttribute("save") != null) {
					SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","service", "SAVE");
					saveSubreportsHandler(request, response);
					return;
				}
				
				if(request.getAttribute("back") != null) {
					SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","service", "BACK");
					endSubreportLookupHandler(request,response, false);
					return;
				}
			}
			else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.RETURN_FROM_LOOKUP)){
				lookupReturnHandler(request, response);	
			} else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.RETURN_BACK_FROM_LOOKUP)){				
				lookupReturnBackHandler(request,response);
			}  else if (message.trim().equalsIgnoreCase(ObjectsTreeConstants.DETAIL_SELECT)) {
				getDetailObject(request, response);
			} else if (message.trim().equalsIgnoreCase(ObjectsTreeConstants.DETAIL_MOD)) {
				modBIObject(request, ObjectsTreeConstants.DETAIL_MOD, response);
			} else if (message.trim().equalsIgnoreCase(ObjectsTreeConstants.DETAIL_NEW)) {
				newBIObject(request, response);
			} else if (message.trim().equalsIgnoreCase(ObjectsTreeConstants.DETAIL_INS)) {
				modBIObject(request, ObjectsTreeConstants.DETAIL_INS, response);
			} else if (message.trim().equalsIgnoreCase(ObjectsTreeConstants.DETAIL_DEL)) {
				delDetailObject(request, ObjectsTreeConstants.DETAIL_DEL, response);
			} else if(message.trim().equalsIgnoreCase(SpagoBIConstants.ERASE_VERSION)) {
				eraseVersion(request, response);
			} else if (message.trim().equalsIgnoreCase("EXIT_FROM_DETAIL")){
				exitFromDetail(request, response);
			}
		} catch (EMFUserError eex) {
			errorHandler.addError(eex);
			return;
		} catch (Exception ex) {
			EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, ex);
			errorHandler.addError(internalError);
			return;
		}
	}	
	
	private void setLoopbackContext(SourceBean request, String message) throws EMFUserError{
		BIObject obj = recoverBIObjectDetails(request, message);
		BIObjectParameter biObjPar = recoverBIObjectParameterDetails(request, obj.getId());
		session.setAttribute("LookupBIObject", obj);
		session.setAttribute("LookupBIObjectParameter", biObjPar);
		session.setAttribute("modality", message);
		session.setAttribute("modalityBkp", message);
		session.setAttribute("actor",actor);
	}
	
	private void delateLoopbackContext() {
		session.delAttribute("LookupBIObject");
		session.delAttribute("LookupBIObjectParameter");
		session.delAttribute("modality");
		session.delAttribute("modalityBkp");
		session.delAttribute("actor");
	}
	
	private Integer getBIObjectIdFromLoopbackContext() {
		Integer id = null;
		BIObject obj = (BIObject)session.getAttribute("LookupBIObject");
		if(obj != null) id = obj.getId();
		return id;
	}
	
	private void setSubreportsLookupContext(Integer masterReportId, SourceBean subreports, int pageNumber){
		session.setAttribute("MASTER_ID", masterReportId);
		session.setAttribute("SUBREPORTS", subreports);	
		session.setAttribute("MESSAGE", "LIST_PAGE");
		session.setAttribute("LIST_PAGE", String.valueOf(pageNumber));	
	}
	
	private void delateSubreportsLookupContext() {
		session.delAttribute("MASTER_ID");
		session.delAttribute("SUBREPORTS");	
		session.delAttribute("MESSAGE");
		session.delAttribute("LIST_PAGE");	
	}
	
	private void startParametersLookupHandler(SourceBean request, String message, SourceBean response) throws EMFUserError, SourceBeanException {
		setLoopbackContext(request, message);		
		response.setAttribute("lookupLoopback", "true");		
	}
	
	private SourceBean subreportsToSourceBean(List subreportList) {
		SourceBean subreports = null;
		try {
			subreports = new SourceBean("ROWS");
			for(int i = 0; i < subreportList.size(); i++) {
	    		Subreport subreport = (Subreport)subreportList.get(i);
	    		SourceBean row = new SourceBean("ROW");
				row.setAttribute("MASTER_ID", subreport.getMaster_rpt_id());
				row.setAttribute("SUBREPORT_ID", subreport.getSub_rpt_id());
				subreports.setAttribute(row);    		
			}
		} catch (SourceBeanException e) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","subreportsToSourceBean","Cannot create subreports SourceBean", e);
		}
		
		
		return subreports;
	}
	
	
	private void startSubreportLookupHandler(SourceBean request, String message, SourceBean response) throws EMFUserError, SourceBeanException {
		
		setLoopbackContext(request, message);	
		Integer masterReportId = getBIObjectIdFromLoopbackContext();		
		
		List subreportList = null;
		try {
			ISubreportDAO subrptdao = DAOFactory.getSubreportDAO();
			subreportList =  subrptdao.loadSubreportsByMasterRptId(masterReportId);
		} catch (Exception e) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","startSubreportLookupHandler","Cannot read subreport list from db", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		
		SourceBean subreports = subreportsToSourceBean(subreportList);
		
		/*
		DataConnection dataConnection = null;
		SQLCommand sqlCommand = null;
		DataResult dataResult = null;
		SourceBean subreports = null;
		try {
			DataConnectionManager dataConnectionManager = DataConnectionManager.getInstance();
			dataConnection = dataConnectionManager.getConnection("spagobi");
			String statement = null;
						
			statement = SQLStatements.getStatement("SELECT_SUBREPORTS_LIST");
			statement = statement.replaceFirst("\\?", masterReportId.toString());
			SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","lookupReportsSaveBackHandler", " statement: " + statement);
			sqlCommand = dataConnection.createSelectCommand(statement);
			dataResult = sqlCommand.execute();
			ScrollableDataResult scrollableDataResult = (ScrollableDataResult)dataResult.getDataObject();
			subreports = scrollableDataResult.getSourceBean();
			SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","lookupReportsSaveBackHandler", " results: " + subreports.toXML());
			
		} catch (Exception ex) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL, "execQuery::executeQuery:", ex);
		} finally {
			Utils.releaseResources(dataConnection, sqlCommand, dataResult);
		} 
		*/
		
		setSubreportsLookupContext(masterReportId, subreports, 1);
				
		response.setAttribute("editLoopback", "true");			
	}
	
	private List getSubreportsId(SourceBean request){
		List results = new ArrayList();
		List attrs = request.getContainedAttributes();
		for(int i = 0; i < attrs.size(); i++){
			SourceBeanAttribute attr = (SourceBeanAttribute)attrs.get(i);
			SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","service", " ATTR -> " + attr.getKey() + "=" + attr.getValue());
			String key = (String)attr.getKey();
			if(key.startsWith("checkbox")) {
				String id = key.substring(key.indexOf(':')+1, key.length());
				SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","service", " ATTR [OK] " + id);
				results.add(new Integer(id));
			}
		}
		
		return results;
	}
	
	private void updateSubreports(SourceBean request, SourceBean response, Integer id)throws SourceBeanException{
		List checkedSubreports = getSubreportsId(request);
		SourceBean subreports = (SourceBean)session.getAttribute("SUBREPORTS");
		for(int i = 0; i < checkedSubreports.size(); i++) {
			SourceBean row = new SourceBean("ROW");
			row.setAttribute("MASTER_ID", id);
			row.setAttribute("SUBREPORT_ID", checkedSubreports.get(i));
			subreports.setAttribute(row);
		}
		session.delAttribute("SUBREPORTS");
		session.setAttribute("SUBREPORTS", subreports);
	}
		
	private void saveSubreportsConfiguration(SourceBean request, SourceBean response) throws SourceBeanException{
		Integer masterReportId = getBIObjectIdFromLoopbackContext();
		updateSubreports(request, response, masterReportId);
		SourceBean subreports = (SourceBean)session.getAttribute("SUBREPORTS");
		List subreportsList = subreports.getAttributeAsList("ROW");
				
		try {
			ISubreportDAO subrptdao = DAOFactory.getSubreportDAO();
			subrptdao.eraseSubreportByMasterRptId(masterReportId);
			for(int i = 0; i < subreportsList.size(); i++) {
				SourceBean subreport = (SourceBean)subreportsList.get(i);
				Integer subReportId = (Integer)subreport.getAttribute("SUBREPORT_ID");
				subrptdao.insertSubreport(new Subreport(masterReportId, subReportId));
			}
			
		} catch (Exception e) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","saveSubreportsConfiguration","Cannot erase/insert subreports from/into db", e);
		}
		
		/*
		DataConnection dataConnection = null;
		SQLCommand sqlCommand = null;
		DataResult dataResult = null;
		try {
			DataConnectionManager dataConnectionManager = DataConnectionManager.getInstance();
			dataConnection = dataConnectionManager.getConnection("spagobi");
			String statement = null;
			
			statement = SQLStatements.getStatement("DELETE_SUBREPORTS");
			statement = statement.replaceFirst("\\?", masterReportId.toString());
			SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","lookupReportsSaveBackHandler", " statement: " + statement);
			sqlCommand = dataConnection.createDeleteCommand(statement);
			dataResult = sqlCommand.execute();
			SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","lookupReportsSaveBackHandler", " results: " + dataResult.toString());
						
			statement = SQLStatements.getStatement("INSERT_SUBREPORTS");			
			for(int i = 0; i < subreportsList.size(); i++) {
				SourceBean subreport = (SourceBean)subreportsList.get(i);
				Integer id = (Integer)subreport.getAttribute("SUBREPORT_ID");
				String statementRewrite = statement;
				statementRewrite = statementRewrite.replaceFirst("\\?", masterReportId.toString());
				statementRewrite = statementRewrite.replaceFirst("\\?", id.toString());
				SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","lookupReportsSaveBackHandler", " statement: " + statementRewrite);
				sqlCommand = dataConnection.createInsertCommand(statementRewrite);
				dataResult = sqlCommand.execute();
				SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","lookupReportsSaveBackHandler", " results: " + dataResult.toString());
			}
		} catch (Exception ex) {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL, "execQuery::executeQuery:", ex);
		} finally {
			Utils.releaseResources(dataConnection, sqlCommand, dataResult);
		} 
		*/
	}
	
	private void endSubreportLookupHandler(SourceBean request, SourceBean response, boolean save) throws SourceBeanException, EMFUserError {

		BIObject obj = (BIObject) session.getAttribute("LookupBIObject");
		BIObjectParameter biObjPar = (BIObjectParameter) session.getAttribute("LookupBIObjectParameter");
		String modality = (String) session.getAttribute("modality");
		if(modality == null) modality = (String)session.getAttribute("modalityBkp");
		actor = (String) session.getAttribute("actor");
		
		if(save) {
			saveSubreportsConfiguration(request, response);
		}
		
		delateLoopbackContext();
		delateSubreportsLookupContext();
		
		response.setAttribute(SpagoBIConstants.ACTOR, actor);
		fillResponse(response);
		reloadCMSInformation(obj);
		prepareBIObjectDetailPage(response, obj, biObjPar, biObjPar.getId().toString(), modality, false, false);
		response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");		
	}
	
	
	private void saveSubreportsHandler(SourceBean request, SourceBean response) throws EMFUserError, SourceBeanException {
		String pageNumberStr = (String)session.getAttribute("PAGE_NUMBER");
		int pageNumber = 1;
		if(pageNumberStr!=null){			
			try {
				pageNumber = Integer.parseInt(pageNumberStr);
			} 
			catch (NumberFormatException ex) {
				TracerSingleton.log(
					Constants.NOME_MODULO,
					TracerSingleton.WARNING,
					"DetailBIObjectModule::moveIntoSubreports:: PAGE_NUMBER nullo");
			} 
		}
		
		
		saveSubreportsConfiguration(request, response);
						
		Integer masterReportId = (Integer)session.getAttribute("MASTER_ID");
		SourceBean subreports = (SourceBean)session.getAttribute("SUBREPORTS");	
		setSubreportsLookupContext(masterReportId, subreports, pageNumber);	
	
		response.setAttribute("editLoopback", "true");		
	}
	
	private void moveIntoSubreportsListHandler(SourceBean request, SourceBean response, boolean moveNext) throws EMFUserError, SourceBeanException {
		String pageNumberStr = (String)session.getAttribute("PAGE_NUMBER");
		int pageNumber = 1;
		if(pageNumberStr!=null){			
			try {
				pageNumber = Integer.parseInt(pageNumberStr);
			} 
			catch (NumberFormatException ex) {
				TracerSingleton.log(
					Constants.NOME_MODULO,
					TracerSingleton.WARNING,
					"DetailBIObjectModule::moveIntoSubreports:: PAGE_NUMBER nullo");
			} 
		}
		
		
		Integer masterReportId = (Integer)session.getAttribute("MASTER_ID");
		updateSubreports(request, response, masterReportId);
		SourceBean subreports = (SourceBean)session.getAttribute("SUBREPORTS");	
		if(moveNext) pageNumber += 1; 
		else pageNumber -= 1;
		setSubreportsLookupContext(masterReportId, subreports, pageNumber);	
			
		
		response.setAttribute("editLoopback", "true");
	}
	
	
	
	
	
	
	
	

	
	private void lookupReturnBackHandler(SourceBean request, SourceBean response) throws SourceBeanException, EMFUserError {

		BIObject obj = (BIObject) session.getAttribute("LookupBIObject");
		BIObjectParameter biObjPar = (BIObjectParameter) session.getAttribute("LookupBIObjectParameter");
		String modality = (String) session.getAttribute("modality");
		if(modality == null) modality = (String)session.getAttribute("modalityBkp");
		
		actor = (String) session.getAttribute("actor");
		session.delAttribute("LookupBIObject");
		session.delAttribute("LookupBIObjectParameter");
		session.delAttribute("modality");
		session.delAttribute("modalityBkp");
		session.delAttribute("actor");
		response.setAttribute(SpagoBIConstants.ACTOR, actor);
		fillResponse(response);
		reloadCMSInformation(obj);
		prepareBIObjectDetailPage(response, obj, biObjPar, biObjPar.getId().toString(), modality, false, false);
		response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
		
	}


	private void lookupReturnHandler(SourceBean request, SourceBean response) throws EMFUserError, SourceBeanException {
		
		BIObject obj = (BIObject) session.getAttribute("LookupBIObject");
		SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","lookupReturnHandler",
				" BIObject = " + obj);
		
		BIObjectParameter biObjPar = (BIObjectParameter) session.getAttribute("LookupBIObjectParameter");
		SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","lookupReturnHandler",
				" BIObjectParameter = " + biObjPar);
		
		String modality = (String) session.getAttribute("modality");
		if(modality == null) modality = (String)session.getAttribute("modalityBkp");
		SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","lookupReturnHandler",
				" modality = " + modality);
		
		
		actor = (String) session.getAttribute("actor");
		SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","lookupReturnHandler",
				" actor = " + actor);
		
		
		String newParIdStr = (String) request.getAttribute("PAR_ID");
		Integer newParIdInt = Integer.valueOf(newParIdStr);
		Parameter newParameter = new Parameter();
		newParameter.setId(newParIdInt);
		biObjPar.setParameter(newParameter);
		biObjPar.setParID(newParIdInt);
		
		delateLoopbackContext();
		
		response.setAttribute(SpagoBIConstants.ACTOR, actor);
		fillResponse(response);
		reloadCMSInformation(obj);
		prepareBIObjectDetailPage(response, obj, biObjPar, biObjPar.getId().toString(), modality, false, false);
		response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
				
	}

	/**
	 * Gets the detail of a BI object  choosed by the user from the 
	 * BI objects list. It reaches the key from the request and asks to the DB all detail
	 * BI objects information, by calling the method <code>loadBIObjectForDetail</code>.
	 *   
	 * @param request The request Source Bean
	 * @param response The response Source Bean
	 * @throws EMFUserError If an exception occurs
	 */
	private void getDetailObject(SourceBean request, SourceBean response)
			throws EMFUserError {
		try {
			response.setAttribute(SpagoBIConstants.ACTOR, actor);
			String path = (String) request
					.getAttribute(ObjectsTreeConstants.PATH);
			BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(
					path);
			if (obj == null) {
				SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE,
						"DetailBIObjectModule", "getDetailObject",
						"BIObject with path "+path+" cannot be retrieved.");
				EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 1040);
				errorHandler.addError(error);
				return;
			}
			Object selectedObjParIdObj = request.getAttribute("selected_obj_par_id");
			String selectedObjParIdStr = "";
			if (selectedObjParIdObj != null) {
				int selectedObjParId = findBIObjParId(selectedObjParIdObj);
				selectedObjParIdStr = new Integer(selectedObjParId).toString();
			}
			fillResponse(response);
			prepareBIObjectDetailPage(response, obj, null, selectedObjParIdStr, ObjectsTreeConstants.DETAIL_MOD, true, true);
			response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
		} catch (Exception ex) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE,
					"DetailBIObjectModule", "getDetailObject",
					"Cannot fill response container", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	/**
	 * Inserts/Modifies the detail of a BI Object according to the user request.
	 * When a BI Object is modified, the <code>modifyBIObject</code> method is
	 * called; when a new BI Object is added, the <code>insertBIObject</code>method
	 * is called. These two cases are differentiated by the <code>mod</code>
	 * String input value .
	 * 
	 * @param request
	 *            The request information contained in a SourceBean Object
	 * @param mod
	 *            A request string used to differentiate insert/modify
	 *            operations
	 * @param response
	 *            The response SourceBean
	 * @throws EMFUserError
	 *             If an exception occurs
	 * @throws SourceBeanException
	 *             If a SourceBean exception occurs
	 */
	private void modBIObject(SourceBean request, String mod, SourceBean response)
		throws EMFUserError, SourceBeanException {

		try {
			// as the request is a multipart request, the fillRequestContainer popolates correctly the service request
			fillRequestContainer(request, errorHandler);
			BIObject obj = recoverBIObjectDetails(request, mod);
			response.setAttribute(SpagoBIConstants.ACTOR, actor);
			String selectedObjParIdStr = null;
			if (mod.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_MOD)) {
				BIObjectParameter biObjPar = null;
				Object selectedObjParIdObj = request
						.getAttribute("selected_obj_par_id");
				Object deleteBIObjectParameter = request.getAttribute("deleteBIObjectParameter");
				
				if (selectedObjParIdObj != null) {
					// it is requested to view another BIObjectParameter than the one visible
					int selectedObjParId = findBIObjParId(selectedObjParIdObj);
					selectedObjParIdStr = new Integer (selectedObjParId).toString();
					String saveBIObjectParameter = (String) request.getAttribute("saveBIObjectParameter");
					if (saveBIObjectParameter != null && saveBIObjectParameter.equalsIgnoreCase("yes")) {
						// it is requested to save the visible BIObjectParameter
						validateFields("BIObjectParameterValidation", "PAGE");
						biObjPar = recoverBIObjectParameterDetails(request, obj.getId());
						// If it's a new BIObjectParameter or if the Parameter was changed controls 
						// that the BIObjectParameter url name is not already in use
						urlNameControl(obj.getId(), biObjPar);
						fillResponse(response);
						reloadCMSInformation(obj);
			    		// if there are some errors, exits without writing into DB
			    		if(!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
			    			prepareBIObjectDetailPage(response, obj, biObjPar, biObjPar.getId().toString(), ObjectsTreeConstants.DETAIL_MOD, false, false);
			    			response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
							return;
						}
						IBIObjectParameterDAO objParDAO = DAOFactory.getBIObjectParameterDAO();
						if (biObjPar.getId().intValue() == -1) {
							// it is requested to insert a new BIObjectParameter
							objParDAO.insertBIObjectParameter(biObjPar);
						} else {
							// it is requested to modify a BIObjectParameter
							objParDAO.modifyBIObjectParameter(biObjPar);
						}
						prepareBIObjectDetailPage(response, obj, null, selectedObjParIdStr, ObjectsTreeConstants.DETAIL_MOD, false, true);
						response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
						return;
					} else {
						fillResponse(response);
						reloadCMSInformation(obj);
						prepareBIObjectDetailPage(response, obj, null, selectedObjParIdStr, ObjectsTreeConstants.DETAIL_MOD, false, true);
						response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
		    			// exits without writing into DB
		    			return;
					}
					
				} else if (deleteBIObjectParameter != null) {
					// it is requested to delete the visible BIObjectParameter
					int objParId = findBIObjParId(deleteBIObjectParameter);
					Integer objParIdInt = new Integer(objParId);
					checkForDependancies(objParIdInt);
					fillResponse(response);
					reloadCMSInformation(obj);
		    		if(!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
		    			prepareBIObjectDetailPage(response, obj, biObjPar, objParIdInt.toString(), ObjectsTreeConstants.DETAIL_MOD, false, false);
		    			response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
						return;
					}
					IObjParuseDAO objParuseDAO = DAOFactory.getObjParuseDAO();
					// deletes all the ObjParuse objects associated to this BIObjectParameter 
					List objParuses = objParuseDAO.loadObjParuses(new Integer(objParId));
					if (objParuses != null && objParuses.size() > 0) {
						Iterator it = objParuses.iterator();
						while (it.hasNext()) {
							ObjParuse objParuse = (ObjParuse) it.next();
							objParuseDAO.eraseObjParuse(objParuse);
						}
					}
					// then deletes the BIObjectParameter
					IBIObjectParameterDAO objParDAO = DAOFactory.getBIObjectParameterDAO();
					BIObjectParameter objPar = objParDAO.loadForDetailByObjParId(new Integer(objParId));
					objParDAO.eraseBIObjectParameter(objPar);
					selectedObjParIdStr = "";
					prepareBIObjectDetailPage(response, obj, null, selectedObjParIdStr, ObjectsTreeConstants.DETAIL_MOD, false, true);
					response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
					return;
					
				} else {
					// It is request to save the BIObject with also the visible BIObjectParameter
					biObjPar = recoverBIObjectParameterDetails(request, obj.getId());
					// If a new BIParameter was visualized and no fields were inserted, the BIParameter is not validated and saved
					boolean biParameterToBeSaved = true;
					if (GenericValidator.isBlankOrNull(biObjPar.getLabel()) 
							&& biObjPar.getId().intValue() == -1 
							&& GenericValidator.isBlankOrNull(biObjPar.getParameterUrlName())
							&& biObjPar.getParID().intValue() == -1)
						biParameterToBeSaved = false;
					if (biParameterToBeSaved) {
						validateFields("BIObjectParameterValidation", "PAGE");
						// If it's a new BIObjectParameter or if the Parameter was changed controls 
						// that the BIObjectParameter url name is not already in use
						urlNameControl(obj.getId(), biObjPar);
					}

					validateFields("BIObjectValidation", "PAGE");
		    		// if there are some errors, exits without writing into DB
					if(!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
						reloadCMSInformation(obj);
						fillResponse(response);
						prepareBIObjectDetailPage(response, obj, biObjPar, biObjPar.getId().toString(), ObjectsTreeConstants.DETAIL_MOD, false, false);
						response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
						return;
					}
					// it is requested to modify the main values of the BIObject
					DAOFactory.getBIObjectDAO().modifyBIObject(obj);
	    			// reloads the BIObject with the updated CMS information
	    			obj = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(obj.getPath());
	    			
	    			if (biParameterToBeSaved) {
						IBIObjectParameterDAO objParDAO = DAOFactory.getBIObjectParameterDAO();
						if (biObjPar.getId().intValue() == -1) {
							// it is requested to insert a new BIObjectParameter
							objParDAO.insertBIObjectParameter(biObjPar);
							// reload the BIObjectParameter with the given label
							biObjPar = reloadBIObjectParameter(obj.getId(), biObjPar.getParameterUrlName());
						} else {
							// it is requested to modify a BIObjectParameter
							objParDAO.modifyBIObjectParameter(biObjPar);
						}
						selectedObjParIdStr = biObjPar.getId().toString();
	    			} else selectedObjParIdStr = "-1";
				}

    		} else {
    			validateFields("BIObjectValidation", "PAGE");
	    		// if there are some errors, exits without writing into DB
    			selectedObjParIdStr = "-1";
    			if(!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
    				obj.setTemplateVersions(new TreeMap());
    				TemplateVersion tv = new TemplateVersion();
    				tv.setVersionName("");
    				obj.setCurrentTemplateVersion(tv);
					fillResponse(response);
					prepareBIObjectDetailPage(response, obj, null, selectedObjParIdStr, ObjectsTreeConstants.DETAIL_INS, false, false);
					response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
					return;
				}
    			// inserts into DB the new BIObject
    			DAOFactory.getBIObjectDAO().insertBIObject(obj);
    			// reloads the BIObject with the correct Id and empty CMS information
    			obj = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(obj.getPath());
    		}
			
			Object saveAndGoBack = request.getAttribute("saveAndGoBack");
			if (saveAndGoBack != null) {
				// it is request to save the main BIObject details and to go back
				response.setAttribute("loopback", "true");
			} else {
				// it is requested to save and remain in the BIObject detail page
				fillResponse(response);
				prepareBIObjectDetailPage(response, obj, null, selectedObjParIdStr, ObjectsTreeConstants.DETAIL_MOD, true, true);
				response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
			}
			
		} catch (Exception ex) {			
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","modBIObject","Cannot fill response container", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	/**
	 * Controls if there are some BIObjectParameter objects that depend by the BIObjectParameter object
	 * at input, given its id.
	 * 
	 * @param objParFatherId The id of the BIObjectParameter object to check
	 * @throws EMFUserError
	 */
	private void checkForDependancies(Integer objParFatherId) throws EMFUserError {
		IObjParuseDAO objParuseDAO = DAOFactory.getObjParuseDAO();
		List objParametersCorrelated = objParuseDAO.getDependencies(objParFatherId);
		if (objParametersCorrelated != null && objParametersCorrelated.size() > 0) {
			HashMap params = new HashMap();
			params.put(AdmintoolsConstants.PAGE,
					DetailBIObjectModule.MODULE_PAGE);
			Vector v = new Vector();
			v.add(objParametersCorrelated.toString());
			EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 1049,
					v, params);
			errorHandler.addError(error);
		}
	}


	/**
	 * Controls that the BIObjectParameter url name is not in use by another BIObjectParameter
	 * 
	 * @param objId The id of the document
	 * @param biObjPar The BIObjectParameter to control before inserting/modifying
	 */
	private void urlNameControl(Integer objId, BIObjectParameter biObjPar) {
		if (objId == null || objId.intValue() < 0 || biObjPar == null || biObjPar.getParameterUrlName() == null) 
			return;
		try {
			IBIObjectParameterDAO objParDAO = DAOFactory.getBIObjectParameterDAO();
			List paruses = objParDAO.loadBIObjectParametersById(objId);
			Iterator it = paruses.iterator();
			while (it.hasNext()) {
				BIObjectParameter aBIObjectParameter = (BIObjectParameter) it.next();
				if (aBIObjectParameter.getParameterUrlName().equals(biObjPar.getParameterUrlName()) 
						&& !aBIObjectParameter.getId().equals(biObjPar.getId())) {
					HashMap params = new HashMap();
					params.put(AdmintoolsConstants.PAGE,
							DetailBIObjectModule.MODULE_PAGE);
					EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 1046,
							new Vector(), params);
					errorHandler.addError(error);
				}
			}
		} catch (EMFUserError e) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailBIObjectModule","urlNameControl","Error while url name control", e);
		}
		
	}
	
	private BIObjectParameter reloadBIObjectParameter(Integer objId, String objParUrlName) throws EMFInternalError {
		if (objId == null || objId.intValue() < 0 || objParUrlName == null || objParUrlName.trim().equals(""))
			throw new EMFInternalError(EMFErrorSeverity.ERROR, "Invalid input data for method reloadBIObjectParameter in DetailBIObjectModule");
		BIObjectParameter objPar = null;
		try {
			IBIObjectParameterDAO objParDAO = DAOFactory.getBIObjectParameterDAO();
			List paruses = objParDAO.loadBIObjectParametersById(objId);
			Iterator it = paruses.iterator();
			while (it.hasNext()) {
				BIObjectParameter aBIObjectParameter = (BIObjectParameter) it.next();
				if (aBIObjectParameter.getParameterUrlName().equals(objParUrlName)) {
					objPar = aBIObjectParameter;
					break;
				}
			}
		} catch (EMFUserError e) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailBIObjectModule","reloadBIObjectParameter","Cannot reload BIObjectParameter", e);
		}
		if (objPar == null) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailBIObjectModule","reloadBIObjectParameter","BIObjectParameter with url name '"+ objParUrlName +"' not found.");
			objPar = createNewBIObjectParameter(objId);
		}
		return objPar;
	}

	/**
	 * Reloads the CMS information of a BIObject: the versioned templates.
	 * Changes the current version template if it is required but without 
	 * writing on CMS.
	 * Other details remain unchanged.
	 * 
	 * @param obj THe BIObject that will be filled with its own CMS information.
	 * @throws EMFUserError
	 */
	private void reloadCMSInformation(BIObject obj) throws EMFUserError {
		IBIObjectDAO objDao = DAOFactory.getBIObjectDAO();
		BIObject temp = objDao.loadBIObjectForDetail(obj.getPath());
		TreeMap templates = temp.getTemplateVersions();
		TemplateVersion currentVersion = temp.getCurrentTemplateVersion();
		obj.setTemplateVersions(templates);
		String requiredCurrVerName = obj.getNameCurrentTemplateVersion();
		if (requiredCurrVerName != null && !requiredCurrVerName.equalsIgnoreCase(currentVersion.getVersionName())) {
			// it is requested to change the current version template
			Collection values = templates.values();
			Iterator it = values.iterator();
			while (it.hasNext()) {
				TemplateVersion template = (TemplateVersion) it.next();
				if (requiredCurrVerName.equals(template.getVersionName())) {
					obj.setCurrentTemplateVersion(template);
					break;
				}
			}
		} else obj.setCurrentTemplateVersion(currentVersion);
		
		if (obj.getCurrentTemplateVersion() == null) {
			// the required version template was not found
			obj.setCurrentTemplateVersion(currentVersion);
			SpagoBITracer.warning(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","reloadCMSInformation","The required version template was not found");
		}
	}


	/**
	 * Fills the response SourceBean with the elements that will be displayed in the BIObject detail page: 
	 * the BIObject itself and the required BIObjectParameter.
	 * 
	 * @param response The response SourceBean to be filled
	 * @param obj The BIObject to be displayed
	 * @param biObjPar The BIObjectParameter to be displayed: if it is null the selectedObjParIdStr will be considered.
	 * @param selectedObjParIdStr The id of the BIObjectParameter to be displayed.
	 * 			If it is blank or null the first BIObjectParameter will be diplayed but in case the BIObject 
	 * 			has no BIObjectParameter a new empty BIObjectParameter will be displayed.
	 * 			If it is "-1" a new empty BIObjectParameter will be displayed.
	 * @param detail_mod The modality
	 * @param initialBIObject Boolean: if true the BIObject to be visualized is the initial BIObject and 
	 * 			a clone will be put in session.
	 * @param initialBIObjectParameter Boolean: if true the BIObjectParameter to be visualized is the initial BIObjectParameter and 
	 * 			a clone will be put in session.
	 * @throws SourceBeanException
	 * @throws EMFUserError
	 */
	private void prepareBIObjectDetailPage(SourceBean response, BIObject obj,
			BIObjectParameter biObjPar, String selectedObjParIdStr,
			String detail_mod, boolean initialBIObject,
			boolean initialBIObjectParameter) throws SourceBeanException,
			EMFUserError {
		
		List biObjParams = DAOFactory.getBIObjectParameterDAO()
				.loadBIObjectParametersById(obj.getId());
		obj.setBiObjectParameters(biObjParams);
		if (biObjPar == null) {
			if (selectedObjParIdStr == null || "".equals(selectedObjParIdStr)) {
				if (biObjParams == null || biObjParams.size() == 0) {
					biObjPar = createNewBIObjectParameter(obj.getId());
					selectedObjParIdStr = "-1";
				} else {
					biObjPar = (BIObjectParameter) biObjParams.get(0);
					selectedObjParIdStr = biObjPar.getId().toString();
				}
			} else if ("-1".equals(selectedObjParIdStr)) {
				biObjPar = createNewBIObjectParameter(obj.getId());
				selectedObjParIdStr = "-1";
			} else {
				int selectedObjParId = Integer.parseInt(selectedObjParIdStr);
				Iterator it = biObjParams.iterator();
				while (it.hasNext()) {
					biObjPar = (BIObjectParameter) it.next();
					if (biObjPar.getId().equals(new Integer(selectedObjParId)))
						break;
				}
			}
		}

		response.setAttribute("selected_obj_par_id", selectedObjParIdStr);
		response.setAttribute(NAME_ATTR_OBJECT, obj);
		response.setAttribute(NAME_ATTR_OBJECT_PAR, biObjPar);
		
		SpagoBITracer.debug(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule", "prepareBIObjectDetailPage", "XXXXXXXXXX " + detail_mod);
		
		
		response.setAttribute(ObjectsTreeConstants.MODALITY, detail_mod);
		
		if (initialBIObject) {
			BIObject objClone = clone(obj);
			session.setAttribute("initial_BIObject", objClone);
		}

		if (initialBIObjectParameter) {
			BIObjectParameter biObjParClone = clone(biObjPar);
			session.setAttribute("initial_BIObjectParameter", biObjParClone);
		}
		
	}


	private BIObjectParameter clone (BIObjectParameter biObjPar) {
		
		if (biObjPar == null) return null;
		
		BIObjectParameter objParClone = new BIObjectParameter();
		objParClone.setId(biObjPar.getId());
		objParClone.setBiObjectID(biObjPar.getBiObjectID());
		objParClone.setLabel(biObjPar.getLabel());
		objParClone.setModifiable(biObjPar.getModifiable());
		objParClone.setMultivalue(biObjPar.getMultivalue());
		objParClone.setParameter(biObjPar.getParameter());
		objParClone.setParameterUrlName(biObjPar.getParameterUrlName());
		objParClone.setParameterValues(biObjPar.getParameterValues());
		objParClone.setParID(biObjPar.getParID());
		objParClone.setProg(biObjPar.getProg());
		objParClone.setRequired(biObjPar.getRequired());
		objParClone.setVisible(biObjPar.getVisible());
		return objParClone;
	}


	private BIObject clone (BIObject obj) {
		
		if (obj == null) return null;
		
		BIObject objClone = new BIObject();
		objClone.setBiObjectTypeCode(obj.getBiObjectTypeCode());
		objClone.setBiObjectTypeID(obj.getBiObjectTypeID());
		objClone.setCurrentTemplateVersion(obj.getCurrentTemplateVersion());
		objClone.setDescription(obj.getDescription());
		objClone.setEncrypt(obj.getEncrypt());
		objClone.setVisible(obj.getVisible());
		objClone.setEngine(obj.getEngine());
		objClone.setId(obj.getId());
		objClone.setLabel(obj.getLabel());
		objClone.setName(obj.getName());
		objClone.setNameCurrentTemplateVersion(obj.getNameCurrentTemplateVersion());
		objClone.setPath(obj.getPath());
		objClone.setRelName(obj.getRelName());
		objClone.setStateCode(obj.getStateCode());
		objClone.setStateID(obj.getStateID());
		objClone.setTemplate(obj.getTemplate());
		
		return objClone;
	}


	private BIObjectParameter recoverBIObjectParameterDetails(SourceBean request, Integer biobjIdInt) {

		String idStr = (String) request.getAttribute("objParId");
		Integer idInt = null;
		if (idStr == null || idStr.trim().equals("")) idInt = new Integer (-1);
		else idInt = new Integer(idStr);
		String parIdStr = (String) request.getAttribute("par_id");
		Integer parIdInt = null;
		if (parIdStr == null || parIdStr.trim().equals("")) 
			parIdInt = new Integer (-1);
		else parIdInt = new Integer (parIdStr);
		String label = (String) request.getAttribute("objParLabel");
	    String parUrlNm = (String)request.getAttribute("parurl_nm");
		String reqFl = (String)request.getAttribute("req_fl");
		Integer reqFlBD = new Integer(reqFl);
		String modFl = (String) request.getAttribute("mod_fl");
		Integer modFlBD = new Integer(modFl);
		String viewFl = (String) request.getAttribute("view_fl");
		Integer viewFlBD = new Integer(viewFl);
		String multFl = (String) request.getAttribute("mult_fl");
		Integer multFlBD = new Integer(multFl);
	   
		BIObjectParameter objPar  = new BIObjectParameter();
		objPar.setId(idInt);
		objPar.setBiObjectID(biobjIdInt);
		objPar.setParID(parIdInt);
        Parameter par = new Parameter();
        par.setId(parIdInt);
        objPar.setParameter(par);
        objPar.setLabel(label);
        objPar.setParameterUrlName(parUrlNm);
        objPar.setRequired(reqFlBD);
        objPar.setModifiable(modFlBD);
        objPar.setVisible(viewFlBD);
        objPar.setMultivalue(multFlBD);
 
		return objPar;
	}


	private BIObjectParameter createNewBIObjectParameter(Integer objId) {
		BIObjectParameter biObjPar = new BIObjectParameter();
		biObjPar.setId(new Integer(-1));
		biObjPar.setParID(new Integer(-1));
		biObjPar.setBiObjectID(objId);
		biObjPar.setLabel("");
		biObjPar.setModifiable(new Integer(0));
		biObjPar.setMultivalue(new Integer(0));
		biObjPar.setParameter(null);
		biObjPar.setParameterUrlName("");
		biObjPar.setProg(new Integer(0));
		biObjPar.setRequired(new Integer(0));
		biObjPar.setVisible(new Integer(0));
		return biObjPar;
	}


	private BIObject recoverBIObjectDetails(SourceBean request, String mod) throws EMFUserError {
		
		BIObject obj = new BIObject();
		
		UploadedFile uploaded = (UploadedFile) request
				.getAttribute("UPLOADED_FILE");
		String idStr = (String) request.getAttribute("id");
		Integer id = new Integer(idStr);
		String name = (String) request.getAttribute("name");
		String label = (String) request.getAttribute("label");
		String description = (String) request.getAttribute("description");
		String relname = (String) request.getAttribute("relname");
		String criptableStr = (String) request.getAttribute("criptable");
		Integer encrypt = new Integer(criptableStr);
		String visibleStr = (String) request.getAttribute("visible");
		Integer visible = new Integer(visibleStr);
		
		String typeAttr = (String) request.getAttribute("type");
		StringTokenizer tokentype = new StringTokenizer(typeAttr, ",");
		String typeIdStr = tokentype.nextToken();
		Integer typeIdInt = new Integer(typeIdStr);
		String typeCode = tokentype.nextToken();
		String engineIdStr = (String) request.getAttribute("engine");
		Engine engine = null;
		if (engineIdStr == null) {
			List engines = DAOFactory.getEngineDAO().loadAllEngines();
			if (engines.size() > 0) engine = (Engine) engines.get(0);
		} else {
			Integer engineIdInt = new Integer(engineIdStr);
			engine = DAOFactory.getEngineDAO().loadEngineByID(engineIdInt);
		}

		String stateAttr = (String) request.getAttribute("state");
		StringTokenizer tokenState = new StringTokenizer(stateAttr, ",");
		String stateIdStr = tokenState.nextToken();
		Integer stateId = new Integer(stateIdStr);
		String stateCode = tokenState.nextToken();
		
		if (mod.equalsIgnoreCase(ObjectsTreeConstants.DETAIL_INS)) {
			String pathParent = "";
			List parentPaths = request.getAttributeAsList(ObjectsTreeConstants.PATH_PARENT);
			if(parentPaths.size() != 1) {
				HashMap errorParams = new HashMap();
				errorParams.put(AdmintoolsConstants.PAGE, TreeObjectsModule.MODULE_PAGE);
				errorParams.put(SpagoBIConstants.ACTOR, actor);
				EMFUserError error = null;
				if(parentPaths.size()<1) {
					error = new EMFUserError(EMFErrorSeverity.ERROR, 1008, new Vector(), errorParams);
				} else {
					error = new EMFUserError(EMFErrorSeverity.ERROR, 1009, new Vector(), errorParams);
				}
				getErrorHandler().addError(error);
			} else {
				pathParent = (String)parentPaths.get(0);
			}				
			obj.setPath(pathParent + "/" + typeCode + "_" + label);
		} else {
			String pathObj = (String) request.getAttribute(ObjectsTreeConstants.PATH);
			obj.setPath(pathObj);
			String nameCurTempVer = (String) request.getAttribute("versionTemplate");
			if (nameCurTempVer != null) {
				obj.setNameCurrentTemplateVersion(nameCurTempVer);
			}
		}
		
		obj.setTemplate(uploaded);
		obj.setBiObjectTypeCode(typeCode);
		obj.setBiObjectTypeID(typeIdInt);
		obj.setDescription(description);
		obj.setEncrypt(encrypt);
		obj.setVisible(visible);
		obj.setEngine(engine);
		obj.setId(id);
		obj.setName(name);
		obj.setLabel(label);
		obj.setRelName(relname);
		obj.setStateCode(stateCode);
		obj.setStateID(stateId);
		
		return obj;
	}


	public int findBIObjParId (Object objParIdObj) {
		String objParIdStr = "";
		if (objParIdObj instanceof String) {
			objParIdStr = (String) objParIdObj;
		} else if (objParIdObj instanceof List) {
			List objParIdList = (List) objParIdObj;
			Iterator it = objParIdList.iterator();
			while (it.hasNext()) {
				Object item = it.next();
				if (item instanceof SourceBean) continue;
				if (item instanceof String) objParIdStr = (String) item;
			}
		}
		int objParId = Integer.parseInt(objParIdStr);
		return objParId;
	}
	
	/**
	 * Deletes a BI Object choosed by user from the engines list.
	 * 
	 * @param request	The request SourceBean
	 * @param mod	A request string used to differentiate delete operation
	 * @param response	The response SourceBean
	 * @throws EMFUserError	If an Exception occurs
	 * @throws SourceBeanException If a SourceBean Exception occurs
	 */
	private void delDetailObject(SourceBean request, String mod, SourceBean response)
		throws EMFUserError, SourceBeanException {
		BIObject obj = null;
		try {
			String path = (String)request.getAttribute(ObjectsTreeConstants.PATH);
        	IBIObjectDAO objdao = DAOFactory.getBIObjectDAO();
			obj = objdao.loadBIObjectForDetail(path);
			objdao.eraseBIObject(obj);
		} catch (Exception ex) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","delDetailObject","Cannot erase object", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		response.setAttribute("loopback", "true");
		response.setAttribute(ObjectsTreeConstants.PATH_PARENT, obj.getPath());
		response.setAttribute(SpagoBIConstants.ACTOR, actor);
	}
	
	/**
	 * Instantiates a new <code>BIObject<code> object when a new BI object insertion 
	 * is required, in order to prepare the page for the insertion.
	 * 
	 * @param response The response SourceBean
	 * @throws EMFUserError If an Exception occurred
	 */
	private void newBIObject(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			response.setAttribute(ObjectsTreeConstants.MODALITY, ObjectsTreeConstants.DETAIL_INS);
            BIObject obj = new BIObject();
            obj.setId(new Integer(0));
            obj.setEngine(null);
            obj.setDescription("");
            obj.setLabel("");
            obj.setName("");
            obj.setEncrypt(new Integer(0));
            obj.setVisible(new Integer(1));
            obj.setRelName("");
            obj.setStateID(null);
            obj.setStateCode("");
            obj.setBiObjectTypeID(null);
            obj.setBiObjectTypeCode("");
            TemplateVersion curVer = new TemplateVersion();
            curVer.setVersionName("");
            curVer.setDataLoad("");
            obj.setCurrentTemplateVersion(curVer);
            obj.setTemplateVersions(new TreeMap());
            response.setAttribute(NAME_ATTR_OBJECT, obj);
            response.setAttribute(SpagoBIConstants.ACTOR, actor);
            fillResponse(response);
            response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
       
		} catch (Exception ex) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","newBIObject","Cannot prepare page for the insertion", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
	/**
	 * Fills the response SourceBean with some needed BI Objects information.
	 * 
	 * @param response The SourceBean to fill
	 */
	private void fillResponse(SourceBean response) {
		try {
			IDomainDAO domaindao = DAOFactory.getDomainDAO();
	        List types = domaindao.loadListDomainsByType("BIOBJ_TYPE");
	        List states = domaindao.loadListDomainsByType("STATE");
	        List engines =  DAOFactory.getEngineDAO().loadAllEngines();
		    response.setAttribute(NAME_ATTR_LIST_ENGINES, engines);
		    response.setAttribute(NAME_ATTR_LIST_OBJ_TYPES, types);
		    response.setAttribute(NAME_ATTR_LIST_STATES, states);
		    
		    String ATTR_PATH_SYS_FUNCT = "SPAGOBI.CMS_PATHS.SYSTEM_FUNCTIONALITIES_PATH";
            RequestContainer requestContainer = getRequestContainer();
            SessionContainer sessionContainer = requestContainer.getSessionContainer();
            SessionContainer permanentSession = sessionContainer.getPermanentContainer();
            IEngUserProfile profile = (IEngUserProfile)permanentSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		    SourceBean pathSysFunctSB = (SourceBean)ConfigSingleton.getInstance().getAttribute(ATTR_PATH_SYS_FUNCT);
		    String pathSysFunct = pathSysFunctSB.getCharacters();
	        TreeObjectsDAO objDao = new TreeObjectsDAO();
	        SourceBean dataResponseSysFunct = objDao.getXmlTreeObjects(pathSysFunct, profile);
			response.setAttribute(dataResponseSysFunct);
		    
		} catch (Exception e) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","fillResponse","Cannot fill the response ", e  );
		}
	}
	
	/**
	 * Fills the request container object with some BIObject and BIObjectParameter information contained into
	 * the request Source Bean (they are all attributes). It is useful for validation process.
	 * 
	 * @param request The request Source Bean 
	 * @throws SourceBeanException If any exception occurred
	 */
	public void fillRequestContainer (SourceBean request, EMFErrorHandler errorHandler) throws Exception{
		RequestContainer req = getRequestContainer();
		String label = (String)request.getAttribute("label");
		String name = (String)request.getAttribute("name");
		String description = (String)request.getAttribute("description");
		String relName = (String)request.getAttribute("relName");
		String engine = (String)request.getAttribute("engine");
		String state = (String)request.getAttribute("state");
		String path = "";
		String objParLabel = (String)request.getAttribute("objParLabel");
		String parurl_nm = (String)request.getAttribute("parurl_nm");
		String par_Id = (String)request.getAttribute("par_Id");
		String req_fl = (String)request.getAttribute("req_fl");
		String mod_fl = (String) request.getAttribute("mod_fl");
		String view_fl = (String) request.getAttribute("view_fl");
		String mult_fl = (String) request.getAttribute("mult_fl");
		Object pathParentObj = request.getAttribute("PATH_PARENT");
		if( (pathParentObj != null) && (!(pathParentObj instanceof String))) {
			errorHandler.addError(new EMFUserError(EMFErrorSeverity.ERROR, 1032));
		}else {
			String pathParent = (String)pathParentObj;
			if(pathParent != null){
				path = pathParent;
			}
		}
		SourceBean _serviceRequest = req.getServiceRequest();
		_serviceRequest.setAttribute("label",label);
		_serviceRequest.setAttribute("description",description);
		_serviceRequest.setAttribute("name",name);
		_serviceRequest.setAttribute("relName",relName);
		if (engine == null) {
			List engines = DAOFactory.getEngineDAO().loadAllEngines();
			if (engines.size() > 0) {
				engine = ((Engine) engines.get(0)).getId().toString();
			}
		}
		_serviceRequest.setAttribute("engine", engine);
		_serviceRequest.setAttribute("state", state);
		_serviceRequest.setAttribute("path", path);
		_serviceRequest.setAttribute("objParLabel", objParLabel == null ? "" : objParLabel);
		_serviceRequest.setAttribute("parurl_nm", parurl_nm == null ? "" : parurl_nm);
		_serviceRequest.setAttribute("par_Id", par_Id == null ? "" : par_Id);
		_serviceRequest.setAttribute("req_fl", req_fl == null ? "" : req_fl);
		_serviceRequest.setAttribute("mod_fl", mod_fl == null ? "" : mod_fl);
		_serviceRequest.setAttribute("view_fl", view_fl == null ? "" : view_fl);
		_serviceRequest.setAttribute("mult_fl", mult_fl == null ? "" : mult_fl);
	}
	
	public boolean validateFields(String businessName, String businessType) throws Exception {
		RequestContainer requestContainer = getRequestContainer();
		ResponseContainer responseContainer = getResponseContainer();
		boolean validate = ValidationCoordinator.validate(businessType, businessName, requestContainer, responseContainer);
		return validate;
	}
	
	public void eraseVersion(SourceBean request, SourceBean response) throws EMFUserError {
		// get path object and name version
		String ver = (String)request.getAttribute(SpagoBIConstants.VERSION);
		String pathObj = (String)request.getAttribute(SpagoBIConstants.PATH);
		String pathVer = pathObj + "/template";
		// get user profile for cms operation
		//SessionContainer permSess = getRequestContainer().getSessionContainer().getPermanentContainer();
		//IEngUserProfile profile = (IEngUserProfile)permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		try {
            // try to delete the version
			CmsManager manager = new CmsManager();
			DeleteOperation delOp = new DeleteOperation(pathVer, ver);
            manager.execDeleteOperation(delOp);
			// populate response
			BIObject obj = DAOFactory.getBIObjectDAO().loadBIObjectForDetail(pathObj);
			response.setAttribute(SpagoBIConstants.ACTOR, actor);
	        fillResponse(response);
	        prepareBIObjectDetailPage(response, obj, null, "", ObjectsTreeConstants.DETAIL_MOD, false, false);
	        response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
		} catch (Exception e) {
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE, "DetailBIObjectModule","eraseVersion","Cannot erase version", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	/**
	 * Clean the SessionContainer from no more useful objects.
	 * 
	 * @param request The request SourceBean
	 * @param response The response SourceBean
	 * @throws SourceBeanException
	 */
	private void exitFromDetail (SourceBean request, SourceBean response) throws SourceBeanException {
		session.delAttribute("initial_BIObject");
		session.delAttribute("initial_BIObjectParameter");
		session.delAttribute("modality");
		session.delAttribute("actor");
		response.setAttribute("loopback", "true");
	}
	
}