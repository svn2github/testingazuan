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
import it.eng.spago.dbaccess.SQLStatements;
import it.eng.spago.dbaccess.Utils;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.DataField;
import it.eng.spago.dbaccess.sql.SQLCommand;
import it.eng.spago.dbaccess.sql.result.DataResult;
import it.eng.spago.dbaccess.sql.result.ScrollableDataResult;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.jdbc.AbstractJdbcDAO;
import it.eng.spagobi.bo.dao.IRoleDAO;
import it.eng.spagobi.constants.AdmintoolsConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.security.RoleSynchronizer;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * This class implements a module which  handles all engine management: has methos for engine load, 
 * details,moodify/insertion and deleting operations. The <code>service</code> method has  a 
 * switch for all these operations, differentiated the ones from the others by a <code>message</code> String.
 * 
 * @author sulis
 */
public class DetailRolesModule extends AbstractModule {
	
	private String modalita = "";
	
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
		SpagoBITracer.debug(AdmintoolsConstants.NAME_MODULE, "DetailRolesModule","service","begin of detail Roles synch/erasing service with message =" +message);

		EMFErrorHandler errorHandler = getErrorHandler();
		try {
			if (message == null) {
				EMFUserError userError = new EMFUserError(EMFErrorSeverity.ERROR, 101);
				SpagoBITracer.debug(AdmintoolsConstants.NAME_MODULE, "DetailRolesModule", "service", "The message parameter is null");
				throw userError;
			}
			if (message.trim().equalsIgnoreCase(AdmintoolsConstants.ROLES_SYNCH)) {
				//String id = (String) request.getAttribute("EXT_ROLE_ID");
				synchronizeRoles(response);
			} else if (message.trim().equalsIgnoreCase(AdmintoolsConstants.DETAIL_DEL)) {
				deleteRole(request, AdmintoolsConstants.DETAIL_MOD, response);
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
	 * Deletes an engine choosed by user from the engines list.
	 * 
	 * @param request	The request SourceBean
	 * @param mod	A request string used to differentiate delete operation
	 * @param response	The response SourceBean
	 * @throws EMFUserError	If an Exception occurs
	 * @throws SourceBeanException If a SourceBean Exception occurs
	 */
	private void deleteRole(SourceBean request, String mod, SourceBean response)
		throws EMFUserError, SourceBeanException {
		
		try {
			String id = (String) request.getAttribute("EXT_ROLE_ID");
			//String rTypeId = (String) request.getAttribute("ROLE_TYPE_ID");
			Integer roleId = Integer.valueOf(id);
			//Integer roleTypeId = Integer.valueOf(rTypeId);
			IRoleDAO roleDAO = DAOFactory.getRoleDAO();
            Role role = new Role();
            role.setId(roleId);
            boolean hasDomAss = hasDomainAss(roleId);
			
			if (hasDomAss){
				HashMap params = new HashMap();
				params.put(AdmintoolsConstants.PAGE, ListEnginesModule.MODULE_PAGE);
				EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 1042, new Vector(), params );
				getErrorHandler().addError(error);
				return;
			}
			roleDAO.eraseRole(role);
		}   catch (EMFUserError e){
			  HashMap params = new HashMap();
			  params.put(AdmintoolsConstants.PAGE, ListEnginesModule.MODULE_PAGE);
			  throw new EMFUserError(EMFErrorSeverity.ERROR, 1042, new Vector(), params);
				
			}
		    catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "DetailRolesModule","deleteRole","Cannot fill response container", ex  );
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
		response.setAttribute("loopback", "true");
	}
	
	public boolean hasDomainAss (Integer roleID) throws EMFUserError{
		SQLCommand cmdSelect = null;
		SQLCommand cmdSelect1 = null;
		DataResult dr = null;
		ScrollableDataResult sdr = null;	
		DataConnection dataConnection = null;
		DataResult dr1 = null;
		ScrollableDataResult sdr1 = null;
		//ArrayList parameters = new ArrayList(1);
		ArrayList parameters1 = new ArrayList(1);
		try{
			dataConnection = AbstractJdbcDAO.getConnection("spagobi");
			//look if there is any domain in sbi_domains
			String strSql = SQLStatements.getStatement("SELECT_DOMAIN_BY_ROLE_ID");
			//DataField dataField = dataConnection.createDataField("VALUE_ID", Types.NUMERIC, roleTypeID);
			//parameters.add(dataField);
			cmdSelect = dataConnection.createSelectCommand(strSql);
			//dr = cmdSelect.execute(parameters);
			//sdr = (ScrollableDataResult) dr.getDataObject();
			//look for functionalities associated to that role
			String strSql1 = SQLStatements.getStatement("SELECT_FUNCT_BY_ROLE_ID");
			DataField dataField1 = dataConnection.createDataField("ROLE_ID", Types.NUMERIC, roleID);
			parameters1.add(dataField1);
			cmdSelect1 = dataConnection.createSelectCommand(strSql1);
			dr1 = cmdSelect1.execute(parameters1);
			sdr1 = (ScrollableDataResult) dr1.getDataObject();
			if(sdr1.hasRows()){
				return true;
			}
			return false;
		
		}catch (Exception ex) {
			SpagoBITracer.major(AdmintoolsConstants.NAME_MODULE, "RoleDAOImpl", "hasDomainAss", "Cannot execute method", ex);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		} finally {
			Utils.releaseResources(dataConnection, cmdSelect, dr);
		
		
	}
	}

	public void synchronizeRoles(SourceBean response) throws SourceBeanException{
		RoleSynchronizer roleSynch = new RoleSynchronizer();
		roleSynch.Synchronize();
		response.setAttribute("loopback", "true");
	}

}