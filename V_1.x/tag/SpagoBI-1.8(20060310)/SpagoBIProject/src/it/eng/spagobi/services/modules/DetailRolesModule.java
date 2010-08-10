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

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IRoleDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.metadata.SbiFunctions;
import it.eng.spagobi.metadata.SbiParuse;
import it.eng.spagobi.security.RoleSynchronizer;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * This class implements a module which  handles roles management. 
 */
public class DetailRolesModule extends AbstractModule {
	
	private String modalita = "";
	
	public void init(SourceBean config) {
	}

	/**
	 * Reads the operation asked by the user and calls the deletion and synchronization methods
	 * @param request The Source Bean containing all request parameters
	 * @param response The Source Bean containing all response parameters
	 * @throws exception If an exception occurs
	 */
	public void service(SourceBean request, SourceBean response) throws Exception {
		String message = (String) request.getAttribute("MESSAGEDET");
		SpagoBITracer.debug(SpagoBIConstants.NAME_MODULE, "DetailRolesModule", 
				"service","begin of detail Roles synch/erasing service with message =" +message);
		EMFErrorHandler errorHandler = getErrorHandler();
		try {
			if (message == null) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
				SpagoBITracer.debug(AdmintoolsConstants.NAME_MODULE, "DetailRolesModule", 
						"service", "The message parameter is null");
				throw userError;
			}
			if (message.trim().equalsIgnoreCase(AdmintoolsConstants.ROLES_SYNCH)) {
				synchronizeRoles(response);
			} else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_DEL)) {
				deleteRole(request, SpagoBIConstants.DETAIL_MOD, response);
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
	 * Deletes a role choosed by user from the roles list.
	 * 
	 * @param request	The request SourceBean
	 * @param mod A request string used to differentiate delete operation
	 * @param response	The response SourceBean
	 * @throws EMFUserError	If an Exception occurs
	 * @throws SourceBeanException If a SourceBean Exception occurs
	 */
	private void deleteRole(SourceBean request, String mod, SourceBean response)
		throws EMFUserError, SourceBeanException {
		try {
			String id = (String) request.getAttribute("EXT_ROLE_ID");
			Integer roleId = Integer.valueOf(id);
			IRoleDAO roleDAO = DAOFactory.getRoleDAO();
			Role role = roleDAO.loadByID(roleId);
			List functsAss = roleDAO.LoadFunctionalitiesAssociated(roleId);
			List usesAss = roleDAO.LoadParUsesAssociated(roleId);
			if(!functsAss.isEmpty() || !usesAss.isEmpty()) {
				String allUses = "";
				Iterator iterUses = usesAss.iterator();
				while(iterUses.hasNext()){
					SbiParuse sbiparuse = (SbiParuse)iterUses.next();
					allUses += sbiparuse.getName() + "<br/>";
				}
				String allfuncts = "";
				Iterator iterFuncts = functsAss.iterator();
				while(iterFuncts.hasNext()){
					SbiFunctions sbifunct = (SbiFunctions)iterFuncts.next();
					allfuncts += sbifunct.getName() + "<br/>";
				}
				Vector params = new Vector();
				params.add(allUses);
				params.add(allfuncts);
				EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 1042, params);
				getErrorHandler().addError(error);
				return;
			}
			roleDAO.eraseRole(role);
		} catch (EMFUserError e){
			  HashMap params = new HashMap();
			  params.put(AdmintoolsConstants.PAGE, ListEnginesModule.MODULE_PAGE);
			  throw new EMFUserError(EMFErrorSeverity.ERROR, 1042, new Vector(), params);
		} catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailRolesModule","deleteRole","Cannot fill response container", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		response.setAttribute("loopback", "true");
	}
	
	

   /**
    * Synchronizes roles importing portal roles missing into SpagoBI
    * @param response The spago framework response sourcebean object
    * @throws SourceBeanException
    */
	private void synchronizeRoles(SourceBean response) throws SourceBeanException{
		RoleSynchronizer roleSynch = new RoleSynchronizer();
		roleSynch.synchronize();
		response.setAttribute("loopback", "true");
	}

}