package it.eng.spagobi.services.security.service;

import java.util.HashMap;


import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;

public class SecurityServiceSupplierTest implements ISecurityServiceSupplier {

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
    public boolean isAuthorized(String userId, String function, String mode) {
	return false;
    }
    
    public boolean checkAuthorization(String token,String function) {
		
	return true;	
    }    
}
