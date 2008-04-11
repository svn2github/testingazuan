package it.eng.spagobi.services.common;

import it.eng.spagobi.services.cas.CasSsoService;

/**
 * Factory Class
 */
public abstract class SsoServiceFactory {

    private SsoServiceFactory(){
	
    }
    /**
     * 
     * @return IProxyService
     */
    public static final SsoServiceInterface createProxyService(){
	return new CasSsoService();
    }
}
