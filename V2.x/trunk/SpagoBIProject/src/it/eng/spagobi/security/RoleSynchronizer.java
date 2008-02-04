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
/*
 * Created on 20-apr-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.security;

import it.eng.spago.base.Constants;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dbaccess.Utils;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.SQLCommand;
import it.eng.spago.dbaccess.sql.result.DataResult;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.bo.Role;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IDomainDAO;
import it.eng.spagobi.commons.dao.IRoleDAO;
import it.eng.spagobi.commons.utilities.SpagoBITracer;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;


/**
 * Contains methods to Syncronize the portal list of roles
 */
public class RoleSynchronizer {
	
	static private Logger logger = Logger.getLogger(RoleSynchronizer.class);
	
	/**
	 * Syncronize the portal roles with SpagoBI roles importing roles missing in SpagoBI. 
	 * if a role yet exist into SpagoBI table list, 
	 * a tracing message is added and the list iteration goes on; if there is a new role, 
	 * it is inserted into role database and another tracing message is added. 
	 */
	public void synchronize() {
		logger.debug("IN");
        try {
            IRoleDAO roleDAO = DAOFactory.getRoleDAO();
        	ConfigSingleton conf = ConfigSingleton.getInstance();
        	TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
        			"RoleSynchronizer::synchronize: config singleton retrived " + conf);
        	SourceBean secClassSB = (SourceBean)conf.getAttribute("SPAGOBI.SECURITY.PORTAL-SECURITY-CLASS");
        	TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
        			"RoleSynchronizer::synchronize: source bean security class retrived " + secClassSB);
        	String portalSecurityProviderClass = (String) secClassSB.getAttribute("className");
        	portalSecurityProviderClass = portalSecurityProviderClass.trim();
        	TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
        			"RoleSynchronizer::synchronize: security class name retrived " + portalSecurityProviderClass);
        	Class secProvClass = Class.forName(portalSecurityProviderClass);
        	TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
        			"RoleSynchronizer::synchronize: security class found " + secProvClass);
        	ISecurityInfoProvider portalSecurityProvider = (ISecurityInfoProvider)secProvClass.newInstance();
        	TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
        			"RoleSynchronizer::synchronize: security class instance created " + portalSecurityProvider);
        	SourceBean secFilterSB = (SourceBean)conf.getAttribute("SPAGOBI.SECURITY.ROLE-NAME-PATTERN-FILTER");
        	TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
        			"RoleSynchronizer::synchronize: source bean filter retrived " + secFilterSB);
            String rolePatternFilter = secFilterSB.getCharacters();
            TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
        			"RoleSynchronizer::synchronize: filter string retrived " + rolePatternFilter);
            Pattern filterPattern = Pattern.compile(rolePatternFilter);
            TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
        			"RoleSynchronizer::synchronize: filter pattern regular expression " + filterPattern);
            Matcher matcher = null;
            List roles = portalSecurityProvider.getRoles();
            TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
        			"RoleSynchronizer::synchronize: complete list retrived " + roles);
        	Role aRole = null;
        	String roleName = null;
        	for (Iterator it = roles.iterator(); it.hasNext(); ){
        		aRole = (Role)it.next();
        		roleName = aRole.getName();
        		matcher = filterPattern.matcher(roleName);
        		if(!matcher.matches())
        			continue;	
        		if (exist(aRole, roleDAO)){
        			logger.info(" Role [" + aRole.getName()+"] already in Database");
        		}else{
        			logger.info(" Role [" + aRole.getName()+"] must be inserted in database");
        			setRoleType(aRole);
        			roleDAO.insertRole(aRole);
        			logger.info(" Portal Role [" + aRole.getName()+"] INSERTED OK");
        		}
        	}
        } catch (EMFUserError emfue) {
        	logger.error(" Exception verified ", emfue);
		} catch(Exception ex){
			logger.error(" An exception has occurred ", ex);
		} finally {
			logger.debug("OUT");
		}
	}
	
	
    /** Returns true if a role already exists into the role list, false 
    * if none. If the role name is found into the roles list, the <code>
    * loadByName</code> method called doesn't throw any exception, so true is returned.
    * 
    * @param pRole The input role to control
    * @param aRoleDAO	The interface role DAO object, used to call list roles
    * loaded by name. 
    * @return A boolean value telling us if the role exists or not.
    */
	private boolean exist(Role pRole, IRoleDAO aRoleDAO){
    	try{
    		Role role = aRoleDAO.loadByName(pRole.getName());
    		if(role!=null)
    			return true;
    		else return false;
    	}catch(Exception e){
    		return false;
    	}
    }
    
	/**
	 * Sets the correct role type, according to the role name and the configured patterns
	 * @param aRole, the role to be modified (information about the role type will be added)
	 */
	private void setRoleType(Role aRole) {
		if (aRole == null) {
			logger.warn("Role in input is null. Returning.");
			return;
		}
		if (isRoleType(aRole, "ADMIN")) {
			logger.debug("Role with name [" + aRole.getName() + "] is ADMIN role type.");
			Integer roleTypeId = findSBIDomainValueID("ROLE_TYPE", "ADMIN");
			aRole.setRoleTypeID(roleTypeId);
			aRole.setRoleTypeCD("ADMIN");
			return;
		}
		if (isRoleType(aRole, "DEV_ROLE")) {
			logger.debug("Role with name [" + aRole.getName() + "] is DEV_ROLE role type.");
			Integer roleTypeId = findSBIDomainValueID("ROLE_TYPE", "DEV_ROLE");
			aRole.setRoleTypeID(roleTypeId);
			aRole.setRoleTypeCD("DEV_ROLE");
			return;
		}		
		if (isRoleType(aRole, "TEST_ROLE")) {
			logger.debug("Role with name [" + aRole.getName() + "] is TEST_ROLE role type.");
			Integer roleTypeId = findSBIDomainValueID("ROLE_TYPE", "TEST_ROLE");
			aRole.setRoleTypeID(roleTypeId);
			aRole.setRoleTypeCD("TEST_ROLE");
			return;
		}
		
		// Role is not ADMIN/DEV_ROLE/TEST_ROLE, default is FUNCT
		Integer roleTypeId = findSBIDomainValueID("ROLE_TYPE", "FUNCT");
		aRole.setRoleTypeID(roleTypeId);
		aRole.setRoleTypeCD("FUNCT");
		
//		if (isRoleType(aRole, "FUNCT")) {
//			logger.debug("Role with name [" + aRole.getName() + "] is FUNCT role type.");
//			Integer roleTypeId = findSBIDomainValueID("ROLE_TYPE", "FUNCT");
//			aRole.setRoleTypeID(roleTypeId);
//			aRole.setRoleTypeCD("FUNCT");
//			return;
//		}
//		// Role is not ADMIN/DEV_ROLE/TEST_ROLE/FUNCT, default is PORTAL
//		Integer roleTypeId = findSBIDomainValueID("ROLE_TYPE", "PORTAL");
//		aRole.setRoleTypeID(roleTypeId);
//		aRole.setRoleTypeCD("PORTAL");
	}
	
	private boolean isRoleType(Role aRole, String roleTypeCd) {
		String roleName = aRole.getName();
		ConfigSingleton conf = ConfigSingleton.getInstance();
		SourceBean adminRolePatternSB = (SourceBean) conf.getAttribute("SPAGOBI.SECURITY.ROLE-TYPE-PATTERNS." + roleTypeCd + "-PATTERN");
		if (adminRolePatternSB != null) {
			String adminPatternStr = adminRolePatternSB.getCharacters();
			Pattern adminPattern = Pattern.compile(adminPatternStr);
			Matcher matcher = adminPattern.matcher(roleName);
    		if (matcher.matches()) {
    			return true;
    		}
		}
		return false;
	}
	
	
	/**
	 * Gets the id for a Domain, given its code and value 
	 * @param domainCode	The Domain code String
	 * @param valueCode	The domain Value Dtring
	 * @return	The Domain ID 
	 */
    private Integer findSBIDomainValueID(String domainCode, String valueCode ){
    	SQLCommand cmd = null;
		DataResult dr = null;
		DataConnection dataConnection = null;
		Integer returnValue = null;
		try {
			IDomainDAO domdao = DAOFactory.getDomainDAO();
			Domain dom = domdao.loadDomainByCodeAndValue(domainCode, valueCode);
			returnValue = dom.getValueId();
		}  catch (Exception ex) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE,this.getClass().toString(), 
			"findSBIDomainValueID", " An exception has occurred ", ex);
		} finally {
			Utils.releaseResources(dataConnection, cmd, dr);
		}
		return returnValue;
    }
}