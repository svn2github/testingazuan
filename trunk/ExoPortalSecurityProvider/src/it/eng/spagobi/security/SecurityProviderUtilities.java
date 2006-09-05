package it.eng.spagobi.security;

import it.eng.spagobi.utilities.SpagoBITracer;

public class SecurityProviderUtilities {

	public void debug(Class classErr, String nameMeth, String message){
		SpagoBITracer.debug("SPAGOBI(ExoSecurityProvider)",
	            			classErr.getName(),
	            			nameMeth,
	            			message);
	}
	
}
