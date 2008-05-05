/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.services.security.service;

import java.util.HashMap;


import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;

public class SecurityServiceSupplierTest implements ISecurityServiceSupplier {

    /* (non-Javadoc)
     * @see it.eng.spagobi.services.security.service.ISecurityServiceSupplier#createUserProfile(java.lang.String)
     */
    public SpagoBIUserProfile createUserProfile(String userId) {
	SpagoBIUserProfile tmp=new SpagoBIUserProfile();
	
	tmp.setUserId(userId);
	
	String[] ruoli=new String[2];
	ruoli[0]="ruolo1";
	ruoli[1]="ruolo2";
	
	String[] f=new String[2];
	f[0]="f1";
	f[1]="f2";
	
	HashMap attr=new HashMap();
	attr.put("nome", "Angelo");
	attr.put("cognome", "Bernabei");
	
	tmp.setAttributes(attr);
	tmp.setFunctions(f);
	tmp.setRoles(ruoli);
	
	return tmp;
    }
    
    /**
     * Checks if is authorized.
     * 
     * @param userId the user id
     * @param function the function
     * @param mode the mode
     * 
     * @return true, if is authorized
     */
    public boolean isAuthorized(String userId, String function, String mode) {
	return false;
    }
    
    /* (non-Javadoc)
     * @see it.eng.spagobi.services.security.service.ISecurityServiceSupplier#checkAuthorization(java.lang.String, java.lang.String)
     */
    public boolean checkAuthorization(String token,String function) {
		
	return true;	
    }    
}
