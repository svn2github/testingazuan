package it.eng.spagobi.services.common;

import it.eng.spagobi.services.cas.CasProxyService;

/**
 * Factory Class
 */
public abstract class IProxyServiceFactory {

    private IProxyServiceFactory(){
	
    }
    /**
     * 
     * @return IProxyService
     */
    public static final IProxyService createProxyService(){
	return new CasProxyService();
    }
}
