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
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.LowFunctionality;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.ILowFunctionalityDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Implements a module which  handles all  low functionalities management: has methods 
 * for low functionalities load, detail, modify/insertion and deleting operations. 
 * The <code>service</code> method has  a switch for all these operations, differentiated the ones 
 * from the others by a <code>message</code> String.
 * 
 * @author sulis
 */
public class DetailFunctionalityModule extends AbstractModule {

	private String modality = "";
	public final static String MODULE_PAGE = "DetailFunctionalityPage";
	public final static String FUNCTIONALITY_OBJ = "FUNCTIONALITY_OBJ";
	public final static String PATH = "PATH";
	private String typeFunct = null;
	
	


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
		String message = (String) request.getAttribute(AdmintoolsConstants.MESSAGE_DETAIL);
		typeFunct = (String) request.getAttribute(AdmintoolsConstants.FUNCTIONALITY_TYPE);
		
		SpagoBITracer.debug(AdmintoolsConstants.NAME_MODULE, "DetailFunctionalityModule","service","begin of detail functionality modify/visualization service with message =" +message);

		try {
			if (message == null) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
				SpagoBITracer.debug(AdmintoolsConstants.NAME_MODULE, "DetailFunctionalityModule", "service", "The message parameter is null");
				throw userError;
			}
			if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_SELECT)) {
				getDetailFunctionality(request, response);
			} else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_MOD)) {
				modDettaglioFunctionality(request, AdmintoolsConstants.DETAIL_MOD, response);
			} else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_NEW)) {
				newDettaglioFunctionality(request, response);
			} else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_INS)) {
				modDettaglioFunctionality(request, AdmintoolsConstants.DETAIL_INS, response);
			} else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_DEL)) {
				delFunctionality(request, AdmintoolsConstants.DETAIL_DEL, response);
			}

		} catch (EMFUserError eex) {
			EMFErrorHandler errorHandler = getErrorHandler();
			errorHandler.addError(eex);
			return;
		} catch (Exception ex) {
			EMFInternalError internalError = new EMFInternalError(EMFErrorSeverity.ERROR, ex);
			EMFErrorHandler errorHandler = getErrorHandler();
			errorHandler.addError(internalError);
			return;
		}
	}
	
	/**
	 * Gets the detail of a low functionality choosed by the user from the 
	 * low functionalities list. It reaches the key from the request and asks 
	 * to the DB all detail parameter use mode information, by calling the 
	 * method <code>loadLowFunctionalityByPath</code>.
	 *   
	 * @param key The choosed low functionality id key
	 * @param response The response Source Bean
	 * @throws EMFUserError If an exception occurs
	 */
	private void getDetailFunctionality(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			this.modality = AdmintoolsConstants.DETAIL_MOD;
			String path = (String) request.getAttribute(DetailFunctionalityModule.PATH);
			response.setAttribute(AdmintoolsConstants.MODALITY, modality);
			if (typeFunct.equals("LOW_FUNCT")) {
				LowFunctionality funct = DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByPath(path);
				response.setAttribute(FUNCTIONALITY_OBJ, funct);
				response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
			}	
		} catch (EMFUserError eex) {
			EMFErrorHandler errorHandler = getErrorHandler();
			errorHandler.addError(eex);
			return;
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailFunctionalityModule","getDetailFunctionality","Cannot fill response container", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}

	/**
	 * Inserts/Modifies the detail of a low functionality according to the user 
	 * request. When a parameter use mode is modified, the <code>modifyLowFunctionality</code> 
	 * method is called; when a new parameter use mode is added, the <code>insertLowFunctionality</code>
	 * method is called. These two cases are differentiated by the <code>mod</code> String input value .
	 * 
	 * @param request The request information contained in a SourceBean Object
	 * @param mod A request string used to differentiate insert/modify operations
	 * @param response The response SourceBean 
	 * @throws EMFUserError If an exception occurs
	 * @throws SourceBeanException If a SourceBean exception occurs
	 */
	private void modDettaglioFunctionality(SourceBean request, String mod, SourceBean response)
		throws EMFUserError, SourceBeanException {
		try {
			//**********************************************************************
			LowFunctionality lowFunct = recoverLowFunctionalityDetails(request, mod);
			response.setAttribute(FUNCTIONALITY_OBJ, lowFunct);
			response.setAttribute(AdmintoolsConstants.MODALITY, mod);
			EMFErrorHandler errorHandler = getErrorHandler();
			if(mod.equalsIgnoreCase(AdmintoolsConstants.DETAIL_INS)) {
				String pathParent = (String)request.getAttribute(AdmintoolsConstants.PATH_PARENT);
				response.setAttribute(AdmintoolsConstants.PATH_PARENT, pathParent);
			}
			
			// if there are some errors into the errorHandler does not write into DB
			if(!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
				response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
				return;
			}
			//**********************************************************************

			if(mod.equalsIgnoreCase(AdmintoolsConstants.DETAIL_INS)) {
				SessionContainer permSess = getRequestContainer().getSessionContainer().getPermanentContainer();
				IEngUserProfile profile = (IEngUserProfile)permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
				DAOFactory.getLowFunctionalityDAO().insertLowFunctionality(lowFunct, profile);
			} else if(mod.equalsIgnoreCase(AdmintoolsConstants.DETAIL_MOD)) {
				DAOFactory.getLowFunctionalityDAO().modifyLowFunctionality(lowFunct);
			}
     	            
		} catch (EMFUserError eex) {
			EMFErrorHandler errorHandler = getErrorHandler();
			errorHandler.addError(eex);
			return;
		} catch (Exception ex) {			
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailFunctionalityModule","modDettaglioFunctionality","Cannot fill response container", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		response.setAttribute(AdmintoolsConstants.LOOPBACK, "true");
	}

	
	
	/**
	 * Deletes a low functionality choosed by user from the low functionality list.
	 * @param request	The request SourceBean
	 * @param mod	A request string used to differentiate delete operation
	 * @param response	The response SourceBean
	 * @throws EMFUserError	If an Exception occurs
	 * @throws SourceBeanException If a SourceBean Exception occurs
	 */
	private void delFunctionality(SourceBean request, String mod, SourceBean response)
		throws EMFUserError, SourceBeanException {
		try {
			if(typeFunct.equals("LOW_FUNCT")) {
				String path = (String)request.getAttribute(PATH);
				ILowFunctionalityDAO funcdao = DAOFactory.getLowFunctionalityDAO();
				LowFunctionality funct = funcdao.loadLowFunctionalityByPath(path);
				SessionContainer permSess = getRequestContainer().getSessionContainer().getPermanentContainer();
				IEngUserProfile profile = (IEngUserProfile)permSess.getAttribute(IEngUserProfile.ENG_USER_PROFILE);		
				funcdao.eraseLowFunctionality(funct, profile);
			}	
		} catch (EMFUserError eex) {
			EMFErrorHandler errorHandler = getErrorHandler();
			errorHandler.addError(eex);
			return;
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailFunctionalityModule","delFunctionality","Cannot fill response container", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		response.setAttribute("loopback", "true");
	}
	
	/**
	 * Instantiates a new <code>LowFunctionalitye<code> object when a new low functionality
	 * insertion is required, in order to prepare the page for the insertion.
	 * 
	 * @param response The response SourceBean
	 * @throws EMFUserError If an Exception occurred
	 */
	private void newDettaglioFunctionality(SourceBean request, SourceBean response) throws EMFUserError {
		try {
			this.modality = AdmintoolsConstants.DETAIL_INS;
			String pathParent = (String) request.getAttribute(AdmintoolsConstants.PATH_PARENT);
			response.setAttribute(AdmintoolsConstants.MODALITY, modality);
			response.setAttribute(AdmintoolsConstants.PATH_PARENT, pathParent);
			
			if(typeFunct.equals("LOW_FUNCT")) {
				LowFunctionality funct = new LowFunctionality();
				funct.setDescription("");
				funct.setId(new Integer(0));
				funct.setCode("");
                funct.setName("");
				LowFunctionality parentFunct = DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByPath(pathParent);
			    Role[] execRoles = new Role[0];
			    Role[] devRoles = new Role[0];
			    Role[] testRoles = new Role[0];
			    if(parentFunct!=null) {
			    	execRoles = parentFunct.getExecRoles();
			    	devRoles = parentFunct.getDevRoles();
			    	testRoles = parentFunct.getTestRoles();
			    }
			    funct.setTestRoles(testRoles);
			    funct.setDevRoles(devRoles);
			    funct.setExecRoles(execRoles);
				response.setAttribute(FUNCTIONALITY_OBJ, funct);
				response.setAttribute(SpagoBIConstants.RESPONSE_COMPLETE, "true");
			}
			
		} catch (EMFUserError eex) {
			EMFErrorHandler errorHandler = getErrorHandler();
			errorHandler.addError(eex);
			return;
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailFunctionalityModule","newDettaglioFunctionality","Cannot prepare page for the insertion", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}
	
	private LowFunctionality recoverLowFunctionalityDetails(SourceBean request, String mod) throws EMFUserError, SourceBeanException {
		String name = (String)request.getAttribute("name");
		name = name.trim();
		String description = (String)request.getAttribute("description");
		description = description.trim();
		String code = (String)request.getAttribute("code");
		code = code.trim();
        			
		List testAttrsList = request.getAttributeAsList("test");
		Role[] testRoles = new Role[testAttrsList.size()];
		for(int i=0; i<testRoles.length; i++) {
			String idRoleStr = (String)testAttrsList.get(i);
			testRoles[i] = DAOFactory.getRoleDAO().loadByID(new Integer(idRoleStr));
		}
		List devAttrsList = request.getAttributeAsList("development");
		Role[] devRoles = new Role[devAttrsList.size()];
		for(int i=0; i<devRoles.length; i++) {
			String idRoleStr = (String)devAttrsList.get(i);
			devRoles[i] = DAOFactory.getRoleDAO().loadByID(new Integer(idRoleStr));
		}
		List execAttrsList = request.getAttributeAsList("execution");
		Role[] execRoles = new Role[execAttrsList.size()];
		for(int i=0; i<execRoles.length; i++) {
			String idRoleStr = (String)execAttrsList.get(i);
			execRoles[i] = DAOFactory.getRoleDAO().loadByID(new Integer(idRoleStr));
		}
		
		LowFunctionality lowFunct = null;
		
		if(mod.equalsIgnoreCase(AdmintoolsConstants.DETAIL_INS)) {
			String pathParent = (String)request.getAttribute(AdmintoolsConstants.PATH_PARENT);
			String newPath = pathParent + "/" + code;
			SourceBean dataLoad = new SourceBean("dataLoad");
			LowFunctionality funct =  DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByPath(newPath);
			if(funct != null) {
				HashMap params = new HashMap();
				params.put(AdmintoolsConstants.PAGE, TreeObjectsModule.MODULE_PAGE);
				params.put(SpagoBIConstants.ACTOR, SpagoBIConstants.ADMIN_ACTOR);
				params.put(SpagoBIConstants.OPERATION, SpagoBIConstants.FUNCTIONALITIES_OPERATION);
				EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 1005, new Vector(), params );
				getErrorHandler().addError(error);
			}
			if(DAOFactory.getLowFunctionalityDAO().existByCode(code)!=null) {
				EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 1027);
				getErrorHandler().addError(error);
			}
			lowFunct = new LowFunctionality();
			lowFunct.setCode(code);
			lowFunct.setDescription(description);
			lowFunct.setName(name);
			lowFunct.setPath(newPath);
			lowFunct.setDevRoles(devRoles);
			lowFunct.setExecRoles(execRoles);
			lowFunct.setTestRoles(testRoles);
		} else if(mod.equalsIgnoreCase(AdmintoolsConstants.DETAIL_MOD)) {
			String idFunct = (String)request.getAttribute(AdmintoolsConstants.FUNCTIONALITY_ID);
			Integer idFunctWithSameCode =DAOFactory.getLowFunctionalityDAO().existByCode(code);
			if((idFunctWithSameCode!=null) && !(idFunctWithSameCode.equals(new Integer(idFunct)))) {
				EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 1027);
				getErrorHandler().addError(error);
			}
			lowFunct = DAOFactory.getLowFunctionalityDAO().loadLowFunctionalityByID(new Integer(idFunct));
			lowFunct.setCode(code);
			lowFunct.setDescription(description);
			lowFunct.setName(name);
			lowFunct.setDevRoles(devRoles);
			lowFunct.setExecRoles(execRoles);
			lowFunct.setTestRoles(testRoles);
		}
		
		return lowFunct;
	}
	
}
