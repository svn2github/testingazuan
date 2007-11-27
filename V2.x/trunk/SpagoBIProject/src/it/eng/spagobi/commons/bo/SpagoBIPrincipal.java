package it.eng.spagobi.commons.bo;

import java.security.Principal;

public class SpagoBIPrincipal implements Principal {

	String userName = "";
	
	public SpagoBIPrincipal(String userName) {
		this.userName = userName;
	}
	
	public String getName() {
		return this.userName;
	}
	
}