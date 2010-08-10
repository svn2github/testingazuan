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

import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.navigation.LightNavigationManager;
import it.eng.spagobi.bo.ModalitiesValue;
import it.eng.spagobi.bo.ParameterUse;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IParameterUseDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.utilities.BIObjectValidator;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.configuration.ConfigSingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Implements a module which  handles all parameter use modes management: has methods 
 * for parameter use modes load, detail, modify/insertion and deleting operations. 
 * The <code>service</code> method has  a switch for all these operations, differentiated the ones 
 * from the others by a <code>message</code> String.
 * 
 * @author sulis
 */
public class DetailModalitiesModule extends AbstractModule {
	private SourceBean modVal = null;
	private String modalita = "";
	private Boolean back = new Boolean(false);
	
	EMFErrorHandler errorHandler = null;

	public void init(SourceBean config) {
	}

	/**
	 * Reads the operation asked by the user and calls the insertion, modify, detail and 
	 * deletion methods
	 * 
	 * @param request The Source Bean containing all request parameters
	 * @param response The Source Bean containing all response parameters
	 * @throws exception If an exception occurs
	 * 
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		
		String message = (String) request.getAttribute("MESSAGEDET");
		Object lovLookup =  request.getAttribute("loadLovLookup"); 
		SpagoBITracer.debug(AdmintoolsConstants.NAME_MODULE, "DetailModalitiesModule","service","begin of detail Modalities Value modify/visualization service with message =" +message);
		errorHandler = getErrorHandler();
		try {
			if (message == null && lovLookup == null) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
				SpagoBITracer.debug(AdmintoolsConstants.NAME_MODULE, "DetailModalitiesModule", "service", "The message parameter is null");
				throw userError;
			}
			if(lovLookup != null){
				lookupLoadHandler (request,message, response);
		    } else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_SELECT)) {
				String useId = (String)request.getAttribute("useId");
			    getDetailModalities(useId, response);
			} else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_MOD)) {
				nameControl (request, "MODIFY");
				modDetailModalities(request, AdmintoolsConstants.DETAIL_MOD, response);
			} else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_NEW)) {
			    String parId =  request.getAttribute ("PAR_ID").toString();
			    newDetailModalities(parId,response);
				response.setAttribute("PAR_ID",parId); 	
			} else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_INS)) {
				nameControl (request, "INSERT");
				modDetailModalities(request, AdmintoolsConstants.DETAIL_INS, response);
			} else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_DEL)) {
				delDetailModalities(request, AdmintoolsConstants.DETAIL_DEL, response);
			} else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.RETURN_FROM_LOOKUP)){
			  lookupReturnHandler(request,response);	
		    } else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.RETURN_BACK_FROM_LOOKUP)){
		      lookupReturnBackHandler(request,response);
		    	
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
	
	/**
	 * Gets the detail of a parameter use mode choosed by the user from the 
	 * parameter use modes list. It reaches the key from the request and asks 
	 * to the DB all detail parameter use mode information, by calling the 
	 * method <code>loadByUseId</code>.
	 *   
	 * @param key The choosed parameter use mode id key
	 * @param response The response Source Bean
	 * @throws EMFUserError If an exception occurs
	 */
	private void getDetailModalities(String useId, SourceBean response) throws EMFUserError {
		try {
			this.modalita = AdmintoolsConstants.DETAIL_MOD;
			response.setAttribute("modality", modalita);	
			ParameterUse param = DAOFactory.getParameterUseDAO().loadByUseID(Integer.valueOf(useId)); 
			response.setAttribute("modalitiesObj",param );	
			loadValuesDomain(response);
		}   catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailModalitiesModule","getDetailModalities","Cannot fill response container", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	/**
	 * Inserts/Modifies the detail of a parameter use mode according to the user 
	 * request. When a parameter use mode is modified, the <code>modifyParameterUse</code> 
	 * method is called; when a new parameter use mode is added, the <code>insertParameterUse</code>
	 * method is called. These two cases are differentiated by the <code>mod</code> String input value .
	 * 
	 * @param request The request information contained in a SourceBean Object
	 * @param mod A request string used to differentiate insert/modify operations
	 * @param response The response SourceBean 
	 * @throws EMFUserError If an exception occurs
	 * @throws SourceBeanException If a SourceBean exception occurs
	 */
	private void modDetailModalities(SourceBean request, String mod, SourceBean response)
		throws EMFUserError, SourceBeanException {
		ParameterUse param = null;
		try {
			
			
			if (mod.equalsIgnoreCase(AdmintoolsConstants.DETAIL_INS)) {
				param = new ParameterUse();
				param.setId(Integer.valueOf((String)request.getAttribute("par_id")));
				
			} else {
				String useIDStr = (String)request.getAttribute("useId");
				Integer useId = Integer.valueOf(useIDStr);
				param = DAOFactory.getParameterUseDAO().loadByUseID(useId);
			}
			
			
			List inRequestChecksList = request.getAttributeAsList("idCheck");
			List toLoadInParUseCheckList = new ArrayList();
			
			String idTmpStr = null;
			for(int i=0; i<inRequestChecksList.size(); i++) {
				idTmpStr = (String)inRequestChecksList.get(i);
				toLoadInParUseCheckList.add(DAOFactory.getChecksDAO().loadCheckByID(new Integer(idTmpStr)));
			}
			
			List inRequestRoleList = request.getAttributeAsList("idExtRole");
			List roles = new ArrayList();
			for(int i=0; i < inRequestRoleList.size(); i++) {
				String idRoleStr = (String)inRequestRoleList.get(i);
				roles.add(DAOFactory.getRoleDAO().loadByID(new Integer(idRoleStr)));  
			}
			
			String idLovStr = (String)request.getAttribute("idLov");
			String description = (String)request.getAttribute("description");
			String name = (String)request.getAttribute("name");
			String label = (String)request.getAttribute("label");
			param.setName(name);
			param.setDescription(description);
			param.setLabel(label);
			if (idLovStr == null || idLovStr.trim().equals("")) param.setIdLov(Integer.valueOf("-1"));
			else param.setIdLov(Integer.valueOf(idLovStr));
			param.setAssociatedRoles(roles);
			param.setAssociatedChecks(toLoadInParUseCheckList);
			
			validateFields("ParameterUsesDinamicValidator", request);
			
			// If there are some errors exits without writing into DB
			if (!errorHandler.isOKBySeverity("ERROR")) {
				return;
			}
			
			if (mod.equalsIgnoreCase(AdmintoolsConstants.DETAIL_INS)) {
			    DAOFactory.getParameterUseDAO().insertParameterUse(param);
			} else {
			    DAOFactory.getParameterUseDAO().modifyParameterUse(param);
			}
			
		} catch (Exception ex) {			
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailModalitiesModule","modDetailModalities","Cannot fill response container", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		response.setAttribute("loopback", "true");
		String id = param.getId().toString();
		response.setAttribute("PAR_ID",id);
		//response.setAttribute(LightNavigationManager.LIGHT_NAVIGATOR_BACK_TO, "1");
	}
	
	/**
	 * Deletes a parameter use mode choosed by user from the parameter use mode list.
	 * 
	 * @param request	The request SourceBean
	 * @param mod	A request string used to differentiate delete operation
	 * @param response	The response SourceBean
	 * @throws EMFUserError	If an Exception occurs
	 * @throws SourceBeanException If a SourceBean Exception occurs
	 */
	private void delDetailModalities(SourceBean request, String mod, SourceBean response)
		throws EMFUserError, SourceBeanException {
		try {
			String useId = (String)request.getAttribute("useId");
            IParameterUseDAO parusedao = DAOFactory.getParameterUseDAO();
			ParameterUse param = parusedao.loadByUseID(Integer.valueOf(useId));
			parusedao.eraseParameterUse(param);
			response.setAttribute("loopback", "true");
			response.setAttribute("PAR_ID", param.getId().toString());
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailModalitiesModule","delDetailModalities","Cannot fill response container", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	
	/**
	 * Instantiates a new <code>ParameterUse<code> object when a new parameter use mode
	 * insertion is required, in order to prepare the page for the insertion.
	 * 
	 * @param response The response SourceBean
	 * @throws EMFUserError If an Exception occurred
	 */
	private void newDetailModalities(String parIdStr, SourceBean response) throws EMFUserError {
		try {
			this.modalita = AdmintoolsConstants.DETAIL_INS;
			response.setAttribute("modality", modalita);
			ParameterUse param = new ParameterUse();
			Integer parId = new Integer(parIdStr);
			param.setId(parId);
		    param.setIdLov(new Integer(-1));
		    param.setName("");
		    param.setDescription("");
		    param.setLabel("");
		    List listRoles = new ArrayList();
		    param.setAssociatedRoles(listRoles);
		    response.setAttribute("modalitiesObj", param);	
		}   catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailModalitiesModule","newDetailModalities","Cannot prepare page for the insertion", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	/**
	 * Loads all possible domain values  for a parameter use mode. Each of them
	 * is stored in a list of <code>Domain</code> objects put into response.
	 * When the isertion/modify parameters page is loaded, the list is needed to 
	 * show information on the Values Recover Type table.
	 * 
	 * @param response The response SourceBean
	 * @throws EMFUserError If an Exception occurred
	 */
	private void loadValuesDomain(SourceBean response)  throws EMFUserError {
		  try {
		       List list = DAOFactory.getDomainDAO().loadListDomainsByType("INPUT_TYPE");
			   response.setAttribute ("listObj", list);
		  } catch (Exception ex) {
				SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailModalitiesModule","loadValuesDomain","Cannot prepare page for the insertion", ex  );
				throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		  }
	}
	
	/**
	 * During a value constraint insertion/modify, controls if the name given to the list of values 
	 * is yet in use.
	 * 
	 * @param request	The request Source Bean
	 * @param operation Defines if the operation is of insertion or modify
	 * @throws EMFUserError If any Exception occurred
	 */
	private void nameControl (SourceBean request, String operation ) throws EMFUserError {
		//String useId = (String)request.getAttribute("useId");
		//Integer id = Integer.valueOf(useId);
		String label = (String)request.getAttribute("label"); 
		//ParameterUse param = DAOFactory.getParameterUseDAO().loadByUseID(id);
		String par_id = (String)request.getAttribute("par_id");
		Integer parId = Integer.valueOf(par_id);
		List allParametersUse = DAOFactory.getParameterUseDAO().loadParametersUseByParId(parId);
		
		//cannot have two ParametersUse with the same name and the same par_id
		if (operation.equalsIgnoreCase("INSERT")){
			Iterator i = allParametersUse.iterator();
			while (i.hasNext()) {
				ParameterUse mod = (ParameterUse)i.next();
				String modLabel = mod.getLabel();
				String modUseId = String.valueOf(mod.getUseID());
			
				if (modLabel.equals(label)) {
					HashMap params = new HashMap();
					params.put(AdmintoolsConstants.PAGE, ListParameterUsesModule.MODULE_PAGE);
					params.put(AdmintoolsConstants.ID_DOMAIN, par_id);
					EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 1025, new Vector(), params);
					errorHandler.addError(error);
				}
			}
		}else{
			String currentUseId = (String)request.getAttribute("useId");
			Iterator i = allParametersUse.iterator();
			while (i.hasNext()) {
				ParameterUse mod = (ParameterUse)i.next();
				String modLabel = mod.getLabel();
				String modUseId = String.valueOf(mod.getUseID());
			
				if (modLabel.equals(label) && (!currentUseId.equalsIgnoreCase(modUseId))) {
					HashMap params = new HashMap();
					params.put(AdmintoolsConstants.PAGE, ListParameterUsesModule.MODULE_PAGE);
					params.put(AdmintoolsConstants.ID_DOMAIN, par_id);
					EMFUserError error = new EMFUserError (EMFErrorSeverity.ERROR, 1025, new Vector(), params);
					errorHandler.addError(error);
				}
			}
		}
	}

	private void lookupLoadHandler(SourceBean request, String modality, SourceBean response) throws EMFUserError, SourceBeanException{
		RequestContainer requestContainer = this.getRequestContainer();
		SessionContainer session = requestContainer.getSessionContainer();
		ParameterUse param = new ParameterUse();
		
		String useId = (String)request.getAttribute("useId");
		String parId = (String)request.getAttribute("par_id");
		String idLovStr = (String)request.getAttribute("idLov");
		String description = (String)request.getAttribute("description");
		String name = (String)request.getAttribute("name");
		String label = (String)request.getAttribute("label");
		List inRequestChecksList = request.getAttributeAsList("idCheck");
		List toLoadInParUseCheckList = new ArrayList();
		
		String idTmpStr = null;
		for(int i=0; i<inRequestChecksList.size(); i++) {
			idTmpStr = (String)inRequestChecksList.get(i);
			toLoadInParUseCheckList.add(DAOFactory.getChecksDAO().loadCheckByID(new Integer(idTmpStr)));
		}
		
		List inRequestRoleList = request.getAttributeAsList("idExtRole");
		List roles = new ArrayList();
		for(int i=0; i < inRequestRoleList.size(); i++) {
			String idRoleStr = (String)inRequestRoleList.get(i);
			roles.add(DAOFactory.getRoleDAO().loadByID(new Integer(idRoleStr)));  
		}
		
		param.setName(name);
		param.setDescription(description);
		param.setLabel(label);

		param.setAssociatedRoles(roles);
		param.setAssociatedChecks(toLoadInParUseCheckList);
		param.setId(Integer.valueOf(parId));
		
		if (idLovStr == null || idLovStr.trim().equalsIgnoreCase("")) idLovStr = "-1";
		param.setIdLov(Integer.valueOf(idLovStr));
		
		if (modality.equals(AdmintoolsConstants.DETAIL_MOD)) {
			param.setUseID(Integer.valueOf(useId));
	    }
		session.setAttribute("LookupParUse", param);
		session.setAttribute("modality", modality);
		response.setAttribute("lookupLoopback", "true");
		
	}	
	private void lookupReturnHandler  (SourceBean request, SourceBean response) throws SourceBeanException {
		RequestContainer requestContainer = this.getRequestContainer();
		SessionContainer session = requestContainer.getSessionContainer();
		ParameterUse param = (ParameterUse) session.getAttribute("LookupParUse");
		String modality = (String)session.getAttribute("modality");
		String selectedLovId = (String)request.getAttribute("ID");
		param.setIdLov(Integer.valueOf(selectedLovId));
		Integer parId = param.getId();
		String par_id = parId.toString();
		response.setAttribute("PAR_ID", par_id);
		response.setAttribute("modalitiesObj", param);
		response.setAttribute("modality",modality);
		String loadFromLookup = "LOAD_FROM_LOOKUP";
		response.setAttribute("LOAD_FROM_LOOKUP",loadFromLookup);
	}
	private void lookupReturnBackHandler  (SourceBean request, SourceBean response) throws SourceBeanException {
		RequestContainer requestContainer = this.getRequestContainer();
		SessionContainer session = requestContainer.getSessionContainer();
		ParameterUse param = (ParameterUse) session.getAttribute("LookupParUse");
		String modality = (String)session.getAttribute("modality");
		Integer parId = param.getId();
		String par_id = parId.toString();
		response.setAttribute("PAR_ID", par_id);
		response.setAttribute("modalitiesObj", param);
		response.setAttribute("modality",modality);
		String loadFromLookup = "LOAD_FROM_LOOKUP";
		response.setAttribute("LOAD_FROM_LOOKUP",loadFromLookup);
	}
	
	public boolean validateFields(String dinValName, SourceBean request) throws Exception{
		ConfigSingleton config = ConfigSingleton.getInstance();
		SourceBean source = (SourceBean)config.getFilteredSourceBeanAttribute("VALIDATIONS.DINAMIC_VALIDATIONS.DINAMIC_VALIDATION", "NAME", dinValName);
		BIObjectValidator val = new BIObjectValidator("DetailModalitiesPage","PAGE");
		RequestContainer req = getRequestContainer();
		ResponseContainer res = getResponseContainer();
		SourceBean validatorSB = (SourceBean)source.getAttribute("SERVICE");
		boolean validate = val.validate(req,res,validatorSB);
		return validate;
	}
}	
	
	
	
	
	
	
	