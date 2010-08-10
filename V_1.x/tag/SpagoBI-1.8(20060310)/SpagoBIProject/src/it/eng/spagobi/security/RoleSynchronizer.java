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

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import it.eng.spago.base.*;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.dbaccess.Utils;
import it.eng.spago.dbaccess.sql.DataConnection;
import it.eng.spago.dbaccess.sql.SQLCommand;
import it.eng.spago.dbaccess.sql.result.DataResult;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.bo.Domain;
import it.eng.spagobi.bo.Role;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IDomainDAO;
import it.eng.spagobi.bo.dao.IRoleDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;


/**
 * Contains methods to Syncronize the portal list of roles
 */
public class RoleSynchronizer {
	
	
	/**
	 * Syncronize the portal roles with SpagoBI roles importing roles missing in SpagoBI. 
	 * if a role yet exist into SpagoBI table list, 
	 * a tracing message is added and the list iteration goes on; if there is a new role, 
	 * it is inserted into role database and another tracing message is added. 
	 */
	public void synchronize() {
		TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
				"RoleSynchronizer::synchronize: start method");
        try {
        	Integer roleTypeID = findSBIDomainValueID("ROLE_TYPE", "PORTAL");
            IRoleDAO roleDAO = DAOFactory.getRoleDAO();
            if (roleTypeID == null){
            	SpagoBITracer.major(SpagoBIConstants.NAME_MODULE,this.getClass().toString(),"Synchronize()", 
            	" Cannot Determine SBI_DOMAINS.VALUE_ID FOR DOMAINS.VALUE_CD=PORTAL AND DOMAIN_CD=ROLE_TYPE");
            }else{
            	ConfigSingleton conf = ConfigSingleton.getInstance();
            	TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
            			"RoleSynchronizer::synchronize: config singleton retrived " + conf);
            	SourceBean secClassSB = (SourceBean)conf.getAttribute("SPAGOBI.SECURITY.PORTAL-SECURITY-CLASS");
            	TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
            			"RoleSynchronizer::synchronize: source bean security class retrived " + secClassSB);
            	String portalSecurityProviderClass = secClassSB.getCharacters();
            	TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
            			"RoleSynchronizer::synchronize: security class name retrived " + portalSecurityProviderClass);
            	Class secProvClass = Class.forName(portalSecurityProviderClass);
            	TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
            			"RoleSynchronizer::synchronize: security class found " + secProvClass);
            	IPortalSecurityProvider portalSecurityProvider = (IPortalSecurityProvider)secProvClass.newInstance();
            	TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
            			"RoleSynchronizer::synchronize: security class instance created " + portalSecurityProvider);
            	SourceBean secFilterSB = (SourceBean)conf.getAttribute("SPAGOBI.SECURITY.ROLE-NAME-PATTERN-FILTER");
            	TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
            			"RoleSynchronizer::synchronize: source bean filter retrived " + secFilterSB);
                String rolePatternFilter = secFilterSB.getCharacters();
                TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
            			"RoleSynchronizer::synchronize: filter string retrived " + rolePatternFilter);
                Pattern pattern = Pattern.compile(rolePatternFilter);
                TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
            			"RoleSynchronizer::synchronize: pattern regular expression " + pattern);
                Matcher matcher = null;
                List roles = portalSecurityProvider.getRoles();
                TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
            			"RoleSynchronizer::synchronize: complete list retrived " + roles);
            	Role aRole = null;
            	String roleName = null;
            	for (Iterator it = roles.iterator(); it.hasNext(); ){
            		aRole = (Role)it.next();
            		roleName = aRole.getName();
            		matcher = pattern.matcher(roleName);
            		if(!matcher.find())
            			continue;	
            		if (exist(aRole, roleDAO)){
            			SpagoBITracer.info(SpagoBIConstants.NAME_MODULE,this.getClass().toString(), 
            			"Synchronize()", " Portal Role [" + aRole.getName()+"] already in Database");
            		}else{
            			SpagoBITracer.info(SpagoBIConstants.NAME_MODULE,this.getClass().toString(), 
            			"Synchronize()", " Portal Role [" + aRole.getName()+"] must be inserted in database");
            			aRole.setRoleTypeID(roleTypeID);
            			aRole.setRoleTypeCD("PORTAL");
            			roleDAO.insertRole(aRole);
            			SpagoBITracer.info(SpagoBIConstants.NAME_MODULE,this.getClass().toString(), 
            			"Synchronize()", " Portal Role [" + aRole.getName()+"] INSERTED OK");
            		}
            	}
            } 
        } catch (EMFUserError emfue) {
        	SpagoBITracer.major(SpagoBIConstants.NAME_MODULE,this.getClass().toString(),
        	"Synchronize()", " Exception verified ", emfue);
		} catch(Exception ex){
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE,this.getClass().toString(), 
			"Synchronize()", " An exception has occurred ", ex);
		} finally {
			TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, 
			"RoleSynchronizer::synchronize: end method");
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
			Domain dom = domdao.loadDomainByCodeAndValue("ROLE_TYPE", "PORTAL");
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