package it.eng.spagobi.mockObjects;

import java.security.Principal;

public class PrincipalImplMock implements Principal {

	private String name = null;
	
	public PrincipalImplMock(){};
	
	public PrincipalImplMock(String aName){
		this.name = aName;
	}

	public String getName() {
		return name;
	}

	public void setName(String aName) {
		this.name = aName;
	}
}
